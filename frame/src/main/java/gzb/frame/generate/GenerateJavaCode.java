package gzb.frame.generate;

import gzb.entity.TableInfo;
import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseMsql;
import gzb.tools.*;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.util.ArrayList;
import java.util.List;

public class GenerateJavaCode {
    static Log log = new LogImpl(GenerateJavaCode.class);

    public static void main(String[] args) throws Exception {
        String path = Config.thisPath() + "/src/main/java";
        String pkg = "com";
        String dbName = "frame";
        generateCode(path, pkg, 2,dbName,dbName);
    }

    ///  type=0 生成全部表  type=1 排除 sys_xxx  type=2 只生成 sys_xxx
    public static void generateCode(String codeSrcPath, String pkg, int type,String frameDbKey,String...userDbKeys) throws Exception {
        DataBase frameDataBase = new DataBaseMsql(frameDbKey);
        String frameDbName=Config.get("db.mysql."+frameDbKey+".name");
        for (int i = 0; i < userDbKeys.length; i++) {
            DataBase userDataBase = new DataBaseMsql(userDbKeys[i]);
            String userDbName=Config.get("db.mysql."+userDbKeys[i]+".name");
            List<TableInfo> list0 = userDataBase.getTableInfo();
            List<TableInfo> list = new ArrayList<>();
            String[] names = new String[]{
                    "sys_users_login_log", "sys_users", "sys_permission",
                    "sys_option", "sys_mapping", "sys_log",
                    "sys_group_permission", "sys_group", "sys_file"
                    , "sys_group_column", "sys_group_table"};

            for (TableInfo tableInfo : list0) {
                int a01 = 0;
                for (String name : names) {
                    if (tableInfo.name.equals(name)) {
                        a01 = 1;
                        break;
                    }
                }
                if (a01 == 1) {
                    if (type == 1) {
                        continue;
                    }
                    if (type == 2) {
                        continue;
                    }
                }
                list.add(tableInfo);
            }
            code(codeSrcPath, pkg, userDbKeys[i], userDbName, list);

            Tools.updateMapping(frameDataBase,list,null);
        }

        System.out.println("运行完毕");

    }

    public static void code(String codeSrcPath, String pkg,String dbKey, String dbName, List<TableInfo> list) throws Exception {

        EntityCode entityCode = new EntityCode(codeSrcPath, pkg, null);
        DBCode dbCode = new DBCode(codeSrcPath, pkg, null);
        DaoImplCode daoImplCode = new DaoImplCode(codeSrcPath, pkg, null);
        DaoCode daoCode = new DaoCode(codeSrcPath, pkg, null);
        SqlToolsCode sqlToolsCode = new SqlToolsCode(codeSrcPath, pkg, null);
        ActionCode actionCode = new ActionCode(codeSrcPath, pkg, null);
        HTMLCode htmlCode = new HTMLCode(codeSrcPath, pkg, null);
        dbCode.start(dbKey,dbName, true);
        entityCode.start(list, true);
        daoImplCode.start(list, true);
        daoCode.start(list, true);
        sqlToolsCode.start(list, true);
        actionCode.start(list, true);
        htmlCode.start(list, true);
    }
}
