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
import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseConfig;
import gzb.frame.db.DataBaseImpl;
import gzb.tools.*;

import java.util.ArrayList;
import java.util.List;

public class GenerateJavaCode {
    public static void main(String[] args) throws Exception {
        String path = Config.thisPath() + "/src/main/java";
        String pkg = "com";
        String dbName = "frame";
        generateCode(path, pkg, 2, true, dbName, dbName);
    }

    public static void generateCode(String codeSrcPath, String pkg, String frameDbKey, String... userDbKeys) throws Exception {
        generateCode(codeSrcPath, pkg, 1, false, frameDbKey, userDbKeys);
    }


    ///  生成代码和ui
    /// codeSrcPath 代码生成目录
    /// pkg 生成到哪个包下 xxx.xxx.xxx
    /// type=0 生成全部表  type=1 排除 sys_xxx  type=2 只生成 sys_xxx
    /// frameDbKey 储存授权 验证 信息 等内容的数据库 key  如果只有一个数据库 frameDbKey和userDbKeys一样即可
    /// userDbKeys 业务数据库 key
    public static void generateCode(String codeSrcPath, String pkg, int type, boolean updateMapping, String frameDbKey, String... userDbKeys) throws Exception {
        DataBaseConfig dataBaseConfig = DataBaseConfig.readConfig(frameDbKey);
        DataBase frameDataBase = new DataBaseImpl(dataBaseConfig);

        String frameDbName = dataBaseConfig.getName();
        for (int i = 0; i < userDbKeys.length; i++) {
            DataBaseConfig dataBaseConfigUser = DataBaseConfig.readConfig(userDbKeys[i]);
            DataBase userDataBase = new DataBaseImpl(dataBaseConfigUser);
            String userDbName = dataBaseConfigUser.getName();
            List<TableInfo> list0 = userDataBase.getTableInfo();
            List<TableInfo> list = new ArrayList<>();
            String[] names = new String[]{
                    "sys_users_login_log", "sys_users", "sys_permission",
                    "sys_option", "sys_mapping", "sys_log",
                    "sys_group_permission", "sys_group", "sys_file"
                    , "sys_group_column", "sys_group_table"};

            for (TableInfo tableInfo : list0) {
                if (type == 0) {
                    list.add(tableInfo);
                    continue;
                }
                boolean suc = false;
                for (String name : names) {
                    if (tableInfo.name.equals(name)) {
                        suc = true;
                        break;
                    }
                }
                if (type == 1 && !suc) {
                    list.add(tableInfo);
                    continue;
                }
                if (type == 2 && suc) {
                    list.add(tableInfo);
                    continue;
                }
            }
            code(codeSrcPath, pkg, userDbKeys[i], userDbName, list, updateMapping);
            if (updateMapping) {
                Tools.updateMapping(frameDataBase, list, null);
            }

        }

        System.out.println("运行完毕");

    }

    public static void code(String codeSrcPath, String pkg, String dbKey, String dbName, List<TableInfo> list, boolean updateMapping) throws Exception {
        if (list == null || list.isEmpty()) {
            return;
        }
        gzb.frame.generate.EntityCode entityCode = new EntityCode(codeSrcPath, pkg, null);
        gzb.frame.generate.DBCode dbCode = new DBCode(codeSrcPath, pkg, null);
        gzb.frame.generate.DaoImplCode daoImplCode = new DaoImplCode(codeSrcPath, pkg, null);
        gzb.frame.generate.DaoCode daoCode = new DaoCode(codeSrcPath, pkg, null);
        //SqlToolsCode sqlToolsCode = new SqlToolsCode(codeSrcPath, pkg, null);
        gzb.frame.generate.ActionCode actionCode = new ActionCode(codeSrcPath, pkg, null);
        dbCode.start(dbKey, dbName, true);
        entityCode.start(list, true);
        daoImplCode.start(list, true);
        daoCode.start(list, true);
        //sqlToolsCode.start(list, true);
        actionCode.start(list, true);
        if (updateMapping) {
            HTMLCode htmlCode = new HTMLCode(codeSrcPath, pkg, null);
            htmlCode.start(list, true);
        }
    }
}
