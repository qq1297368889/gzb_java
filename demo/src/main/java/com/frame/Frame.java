package com.frame;

import gzb.frame.db.BaseDaoImpl;
public class Frame<T> extends BaseDaoImpl<T> {
    //数据库信息 在这里指定
    public Frame() {
        try {
            //对应配置文件里的 db.mysql.xxx 的 xxx
            init("db001");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}