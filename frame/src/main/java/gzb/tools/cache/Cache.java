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

package gzb.tools.cache;

import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.Tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    //默认 缓存 具体实现取决于配置文件
    public static GzbCache gzbCache=Config.cache_gzbCache;
    //session 缓存 具体实现取决于配置文件
    public static GzbCache session=Config.cache_session;
    //数据库查询 缓存 具体实现取决于配置文件
    public static GzbCache dataBaseCache=Config.cache_dataBaseCache;
    //全局缓存 是map实现 避免一些对象无法存入redis 并且重启会消失
    public static GzbCache gzbMap=Config.cache_gzbMap;
}
