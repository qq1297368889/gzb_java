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

package gzb.frame.generate;

import gzb.entity.TableInfo;

import java.util.List;

public class DecoratorCode extends Base {
    public DecoratorCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code;
        for (final TableInfo tableInfo : list) {
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".decorator;\n"+
                    "\n" +
                    "import gzb.entity.RunRes;\n" +
                    "import gzb.frame.annotation.*;\n" +
                    "import gzb.frame.netty.entity.Request;\n" +
                    "import gzb.frame.netty.entity.Response;\n" +
                    "import gzb.tools.json.GzbJson;\n" +
                    "import gzb.tools.log.Log;\n" +
                    "\n" +
                    "\n" +
                    "//标注 这是一个装饰器\n" +
                    "//@Decorator\n" +
                    "public class ControllerDecorator {\n" +
                    "\n" +
                    "    /// 依赖注入规则 和 控制器 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）\n" +
                    "    /// 装饰器 @DecoratorStart(value = \"/xxx/\", sort = 0, type = false, method = {\"GET\", \"POST\", \"PUT\", \"DELETE\"}) 说明\n" +
                    "    //在请求进入前执行装饰器 如需生效条件为 1. 路径匹配正确 2.对应端点有标注 @DecoratorOpen()\n" +
                    "    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截\n" +
                    "    //type 指定为 true  只要链接包含就会拦截 不再只进行前缀匹配\n" +
                    "    //turn 指定为 true  原包含匹配 改为不包含匹配\n" +
                    "    //method 要匹配的方法 GET POST PUT DELETE\n" +
                    "    //sort排序 越小越先执行\n" +
                    "    /// @DecoratorEnd 和 @DecoratorStart 规则基本一致 可以看下方代码示例的描述\n" +
                    "    @Resource\n" +
                    "    Log log;\n" +
                    "    //可以定义多个对象 被自动注入\n" +
                    "    //@Resource\n" +
                    "    //XxxDao xxDao;\n" +
                    "\n" +
                    "    //在请求进入前执行装饰器 如需生效条件为 1. 路径匹配正确 2.对应端点有标注 @DecoratorOpen()\n" +
                    "    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截\n" +
                    "    //type 指定为 true  只要链接包含就会拦截 不再只进行前缀匹配\n" +
                    "    //turn 指定为 true  原包含匹配 改为不包含匹配\n" +
                    "    //method 要匹配的方法 GET POST PUT DELETE\n" +
                    "    //sort排序 越小越先执行\n" +
                    "    @DecoratorStart(value = \"/system/\", sort = 0, type = false, method = {\"GET\", \"POST\", \"PUT\", \"DELETE\"})\n" +
                    "    public RunRes start0(Request request, Response response, RunRes runRes, GzbJson gzbJson) {\n" +
                    "        log.i(\"start0\");\n" +
                    "        /// runRes.intercept(); 拦截 且无输出\n" +
                    "        //runRes.intercept(gzbJson.fail(\"调用被拦截\"));\n" +
                    "        //runRes.release(new SysUsers());//直接放行  且输入自定义对象到上下文 这个对象可以被后续处理器 控制器 等一系列 组件获取\n" +
                    "        //runRes.release();//直接放行不向后续处理器传递对象\n" +
                    "        // 这个对象可以被后续处理器 哦不对 装饰器 在参数中接收到\n" +
                    "        // 注意 是和service一样的类型匹配不要有重复的类型避免混淆\n" +
                    "        return runRes;\n" +
                    "    }\n" +
                    "\n" +
                    "    //这个装饰器因为 顺序是1 所以会在上一个装饰器 0 之后执行\n" +
                    "    // 也就是说 这个装饰器会在上一个装饰器放行之后执行  如果上一个装饰器没有放行 那么这个装饰器就不会执行\n" +
                    "    @DecoratorStart(value = \"/system/\", sort = 1, type = false, method = {\"GET\", \"POST\", \"PUT\", \"DELETE\"})\n" +
                    "    public RunRes start1(Request request, Response response, RunRes runRes, GzbJson gzbJson) {\n" +
                    "        log.i(\"start1\");\n" +
                    "        /// runRes.intercept(); 拦截 且无输出\n" +
                    "        //runRes.intercept(gzbJson.fail(\"调用被拦截\"));\n" +
                    "        //runRes.release(new SysUsers());//直接放行  且输入自定义对象到上下文 这个对象可以被后续处理器 控制器 等一系列 组件获取\n" +
                    "        // 这个对象可以被后续处理器 哦不对 装饰器 在参数中接收到\n" +
                    "        // 注意 是和service一样的类型匹配不要有重复的类型避免混淆\n" +
                    "        return runRes;\n" +
                    "    }\n" +
                    "\n" +
                    "    //在请求进入前执行装饰器 如需生效条件为 1. 路径匹配正确 2.对应端点有标注 @DecoratorOpen()\n" +
                    "    //规则和 @DecoratorStart 一样 只是这个是在请求处理完成后执行的 也就是说 这个装饰器会在控制器方法执行完成后执行  这个时候 runRes 中就有了控制器方法的返回值了\n" +
                    "    @DecoratorEnd(value = \"/system/\", sort = 1, type = false, method = {\"GET\", \"POST\", \"PUT\", \"DELETE\"})\n" +
                    "    public RunRes end0(Request request, Response response, RunRes runRes, GzbJson gzbJson) {\n" +
                    "        log.i(\"end0\");\n" +
                    "        // runRes.getData(); //这是响应数据 可以修改 修改后返回即可\n" +
                    "        /// runRes.intercept(); 拦截 且无输出\n" +
                    "        //runRes.intercept(gzbJson.fail(\"调用被拦截\"));//拦截 修改响应\n" +
                    "        return runRes;\n" +
                    "    }\n" +
                    "\n" +
                    "    //规则和 @DecoratorStart 一样 只是这个是在请求处理完成后执行的 也就是说 这个装饰器会在控制器方法执行完成后执行  这个时候 runRes 中就有了控制器方法的返回值了\n" +
                    "    @DecoratorEnd(value = \"/system/\", sort = 1, type = false, method = {\"GET\", \"POST\", \"PUT\", \"DELETE\"})\n" +
                    "    public RunRes end1(Request request, Response response, RunRes runRes, GzbJson gzbJson) {\n" +
                    "        log.i(\"end1\");\n" +
                    "        // runRes.getData(); //这是响应数据 可以修改 修改后返回即可\n" +
                    "        /// runRes.intercept(); 拦截 且无输出\n" +
                    "        //runRes.intercept(gzbJson.fail(\"调用被拦截\"));//拦截 修改响应\n" +
                    "        return runRes;\n" +
                    "    }\n" +
                    "}\n";
            saveFile(getFilePath(this.pkg + "." + tableInfo.getDbNameLowerCase() + ".decorator",
                    "ControllerDecorator"), code, save);
        }
    }
}