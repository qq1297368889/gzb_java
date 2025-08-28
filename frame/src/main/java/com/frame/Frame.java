package com.frame;

import gzb.frame.db.BaseDaoImpl;
import gzb.tools.Config;

public class Frame<T> extends BaseDaoImpl<T> {
    //数据库信息 在这里指定
    public Frame() {
        try {
          init(Config.get("db.frame.name","frame"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}