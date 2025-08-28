package com.acquisition;

import gzb.frame.db.BaseDaoImpl;
public class Acquisition<T> extends BaseDaoImpl<T> {
    //数据库信息 在这里指定
    public Acquisition() {
        try {
          init("acquisition");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}