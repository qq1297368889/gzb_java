package com.tfb;

import gzb.frame.db.BaseDaoImpl;
public class Tfb<T> extends BaseDaoImpl<T> {
    //数据库信息 在这里指定
    public Tfb() {
        try {
            //对应配置文件里的 db.mysql.xxx 的 xxx
            init("db002");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}