package gzb.frame.factory.v5;

import gzb.frame.annotation.*;
import gzb.frame.factory.ClassTools;
import gzb.tools.log.Log;
import gzb.tools.thread.ServiceThread;

import java.lang.reflect.Method;
/// 遇到一些问题 暂时搁置 还是扫描源码稳妥
public class GzbFactory {

    static Log log=Log.log;
    public static class Entity {
        RequestMapping requestMapping;
        Header header;
        Controller controller;
        Service service;
        CacheRequest cache_request;
        CrossDomain crossDomain;
        DataBaseEventFactory dataBaseEventFactory;
        DataBaseEventSelect dataBaseEventSelect;
        DataBaseEventDelete dataBaseEventDelete;
        DataBaseEventUpdate dataBaseEventUpdate;
        DataBaseEventSave dataBaseEventSave;
        ThreadFactory threadFactory;
        Decorator decorator;
        DecoratorEnd decoratorEnd;
        DecoratorStart decoratorStart;
        DecoratorOpen decoratorOpen;
        GetMapping getMapping;
        PostMapping postMapping;
        PutMapping putMapping;
        DeleteMapping deleteMapping;
        EntityAttribute entityAttribute;
        EventLoop eventLoop;
        Limitation limitation;
        ManualRespond manualRespond;
        Resource resource;
        ThreadInterval threadInterval;
        Transaction transaction;

        public static void read(Class<?> class1) {

        }
    }


    public static void start() {

    }

    public static void load(Class<?> class1) {

    }

    public static void loadController(Class<?> class1) {
        Controller controller = ClassTools.getAnnotation(class1,Controller.class);
        if (controller == null) {
            return;
        }
        RequestMapping requestMapping = ClassTools.getAnnotation(class1,RequestMapping.class);
        Method[]methods=class1.getMethods();
        for (Method method : methods) {
            RequestMapping requestMappingMethod = ClassTools.getAnnotation(method,RequestMapping.class);
            GetMapping getMapping  = ClassTools.getAnnotation(method,GetMapping.class);
            PostMapping postMapping  = ClassTools.getAnnotation(method,PostMapping.class);
            PutMapping putMapping  = ClassTools.getAnnotation(method,PutMapping.class);
            DeleteMapping deleteMapping  = ClassTools.getAnnotation(method,DeleteMapping.class);
            ManualRespond manualRespond  = ClassTools.getAnnotation(method,ManualRespond.class);
            EventLoop eventLoop  = ClassTools.getAnnotation(method,EventLoop.class);
            CacheRequest cacheRequest  = ClassTools.getAnnotation(method,CacheRequest.class);
            Header header  = ClassTools.getAnnotation(method,Header.class);
            Limitation limitation  = ClassTools.getAnnotation(method,Limitation.class);
            Transaction transaction  = ClassTools.getAnnotation(method,Transaction.class);
            if (requestMappingMethod == null && getMapping == null && postMapping == null && putMapping == null && deleteMapping == null) {
                continue;
            }
            Class<?>[]parameterTypes = method.getParameterTypes();
           java.lang.reflect.Parameter[]parameterArray= method.getParameters();
            String[]names=ClassTools.getParameterNamesByAsm(method,parameterTypes).toArray(new String[0]);
            int[]types=new int[parameterTypes.length];// 0未指定 1服务 2请求参数
            for (int i = 0; i < parameterArray.length; i++) {
                Parameter parameter  = ClassTools.getAnnotation(parameterArray[i],Parameter.class);
                if (parameter==null) {
                    continue;
                }
                names[i]=parameter.value();
                types[i]=parameter.service()?1:2;
            }
            log.d(class1.getName(),method.getName(),names,types,parameterTypes);

        }
    }
}
