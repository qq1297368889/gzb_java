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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateJavaCode {
    public static void main(String[] args) throws Exception {
        String path = Config.thisPath() + "/src/main/java";
        String pkg = "com";
        String dbName = "frame";
        generateCode(path, pkg, 2, true, dbName, dbName);
    }
    public static void generateCodeV0(String codeSrcPath, String pkg, String dbKey) throws Exception {
        DataBaseConfig dataBaseConfig = DataBaseConfig.readConfig(dbKey);
        DataBase dataBase = new DataBaseImpl(dataBaseConfig);
        List<TableInfo> list0 = dataBase.getTableInfo();
        code(codeSrcPath, pkg, dbKey, dataBaseConfig.getName(), list0, false);
        System.out.println("运行完毕");

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
                    "sys_option", "sys_option_request", "sys_option_sql", "sys_log",
                    "sys_group_permission", "sys_group", "sys_file"
                    , "sys_group_column", "sys_group_table", "sys_role", "sys_role_group"
                    , "sys_role_table", "sys_role_column", "sys_mapping_table", "sys_mapping_column"};
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
                update_mapping(frameDataBase, list);
                update_permission(frameDataBase, list);
            }

        }

        System.out.println("运行完毕");

    }

    public static void code(String codeSrcPath, String pkg, String dbKey, String dbName, List<TableInfo> list, boolean updateMapping) throws Exception {
        if (list == null || list.isEmpty()) {
            return;
        }
        EntityCode entityCode = new EntityCode(codeSrcPath, pkg, null);
        DBCode dbCode = new DBCode(codeSrcPath, pkg, null);
        DaoImplCode daoImplCode = new DaoImplCode(codeSrcPath, pkg, null);
        DaoCode daoCode = new DaoCode(codeSrcPath, pkg, null);
        ActionCode actionCode = new ActionCode(codeSrcPath, pkg, null);
        JavaScriptCode javaScriptCode = new JavaScriptCode(codeSrcPath, pkg, null);
        dbCode.start(dbKey, dbName, true);
        entityCode.start(list, true);
        daoImplCode.start(list, true);
        daoCode.start(list, true);
        actionCode.start(list, true);
        if (updateMapping) {
            javaScriptCode.start(list);
        }
    }

    public static void update_permission(DataBase dataBase, List<TableInfo> listTableInfo) throws Exception {
        List<GzbMap> listGzbMap = null;
        Long id = null;
        String sql;
        id = null;
        for (TableInfo tableInfo : listTableInfo) {
            if (id == null) {
                listGzbMap = dataBase.selectGzbMap("select sys_permission_id from sys_permission where sys_permission_name=? and sys_permission_sup=? and sys_permission_type = ?", new Object[]{tableInfo.dbNameLowerCase, 0, 1});
                if (listGzbMap.size() == 0) {
                    id = OnlyId.getDistributed();
                    sql = "INSERT INTO sys_permission(" +
                            "sys_permission_id, sys_permission_name, sys_permission_data, sys_permission_type, " +
                            "sys_permission_desc, sys_permission_sup, sys_permission_sort) VALUES (" +
                            "" + id + ", '" + tableInfo.dbNameLowerCase + "', NULL, 1, " +
                            "NULL, 0, 0)";
                    dataBase.runSqlAsync(sql, null);
                } else {
                    id = listGzbMap.get(0).getLong("sysPermissionId");
                }
            }


            listGzbMap = dataBase.selectGzbMap("select sys_permission_id from sys_permission where sys_permission_name=? and sys_permission_sup=? and sys_permission_type = ?", new Object[]{tableInfo.nameHumpLowerCase, id, 1});
            if (listGzbMap.size() == 0) {
                sql = "INSERT INTO sys_permission(" +
                        "sys_permission_id, sys_permission_name, sys_permission_data, sys_permission_type, " +
                        "sys_permission_desc, sys_permission_sup, sys_permission_sort) VALUES (" +
                        OnlyId.getDistributed() + ", '" + tableInfo.nameHumpLowerCase + "', 'list.html?config=" + tableInfo.nameHumpLowerCase + "', 1, " +
                        "NULL, " + id + ", 0)";
                dataBase.runSqlAsync(sql, null);
            }
        }
    }

    //生成基本 mapping
    public static void update_mapping(DataBase dataBase, List<TableInfo> listTableInfo) throws Exception {
        List<GzbMap> listGzbMap = null;
        Long id = null;
        String sql;
        Map<String, String> map0 = new HashMap<>();
        for (TableInfo tableInfo : listTableInfo) {
            listGzbMap = dataBase.selectGzbMap("select sys_mapping_table_id from sys_mapping_table where sys_mapping_table_name=?", new Object[]{tableInfo.nameHumpLowerCase});
            map0.put(tableInfo.nameHumpLowerCase, "0");
            if (listGzbMap.size() == 0) {
                id = OnlyId.getDistributed();
                sql = "INSERT INTO sys_mapping_table(sys_mapping_table_id, sys_mapping_table_name, sys_mapping_table_title) VALUES (" + id + ", '" + tableInfo.nameHumpLowerCase + "', '" + tableInfo.nameHumpLowerCase + "');";
                dataBase.runSqlAsync(sql, null);
            } else {
                id = listGzbMap.get(0).getLong("sysMappingTableId");
            }
            //把每个表的请求引用 都加入 选项引用列表  sys_option_request_url
            String key=tableInfo.nameHumpLowerCase;
            listGzbMap = dataBase.selectGzbMap("select * from sys_option_request where sys_option_request_key = ?", new Object[]{key});
            System.out.println(key+" "+listGzbMap+"  "+listGzbMap.size());
            if (listGzbMap.size() == 0) {
                sql="INSERT INTO sys_option_request(" +
                        "sys_option_request_id, " +
                        "sys_option_request_url, sys_option_request_met, sys_option_request_data, sys_option_request_title_name, sys_option_request_val_name, " +
                        "sys_option_request_search_url, sys_option_request_search_met, sys_option_request_search_data, sys_option_request_search_title_name, sys_option_request_search_val_name, " +
                        "sys_option_request_key" +
                        ") VALUES (" +
                        ""+ OnlyId.getDistributed() +", " +
                        "'/system/v1.0.0/"+tableInfo.nameHumpLowerCase+"/query', 'GET', 'field=xxxxx&value=${xxxxx}&symbol=1&montage=1', 'xxxxx', 'xxxxx', " +
                        "'/system/v1.0.0/"+tableInfo.nameHumpLowerCase+"/query', 'GET', 'field=xxxxx&value=${xxxxx}&symbol=7&montage=1', 'xxxxx', 'xxxxx', " +
                        "'"+ key +"');";
                dataBase.runSql(sql, null);
            }

            key=tableInfo.nameHumpLowerCase;
            listGzbMap = dataBase.selectGzbMap("select * from sys_option_sql where sys_option_sql_key = ?", new Object[]{key});
            if (listGzbMap.size() == 0) {
                sql="INSERT INTO sys_option_sql(" +
                        "sys_option_sql_id, sys_option_sql_sql, sys_option_sql_title_name, sys_option_sql_val_name, sys_option_sql_key) VALUES (" +
                        ""+ OnlyId.getDistributed() +", 'select * from "+ tableInfo.name +"', 'xxxxx', 'xxxxx', '"+key+"');";
                dataBase.runSql(sql, null);
            }
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                String name = tableInfo.columnNamesHumpLowerCase.get(i);
                //生成映射元数据
                listGzbMap = dataBase.selectGzbMap("select sys_mapping_column_id from sys_mapping_column where sys_mapping_column_table = ? and sys_mapping_column_name=?", new Object[]{id, name});
                map0.put(tableInfo.nameHumpLowerCase + "." + name, "0");
                if (listGzbMap.size() == 0) {
                    String option = "";
                    //检查列是否 可能是某种选项 sys_option
                    listGzbMap = dataBase.selectGzbMap("select sys_option_id from sys_option where sys_option_key=?", new Object[]{name});
                    if (listGzbMap.size() > 0) {
                        option = name;
                    }
                    if (listGzbMap.size() == 0) {
                        if (name.contains("state") || name.contains("status")) {
                            option = "def_state";
                        }
                    }
                    //检查 是不是某种引用
                    String request="";
                    listGzbMap = dataBase.selectGzbMap("select sys_option_request_id from sys_option_request where sys_option_request_key=?", new Object[]{name});
                    if (listGzbMap.size() > 0) {
                        request = name;
                    }
                    String sql_op="";
                    listGzbMap = dataBase.selectGzbMap("select sys_option_sql_id from sys_option_sql where sys_option_sql_key=?", new Object[]{name});
                    if (listGzbMap.size() > 0) {
                        sql_op = name;
                    }

                    sql = "INSERT INTO sys_mapping_column" +
                            "(sys_mapping_column_id, sys_mapping_column_name, sys_mapping_column_title, sys_mapping_column_width, " +
                            "sys_mapping_column_file, sys_mapping_column_image, sys_mapping_column_date, sys_mapping_column_number, sys_mapping_column_text, " +
                            "sys_mapping_column_table, sys_mapping_column_request, sys_mapping_column_option, sys_mapping_column_sql) VALUES " +
                            "(" + OnlyId.getDistributed() + ", '" + name + "', '" + tableInfo.getColumnDesc().get(i) + "', 150, " +
                            "null, null, null, 0, 1, " +
                            id + ", '"+request+"', '" + option + "', '"+sql_op+"');";
                    dataBase.runSqlAsync(sql, null);
                }
            }
        }
        //删除失效的列或表
        listGzbMap = dataBase.selectGzbMap("select sys_mapping_table_id,sys_mapping_table_name from sys_mapping_table", new Object[]{});
        for (GzbMap gzbMap : listGzbMap) {
            String t_name = gzbMap.getString("sysMappingTableName");
            Long t_id = gzbMap.getLong("sysMappingTableId");
            if (map0.get(t_name) == null) {
                dataBase.runSqlAsync("delete from sys_mapping_table where sys_mapping_table_id = ?", new Object[]{t_id});
            }
            List<GzbMap> listGzbMap2 = dataBase.selectGzbMap("select sys_mapping_column_id,sys_mapping_column_name from sys_mapping_column where sys_mapping_column_table = ?", new Object[]{t_id});
            for (GzbMap gzbMap2 : listGzbMap2) {
                String t_name2 = gzbMap2.getString("sysMappingColumnName");
                Long t_id2 = gzbMap.getLong("sysMappingColumnId");
                if (map0.get(t_name + "." + t_name2) == null) {
                    dataBase.runSqlAsync("delete from sys_mapping_column where sys_mapping_column_id = ?", new Object[]{t_id2});
                }
            }
        }
        //检查角色专属映射
        listGzbMap = dataBase.selectGzbMap("select sys_role_table_id,sys_role_table_name from sys_role_table", new Object[]{});
        for (GzbMap gzbMap : listGzbMap) {
            String t_name = gzbMap.getString("sysRoleTableName");
            Long t_id = gzbMap.getLong("sysRoleTableId");
            if (map0.get(t_name) == null) {
                dataBase.runSqlAsync("delete from sys_role_table where sys_role_table_id = ?", new Object[]{t_id});
            }
            List<GzbMap> listGzbMap2 = dataBase.selectGzbMap("select sys_role_column_id,sys_role_column_name from sys_role_column where sys_role_column_table = ?", new Object[]{t_id});
            for (GzbMap gzbMap2 : listGzbMap2) {
                String t_name2 = gzbMap2.getString("sysRoleColumnName");
                Long t_id2 = gzbMap.getLong("sysRoleColumnId");
                if (map0.get(t_name + "." + t_name2) == null) {
                    dataBase.runSqlAsync("delete from sys_role_column where sys_role_column_id = ?", new Object[]{t_id2});
                }
            }
        }



    }
}
