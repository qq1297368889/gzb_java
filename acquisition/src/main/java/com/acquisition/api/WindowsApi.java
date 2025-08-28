package com.acquisition.api;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;
import gzb.tools.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping(value = "/windows/", header = "content-type:application/json;charset=UTF-8")
public class WindowsApi {
    /// windows/restart?name=k001
    @GetMapping("restart")
    public Object restart(JSONResult result,String name) throws Exception {
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    ProcessUtils.start("C:/Sandbox/"+name+".stop.bat");
                    sleep(2000);
                    ProcessUtils.start("C:/Sandbox/"+name+".bat");
                    sleep(2000);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();

        return result.success("已完成");
    }

}
