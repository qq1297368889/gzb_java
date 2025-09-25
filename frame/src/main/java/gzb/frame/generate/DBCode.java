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
import gzb.tools.Tools;

public class DBCode extends Base {
    public DBCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(String dbKey, String dbName, boolean save) {
        String code = "package " + this.pkg + "." + Tools.lowStr_hump(dbName) + ";\n" +
                "\n" +
                "import gzb.frame.db.BaseDaoImpl;\n" +
                "public class " + Tools.lowStr_d(Tools.lowStr_hump(dbName)) + "<T> extends BaseDaoImpl<T> {\n" +
                "    //数据库信息 在这里指定\n" +
                "    public " + Tools.lowStr_d(Tools.lowStr_hump(dbName)) + "() {\n" +
                "        try {\n" +
                "            //对应配置文件里的 db.mysql.xxx 的 xxx\n" +
                "            init(\"" + dbKey + "\");\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "}";
        saveFile(getFilePath(this.pkg + "." + Tools.lowStr_hump(dbName), Tools.lowStr_d(Tools.lowStr_hump(dbName))), code, save);
    }
}