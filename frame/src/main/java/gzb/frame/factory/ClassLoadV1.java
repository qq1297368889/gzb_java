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

import gzb.exception.GzbException0;
import gzb.tools.Config;
import gzb.tools.log.Log;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类加载器，用于动态编译和加载 Groovy 或 Java 代码。
 * 本方案为每次加载使用独立的类加载器，以支持热更新。
 */
public class ClassLoadV1 {
    public Log log = Log.log;


    // --- 现有方法：用于单个源代码编译和加载 ---


    public  Map<String, Class<?>> compileAndLoad(Map<String, String> sourcesMap) throws Exception {
        long start = System.currentTimeMillis();
        Map<String, Class<?>> map = compile(sourcesMap);
        long end = System.currentTimeMillis();
        log.d("编译耗时", end - start, map.size(), "个类");
        return map;
    }

    /**
     * 核心编译和加载方法 (单文件)。
     * (保持原样，未修改)
     */
    public  Class<?> compileAndLoad(String javaCode, String className) throws Exception {

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
        options.add("-parameters");
        options.add("-encoding");
        options.add(Config.encoding.name());
        String jarPath = System.getProperty("java.class.path");
        String separator = File.pathSeparator;
        String completeClasspath = jarPath + separator + ".";

        options.add("-classpath");
        options.add(completeClasspath);

        //log.t("-classpath", completeClasspath);
        JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean success = task.call();

        if (!success) {
            StringBuilder errorMessage = new StringBuilder();
            diagnostics.getDiagnostics().forEach(
                    d -> errorMessage.append(d.getMessage(null)).append("\n"));
            throw new Exception("编译失败：" + "\r\n" + errorMessage + "\r\n" + className + "\r\n" + javaCode);
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
     * 核心编译和加载方法 (批量版)。
     *
     * @param sourcesMap 键为类的全限定名，值为 Java 源代码
     * @return 编译后的 Class 对象 Map (键为类名，值为 Class 对象)
     * @throws Exception 编译或加载失败
     */
    public  Map<String, Class<?>> compile(Map<String, String> sourcesMap) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("无法获取 Java 编译器，请确保 JDK 已正确安装。");
        }

        // 1. 准备所有的源代码文件对象
        List<JavaFileObject> compilationUnits = new ArrayList<>();
        for (Map.Entry<String, String> entry : sourcesMap.entrySet()) {
            compilationUnits.add(new JavaSourceFromString(entry.getKey(), entry.getValue()));
        }

        // 2. 声明并初始化全局字节码收集器
        // 键: 编译输出的类名 (可能包含 $ 符号)
        // 值: 存储字节码的内存对象
        final Map<String, JavaClassInMemory> outputClasses = new HashMap<>();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // 3. 定制 FileManager：无条件捕获所有 CLASS 文件输出
        JavaFileManager fileManager = new ForwardingJavaFileManager<StandardJavaFileManager>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {

                // 【核心修复点】: 只要编译器输出的是 CLASS 文件，就将其捕获到 outputClasses 中
                if (kind == JavaFileObject.Kind.CLASS) {
                    // 为当前类（无论是主类、内部类、匿名类）创建新的存储对象
                    JavaClassInMemory classInMemory = new JavaClassInMemory(className, kind);

                    // 将输出的类名和其存储对象都放入全局 Map 中
                    outputClasses.put(className, classInMemory);

                    return classInMemory;
                }

                // 对于非 CLASS 输出（如资源文件），委托给标准文件管理器
                return super.getJavaFileForOutput(location, className, kind, sibling);
            }
        };

        // 4. 编译选项设置 (保持不变)
        List<String> options = new ArrayList<>();
        options.add("-Xlint:unchecked");
        options.add("-Xdiags:verbose");
        options.add("-parameters");
        options.add("-encoding");
        options.add(Config.encoding.name());
        String jarPath = System.getProperty("java.class.path");
        String separator = File.pathSeparator;
        String completeClasspath = jarPath + separator + ".";

        options.add("-classpath");
        options.add(completeClasspath);

        // 5. 一次性调用编译任务
        JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean success = task.call();

        if (!success) {
            StringBuilder errorMessage = new StringBuilder();
            diagnostics.getDiagnostics().forEach(
                    d -> errorMessage.append(d.getMessage(null)).append("\n"));
            throw new Exception("批量编译失败：" + "\r\n" + errorMessage);
        }

