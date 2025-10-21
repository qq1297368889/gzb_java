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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类加载器，用于动态编译和加载 Groovy 或 Java 代码。
 * 本方案为每次加载使用独立的类加载器，以支持热更新。
 */
public class ClassLoad {
    public static Log log = Log.log;
    //public static final ClassLoadV2 load = new ClassLoadV2();
    //public static final ClassLoadV1 load = new ClassLoadV1();

    // --- 现有方法：用于单个源代码编译和加载 ---

    /**
     * 从字符串编译 Java 代码。
     *
     * @param javaCode Java 源代码字符串
     * @return 编译后的 Class 对象
     * @throws Exception 编译或加载失败
     */
    public static Class<?> compileJavaCode(String javaCode) throws Exception {
        String className = extractPublicClassName(javaCode);
        return compileJavaCode(javaCode,className);
    }
    public static Class<?> compileJavaCode(String javaCode,String className) throws Exception {
        long start = System.currentTimeMillis();
        Class<?> class1 = new ClassLoadV1().compileAndLoad(javaCode, className);
        long end = System.currentTimeMillis();
        log.d("编译耗时", end - start, class1.getName());
        return class1;
    }
    public static Map<String, Class<?>> compileJavaCode(Map<String, String> sourcesMap) throws Exception {
        long start = System.currentTimeMillis();
        Map<String, Class<?>> map = new ClassLoadV1().compileAndLoad(sourcesMap);
        long end = System.currentTimeMillis();
        log.d("编译耗时", end - start, map.size(), "个类");
        return map;
    }

    public static Map<String, Class<?>> compileJavaCode0(Map<String, String> sourcesMap) throws Exception {
        Map<String, Class<?>> map = new HashMap<>();
        for (Map.Entry<String, String> stringStringEntry : sourcesMap.entrySet()) {
            map.put(stringStringEntry.getKey(), compileJavaCode(stringStringEntry.getValue(),stringStringEntry.getKey()));
        }
        return map;
    }
    /**
     * 快速提取公共类名，用于命名虚拟文件。
     * (保持原样，未修改)
     */
    public static String extractPublicClassName(String javaCode) {
        String packageName = "";
        String publicClassName = "InMemory"; // 默认名称

        Pattern packagePattern = Pattern.compile("package\\s+([a-zA-Z0-9_.]+)\\s*;", Pattern.MULTILINE);
        Matcher packageMatcher = packagePattern.matcher(javaCode);
        if (packageMatcher.find()) {
            packageName = packageMatcher.group(1).trim();
        }

        Pattern classPattern = Pattern.compile(
                "public\\s+(?:(?:abstract|final)\\s+)?(?:class|interface|enum)\\s+([a-zA-Z0-9_$]+)",
                Pattern.MULTILINE);
        Matcher classMatcher = classPattern.matcher(javaCode);
        if (classMatcher.find()) {
            publicClassName = classMatcher.group(1).trim();
        }
        String name = packageName.isEmpty() ? publicClassName : packageName + "." + publicClassName;
        return name;
    }

}