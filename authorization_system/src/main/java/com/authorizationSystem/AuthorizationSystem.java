package com.authorizationSystem;

import gzb.frame.db.BaseDaoImpl;
public class AuthorizationSystem<T> extends BaseDaoImpl<T> {
    //数据库信息 在这里指定
    public AuthorizationSystem() {
        try {
          init("db001");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}