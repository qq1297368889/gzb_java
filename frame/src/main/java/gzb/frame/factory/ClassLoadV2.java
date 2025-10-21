package gzb.frame.factory;

import gzb.exception.GzbException0;
import gzb.tools.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

public class ClassLoadV2 {

    private static final Logger log = LoggerFactory.getLogger(ClassLoadV2.class);
    private final JavaCompiler compiler;
    private final StandardJavaFileManager standardFileManager;
    public boolean outWarn = false;
    public ClassLoadV2() {
        this(false);
    }
    public ClassLoadV2(boolean outWarn) {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (this.compiler == null) {
            throw new IllegalStateException("JDK compiler not available. Ensure you are running on a JDK (not a JRE).");
        }
        this.outWarn=outWarn;
        this.standardFileManager = compiler.getStandardFileManager(null, null, null);
    }
    public List<String>readConfig(){
        List<String> options = new ArrayList<>();
        options.add("-parameters");
        options.add("-encoding");
        options.add(Config.encoding.name());
        String jarPath = System.getProperty("java.class.path");
        String separator = File.pathSeparator;
        String completeClasspath = jarPath + separator + ".";

        options.add("-classpath");
        options.add(completeClasspath);
        options.add("--add-exports");
        options.add("java.base/jdk.internal.loader=ALL-UNNAMED"); // 打破一些限制
        return options;
    }
    /**
     * 核心编译和加载方法 (单文件)。
     * (保持原样，未修改)
     */
    public  Class<?> compileAndLoad(String sourceCode, String className) throws Exception {
        DynamicJavaFileManager fileManager = new DynamicJavaFileManager(standardFileManager);
        List<JavaFileObject> compilationUnits = new ArrayList<>();
        SimpleJavaFileObject sourceFile = new SimpleJavaFileObject(
                java.net.URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
                JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return sourceCode;
            }
        };
        fileManager.addSource(className, sourceFile);
        compilationUnits.add(sourceFile);

        JavaCompiler.CompilationTask task = compiler.getTask(
                null, // Writer for output
                fileManager,
                null, // DiagnosticListener
                readConfig(),
                null, // Annotation processing class names
                compilationUnits);

        if (!task.call()) {
            throw new GzbException0("编译类失败"+className);
        }

        // 3. 使用自定义 ClassLoader 加载编译结果
        DynamicClassLoader classLoader = new DynamicClassLoader(
                Thread.currentThread().getContextClassLoader(),
                fileManager.getClassFileObjects());
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new GzbException0("编译类失败"+className, e);
        }

    }
    /**
     * 编译并加载一批 Java 源代码。
     * * @param sourcesMap 键为 FQCN (Fully Qualified Class Name)，值为源代码字符串
     *
     * @return 包含所有加载的 Class 对象的 Map
     * @throws RuntimeException 如果编译失败
     */
    public Map<String, Class<?>> compileAndLoad(Map<String, String> sourcesMap) {
        DynamicJavaFileManager fileManager = new DynamicJavaFileManager(standardFileManager);
        List<JavaFileObject> compilationUnits = new ArrayList<>();

        // 1. 准备所有源代码文件对象
        for (Map.Entry<String, String> entry : sourcesMap.entrySet()) {
            String className = entry.getKey();
            String sourceCode = entry.getValue();
            // SourceFileObject 是 SimpleJavaFileObject 的一个简单实现，用于包装源代码
            SimpleJavaFileObject sourceFile = new SimpleJavaFileObject(
                    java.net.URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
                    JavaFileObject.Kind.SOURCE) {
                @Override
                public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                    return sourceCode;
                }
            };

            fileManager.addSource(className, sourceFile);
            compilationUnits.add(sourceFile);
        }


        JavaCompiler.CompilationTask task = compiler.getTask(
                null, // Writer for output
                fileManager,
                null, // DiagnosticListener
                readConfig(),
                null, // Annotation processing class names
                compilationUnits);

        if (!task.call()) {
            throw new RuntimeException("Compilation failed! Check logs.");
            // 实际应用中应该处理 DiagnosticListener 捕获详细错误信息
        }

        // 3. 使用自定义 ClassLoader 加载编译结果
        DynamicClassLoader classLoader = new DynamicClassLoader(
                Thread.currentThread().getContextClassLoader(),
                fileManager.getClassFileObjects());

        Map<String, Class<?>> loadedClasses = new HashMap<>();
        try {
            for (String className : sourcesMap.keySet()) {
                Class<?> compiledClass = classLoader.loadClass(className);
                loadedClasses.put(className, compiledClass);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load compiled class.", e);
        }

        return loadedClasses;
    }

    class DynamicClassLoader extends ClassLoader {
        // 存储编译后的JavaFileObject（ClassFileObject）
        private final Map<String, JavaFileObject> classFileObjects;

        public DynamicClassLoader(ClassLoader parent, Map<String, JavaFileObject> classFileObjects) {
            super(parent);
            this.classFileObjects = classFileObjects;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // 1. 优先查找同批次编译的类
            JavaFileObject fileObject = classFileObjects.get(name);

            if (fileObject instanceof ClassFileObject) {
                byte[] bytes = ((ClassFileObject) fileObject).getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }

            // 2. 回退到父ClassLoader查找外部依赖
            return super.findClass(name);
        }
    }

    class ClassFileObject extends SimpleJavaFileObject {
        private ByteArrayOutputStream outputStream;

        protected ClassFileObject(String className, Kind kind) {
            super(URI.create("string:///" + className.replace('.', '/') + kind.extension), kind);
        }

        // 用于在编译过程中接收字节码
        @Override
        public OutputStream openOutputStream() {
            outputStream = new ByteArrayOutputStream();
            return outputStream;
        }

        // 供ClassLoader读取字节码
        public byte[] getBytes() {
            return outputStream.toByteArray();
        }
    }

    class DynamicJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        // 存储当前编译批次的所有JavaFileObject（Source和Class）
        private final Map<String, JavaFileObject> classFileObjects = new HashMap<>();
        public DynamicJavaFileManager(StandardJavaFileManager standardManager) {
            super(standardManager);
        }

        public void addSource(String className, JavaFileObject sourceFile) {
            classFileObjects.put(className, sourceFile);
        }

        public Map<String, JavaFileObject> getClassFileObjects() {
            return classFileObjects;
        }

        // 关键方法：用于javac查找依赖类（Source或Class）
        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
            return super.getJavaFileForInput(location, className, kind);
        }

        // 关键方法：用于javac创建编译后的类文件
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            ClassFileObject fileObject = new ClassFileObject(className, kind);
            // 将编译后的ClassFileObject加入到Map中，供其他同批次类查找依赖
            classFileObjects.put(className, fileObject);
            //System.out.println("classFileObjects："+className +" "+fileObject);
            return fileObject;
        }
    }
}



