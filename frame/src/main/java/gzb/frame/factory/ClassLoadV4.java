
/*
 * Copyright [2025] [GZB ONE]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gzb.frame.factory;

import gzb.exception.GzbException0;
import gzb.frame.server.tcp.ByteTools;
import gzb.tools.Config;
import gzb.tools.log.Log;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

public class ClassLoadV4 {
    public static Log log = Log.log;

    public static Map<String, Class<?>> load(String className, String javaCode) throws Exception {
        Map<String, String> sourceMap = new HashMap<>(1);
        sourceMap.put(className, javaCode);
        return load(sourceMap);
    }

    public static Map<String, Class<?>> load(String className, byte[] classByte) throws Exception {
        Map<String, byte[]> byteMap = new HashMap<>(1);
        byteMap.put(className, classByte);
        return loadFromBytes(byteMap);
    }

    public static Map<String, Class<?>> load(Map<String, String> nameJavaCodeMap) throws Exception {
        if (nameJavaCodeMap == null || nameJavaCodeMap.isEmpty()) {
            throw new IllegalArgumentException("源码Map不能为空或空集合");
        }
/// 检查编译缓存
        Map<String, Class<?>> compiledClasses = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = nameJavaCodeMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            Map<String, byte[]> map = ClassByteTools.readBytes(entry.getKey(), nameJavaCodeMap);
            if (map.size() > 0) {
                Map<String, Class<?>> maps = loadFromBytes(map);
                maps.forEach((k, v) -> {
                    compiledClasses.put(k, v);
                });
                iterator.remove();//保留未缓存的类 编译
            }
        }
        if (nameJavaCodeMap.size()<1) {
            return compiledClasses;
        }
        // 1. 获取JDK编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("无法获取Java编译器，请确保运行环境为JDK（非JRE）");
        }

        // 2. 准备编译单元（纯内存，不落盘）
        List<JavaFileObject> compilationUnits = new ArrayList<>();
        for (Map.Entry<String, String> entry : nameJavaCodeMap.entrySet()) {
            String className = entry.getKey();
            String javaCode = entry.getValue();
            compilationUnits.add(new JavaSourceFromString(className, javaCode));
        }

        // 3. 内存字节码收集器（捕获主类+内部类）
        Map<String, JavaClassInMemory> classByteCollector = new HashMap<>();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        // 4. 定制文件管理器（纯内存输出，不落盘）
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, Charset.forName(Config.encoding.name()));
        JavaFileManager fileManager = new ForwardingJavaFileManager<StandardJavaFileManager>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
                // 捕获所有CLASS类型输出（主类、内部类、匿名类）
                if (kind == JavaFileObject.Kind.CLASS) {
                    JavaClassInMemory classInMemory = new JavaClassInMemory(className, kind);
                    classByteCollector.put(className, classInMemory);
                    return classInMemory;
                }
                return super.getJavaFileForOutput(location, className, kind, sibling);
            }
        };

        // 5. 编译选项配置
        List<String> options = new ArrayList<>();
        options.add("-parameters");          // 保留方法参数名
        options.add("-encoding");            // 编码配置
        options.add(Config.encoding.name());
        options.add("-classpath");           // 类路径（复用当前运行环境）
        options.add(System.getProperty("java.class.path") + File.pathSeparator + ".");

        // 6. 执行编译
        JavaCompiler.CompilationTask task = compiler.getTask(
                null, fileManager, diagnostics, options, null, compilationUnits
        );
        boolean compileSuccess = task.call();

        // 7. 编译失败处理
        if (!compileSuccess) {
            StringBuilder errorMsg = new StringBuilder("编译失败：\n");
            diagnostics.getDiagnostics().forEach(d -> errorMsg.append(d.getMessage(null)).append("\n"));
            throw new Exception(errorMsg.toString());
        }

        // 8. 批量加载字节码（每个类独立加载器）
        Map<String, byte[]> byteMap = new HashMap<>();
        for (Map.Entry<String, JavaClassInMemory> entry : classByteCollector.entrySet()) {
            String className = entry.getKey();
            byte[] classBytes = entry.getValue().getBytes();
            if (classBytes.length > 0) {
                byteMap.put(className, classBytes);
                ClassByteTools.saveBytes(className,nameJavaCodeMap,classBytes);
            } else {
                log.w("ClassLoadV4", "类字节码为空，跳过加载", className);
            }
        }

        Map<String, Class<?>> compiledClasses2 = loadFromBytes(byteMap);
        for (Map.Entry<String, Class<?>> stringClassEntry : compiledClasses2.entrySet()) {
            compiledClasses.put(stringClassEntry.getKey(), stringClassEntry.getValue());
        }
        // 9. 加载字节码并返回结果
        return compiledClasses;
    }

    public static Map<String, Class<?>> loadFromBytes(Map<String, byte[]> byteMap) throws Exception {
        Map<String, Class<?>> resultMap = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : byteMap.entrySet()) {
            String className = entry.getKey();
            byte[] classBytes = entry.getValue();

            if (classBytes == null || classBytes.length == 0) {
                log.w("ClassLoadV4", "字节码为空，跳过加载", className);
                continue;
            }

            // 核心：每个类创建独立的加载器（彻底隔离）
            HotSwapClassLoader classLoader = new HotSwapClassLoader();
            Class<?> clazz = classLoader.loadFromBytes(className, classBytes);
            resultMap.put(className, clazz);
        }
        return resultMap;
    }

    public static class JavaSourceFromString extends SimpleJavaFileObject {
        private final String code;

        public JavaSourceFromString(String className, String code) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    public static class JavaClassInMemory extends SimpleJavaFileObject {
        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        public JavaClassInMemory(String className, Kind kind) {
            super(URI.create("string:///" + className.replace('.', '/') + kind.extension), kind);
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return bos;
        }

        public byte[] getBytes() {
            return bos.toByteArray();
        }
    }

    public static class HotSwapClassLoader extends ClassLoader {
        /**
         * 构造器：父加载器绑定当前上下文类加载器（保证归属应用类加载器层级）
         */
        public HotSwapClassLoader(ClassLoader parent) {
            super(parent);
        }
        /**
         * 强制父加载器为AppClassLoader（主加载器），复刻同项目的加载器层级
         */
        public HotSwapClassLoader() {
            // 核心：ClassLoader.getSystemClassLoader() = AppClassLoader（主加载器）
            super(ClassLoader.getSystemClassLoader());
            // 验证：确保父加载器不是ExtClassLoader
            if (getParent().getClass().getName().contains("ExtClassLoader") ||
                    getParent().getClass().getName().contains("PlatformClassLoader")) {
                throw new IllegalStateException("自定义加载器父加载器错误，应为AppClassLoader");
            }
        }

        /**
         * 内存加载字节码（不落盘）
         *
         * @param className  类全限定名
         * @param classBytes 类字节码
         * @return 加载后的Class实例
         */
        public Class<?> loadFromBytes(String className, byte[] classBytes) {
            if (className == null || className.trim().isEmpty()) {
                throw new IllegalArgumentException("类名不能为空");
            }
            if (classBytes == null || classBytes.length == 0) {
                throw new IllegalArgumentException("类字节码不能为空");
            }
            // 纯内存定义类，每个类归属当前独立加载器
            return defineClass(className, classBytes, 0, classBytes.length);
        }
        /**
         * 重写loadClass：核心优化点
         * 1. 核心包（如entity）强制走父加载器
         * 2. 动态类走当前加载器
         */
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            // 加锁避免并发加载问题
            synchronized (getClassLoadingLock(name)) {
                Class<?> clazz = findLoadedClass(name);
                if (clazz != null) {
                    if (resolve) resolveClass(clazz);
                    return clazz;
                }

                clazz = super.loadClass(name, resolve);
                if (resolve) resolveClass(clazz);
                return clazz;
            }
        }

    }

}