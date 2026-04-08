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
 *//*

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
import java.util.*;

*/
/**
 * 重构后：
 * 1. 废弃批量编译逻辑（仅保留批量传入形式）
 * 2. 每个类使用独立的HotSwapClassLoader（彻底隔离类加载上下文）
 * 3. 移除无效的“子类优先”加载逻辑，简化加载器
 * 4. 复用原有核心编译代码，保证兼容性
 *//*

public class ClassLoadV3 {
    public Log log = Log.log;

    public Map<String, Class<?>> compileAndLoad(Map<String, String> sourcesMap) throws Exception {
        Map<String, Class<?>> compiledClasses = new HashMap<>();
        // 核心：批量传入 → 循环单次编译（每个类独立处理）
        for (Map.Entry<String, String> entry : sourcesMap.entrySet()) {
            String className = entry.getKey();
            String javaCode = entry.getValue();
            // 调用单次编译方法，每个类生成独立加载器
            Class<?> clazz = compileAndLoad0(javaCode, className);
            compiledClasses.put(className, clazz);
        }
        return compiledClasses;
    }

    public Class<?> compileAndLoad0(String javaCode, String className) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("无法获取 Java 编译器，请确保 JDK 已正确安装。");
        }

        // 1. 准备编译源文件
        JavaFileObject source = new JavaSourceFromString(className, javaCode);
        JavaClassInMemory classInMemory = new JavaClassInMemory(className, JavaFileObject.Kind.CLASS);
        List<JavaFileObject> compilationUnits = Collections.singletonList(source);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        // 2. 定制文件管理器（捕获字节码）
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager fileManager = new ForwardingJavaFileManager<StandardJavaFileManager>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
                return classInMemory;
            }
        };

        // 3. 编译选项（保持原有逻辑）
        List<String> options = new ArrayList<>();
        options.add("-parameters");
        options.add("-encoding");
        options.add(Config.encoding.name());
        String jarPath = System.getProperty("java.class.path");
        String separator = File.pathSeparator;
        String completeClasspath = jarPath + separator + ".";
        options.add("-classpath");
        options.add(completeClasspath);

        // 4. 执行编译
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean success = task.call();
        if (!success) {
            StringBuilder errorMessage = new StringBuilder();
            diagnostics.getDiagnostics().forEach(d -> errorMessage.append(d.getMessage(null)).append("\n"));
            throw new Exception("编译失败：\r\n" + errorMessage + "\r\n" + className + "\r\n" + javaCode);
        }

        // 5. 校验字节码
        byte[] bytes = classInMemory.getBytes();
        if (bytes.length == 0) {
            throw new Exception("编译后的字节码为空，可能存在依赖缺失或语法错误");
        }

        // 核心：每个类创建独立的加载器（父加载器为当前上下文类加载器）
        HotSwapClassLoader classLoader = new HotSwapClassLoader(Thread.currentThread().getContextClassLoader());
        return classLoader.loadFromBytes(className, bytes);
    }

    public Class<?> compileAndLoad(String javaCode, String className) throws Exception {
        Map<String, String> sourcesMap = new HashMap<>();
        sourcesMap.put(className, javaCode);
        return compileAndLoad(sourcesMap).get(className);
    }

    public Class<?> loadClassFromBytes(String className, byte[] classBytes) throws Exception {
        if (classBytes == null || classBytes.length == 0) {
            throw new IllegalArgumentException("类字节码数组不能为空或空数组");
        }
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("类全限定名不能为空");
        }
        // 每个类创建独立加载器
        HotSwapClassLoader classLoader = new HotSwapClassLoader(Thread.currentThread().getContextClassLoader());
        return classLoader.loadFromBytes(className, classBytes);
    }

    public Map<String, Class<?>> loadClassFromBytesBatch(Map<String, byte[]> byteMap) throws Exception {
        Map<String, Class<?>> classMap = new HashMap<>(byteMap.size());
        // 核心：批量传入 → 循环单次加载（每个类独立加载器）
        for (Map.Entry<String, byte[]> entry : byteMap.entrySet()) {
            String className = entry.getKey();
            byte[] classBytes = entry.getValue();
            if (classBytes != null && classBytes.length > 0) {
                try {
                    Class<?> clazz = loadClassFromBytes(className, classBytes);
                    classMap.put(className, clazz);
                } catch (Throwable t) {
                    log.e("批量加载字节码失败", className, t.getMessage());
                    throw new Exception("加载类[" + className + "]字节码失败", t);
                }
            } else {
                log.w("批量加载字节码", "类字节码为空", className);
            }
        }
        return classMap;
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

    static class HotSwapClassLoader extends ClassLoader {
        public HotSwapClassLoader(ClassLoader parent) {
            super(parent);
        }
        public Class<?> loadFromBytes(String name, byte[] bytecode) {
            if (bytecode == null || bytecode.length == 0) {
                throw new IllegalArgumentException("类字节码为空：" + name);
            }
            // 直接定义类，每个类归属于当前独立加载器
            return defineClass(name, bytecode, 0, bytecode.length);
        }
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                c = super.loadClass(name, resolve);
            }
            if (c == null) {
                throw new GzbException0("类加载失败：" + name);
            }
            return c;
        }
    }

}*/
