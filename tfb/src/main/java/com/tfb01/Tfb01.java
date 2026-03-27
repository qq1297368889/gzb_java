package com.tfb01;

import gzb.frame.db.BaseDaoImpl;
public class Tfb01<T> extends BaseDaoImpl<T> {
    //数据库信息 在这里指定
    public Tfb01() {
        try {
            //对应配置文件里的 db.mysql.xxx 的 xxx
            init("db002");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}