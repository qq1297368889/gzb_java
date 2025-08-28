package gzb.frame.factory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import groovy.lang.GroovyClassLoader;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

public class ClassLoad {
    public static Log log = new LogImpl();
    public static Map<Class<?>, byte[]> mapClassByte = new HashMap<Class<?>, byte[]>();
    public static Map<Class<?>, String> mapClassCode = new HashMap<Class<?>, String>();

    public static Class<?> compileGroovyClassCode(String groovyCode) throws Exception {
      long start = System.currentTimeMillis();
        Class<?> class1 = new GroovyClassLoader(Thread.currentThread().getContextClassLoader()).parseClass(groovyCode);
        long end = System.currentTimeMillis();
        log.d("编译","groovy",class1.getName(),"耗时",end - start);
        return class1;
    }

    public static Class<?> compileJavaClassCode(String javaCode) throws Exception {
        long start = System.currentTimeMillis();
        Class<?> class1 = compileJavaCode(javaCode);
        long end = System.currentTimeMillis();
        log.d("编译","java",class1.getName(),"耗时",end - start);
        return class1;
    }

    public static Class<?> compileJavaCode(File file) throws Exception {
        String javaCode = Tools.fileReadString(file);
        return compileJavaCode(javaCode);
    }

    public static Class<?> compileJavaCode(String javaCode) throws Exception {
        String className = extractClassName(javaCode);
        return compileJavaCode(javaCode, className);
    }

    public static Class<?> compileJavaCode(String javaCode, String className) throws Exception {
        Class aClass;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("无法获取 Java 编译器，请确保 JDK 已正确安装。");
        }

        JavaFileObject source = new JavaSourceFromString(className, javaCode);
        JavaClassInMemory classInMemory = new JavaClassInMemory(className, JavaFileObject.Kind.CLASS);

        List<JavaFileObject> compilationUnits = new ArrayList<>();
        compilationUnits.add(source);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager fileManager = new ForwardingJavaFileManager<StandardJavaFileManager>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
                return classInMemory;
            }
        };

        // 添加类路径（处理依赖）
        List<String> options = new ArrayList<>();
        options.add("-classpath");
        options.add(System.getProperty("java.class.path"));

        JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean success = task.call();

        if (!success) {
            StringBuilder errorMessage = new StringBuilder();
            diagnostics.getDiagnostics().forEach(
                    d ->
                            errorMessage.append(d.getMessage(null)).append("\n"));
            throw new Exception("编译失败：\n" + errorMessage);
        }

        byte[] bytes = classInMemory.getBytes();
        if (bytes.length == 0) {
            throw new Exception("编译后的字节码为空，可能存在依赖缺失或语法错误");
        }

        MemoryClassLoader classLoader = new MemoryClassLoader(classInMemory, getNewClassLoader(className, bytes));
        aClass = classLoader.loadClass(className);
        mapClassByte.put(aClass, bytes);
        mapClassCode.put(aClass, javaCode);
        return aClass;
    }


    public static String extractClassName(String javaCode) {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> parseResult = javaParser.parse(javaCode);
        if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
            CompilationUnit cu = parseResult.getResult().get();
            String packageName = cu.getPackageDeclaration()
                    .map(pkg -> pkg.getNameAsString())
                    .orElse("");

            ClassOrInterfaceDeclaration classDecl = cu.findAll(ClassOrInterfaceDeclaration.class)
                    .stream()
                    .filter(ClassOrInterfaceDeclaration::isPublic)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Java 代码中未找到 public 类"));

            String className = classDecl.getNameAsString();
            return packageName.isEmpty() ? className : packageName + "." + className;
        } else {
            throw new IllegalArgumentException("Java 代码解析失败");
        }
    }

    public static ClassLoader getNewClassLoader(final String className, final byte[] bytecode) {
        return new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals(className)) {
                    return defineClass(name, bytecode, 0, bytecode.length);
                }
                return super.findClass(name);
            }
            // 可以根据需要重写其他方法，例如 getResource 等
        };
    }

    // 自定义 JavaFileObject 用于内存中存储 Java 源代码
    static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    // 自定义 JavaFileObject 用于内存中存储编译后的字节码
    static class JavaClassInMemory extends SimpleJavaFileObject {
        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        JavaClassInMemory(String name, Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return bos;
        }

        public byte[] getBytes() {
            return bos.toByteArray();
        }
    }

    // 自定义类加载器，用于加载内存中的字节码
    static class MemoryClassLoader extends ClassLoader {
        private final JavaClassInMemory classInMemory;

        MemoryClassLoader(JavaClassInMemory classInMemory, ClassLoader parent) {
            super(parent);
            this.classInMemory = classInMemory;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classInMemory.getBytes();
            if (bytes.length == 0) {
                throw new ClassNotFoundException("编译后的字节码数据为空: " + name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

}