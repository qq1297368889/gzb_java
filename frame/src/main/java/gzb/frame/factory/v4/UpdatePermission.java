package gzb.frame.factory.v4;

import gzb.frame.db.DataBase;
import gzb.entity.HttpMapping;
import gzb.tools.OnlyId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UpdatePermission {

    public static void exec(Map<String, HttpMapping[]> mapHttpMapping0, String[] met, DataBase dataBase) {
        Map<String, List<String>> map = new ConcurrentHashMap<>();
        for (Map.Entry<String, HttpMapping[]> stringEntry : mapHttpMapping0.entrySet()) {
            for (int i = 0; i < stringEntry.getValue().length; i++) {
                HttpMapping httpMapping = stringEntry.getValue()[i];
                if (httpMapping != null) {
                    String name = stringEntry.getKey() + "-" + met[i];
                    String name_f = httpMapping.httpMappingFun.getClass().getName();
                    List<String> list0 = map.get(name_f);
                    if (list0 == null) {
                        list0 = new ArrayList<>();
                        map.put(name_f, list0);
                    }
                    list0.add(name);
                    break;
                }
            }
        }
        String sql = "INSERT INTO sys_permission(" +
                "sys_permission_id, sys_permission_name, sys_permission_data, " +
                "sys_permission_type, sys_permission_desc, sys_permission_sup, " +
                "sys_permission_sort) " +
                "SELECT ?,?,?,?,?,?,? " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE sys_permission_name = ?)";

        for (Map.Entry<String, List<String>> stringListEntry : map.entrySet()) {
            Object[] objects = new Object[8];
            long s_id = OnlyId.getDistributed();
            objects[0] = s_id;
            objects[1] = stringListEntry.getKey();
            objects[2] = null;
            objects[3] = 2;
            objects[4] = null;
            objects[5] = 0;
            objects[6] = 0;
            objects[7] = stringListEntry.getKey();
            dataBase.runSqlAsync(sql, objects);
            for (String string : stringListEntry.getValue()) {
                Object[] objects0 = new Object[8];
                objects0[0] = OnlyId.getDistributed();
                objects0[1] = string;
                objects0[2] = null;
                objects0[3] = 2;
                objects0[4] = null;
                objects0[5] = s_id;
                objects0[6] = 0;
                objects0[7] = string;
                dataBase.runSqlAsync(sql, objects0);
            }

        }

    }

}
