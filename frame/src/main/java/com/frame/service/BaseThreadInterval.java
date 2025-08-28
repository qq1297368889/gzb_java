package com.frame.service;

import com.frame.dao.SysMappingDao;
import com.frame.dao.SysOptionDao;
import com.frame.entity.SysMapping;
import com.frame.entity.SysOption;
import gzb.entity.TableInfo;
import gzb.frame.annotation.Service;
import gzb.frame.annotation.ThreadInterval;
import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.GzbMap;
import gzb.tools.log.Log;

import java.util.List;

public class BaseThreadInterval {
    /*自动更新配置文件*/
    @ThreadInterval(async = true, num = 1, value = 5000)
    public void autoLoadConfig(GzbMap gzbMap, Log log) throws Exception {
        Integer num = gzbMap.getInteger("num01");
        if (num == null) {
            num = 1;
        }
        gzbMap.put("num01", num + 1);
        //log.d("num", num);
        Config.load();
    }

}
