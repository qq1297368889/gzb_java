package com.frame.system;

import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseFactory;
import gzb.tools.Config;

public class BaseAction {


    public static DataBase dataBase;

    static {
        String name = Config.get("db.frame.key", "frame");
        try {
            dataBase = DataBaseFactory.getDataBase(name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("默认数据连接失败：" + name);
        }
    }



}
