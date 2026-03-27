package com.tfb01.dao.impl;
import gzb.frame.annotation.Service;
import com.tfb01.dao.WorldDao;
import com.tfb01.entity.World;
import com.tfb01.Tfb01;
import gzb.frame.db.AsyncFactory;
import gzb.frame.factory.v4.entity.CacheObject;
import gzb.tools.Tools;
import gzb.tools.cache.object.ObjectCache;
import gzb.tools.log.Log;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WorldDaoImpl extends Tfb01<World> implements WorldDao{
    Object[]objects=new Object[0];
    public List<World> query(int num) throws Exception {
        String sql=null;
        ObjectCache.Entity entity0 = ObjectCache.SB_CACHE0.get();
        int index0 = entity0.open();
        try{
            StringBuilder stringBuilder=entity0.get(index0);
            stringBuilder.append("select * from world where id in (");
            for (int i = 0; i < num; i++) {
                stringBuilder.append(ThreadLocalRandom.current().nextInt(10000) + 1);
                if (i<num-1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(")");
            sql= stringBuilder.toString();
        }finally {
            entity0.close(index0);
        }
        return query(sql,objects,null,null,0,0,-1);

    }
}