        // 6. 加载所有编译成功的类
        HotSwapClassLoader classLoader = new HotSwapClassLoader(Thread.currentThread().getContextClassLoader());
        Map<String, Class<?>> compiledClasses = new HashMap<>();
        Map<String, String> sourcesMap_new = new HashMap<>();
        // 遍历 outputClasses，加载所有捕获到的字节码
        for (Map.Entry<String, JavaClassInMemory> entry : outputClasses.entrySet()) {
            String className = entry.getKey();
            byte[] bytes = entry.getValue().getBytes();

            if (bytes.length > 0) {
                //有些同批次依赖问题导致出错 这些类虽然出错一般不影响运行 但是 还是过滤一下 尽量全部成功 如果无法成功调用单词编译再试 还是不行的话 抛出错误
                try {
                    compiledClasses.put(className, classLoader.loadFromBytes(className, bytes));
                } catch (Throwable t) {
                    sourcesMap_new.put(className, sourcesMap.get(className));
                }
            } else {
                log.w("批量编译", "类字节码为空", className);
            }
        }
        if (sourcesMap_new.size() > 0 && compiledClasses.size() > 0) {
            Map<String, Class<?>> compiledClasses0 = compile(sourcesMap_new);
            for (Map.Entry<String, Class<?>> stringClassEntry : compiledClasses0.entrySet()) {
                compiledClasses.put(stringClassEntry.getKey(), stringClassEntry.getValue());
            }
        }
        if (sourcesMap_new.size() > 0) {
            if (compiledClasses.size() > 0) {
                log.d("重新编译", sourcesMap_new);
                Map<String, Class<?>> compiledClasses0 = compile(sourcesMap_new);
                for (Map.Entry<String, Class<?>> stringClassEntry : compiledClasses0.entrySet()) {
                    compiledClasses.put(stringClassEntry.getKey(), stringClassEntry.getValue());
                }
            } else {
                //重试 还不行的话 输出错误信息
                for (Map.Entry<String, String> stringStringEntry : sourcesMap_new.entrySet()) {
                    String className = stringStringEntry.getKey();
                    Class<?> aClass = compileAndLoad(stringStringEntry.getValue(), className);
                    compiledClasses.put(className, aClass);
                }
            }
        }

        return compiledClasses;
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

    //static Map<String, byte[]> classBytesMap = new ConcurrentHashMap<>();
    static class HotSwapClassLoader extends ClassLoader {
        // 存储本批次已通过 defineClass 加载的类，或尚未 define 但已捕获的字节码
        // 这里沿用您现有的 mapByte 结构，但为了类引用查找，我们更关注的是
        // 确保本 ClassLoader 知道哪些类是它自己加载的。
        // 由于 loadFromBytes 已经调用了 defineClass，所以 findLoadedClass 可以检查已加载的。
        // mapByte 主要用于在 findClass 中提供字节码，但您当前的版本是直接在 loadFromBytes 中 define，所以 mapByte 实际只用于记录。
        // 为了实现批次优先查找，我们可以直接利用 mapByte 来判断类是否属于本批次。

        public HotSwapClassLoader(ClassLoader parent) {
            super(parent);
        }

        /**
         * 用于首次将编译后的字节码加载进本 ClassLoader。
         */
        public Class<?> loadFromBytes(String name, byte[] bytecode) {
            // 记录字节码，以备后续类引用查找时使用（虽然本实现直接 define 了，主要用于辅助理解）
            //classBytesMap.put(name, bytecode);
            //log.d("loadFromBytes",name);
            // 直接定义类，这是首次加载
            return defineClass(name, bytecode, 0, bytecode.length);
        }


        /**
         * 关键：实现类引用查找的“子类优先”策略。
         * 当本批次中一个类（如 ClassA）引用另一个类（如 ClassB）时，
         * JVM 会调用 ClassA 的 ClassLoader（即本 HotSwapClassLoader）的 loadClass 方法来加载 ClassB。
         */
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            Class<?> c = null;
    /*        byte[] bytes = classBytesMap.get(name);
            log.d("loadClass",name);
            if (bytes != null) {
                c = loadFromBytes(name, bytes);
            }*/
            if (c == null) {
                c = findLoadedClass(name);
            }


            if (c == null) {
                c = super.loadClass(name, false);
            }
            if (c == null) {
                throw new GzbException0("编译失败：" + name);
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }

    }
}