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
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.json.GzbJson;
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
    //外部调用入口 唯一类  v1 vxxx都是内部使用 只需要在这里处理
    public static Class<?> compileJavaCode(String code) throws Exception {
        String className = ClassTools.extractPublicClassName(code);
        return compileJavaCode(code, className);
    }

    //外部调用入口 唯一类  v1 vxxx都是内部使用 只需要在这里处理
    public static Class<?> compileJavaCode(String code, String className) throws Exception {
        try {
            Class<?> aClass0 = Class.forName(className);
            //出错说明不是目标类 需要编译
            aClass0.getMethod("_gzb_call_x01"
                    ,int.class, Map.class, Request.class, Response.class, Map.class,
                    GzbJson.class, Log.class,Object[].class);
            log.d("无需编译", aClass0.getName());
            return aClass0;
        } catch (Exception e) {
            long start = System.currentTimeMillis();
            Map<String, String> sourcesMap = new HashMap<>();
            sourcesMap.put(className, code);
            Map<String, Class<?>> map = ClassLoadV4.load(sourcesMap);
            long end = System.currentTimeMillis();
            log.d("编译耗时", end - start, map);
            return map.get(className);

        }
    }

    public static Map<String, Class<?>> compileJavaCode(Map<String, String> sourcesMap) throws Exception {
        Map<String, Class<?>> map = new ConcurrentHashMap<>();
        for (Map.Entry<String, String> stringStringEntry : sourcesMap.entrySet()) {
            Class<?> aClass = compileJavaCode(stringStringEntry.getValue(), stringStringEntry.getKey());
            map.put(stringStringEntry.getKey(), aClass);
        }
        return map;
    }



}