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

public class HotBackgroundThreadCode extends Base {
    public HotBackgroundThreadCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code;
        for (final TableInfo tableInfo : list) {
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".thread.hot;\n" +
                    "import gzb.frame.annotation.ThreadFactory;\n" +
                    "import gzb.frame.annotation.ThreadInterval;\n" +
                    "import gzb.tools.json.Result;\n" +
                    "import gzb.tools.log.Log;\n" +
                    "import java.util.concurrent.locks.Lock;\n" +
                    "\n" +
                    "//标注 这是一个需要被框架调度的类(后台线程)\n" +
                    "//@ThreadFactory\n" +
                    "public class HotBackgroundThread {\n" +
                    "    /// 框架提供的对象  和 @service对象 都可以被注入到类变量和参数\n" +
                    "    /// 注意 这个类 在运行中 不能直接被删除 否则线程失控。（重新创建并有一致签名 即可接着影响线程的行为）\n" +
                    "    /// 之所以提供线程模型是因为 热更新 需要的对象 通过 参数传递而不一直持有  可以防止使用旧版本对象\n" +
                    "\n" +
                    "    /// 修改方法内容 不修改签名的话 可以在不丢失 状态的情况下 热更新\n" +
                    "    /// 修改签名的话会丢失状态 重新开始调度\n" +
                    "    /// 私有变量 不会有并发问题 但是多次被调用时 会记录状态  专属 result\n" +
                    "    /// 方法专属锁 lock  仅该方法内部使用 启用多个线程的时候 防止并发  和其他被调度的方法 不是同一个锁\n" +
                    "    /// 定义 这是一个需要被调度的方法 num是启动线程数量  value 是调度周期 单位毫秒\n" +
                    "    @ThreadInterval(num=0 ,value=1000)\n" +
                    "    public Object test001(Result result, Log log, Lock lock) throws Exception {\n" +
                    "        //sysUsersDao @service 实现类\n" +
                    "        int intx=result.getInteger(\"intx\",0);\n" +
                    "        intx++;\n" +
                    "        result.set(\"intx\",intx);\n" +
                    "        log.d(intx,lock,result.hashCode());\n" +
                    "        return null;\n" +
                    "        /// 继续循环 如果不需要主动停止 可以直接设置为 void方法  返回非null 即可停止被框架调度 本次启动无法再次被调度 除非方法签名出现变法\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";
            saveFile(getFilePath(this.pkg + "." + tableInfo.getDbNameLowerCase() + ".thread.hot",
                    "HotBackgroundThread"), code, save);
        }
    }
}
//HotBackgroundThread