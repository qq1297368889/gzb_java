
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
import java.util.concurrent.ConcurrentHashMap;

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
                iterator.remove();
            }
        }
        if (nameJavaCodeMap.size() < 1) {
            return compiledClasses;
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("无法获取Java编译器，请确保运行环境为JDK（非JRE）");
        }

        List<JavaFileObject> compilationUnits = new ArrayList<>();
        for (Map.Entry<String, String> entry : nameJavaCodeMap.entrySet()) {
            String className = entry.getKey();
            String javaCode = entry.getValue();
            compilationUnits.add(new JavaSourceFromString(className, javaCode));
        }

        Map<String, JavaClassInMemory> classByteCollector = new HashMap<>();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
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

        List<String> options = new ArrayList<>();
        options.add("-Xdiags:verbose");
        options.add("-parameters");          // 保留方法参数名
        options.add("-encoding");            // 编码配置
        options.add(Config.encoding.name());
        options.add("-classpath");           // 类路径（复用当前运行环境）
        options.add(System.getProperty("java.class.path") + File.pathSeparator + ".");

        JavaCompiler.CompilationTask task = compiler.getTask(
                null, fileManager, diagnostics, options, null, compilationUnits
        );
        boolean compileSuccess = task.call();

        if (!compileSuccess) {
            StringBuilder errorMsg = new StringBuilder("编译失败：\n");
            diagnostics.getDiagnostics().forEach(d -> errorMsg.append(d.getMessage(null)).append("\n"));
            throw new Exception(errorMsg.toString());
        }

        Map<String, byte[]> byteMap = new HashMap<>();
        for (Map.Entry<String, JavaClassInMemory> entry : classByteCollector.entrySet()) {
            String className = entry.getKey();
            byte[] classBytes = entry.getValue().getBytes();
            if (classBytes.length > 0) {
                byteMap.put(className, classBytes);
                ClassByteTools.saveBytes(className, nameJavaCodeMap, classBytes);
            } else {
                log.w("ClassLoadV4", "类字节码为空，跳过加载", className);
            }
        }

        Map<String, Class<?>> compiledClasses2 = loadFromBytes(byteMap);
        for (Map.Entry<String, Class<?>> stringClassEntry : compiledClasses2.entrySet()) {
            compiledClasses.put(stringClassEntry.getKey(), stringClassEntry.getValue());
        }
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
            HotSwapClassLoader classLoader = new HotSwapClassLoader(byteMap);
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
        Map<String, byte[]> byteMap=null;
        public HotSwapClassLoader(ClassLoader parent) {
            super(parent);
        }

        public HotSwapClassLoader(Map<String, byte[]> byteMap) {
            super(ClassLoader.getSystemClassLoader());
            if (this.byteMap==null) {
                this.byteMap=new HashMap<>();
            }
            for (Map.Entry<String, byte[]> stringEntry : byteMap.entrySet()) {
                this.byteMap.put(stringEntry.getKey(), stringEntry.getValue());
            }
        }
        public Class<?> loadFromBytes(String className, byte[] classBytes) {
            if (className == null || className.trim().isEmpty()) {
                throw new IllegalArgumentException("类名不能为空");
            }
            if (classBytes == null || classBytes.length == 0) {
                throw new IllegalArgumentException("类字节码不能为空");
            }
            return defineClass(className, classBytes, 0, classBytes.length);
        }
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                byte[] c_bs = byteMap.remove(name);
                if (c_bs != null) {
                    c = defineClass(name, c_bs, 0, c_bs.length);
                }
            }
            if (c == null) {
                c = super.loadClass(name, resolve);
            }
            return c;
        }

    }

}