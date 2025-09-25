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

public class DaoImplCode extends Base {
    public DaoImplCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code;
        for (final TableInfo tableInfo : list) {
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao.impl;\n" +
                    "import gzb.frame.annotation.Service;\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao." + tableInfo.getNameHumpUpperCase() + "Dao;\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".entity." + tableInfo.getNameHumpUpperCase() + ";\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameHumpLowerCase() + "." + tableInfo.getDbNameHumpUpperCase() + ";\n" +
                    "@Service\n" +
                    "public class " + tableInfo.getNameHumpUpperCase() + "DaoImpl extends " + tableInfo.getDbNameHumpUpperCase() + "<" + tableInfo.getNameHumpUpperCase() + "> implements " + tableInfo.getNameHumpUpperCase() + "Dao{\n" +
                    "\n" +
                    "}";
            saveFile(getFilePath(this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao.impl", tableInfo.getNameHumpUpperCase() + "DaoImpl"), code, save);
        }
    }
}