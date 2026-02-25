package gzb.frame.factory;

import gzb.entity.HttpMapping;
import gzb.tools.log.Log;

import java.util.Map;
//类加载回调  可以获得类加载开始前  加载完毕后的事件  以及每个类加载开始前  加载完毕后的事件
public class ClassLoadEvent {
    public void eventClassStart(String className) {
        Log.log.t("加载类开始:", className);
    }

    public void eventClassEnd(String className) {
        Log.log.t("加载类开始:", className);
    }

    public void eventStart() {
        Log.log.t("加载类开始");
    }

    public void eventEnd(Map<String, HttpMapping[]> mapHttpMapping0) {
        Log.log.t("加载类完毕");
    }

}
