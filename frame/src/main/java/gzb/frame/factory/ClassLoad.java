/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame.factory;

import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类加载器，用于动态编译和加载 Groovy 或 Java 代码。
 * 本方案为每次加载使用独立的类加载器，以支持热更新。
 */
public class ClassLoad {
    public static Log log= Config.log;

    /**
     * 从文件编译 Java 代码。
     * @param file Java 源文件
     * @return 编译后的 Class 对象
     * @throws Exception 编译或加载失败
     */
    public static Class<?> compileJavaCode(File file) throws Exception {
        String javaCode = Tools.fileReadString(file);
        return compileJavaCode(javaCode);
    }

    /**
     * 从字符串编译 Java 代码。
     * @param javaCode Java 源代码字符串
     * @return 编译后的 Class 对象
     * @throws Exception 编译或加载失败
     */
    public static Class<?> compileJavaCode(String javaCode) throws Exception {
        String className = extractPublicClassName(javaCode);
        long start = System.currentTimeMillis();
        Class<?> class1 = compileJavaCode(javaCode, className);
        long end = System.currentTimeMillis();
        log.i("编译", "java", class1.getName(), "耗时", end - start);
        return class1;
    }

    /**
     * 核心编译和加载方法。
     * @param javaCode Java 源代码
     * @param className 类的全限定名
     * @return 编译后的 Class 对象
     * @throws Exception 编译或加载失败
     */
    public static Class<?> compileJavaCode(String javaCode, String className) throws Exception {
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

        List<String> options = new ArrayList<>();
        options.add("-classpath");
        options.add(System.getProperty("java.class.path"));

        JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean success = task.call();

        if (!success) {
            StringBuilder errorMessage = new StringBuilder();
            diagnostics.getDiagnostics().forEach(
                    d -> errorMessage.append(d.getMessage(null)).append("\n"));
            throw new Exception("编译失败："+"\r\n" + errorMessage+"\r\n" +className+"\r\n"+javaCode);
        }

        byte[] bytes = classInMemory.getBytes();
        if (bytes.length == 0) {
            throw new Exception("编译后的字节码为空，可能存在依赖缺失或语法错误");
        }

        // 关键步骤：为每次加载创建一个新的、独立的类加载器
        HotSwapClassLoader classLoader = new HotSwapClassLoader(Thread.currentThread().getContextClassLoader());
        return classLoader.loadFromBytes(className, bytes);
    }

    /**
     * 快速提取公共类名，用于命名虚拟文件。
     * 这个方法使用轻量级的字符串查找，比 JavaParser 快无数倍。
     *
     * @param javaCode Java 源代码
     * @return 类的全限定名
     */
    private static String extractPublicClassName(String javaCode) {
        String packageName = "";
        String publicClassName = "InMemory"; // 默认名称

        // 1. 使用正则表达式匹配包名
        Pattern packagePattern = Pattern.compile("package\\s+([a-zA-Z0-9_\\.]+)\\s*;", Pattern.MULTILINE);
        Matcher packageMatcher = packagePattern.matcher(javaCode);
        if (packageMatcher.find()) {
            packageName = packageMatcher.group(1).trim();
        }

        // 2. 使用正则表达式匹配公共类、接口或抽象类名
        Pattern classPattern = Pattern.compile(
                "public\\s+(?:(?:abstract|final)\\s+)?(?:class|interface|enum)\\s+([a-zA-Z0-9_\\$]+)",
                Pattern.MULTILINE);
        Matcher classMatcher = classPattern.matcher(javaCode);
        if (classMatcher.find()) {
            publicClassName = classMatcher.group(1).trim();
        }

        return packageName.isEmpty() ? publicClassName : packageName + "." + publicClassName;
    }

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

    /**
     * 为热插拔场景设计的类加载器。
     * 每次加载新版本类时，都使用一个新的 HotSwapClassLoader 实例。
     */
    static class HotSwapClassLoader extends ClassLoader {
        public HotSwapClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> loadFromBytes(String name, byte[] bytecode) {
            // 直接定义类，避免重复定义，并且将类定义在这个新的加载器实例中
            return defineClass(name, bytecode, 0, bytecode.length);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // 对于 HotSwapClassLoader 而言，它只负责加载我们传给它的那个类。
            // 依赖的其它类（如 GzbOneInterface）则委托给父加载器。
            return super.findClass(name);
        }
    }
}