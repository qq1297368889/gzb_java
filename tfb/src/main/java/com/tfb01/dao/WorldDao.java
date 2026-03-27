package com.tfb01.dao;

import com.tfb01.entity.World;
import gzb.frame.db.BaseDao;

import java.util.List;

public interface WorldDao extends BaseDao<World> {
    List<World> query(int num) throws Exception ;
}
