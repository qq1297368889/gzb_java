package com.frame.api;

import com.frame.dao.SysFileDao;
import com.frame.entity.SysFile;
import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

@Controller
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
//@CrossDomain(allowCredentials = false)
@RequestMapping(value = "/test/new/api/v1")
public class TestNewApi {
    @Resource(value = "com.frame.dao.impl.SysFileDaoImpl")
    SysFileDao sysFileDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;
    @DecoratorOpen
    @GetMapping("list")
    public Object list(GzbJson result, SysFile sysFile, String sortField, String sortType, Integer page, Integer size) throws Exception {
        if(sysFile == null){
            sysFile = new SysFile();
        }
        //修改其他逻辑 ....
        return sysFileDao.queryPage(sysFile, sortField, sortType, page, size, 100, 1000);
    }
}
