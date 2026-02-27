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

public class DataBaseEventCode extends Base {
    public DataBaseEventCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code;
        for (final TableInfo tableInfo : list) {
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".event;\n" +
                    "\n" +
                    "import com."+ tableInfo.getDbNameLowerCase() +".dao.*;\n" +
                    "import com."+ tableInfo.getDbNameLowerCase() +".entity.*;\n" +
                    "import gzb.frame.annotation.*;\n" +
                    "import gzb.frame.netty.entity.Request;\n" +
                    "import gzb.tools.Tools;\n" +
                    "import gzb.tools.log.Log;\n" +
                    "\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "// 标注为 数据库事件注册 类\n" +
                    "// 这个注释掉 就不会触发事件了 因为可能会影响性能 所以需要手动开启而非自动开启\n" +
                    "// 为什么要生成这个类呢 主要是为了提供代码即为文档的功能 让用户更清晰的知道怎么注册事件\n" +
                    "// @DataBaseEventFactory\n" +
                    "public class " + tableInfo.getNameHumpUpperCase() + "Event {\n" +
                    "\n" +
                    "    /// 依赖注入规则 和 controller 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）\n" +
                    "\n" +
                    "    @Resource\n" +
                    "    Log log;\n" +
                    "    @Resource\n" +
                    "    " + tableInfo.getNameHumpUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao;//不仅限于这个对应的dao 其他任何 service都可以被注入\n" +
                    "\n" +
                    "    //entity 注册到 某个实体类 数据库 新增事件\n" +
                    "    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false\n" +
                    "    //depth 事件传播深度 防止循环传播\n" +
                    "    @DataBaseEventUpdate(entity = "+ tableInfo.getNameHumpUpperCase() +".class, executionBefore = false, depth = 5)\n" +
                    "    public void update(\n" +
                    "            "+ tableInfo.getNameHumpUpperCase() +" " + tableInfo.getNameHumpLowerCase() +", //对应的实体类对象\n" +
                    "            Request request //来自上下文中的对象 和 controller 注入规则一致\n" +
                    "    ) throws Exception {\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    @DataBaseEventSelect(entity = "+ tableInfo.getNameHumpUpperCase() +".class, executionBefore = false, depth = 5)\n" +
                    "    public void select("+ tableInfo.getNameHumpUpperCase() +" " + tableInfo.getNameHumpLowerCase() +") throws Exception {\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    @DataBaseEventSave(entity = "+ tableInfo.getNameHumpUpperCase() +".class, executionBefore = false, depth = 5)\n" +
                    "    public void save("+ tableInfo.getNameHumpUpperCase() +" " + tableInfo.getNameHumpLowerCase() +") throws Exception {\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    @DataBaseEventDelete(entity = "+ tableInfo.getNameHumpUpperCase() +".class, executionBefore = false, depth = 5)\n" +
                    "    public void delete("+ tableInfo.getNameHumpUpperCase() +" " + tableInfo.getNameHumpLowerCase() +") throws Exception {\n" +
                    "\n" +
                    "    }\n" +
                    "}";
            saveFile(getFilePath(this.pkg + "." + tableInfo.getDbNameLowerCase() + ".event", tableInfo.getNameHumpUpperCase() + "Event"), code, save);
        }
    }
}

//Decorator