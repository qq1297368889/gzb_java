package gzb.frame.factory;

import gzb.entity.ThreadInfo;
import gzb.frame.annotation.*;
import gzb.frame.server.http.RequestReadAll;
import gzb.frame.server.http.constant.Constant;
import gzb.frame.server.http.entity.*;
import gzb.tools.Config;
import gzb.tools.GzbMap;
import gzb.tools.JSONResult;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import gzb.tools.session.SessionTool;
import gzb.tools.thread.ThreadPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 已知问题 读取变量名 有问题 考虑用更好的方式 比如从byte里读  抽象语法树也行 待解决 2025-04-28   2025-04-30  问题算是修复了 静待观察
 */
public class ClassFactory {
    //必须在前边 否则有类加载顺序问题
    public static Log log = new LogImpl();
    public MapClass mapClass = new MapClass();
    public String codeDir = null;
    public int serverThreadState = 0;
    public int serverState = 0;
    public Lock lockInterval = new ReentrantLock();
    public ThreadPool threadPool = new ThreadPool(10, 0);



    public void init() {
        if (serverThreadState > 0) {
            System.out.println("重复调用 init");
            return;
        }
        try {
            if (codeDir == null) {
                codeDir = (Config.get("gzb.system.code.dir", null));
            }
            if (codeDir == null) {
                System.out.println("预料外的情况,源码加载目录为空");
            } else {
                loadFiles(codeDir);
                //启动线程
                ThreadPool.pool.startThread(1, "ClassFactory-update", false, () -> {
                    while (true) {
                        try {
                            Thread.sleep(3000);
                            loadFiles(codeDir);
                      /*      log.d("加载类数量："+mapClass.mapClass0.size(),
                                    "对象数量："+mapClass.mapObjectAll0.size(),
                                    "类元数据数量："+mapClass.mapClassInfo0.size(),
                                    "http映射数量："+mapClass.mapping0.size(),
                                    "装饰器数量："+mapClass.mapDecorator0.size(),
                                    "类文件数量："+mapClass.mapClassFile0.size(),
                                    "定时线程数量："+mapClass.mapThread0.size());
*/
                        } catch (Exception e) {
                            log.d("ClassFactory.init.startThread 出现错误", e);
                        }
                    }
                });
                serverThreadState++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startInterval(int num, String metKey, Class aClass, Map<String, ThreadInfo> mapThread, Map<String, Object> mapObjectAll, Map<String, ClassInfo> mapClassInfo) {
        long lv = 1;
        lockInterval.lock();
        int mapThreadSize = mapThread.size();
        try {
            for (int i = 0; i < mapThreadSize; i++) {
                String key = metKey + "_" + i + "_" + lv;
                ThreadInfo thread = mapThread.remove(key);
                //log.d("interval", "del ", mapThread.size(), thread);
            }
            for (int i = 0; i < num; i++) {
                String key = metKey + "_" + i + "_" + lv;
                Thread thread = ThreadPool.pool.startThread(1, "interval-" + key, () -> {
                    while (serverState != 1) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    MethodInfo methodInfo = null;
                    ThreadInfo threadInfo0 = mapThread.get(key);
                    while (threadInfo0 != null && Thread.currentThread() == threadInfo0.getThread()) {
                        long start = System.currentTimeMillis();
                        try {
                            while (serverState != 1) {
                                Thread.sleep(50);
                            }
                            ClassInfo classInfo = mapClassInfo.get(aClass.getName());
                            if (classInfo == null) {
                                break;
                            }
                            Map<String, MethodInfo> mapMethodInfo = classInfo.getMapMethodInfo();
                            if (mapMethodInfo == null) {
                                break;
                            }
                            methodInfo = mapMethodInfo.get(metKey);
                            if (methodInfo == null) {
                                break;
                            }
                            Object object = mapObjectAll.get(aClass.getName());
                            injectClass(object, null, mapObjectAll);
                            if (methodInfo.getMethod().getDeclaringClass().isInstance(object)) {
                                MethodInfo finalMethodInfo = methodInfo;
                                List<Object> list = getMethodParameter(
                                        null,
                                        mapObjectAll,
                                        new Object[]{threadInfo0.getLock(), threadInfo0.getGzbMap()},
                                        finalMethodInfo.getTypeClass(),
                                        finalMethodInfo.getTypeName());
                                if (!methodInfo.getThreadInterval().async()) {
                                    threadPool.execute0(() -> {
                                        try {
                                            finalMethodInfo.getMethod().invoke(object, list.toArray());
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                } else {
                                    finalMethodInfo.getMethod().invoke(object, list.toArray());
                                }
                            }
                        } catch (InterruptedException e) {
                            log.e("interval InterruptedException", e);
                            break;
                        } catch (Exception e) {
                            log.e("interval Exception", e);
                        } finally {
                            long end = System.currentTimeMillis();
                            if (methodInfo != null) {
                                try {
                                    long mm = methodInfo.getThreadInterval().value() - (end - start);
                                    if (mm > 0) {
                                        Thread.sleep(methodInfo.getThreadInterval().value() - (end - start));
                                    }
                                } catch (InterruptedException e) {
                                    log.d("InterruptedException");
                                    break;
                                }
                            } else {
                                log.e("methodInfo", null);
                            }

                        }
                        threadInfo0 = mapThread.get(key);
                    }
                    //log.d("interval", "end ", key, mapThread.size());
                }).get(0);
                ThreadInfo threadInfo = new ThreadInfo();
                threadInfo.setStart(System.currentTimeMillis());
                threadInfo.setThread(thread);
                threadInfo.setLock(new ReentrantLock());
                threadInfo.setName(thread.getName());
                threadInfo.setStop(0);
                threadInfo.setGzbMap(new GzbMap());

                mapThread.put(key, threadInfo);
                //log.d("interval", "start ", thread, mapThread.size());
            }
        } finally {
            lockInterval.unlock();
        }

    }

    public Class compileClassCode(String code) {
        Class class1 = null;
        Exception exception = null;
        try {
            class1 = ClassLoad.compileGroovyClassCode(code);
        } catch (Exception e) {
            exception = e;
        }
        if (class1 == null) {
            try {
                class1 = ClassLoad.compileJavaClassCode(code);
            } catch (Exception e2) {
                log.e("编译错误", e2, exception);
            }
        }
        return class1;
    }


    public void loadSystemPackage() throws Exception {
        String packageName = Config.get("gzb.system.package", null);
        loadPackage(packageName);
    }

    public void loadPackage(String packageName) throws Exception {
        loadClassInfo(LogImpl.class, null, mapClass.mapClassInfo0, mapClass.mapDecorator0, mapClass.mapping0, mapClass.mapObjectAll0, mapClass.mapClass0, mapClass.mapThread0);

        if (packageName == null) {
            return;
        }
        List<Class<?>> listClass = new ArrayList<>();
        String[] arrAy = packageName.split(",");
        for (int i = 0; i < arrAy.length; i++) {
            if (arrAy[i] == null || arrAy[i].isEmpty()) {
                continue;
            }
            List<Class<?>> listClassSub = Tools.getClasses(arrAy[i]);
            listClass.addAll(listClassSub);
        }
        for (int i = 0; i < listClass.size(); i++) {
            loadClassInfo(listClass.get(i), null, mapClass.mapClassInfo0, mapClass.mapDecorator0, mapClass.mapping0, mapClass.mapObjectAll0, mapClass.mapClass0, mapClass.mapThread0);
        }
    }

    public void loadFiles(String path) throws Exception {
        List<File> list = Tools.fileSub(path, 2);
        List<Class> listClass = new ArrayList<>();
        boolean next = false;
        for (File file : list) {
            if (!file.getPath().endsWith(".groovy") && !file.getPath().endsWith(".java")) {
                continue;
            }
            ClassFileInfo classFileInfo = mapClass.mapClassFile0.get(file.getPath());
            if (classFileInfo == null || classFileInfo.getTime() != file.lastModified()) {
                next = true;
                break;
            }
        }
        if (!next) {
            return;
        }
        serverState = 2;
        try {
            mapClass.to_0_1();
            loadSystemPackage();
            for (File file : list) {
                if (!file.getPath().endsWith(".groovy") && !file.getPath().endsWith(".java")) {
                    continue;
                }
                String code = Tools.fileReadString(file);
                Class aClass = null;
                ClassFileInfo classFileInfo = mapClass.mapClassFile0.get(file.getPath());
                if (classFileInfo != null) {
                    if (classFileInfo.getTime() == file.lastModified()) {
                        aClass = mapClass.mapClass0.get(classFileInfo.getClassName());
                    } else {

                        aClass = compileClassCode(code);
                        if (aClass==null) {
                            log.d("更新类 编译失败", null, file.getPath());
                        }else{
                            log.d("更新类 编译成功", aClass.getName(), file.getPath());
                        }
                    }
                } else {

                    aClass = compileClassCode(code);
                    if (aClass==null) {
                        log.d("新类 编译失败", null, file.getPath());
                    }else{
                        log.d("新类 编译成功", aClass.getName(), file.getPath());
                    }

                }
                listClass.add(aClass);
                //compileJavaClassCode    compileGroovyClassCode  compileClassCode
                classFileInfo = new ClassFileInfo();
                classFileInfo.setFile(file);
                classFileInfo.setState(0);
                classFileInfo.setTime(file.lastModified());
                classFileInfo.setClassName(aClass.getName());
                mapClass.mapClassFile0.put(file.getPath(), classFileInfo);
                loadClassInfo(aClass, code, mapClass.mapClassInfo0, mapClass.mapDecorator0, mapClass.mapping0, mapClass.mapObjectAll0, mapClass.mapClass0, mapClass.mapThread0);
            }
            mapClass.delete_0();
        } catch (Exception e) {
            log.e("loadFiles", e);
        }

        serverState = 1;
    }

    private Object handlePrimitiveArray(Class<?> componentType, Object[] objects) {
        try {
            if (objects != null && objects.length > 0 && objects[0] != null) {
                if (componentType == int.class) {
                    return Tools.toArrayType(objects, new int[objects.length]);
                } else if (componentType == long.class) {
                    return Tools.toArrayType(objects, new long[objects.length]);
                } else if (componentType == double.class) {
                    return Tools.toArrayType(objects, new double[objects.length]);
                } else if (componentType == float.class) {
                    return Tools.toArrayType(objects, new float[objects.length]);
                } else if (componentType == boolean.class) {
                    return Tools.toArrayType(objects, new boolean[objects.length]);
                } else if (componentType == char.class) {
                    return Tools.toArrayType(objects, new char[objects.length]);
                } else if (componentType == short.class) {
                    return Tools.toArrayType(objects, new short[objects.length]);
                } else if (componentType == byte.class) {
                   /*
                    log.d(objects,objects.getClass(),objects instanceof Object[]);
                    return Tools.toArrayType(objects, new byte[objects.length]);
                    */
                }
            }
        } catch (NumberFormatException e) {
            log.e(e);
        }
        return null;
    }

    private Object handlePrimitive(Class<?> type, Object[] objects) {
        if (objects != null && objects.length > 0 && objects[0] != null) {
            if (type == int.class || type == Integer.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Integer.parseInt(objects[0].toString());
            } else if (type == long.class || type == Long.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Long.valueOf(objects[0].toString());
            } else if (type == double.class || type == Double.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Double.valueOf(objects[0].toString());
            } else if (type == float.class || type == Float.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Float.valueOf(objects[0].toString());
            } else if (type == boolean.class || type == Boolean.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Boolean.valueOf(objects[0].toString());
            } else if (type == char.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return objects[0].toString().charAt(0);
            } else if (type == short.class || type == Short.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Short.valueOf(objects[0].toString());
            } else if (type == byte.class || type == Byte.class) {
                if (objects[0].toString().isEmpty()) {
                    return null;
                }
                return Byte.valueOf(objects[0].toString());
            } else if (type == String.class) {
                if (objects[0].toString().isEmpty()) {
                    return "";
                }
                return objects[0].toString();
            }
        }
        return null;
    }

    //传入对象 给对象的所有类变量进行注入 如果是接口那么 mapObject.get(类变量类型全名) 能取出对应实现类对象 如果是null说明不存在  如果存在就注入进去
    //传入对象 给对象的所有类变量进行注入 如果不是接口那么 mapObject.get(类变量类型全名) 能取出对应实现类对象 如果是null说明不存在  如果存在就注入进去
    public boolean injectClass(Object object, Object[] data, Map<String, Object> mapObjectAll) throws IllegalAccessException {
        if (object == null) {
            return false;
        }

        Field[] fields = getCombinedFields(object.getClass());
        for (int i = 0; i < fields.length; i++) {
            Resource resources = fields[i].getDeclaredAnnotation(Resource.class);
            Object obj1 = null;
            String key = fields[i].getType().getName();
            if (resources != null) {
                if (resources.value().length() > 0) {
                    key = resources.value();
                }
                obj1 = mapObjectAll.get(key);
                if (obj1 == null && data != null) {
                    for (int i1 = 0; i1 < data.length; i1++) {
                        if (data[i1] != null) {
                            if (data[i1].getClass().getName().equals(key)) {
                                obj1 = data[i1];
                                break;
                            }
                            if (obj1 == null) {
                                Class[] classes = data[i1].getClass().getInterfaces();
                                for (Class aClass : classes) {
                                    if (aClass.getName().equals(key)) {
                                        obj1 = data[i1];
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (obj1 != null) {
                    //log.d("injectClass",fields[i].getName(), obj1, key,object);
                    injectClass(obj1, data, mapObjectAll);
                    fields[i].setAccessible(true);
                    fields[i].set(object, obj1);
                    fields[i].setAccessible(false);
                }
            }


        }
        return true;
    }


    public List<Object> getMethodParameter(Map<String, Object[]> requestDataMap, Map<String, Object> mapObject, Object[] arrayObject, Class[] TypeClass, String[] TypeName) {
        List<Object> listObject = new ArrayList<>();
        try {
            if (TypeClass != null) {
                for (int n = 0; n < TypeClass.length; n++) {
                    Class<?> paramType = TypeClass[n];
                    String paramName = TypeName[n];
                    Object paramValue = null;
                    if (arrayObject != null) {
                        for (int i = 0; i < arrayObject.length; i++) {
                            if (arrayObject[i] == null) {
                                continue;
                            }
                            if (paramType.isAssignableFrom(arrayObject[i].getClass())) {
                                paramValue = arrayObject[i];
                            }
                        }
                    }

                    if (paramValue == null && mapObject != null) {
                        paramValue = mapObject.get(paramType.getName());
                    }
                    if (requestDataMap != null) {
                        if (paramValue == null) {
                            Object[] values = requestDataMap.get(paramName);
                            if (values != null && values.length > 0) {
                                try {
                                    if (values[0] instanceof File) {
                                        paramValue = values[0];
                                    } else if (paramType == String.class) {
                                        paramValue = values[0];
                                    } else if (paramType == int.class || paramType == Integer.class) {
                                        paramValue = Integer.parseInt(values[0].toString());
                                    } else if (paramType == long.class || paramType == Long.class) {
                                        paramValue = Long.parseLong(values[0].toString());
                                    } else if (paramType == double.class || paramType == Double.class) {
                                        paramValue = Double.parseDouble(values[0].toString());
                                    } else if (paramType == boolean.class || paramType == Boolean.class) {
                                        paramValue = Boolean.parseBoolean(values[0].toString());
                                    } else if (paramType == byte.class || paramType == Byte.class) {
                                        paramValue = Byte.parseByte(values[0].toString());
                                    } else if (paramType.isArray()) {
                                        Class<?> componentType = paramType.getComponentType();
                                        Object array = Array.newInstance(componentType, values.length);
                                        for (int i = 0; i < values.length; i++) {
                                            if (componentType == int.class || componentType == Integer.class) {
                                                Array.set(array, i, Integer.parseInt(values[i].toString()));
                                            } else if (componentType == long.class || componentType == Long.class) {
                                                Array.set(array, i, Long.parseLong(values[i].toString()));
                                            } else if (componentType == double.class || componentType == Double.class) {
                                                Array.set(array, i, Double.parseDouble(values[i].toString()));
                                            } else if (componentType == boolean.class || componentType == Boolean.class) {
                                                Array.set(array, i, Boolean.parseBoolean(values[i].toString()));
                                            }else{
                                                Array.set(array, i, values[i]);
                                            }
                                        }
                                        paramValue = array;
                                    }
                                } catch (NumberFormatException e) {
                                    // 处理数据格式转换异常
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    if (paramValue == null && requestDataMap != null && !paramType.isArray()) {
                        if (paramType != Boolean.class &&
                                paramType != Byte.class &&
                                paramType != Short.class &&
                                paramType != Integer.class &&
                                paramType != Long.class &&
                                paramType != Float.class &&
                                paramType != Double.class &&
                                paramType != Character.class&&
                                paramType != String.class
                                && !paramType.isPrimitive()) {
                            Field[] fields = getCombinedFields(paramType);
                            for (Field field : fields) {
                                Class<?>clazz=field.getType();
                                Object[] strings = requestDataMap.get(field.getName());
                                Object obj01 = null;
                                if (field.getType().isArray()) {
                                    clazz = field.getType().getComponentType();
                                    obj01 = (handlePrimitiveArray(clazz, strings));
                                } else {
                                    obj01 = (handlePrimitive(clazz, strings));
                                }

                                if (obj01 != null) {
                                    if (paramValue == null) {
                                        paramValue = paramType.getDeclaredConstructor().newInstance();
                                    }
                                    field.setAccessible(true);
                                    field.set(paramValue, obj01);
                                    field.setAccessible(false);
                                }
                            }
                        }

                    }
                    listObject.add(paramValue);
                }
            }
        } catch (Exception e) {
            log.e(e);
            return listObject;
        }
        return listObject;
    }


    public String getMethodSing(String name, Class[] classes) {
        String key = name;
        key += "[";
        for (int i = 0; i < classes.length; i++) {
            key += classes[i].getName();
            if (i < classes.length - 1) {
                key += ",";
            }
        }
        key += "]";
        return key;
    }

    public List<String> getInterfacesName(Class<?> clazz) {
        List<String> listString;
        // 获取类实现的接口数组
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            interfaces = new Class[]{};
        }
        List<Class> list = new ArrayList<>(Arrays.asList(interfaces));
        list.add(clazz);
        listString = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            if (name.equals("groovy.lang.GroovyObject")) {
                continue;
            }
            listString.add(name);
        }
        return listString;
    }


    public Field[] getCombinedFields(Class<?> clazz) {
        Field[] fields;
        Set<Field> combinedFields = new HashSet<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] publicFields = clazz.getFields();
        combinedFields.addAll(Arrays.asList(declaredFields));
        combinedFields.addAll(Arrays.asList(publicFields));
        fields = combinedFields.toArray(new Field[0]);
        return fields;
    }

    public void delete(String className, List<String> listClassName, Map<String, ClassInfo> mapClassInfo, Map<String, Object> mapObjectAll, Map<String, Class> mapClass) throws Exception {
        for (int i = 0; i < listClassName.size(); i++) {
            if (listClassName.get(i).equals(className)) {
                listClassName.remove(i);
            }
        }
        mapClass.remove(className);
        mapObjectAll.remove(className);
        mapClassInfo.remove(className);
    }

    public void loadClass(String className, Map<String, Class> mapClass) throws Exception {
        loadClass(Class.forName(className), mapClass);
    }

    public void loadClass(Class aClass, Map<String, Class> mapClass) {
        mapClass.put(aClass.getName(), aClass);
    }

    public <T> Class<T> getClass(Class<T> aClass) {
        return getClass(aClass.getName());
    }

    public <T> Class<T> getClass(String className) {
        return mapClass.mapClass0.get(className);
    }

    public <T> T getObject(Class<T> aClass) {
        Object obj = getObject(aClass.getName());
        if (obj == null) {
            Class[] classes = aClass.getInterfaces();
            for (int i = 0; i < classes.length; i++) {
                obj = getObject(classes[i].getName());
                if (obj != null) {
                    break;
                }
            }
        }
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    public <T> T getObject(String key) {
        Object obj = mapClass.mapObjectAll0.get(key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    public void put1(String url, String met, Object[] data, Map<String, Object[]> mapping) {
        //log.d(url + "-" + met, data);
        mapping.put(url + "-" + met, data);
    }

    public void loadMapping(MethodInfo methodInfo, ClassInfo classInfo, int j, Map<String, Object[]> mapping) {

        String webPath0 = "/";
        if (classInfo.getRequestMapping() == null || classInfo.getRequestMapping().value()[0].length() > 0) {
            webPath0 = "/" + classInfo.getRequestMapping().value()[j] + "/";
        }
        //路径映射
        if (methodInfo.getRequestMapping() != null) {
            for (int i1 = 0; i1 < methodInfo.getRequestMapping().value().length; i1++) {
                if (methodInfo.getRequestMapping().value()[i1] == null || methodInfo.getRequestMapping().value()[i1].length() < 1) {
                    continue;
                }
                String webPath = webPath0;
                webPath += methodInfo.getRequestMapping().value()[i1] + "/";
                webPath = Tools.webPathFormat(webPath);
                put1(webPath, Constant.requestMethod[0], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
                put1(webPath, Constant.requestMethod[1], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
                put1(webPath, Constant.requestMethod[2], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
                put1(webPath, Constant.requestMethod[3], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
            }

        }
        if (methodInfo.getGetMapping() != null) {
            for (int i1 = 0; i1 < methodInfo.getGetMapping().value().length; i1++) {
                if (methodInfo.getGetMapping().value()[i1] == null || methodInfo.getGetMapping().value()[i1].length() < 1) {
                    continue;
                }
                String webPath = webPath0;
                webPath += methodInfo.getGetMapping().value()[i1] + "/";
                webPath = Tools.webPathFormat(webPath);
                put1(webPath, Constant.requestMethod[0], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
            }
        }
        if (methodInfo.getPostMapping() != null) {
            for (int i1 = 0; i1 < methodInfo.getPostMapping().value().length; i1++) {
                if (methodInfo.getPostMapping().value()[i1] == null || methodInfo.getPostMapping().value()[i1].length() < 1) {
                    continue;
                }
                String webPath = webPath0;
                webPath += methodInfo.getPostMapping().value()[i1] + "/";
                webPath = Tools.webPathFormat(webPath);
                put1(webPath, Constant.requestMethod[1], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
            }
        }
        if (methodInfo.getPutMapping() != null) {
            for (int i1 = 0; i1 < methodInfo.getPutMapping().value().length; i1++) {
                if (methodInfo.getPutMapping().value()[i1] == null || methodInfo.getPutMapping().value()[i1].length() < 1) {
                    continue;
                }
                String webPath = webPath0;
                webPath += methodInfo.getPutMapping().value()[i1] + "/";
                webPath = Tools.webPathFormat(webPath);
                put1(webPath, Constant.requestMethod[2], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
            }
        }
        if (methodInfo.getDeleteMapping() != null) {
            for (int i1 = 0; i1 < methodInfo.getDeleteMapping().value().length; i1++) {
                if (methodInfo.getDeleteMapping().value()[i1] == null || methodInfo.getDeleteMapping().value()[i1].length() < 1) {
                    continue;
                }
                String webPath = webPath0;
                webPath += methodInfo.getDeleteMapping().value()[i1] + "/";
                webPath = Tools.webPathFormat(webPath);
                put1(webPath, Constant.requestMethod[3], new Object[]{classInfo.getaClass().getName(), methodInfo.getMethod().getName(), methodInfo.getTypeClass()}, mapping);
            }
        }
    }


    public boolean loadClassInfo(Class aClass, String code, Map<String, ClassInfo> mapClassInfo, Map<String, String> mapDecorator, Map<String, Object[]> mapping, Map<String, Object> mapObjectAll, Map<String, Class> mapClass, Map<String, ThreadInfo> mapThread) throws Exception {

        loadClass(aClass, mapClass);
        ClassInfo classInfo = new ClassInfo();

        classInfo.setController((Controller) aClass.getDeclaredAnnotation(Controller.class));
        classInfo.setService((Service) aClass.getDeclaredAnnotation(Service.class));
        classInfo.setRequestMapping((RequestMapping) aClass.getDeclaredAnnotation(RequestMapping.class));
        classInfo.setDecorator((Decorator) aClass.getDeclaredAnnotation(Decorator.class));
        classInfo.setServerEndpoint((ServerEndpoint) aClass.getDeclaredAnnotation(ServerEndpoint.class));


        if (classInfo.getService() == null && classInfo.getController() == null && classInfo.getDecorator() == null) {
            return false;
        }
        classInfo.setaClass(aClass);
        classInfo.setObjectKey(aClass.getName());
        classInfo.setMapMethodInfo(readMethodInfo(aClass, code, mapObjectAll, mapClassInfo, mapThread));
        classInfo.setListFieldInfo(readFieldInfo(aClass));



        loadObject(aClass, null, mapObjectAll);
        mapClassInfo.put(aClass.getName(), classInfo);
        if (classInfo.getDecorator() == null && classInfo.getController() == null) {
            return false;
        }
        for (Iterator<Map.Entry<String, MethodInfo>> it = classInfo.getMapMethodInfo().entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, MethodInfo> en = it.next();
            if (en.getValue() == null) {
                continue;
            }
            MethodInfo methodInfo = en.getValue();
            //装饰器
            if (classInfo.getDecorator() != null) {
                String key = null;
                if (methodInfo.getDecoratorStart() != null && methodInfo.getDecoratorStart().value().length() > 0) {
                    key = Tools.webPathFormat(methodInfo.getDecoratorStart().value()) + "-" + getMethodSing(methodInfo.getMethod().getName(), methodInfo.getTypeClass());
                    mapDecorator.put(key, classInfo.getaClass().getName());
                }
                if (methodInfo.getDecoratorEnd() != null && methodInfo.getDecoratorEnd().value().length() > 0) {
                    key = Tools.webPathFormat(methodInfo.getDecoratorEnd().value()) + "-" + getMethodSing(methodInfo.getMethod().getName(), methodInfo.getTypeClass());
                    mapDecorator.put(key, classInfo.getaClass().getName());
                }

            }

            if (classInfo.getController() != null && classInfo.getRequestMapping()!=null) {
                for (int j = 0; j < classInfo.getRequestMapping().value().length; j++) {
                    loadMapping(methodInfo, classInfo, j, mapping);
                }
            }

        }
        return true;
    }


    public List<FieldInfo> readFieldInfo(Class<?> aClass) {
        aClass.getFields();
        aClass.getDeclaredFields();
        Field[] fields = getCombinedFields(aClass);
        List<FieldInfo> listFieldInfo = new ArrayList<>();
        for (Field field : fields) {
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setField(field);
            fieldInfo.setResource(field.getAnnotation(Resource.class));
            listFieldInfo.add(fieldInfo);
        }
        return listFieldInfo;
    }

    public Map<String, MethodInfo> readMethodInfo(Class<?> aClass, String code, Map<String, Object> mapObjectAll, Map<String, ClassInfo> mapClassInfo, Map<String, ThreadInfo> mapThread) {
        // 获取类中的所有方法
        Method[] methods = Tools.readMethods(aClass);
        // 用于存储当前类的方法信息
        Map<String, MethodInfo> classMethods = new HashMap<>();
        // 遍历类中的每个方法
        for (Method method : methods) {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setRequestMapping(method.getAnnotation(RequestMapping.class));
            methodInfo.setGetMapping(method.getAnnotation(GetMapping.class));
            methodInfo.setPostMapping(method.getAnnotation(PostMapping.class));
            methodInfo.setPutMapping(method.getAnnotation(PutMapping.class));
            methodInfo.setDeleteMapping(method.getAnnotation(DeleteMapping.class));
            methodInfo.setDecoratorStart(method.getAnnotation(DecoratorStart.class));
            methodInfo.setDecoratorEnd(method.getAnnotation(DecoratorEnd.class));
            methodInfo.setDecoratorOpen(method.getAnnotation(DecoratorOpen.class));
            methodInfo.setThreadInterval(method.getAnnotation(ThreadInterval.class));
            methodInfo.setLimitation(method.getAnnotation(Limitation.class));
            if (methodInfo.getDecoratorEnd() == null
                    && methodInfo.getDecoratorOpen() == null
                    && methodInfo.getDecoratorStart() == null
                    && methodInfo.getRequestMapping() == null
                    && methodInfo.getGetMapping() == null
                    && methodInfo.getPostMapping() == null
                    && methodInfo.getPutMapping() == null
                    && methodInfo.getDeleteMapping() == null
                    && methodInfo.getThreadInterval() == null
                    && methodInfo.getLimitation() == null) {
                continue;
            }
            // 获取方法的名称
            String methodName = method.getName();
            // 获取方法的参数类型数组
            Class<?>[] parameterTypes = method.getParameterTypes();
            String[] parameterTypeNames;

            //parameterTypeNames = ClassLoad.getParameterNames(aClass, method).toArray(new String[]{});
            parameterTypeNames =  ClassTools.getParameterNames(method).toArray(new String[0]);

            if (parameterTypeNames.length == 0 && code != null) {
                parameterTypeNames = ClassTools.getParameterNames(code, methodName, parameterTypes).toArray(new String[]{});
            }
            methodInfo.setMethod(method);
            methodInfo.setTypeClass(parameterTypes);
            methodInfo.setTypeName(parameterTypeNames);
            //classMethods.put(methodName + Tools.joinArray(parameterTypes), methodInfo);
            String key = getMethodSing(methodName, parameterTypes);
            classMethods.put(key, methodInfo);
            if (methodInfo.getThreadInterval() != null) {
                startInterval(methodInfo.getThreadInterval().num(), key, aClass, mapThread, mapObjectAll, mapClassInfo);
            }
            if (methodInfo.getLimitation() != null && methodInfo.getLimitation().value() > 0) {
                mapClass.semaphoreMap.put(key,new Semaphore(methodInfo.getLimitation().value()));
            }else{
                mapClass.semaphoreMap.remove(key);
            }
            //调试用
           // log.d(methodInfo.getMethod().getName());
            if (methodInfo.getMethod().getName().equals("t02")) {
                log.d("methodInfo", methodInfo);
            }
        }
        return classMethods;
    }


    public Object createObject(Class<?> aClass, Map<String, Object> mapObjectAll) throws Exception {
        return createObject(aClass, null, mapObjectAll);
    }

    public Object createObject(Class<?> aClass, List<String> listClassName, Map<String, Object> mapObjectAll) throws Exception {
        if (listClassName == null) {
            listClassName = new ArrayList<>();
        }
        // 尝试获取匹配的构造函数
        Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            // 检查传入的 objects 数组是否能匹配构造函数的参数类型
            if (parameterTypes.length == listClassName.size()) {
                boolean isMatch = true;
                List<Object> objects = new ArrayList<>();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Object object0 = mapObjectAll.get(listClassName.get(i));
                    if (object0 != null && !parameterTypes[i].isAssignableFrom(object0.getClass())) {
                        isMatch = false;
                        break;
                    }
                    objects.add(object0);
                }
                if (isMatch) {
                    // 创建对象实例
                    return constructor.newInstance(objects.toArray());
                }
            }
        }
        return aClass.newInstance();
    }

    public void loadObject(Class<?> aClass, Map<String, Object> mapObjectAll) throws Exception {
        loadObject(aClass, null, mapObjectAll);
    }

    public void loadObject(Class<?> aClass, List<String> listClassName, Map<String, Object> mapObjectAll) throws Exception {
        if (aClass.isInterface()) {
            return;
        }
        Object obj = createObject(aClass, listClassName, mapObjectAll);
        List<String> list = getInterfacesName(aClass);
        for (int i = 0; i < list.size(); i++) {
            //log.d("loadObject",list.get(i));
            mapObjectAll.put(list.get(i), obj);
            mapObjectAll.put(obj.getClass().getName(), obj);
        }
    }


    public Class getClass(String className, Map<String, Class> mapClass) {
        return mapClass.get(className);
    }


    public ClassInfo getClassInfo(String className, Map<String, ClassInfo> mapClassInfo) {
        return mapClassInfo.get(className);
    }

    public RunRes call(String className, String metName, Object[] objects, Class[] types, Map<String, ClassInfo> mapClassInfo, Map<String, Object> mapObjectAll) throws InvocationTargetException, IllegalAccessException {
        RunRes runRes = new RunRes();
        runRes.setStart(System.nanoTime());
        runRes.setState(0);
        runRes.setData(null);
        ClassInfo classInfo = mapClassInfo.get(className);
        if (classInfo == null) {
            runRes.setEnd(System.nanoTime());
            return runRes;
        }
        Object obj01 = mapObjectAll.get(classInfo.getaClass().getName());
        if (obj01 == null) {
            runRes.setEnd(System.nanoTime()).setMsg("异常的空指针 -1");
            return runRes;
        }
        Map<String, MethodInfo> mapMethodInfo = classInfo.getMapMethodInfo();
        if (mapMethodInfo == null) {
            runRes.setEnd(System.nanoTime()).setMsg("异常的空指针 -2");
            return runRes;
        }
        String key = getMethodSing(metName, types);
        MethodInfo methodInfo = mapMethodInfo.get(key);
        if (methodInfo == null) {
            runRes.setEnd(System.nanoTime()).setMsg("异常的空指针 -3");
            return runRes;
        }
        Class<?>[] classArray = methodInfo.getTypeClass();
        if (classArray == null) {
            runRes.setEnd(System.nanoTime()).setMsg("异常的空指针 -4");
            return runRes;
        }
        if (objects.length == classArray.length) {
            boolean isMatch = true;
            for (int j = 0; j < classArray.length; j++) {

                if (objects[j] == null) {
                    continue;
                }
                Class<?> paramType = classArray[j];
                Class<?> argType = objects[j].getClass();
                if (paramType.isPrimitive()) {
                    // 如果参数类型是基本数据类型，检查其包装类是否匹配
                    Class<?> wrapperType = Constant.PRIMITIVE_WRAPPER_MAP.get(paramType);
                    if (!wrapperType.isAssignableFrom(argType)) {
                        isMatch = false;
                        break;
                    }
                } else if (!paramType.isAssignableFrom(argType)) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                runRes.setEnd(System.nanoTime());
                runRes.setState(1);
                //injectClass(obj01, objects, mapObjectAll);
                runRes.setData(methodInfo.getMethod().invoke(obj01, objects)).setMsg("方法调用成功");
                return runRes;
            }
        }
        runRes.setEnd(System.nanoTime());
        return runRes;
    }

    //,Map<String,String>mapDecorator,Map<String,Object>mapObjectAll,Map<String,ClassInfo>mapClassInfo,Map<String,Map<String, Object[]>>mapping
    public RunRes webRequest(String httpUrl, String requestMethod, HttpServletRequest request, HttpServletResponse response, Object[] objects) {
        RunRes runRes = new RunRes();
        runRes.setStart(System.nanoTime());
        runRes.setState(500);
        Semaphore semaphore=null;
        try {
            String url = Tools.webPathFormat(httpUrl);//Tools.webPathFormat(request.getRequestURI());
            Object[] info1 = mapClass.mapping0.get(url + "-" + requestMethod);
            if (info1 == null || info1.length != 3) {
                runRes.setEnd(System.nanoTime()).setMsg("映射路径不存在-1");
                runRes.setState(404);
                return runRes;
            }
            String className = info1[0].toString();
            String metName = info1[1].toString();
            Class[] types = (Class[]) info1[2];
            Map<String, Object[]> requestDataMap = new HashMap<>();
            if (request != null) {
                Map<String, String[]> originalParams = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : originalParams.entrySet()) {
                    String key = entry.getKey();
                    String[] value = entry.getValue();
                    requestDataMap.put(key, value);
                }
                RequestReadAll.read(request, requestDataMap);
            }
            ClassInfo classInfo = mapClass.mapClassInfo0.get(className);
            if (classInfo == null) {
                runRes.setEnd(System.nanoTime()).setMsg("映射路径不存在-2");
                runRes.setState(404);
                return runRes;
            }
            Object obj01 = mapClass.mapObjectAll0.get(classInfo.getaClass().getName());
            if (obj01 == null) {
                runRes.setEnd(System.nanoTime()).setMsg("映射路径不存在-4");
                runRes.setState(404);
                return runRes;
            }
            Map<String, MethodInfo> mapMethodInfo = classInfo.getMapMethodInfo();
            if (mapMethodInfo == null) {
                runRes.setEnd(System.nanoTime()).setMsg("映射路径不存在-5");
                runRes.setState(404);
                return runRes;
            }
            String key = getMethodSing(metName, types);
            MethodInfo methodInfo = mapMethodInfo.get(key);
            if (methodInfo == null) {
                runRes.setEnd(System.nanoTime()).setMsg("映射路径不存在-6");
                runRes.setState(404);
                return runRes;
            }
            if (methodInfo.getLimitation()!=null && methodInfo.getLimitation().value() > 0) {
                semaphore = mapClass.semaphoreMap.get(key);
                if (semaphore!=null) {
                    boolean state=semaphore.tryAcquire();
                    if (!state) {
                        semaphore=null;
                        response.setHeader("Content-Type","application/json;charset=UTF-8");
                        runRes.setEnd(System.nanoTime()).setMsg("请求量超过限流阈值，请稍后重试");
                        runRes.setData(new JSONResult().fail("请求量超过限流阈值，请稍后重试").toString());
                        runRes.setState(200);
                        return runRes;
                    }
                }
            }
            setHeader(response, classInfo.getRequestMapping(), methodInfo);
            List<Object> listObject = new ArrayList<>();
            if (objects != null) {
                listObject.addAll(Arrays.asList(objects));
            }
            listObject.add(request);
            listObject.add(response);
            if (request != null && response != null) {
                listObject.add(request.getSession(false));
                listObject.add(SessionTool.getSession(request, response));
            }
            /// 注入 响应体对象
            listObject.add(new JSONResult());
            /// 注入 请求参数
            listObject.add(requestDataMap);
            /// 注入 默认数据库
            Object[] arrayObject = listObject.toArray();
            injectClass(obj01, arrayObject, mapClass.mapObjectAll0);
            List<Object> listParameter = null;
            if (methodInfo.getDecoratorOpen() != null && methodInfo.getDecoratorOpen().value()) {
                List<RunRes> list = decCall(1, url, listObject, requestDataMap, null,
                        mapClass.mapDecorator0, mapClass.mapClassInfo0, mapClass.mapObjectAll0);
                for (RunRes res : list) {
                    if (res==null) {
                        if (response != null) {
                            response.setHeader("Content-Type","application/json;charset=UTF-8");
                        }
                        runRes.setEnd(System.nanoTime()).setMsg("请求被拦截");
                        runRes.setData(new JSONResult().fail("请求被拦截").toString());
                        runRes.setState(200);
                        return runRes;
                    }
                    if (res.getState() != 200) {
                        res.setState(200);
                        return res;
                    }
                    if (res.getData() != null) {
                        listObject.add(res.getData());
                    }
                }
            }
            listParameter = getMethodParameter(requestDataMap, mapClass.mapObjectAll0, listObject.toArray(), types, methodInfo.getTypeName());


            Object res1 = methodInfo.getMethod().invoke(obj01, listParameter.toArray());
            listObject.add(res1);
            if (methodInfo.getDecoratorOpen() != null && methodInfo.getDecoratorOpen().value()) {
                List<RunRes> list = decCall(2, url, listObject, requestDataMap, res1, mapClass.mapDecorator0, mapClass.mapClassInfo0, mapClass.mapObjectAll0);
                for (RunRes res : list) {
                    if (res == null || res.getState() != 200) {
                        runRes.setEnd(System.nanoTime()).setMsg("请求被[end]装饰器拦截");
                        runRes.setState(400);
                        return runRes;
                    }
                    res1 = runRes.getData();
                }
            }
            runRes.setData(res1);
            runRes.setState(200);
            runRes.setEnd(System.nanoTime()).setMsg("请求成功");
            return runRes;
        } catch (Exception e) {
            log.e("webRequest un err", e);
            if (response != null) {
                response.setHeader("Content-Type","application/json;charset=UTF-8");
            }
            runRes.setEnd(System.nanoTime()).setMsg("没找到对应请求处理端点");
            runRes.setData(new JSONResult().fail("没找到对应请求处理端点").toString());
            runRes.setState(200);
            return runRes;
        }finally {
            if (semaphore!=null) {
                semaphore.release();
            }
        }
    }

    public List<RunRes> decCall(int type, String url, List<Object> listObject, Map<String, Object[]> requestDataMap, Object resData, Map<String, String> mapDecorator, Map<String, ClassInfo> mapClassInfo, Map<String, Object> mapObjectAll) {
        List<RunRes> list = new ArrayList<>();
        for (Map.Entry<String, String> entry1 : mapDecorator.entrySet()) {
            String name = entry1.getValue();
            ClassInfo classInfo = mapClassInfo.get(name);
            boolean next = true;
            for (Map.Entry<String, MethodInfo> entry2 : classInfo.getMapMethodInfo().entrySet()) {
                MethodInfo methodInfo = entry2.getValue();
                boolean res1;
                String decUrl = null;
                if (type == 1 && methodInfo.getDecoratorStart() != null && methodInfo.getDecoratorStart().value() != null && methodInfo.getDecoratorStart().value().length() > 0) {
                    decUrl = methodInfo.getDecoratorStart().value();
                    if (methodInfo.getDecoratorStart().type()) {
                        res1 = url.indexOf(decUrl) > -1;
                    } else {
                        res1 = url.startsWith(decUrl);
                    }
                    if (methodInfo.getDecoratorStart().turn()) {
                        res1 = !res1;
                    }

                    next = false;
                } else if (type == 2 && methodInfo.getDecoratorEnd() != null && methodInfo.getDecoratorEnd().value() != null && methodInfo.getDecoratorEnd().value().length() > 0) {
                    decUrl = methodInfo.getDecoratorEnd().value();
                    if (methodInfo.getDecoratorEnd().type()) {
                        res1 = url.indexOf(decUrl) > -1;
                    } else {
                        res1 = url.startsWith(decUrl);
                    }
                    if (methodInfo.getDecoratorEnd().turn()) {
                        res1 = res1 == false;
                    }

                    next = false;
                } else {
                    res1 = false;
                }
                if (res1) {
                    Object obj01 = mapObjectAll.get(classInfo.getObjectKey());
                    RunRes runRes = new RunRes();
                    runRes.err();
                    try {
                        RunRes runRes1 = new RunRes();
                        runRes1.setData(resData);
                        runRes1.setState(200);
                        runRes1.setMsg("system");
                        runRes1.setStart(System.nanoTime());
                        listObject.add(runRes1);
                        Object[] objects = getMethodParameter(requestDataMap, mapObjectAll, listObject.toArray(),
                                methodInfo.getTypeClass(),
                                methodInfo.getTypeName()).toArray();
                        listObject.remove(listObject.size() - 1);
                        if (type == 1) {
                            runRes = (RunRes) methodInfo.getMethod().invoke(obj01, objects);
                            runRes.setEnd(System.nanoTime());
                            if (runRes.getMsg() != null && runRes.getMsg().equals("system")) {
                                runRes.setMsg(null);
                            }
                        } else if (type == 2) {
                            runRes = (RunRes) methodInfo.getMethod().invoke(obj01, objects);
                            runRes.setEnd(System.nanoTime());
                            if (runRes.getMsg() != null && runRes.getMsg().equals("system")) {
                                runRes.setMsg(null);
                            }
                        }
                        list.add(runRes);
                        if (runRes.getState() != 200) {
                            break;
                        }
                    } catch (Exception e) {
                        log.e(e, e.getCause());
                    }
                }

            }
            if (!next) {
                break;
            }
        }
        return list;
    }

    public void setHeader(HttpServletResponse response, RequestMapping requestMapping, MethodInfo methodInfo) {
        if (response == null) {
            return;
        }
        String crossDomain = "";
        List<String> header = new ArrayList<>();
        if (requestMapping != null) {
            for (int i = 0; i < requestMapping.header().length; i++) {
                if (requestMapping.header()[i].length() > 0) {
                    header.add(requestMapping.header()[i]);
                }
            }
            crossDomain = requestMapping.crossDomain();
        }
        if (methodInfo.getRequestMapping() != null) {
            for (int i = 0; i < methodInfo.getRequestMapping().header().length; i++) {
                if (methodInfo.getRequestMapping().header()[i].length() > 0) {
                    header.add(methodInfo.getRequestMapping().header()[i]);
                }
            }
            crossDomain = methodInfo.getRequestMapping().crossDomain();
        }
        if (methodInfo.getGetMapping() != null) {
            for (int i = 0; i < methodInfo.getGetMapping().header().length; i++) {
                if (methodInfo.getGetMapping().header()[i].length() > 0) {
                    header.add(methodInfo.getGetMapping().header()[i]);
                }
            }
            crossDomain = methodInfo.getGetMapping().crossDomain();
        }
        if (methodInfo.getPostMapping() != null) {
            for (int i = 0; i < methodInfo.getPostMapping().header().length; i++) {
                if (methodInfo.getPostMapping().header()[i].length() > 0) {
                    header.add(methodInfo.getPostMapping().header()[i]);
                }
            }
            crossDomain = methodInfo.getPostMapping().crossDomain();
        }
        if (methodInfo.getPutMapping() != null) {
            for (int i = 0; i < methodInfo.getPutMapping().header().length; i++) {
                if (methodInfo.getPutMapping().header()[i].length() > 0) {
                    header.add(methodInfo.getPutMapping().header()[i]);
                }
            }
            crossDomain = methodInfo.getPutMapping().crossDomain();
        }
        if (methodInfo.getDeleteMapping() != null) {
            for (int i = 0; i < methodInfo.getDeleteMapping().header().length; i++) {
                if (methodInfo.getDeleteMapping().header()[i].length() > 0) {
                    header.add(methodInfo.getDeleteMapping().header()[i]);
                }
            }
            crossDomain = methodInfo.getDeleteMapping().crossDomain();
        }
        for (int i = 0; i < header.size(); i++) {
            if (header.get(i).length() > 0) {
                String[] ss1 = header.get(i).split(":", 2);
                if (ss1.length == 2) {
                    response.setHeader(ss1[0].trim(), ss1[1].trim());
                }
            }
        }
        if (crossDomain.length() > 0) {
            // 如果没有传递域名，则允许所有域名进行跨域访问
            response.setHeader("Access-Control-Allow-Origin", requestMapping.crossDomain());
            // 允许的请求方法
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            // 允许的请求头
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            // 是否允许携带凭证（如 cookies）
            response.setHeader("Access-Control-Allow-Credentials", "true");
            // 预检请求的有效期（秒）
            response.setHeader("Access-Control-Max-Age", "3600");
        }
    }
}
