package gzb.frame.factory;

import gzb.entity.HttpMapping;
import gzb.frame.language.Template;
import gzb.tools.log.Log;

import java.util.Map;
//类加载回调  可以获得类加载开始前  加载完毕后的事件  以及每个类加载开始前  加载完毕后的事件
public class ClassLoadEvent {
    public void eventClassStart(String className) {
        Log.log.t(Template.THIS_LANGUAGE[76], className);
    }

    public void eventClassEnd(String className) {
        Log.log.t(Template.THIS_LANGUAGE[77], className);
    }

    public void eventStart() {
        Log.log.t(Template.THIS_LANGUAGE[74]);
    }

    public void eventEnd(Map<String, HttpMapping[]> mapHttpMapping0) {
        Log.log.t(Template.THIS_LANGUAGE[75]);
    }

}
