/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame.factory.v4;

import gzb.frame.PublicData;
import gzb.frame.annotation.*;
import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseFactory;
import gzb.frame.db.EventFactory;
import gzb.frame.factory.*;
import gzb.entity.ClassEntity;
import gzb.entity.DecoratorEntity;
import gzb.entity.HttpMapping;
import gzb.entity.ThreadEntity;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.entity.RunRes;
import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbNodeMap;
import gzb.tools.json.GzbJson;
import gzb.tools.json.GzbJsonImpl;
import gzb.tools.json.ResultImpl;
import gzb.tools.log.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class FactoryImplV2 implements Factory {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(FactoryImplV2.class));
        FactoryImplV2 factory = new FactoryImplV2();
    }

    public Map<String, Map<String, Method>> mapClassMethod = new ConcurrentHashMap<>();
    public static Log log = Log.log;
    public Map<String, ClassEntity> mapClassEntity = new ConcurrentHashMap<>();
    // public Map<String, HttpMapping[]> mapHttpMapping0 = new ConcurrentHashMap<>();
/*   public GzbNodeMap<HttpMapping[]> gzbNodeMap = new GzbNodeMap<>();
    public Map<String, HttpMapping[]> mapHttpMapping0 = new ConcurrentHashMap<String, HttpMapping[]>(){
        @Override
        public HttpMapping[] put(String key, HttpMapping[] value) {
            gzbNodeMap.put((String)key,(HttpMapping[])value);
            return super.put(key,value);
        }

        @Override
        public HttpMapping[] get(Object key) {
            return gzbNodeMap.get((String)key);
        }
    };*/
    public  Map<String, HttpMapping[]> mapHttpMapping0 = new ConcurrentHashMap<String, HttpMapping[]>();
    public Map<String, Object> mapObject0 = new ConcurrentHashMap<>();
    List<DecoratorEntity> listDecoratorEntity = new ArrayList<>();
    public Map<String, Map<String, String>> mapHttpMappingOld0 = new ConcurrentHashMap<>();
    public static int serverState = -1;
    public String[] met = Constant.requestMethod;
    public Map<String, ThreadEntity> mapListThreadEntity0 = new ConcurrentHashMap<>();

    public GzbJson gzbJson = PublicData.gzbJson;

    public void loadJavaDir(String classDir, String pwd, String iv) throws Exception {
        //缓存对象
        ClassTools.putObject(Cache.gzbCache.getClass(), null, mapObject0, Cache.gzbCache);
        ClassTools.putObject(Cache.gzbQueue.getClass(), null, mapObject0, Cache.gzbQueue);
        //JSON对象
        ClassTools.putObject(gzbJson.getClass(), null, mapObject0, gzbJson);
        //数据库事件对象
        ClassTools.putObject(PublicData.eventFactory.getClass(), null, mapObject0, PublicData.eventFactory);
        //日志对象
        ClassTools.putObject(Log.log.getClass(), null, mapObject0, Log.log);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    List<ClassEntity> listClassEntity = loadFiles(classDir, pwd, iv, mapClassEntity);
                    load(listClassEntity);
                } catch (Exception e) {
                    log.e(e);
                }
                serverState = 1;
                Tools.sleep(1000 * 3);
            }
        });
        thread.start();
        while (serverState == -1) {
            Tools.sleep(1);
        }
        System.gc();
    }

    public void loadServerHttp() throws Exception {

        //缓存对象
        ClassTools.putObject(Cache.gzbCache.getClass(), null, mapObject0, Cache.gzbCache);
        ClassTools.putObject(Cache.gzbQueue.getClass(), null, mapObject0, Cache.gzbQueue);
        //JSON对象
        ClassTools.putObject(gzbJson.getClass(), null, mapObject0, gzbJson);
        //数据库事件对象
        ClassTools.putObject(PublicData.eventFactory.getClass(), null, mapObject0, PublicData.eventFactory);
        //日志对象
        ClassTools.putObject(Log.log.getClass(), null, mapObject0, Log.log);
        UpdateServer updateServer = new UpdateServer();
        Thread thread = new Thread(() -> {
            int miao = 0;
            long ver = Config.version;
            while (true) {

                try {
                    //登陆验证  两份 验证一次是否在线
                    if (miao % 120 == 0) {
                        updateServer.reg_server(1L);
                    }
                    //检查更新
                    if (miao % 5 == 0) {
                        List<Map<String, Object>> list = updateServer.readFileList(ver);
                        if (list != null) {
                            Map<String, String> map0 = new HashMap<>();
                            for (Map<String, Object> map : list) {
                                updateServer.readListCode(map0, map.get("data").toString());
                            }
                            System.out.println("准备编译：" + map0.size());
                            Map<String, Class<?>> listClass = null;
                            if (map0.size() > 0) {
                                listClass = ClassLoad.compileJavaCode(map0);
                            } else {
                                listClass = new HashMap<>();
                            }
                            System.out.println("编译成功：" + listClass.size());
                            List<ClassEntity> listClassEntity = new ArrayList<>(listClass.size());
                            for (Map.Entry<String, Class<?>> stringClassEntry : listClass.entrySet()) {
                                ClassEntity classEntity0 = new ClassEntity();
                                classEntity0.sign = stringClassEntry.getKey();
                                classEntity0.code = map0.get(stringClassEntry.getKey());
                                classEntity0.clazz = stringClassEntry.getValue();
                                classEntity0.iv = null;
                                classEntity0.pwd = null;
                                classEntity0.filePath = stringClassEntry.getKey();
                                mapClassEntity.put(stringClassEntry.getKey(), classEntity0);
                                listClassEntity.add(classEntity0);
                            }
                            load(listClassEntity);
                            ver = updateServer.version;
                        }

                    }
                    if (miao > 12000) {
                        miao = 0;
                    }
                } catch (Exception e) {
                    log.e(e);
                }
                serverState = 1;
                miao++;
                Tools.sleep(1000 * 60, 1000 * 600);
            }


        });
        thread.start();
        while (serverState == -1) {
            Tools.sleep(1);
        }
        System.gc();
    }

    public void load(List<ClassEntity> listClassEntity) throws Exception {
        if (!listClassEntity.isEmpty()) {
            loadObjects(listClassEntity, mapObject0, Service.class);
            loadObjects(listClassEntity, mapObject0, Controller.class);
            loadObjects(listClassEntity, mapObject0, DataBaseEventFactory.class);
            loadObjects(listClassEntity, mapObject0, Decorator.class);
            loadObjects(listClassEntity, mapObject0, ThreadFactory.class);

            //mapObject0 中 所有对象 执行重新注入一遍
            for (Map.Entry<String, Object> stringObjectEntry : mapObject0.entrySet()) {
                ClassTools.classInject(stringObjectEntry.getValue(), null, mapObject0);
            }
            //重新加载一遍 控制器映射
            for (Map.Entry<String, ClassEntity> stringClassEntityEntry : mapClassEntity.entrySet()) {
                ClassEntity classEntity = stringClassEntityEntry.getValue();
                if (classEntity.clazz == null) {
                    continue;
                }
                loadController(classEntity.clazz, mapObject0.get(classEntity.clazz.getName()), mapHttpMapping0, mapHttpMappingOld0, listDecoratorEntity);
            }
            if (Config.permissionsOpen) {
                DataBase dataBase = null;
                try {
                    dataBase = DataBaseFactory.getDataBase(Config.frameDbKey);
                } catch (Exception e) {
                    log.e("默认数据连接失败 请确保 配置项 db.frame.key 填写正确", e);
                }
                UpdatePermission.exec(mapHttpMapping0, met, dataBase);
            }
        }
    }

    public void load(Map<String, String> sourcesMap, String pwd, String iv) throws Exception {
        if (sourcesMap == null || sourcesMap.size() == 0) {
            return;
        }
        Map<String, File> fileMap = new HashMap<>();
        List<ClassEntity> listClassEntity = loadFiles(sourcesMap, fileMap, pwd, iv, mapClassEntity);
        load(listClassEntity);
    }

    public List<ClassEntity> loadFiles(String classDir, String pwd, String iv, Map<String, ClassEntity> mapClassEntity) throws Exception {
        List<File> listFile = new ArrayList<>();
        String[] arr1 = classDir.split(",");
        Map<String, File> fileMap = new HashMap<>();
        Map<String, String> sourcesMap = new HashMap<>();
        File[] files = new File[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = Tools.pathFormat(arr1[i].trim());
            if (arr1[i].isEmpty()) {
                continue;
            }
            File file = new File(arr1[i]);
            files[i] = file;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i] == null) {
                continue;
            }
            if (files[i].isDirectory()) {
                //long start = System.nanoTime();
                List<File> list = FileTools.subFileAll(files[i], 2, ".java");
                //long end = System.nanoTime();
                //log.w(end-start);
                listFile.addAll(list);
            } else {
                log.w("代码目录不存在或错误", arr1[i]);
            }
        }
        for (File file : listFile) {
            String path = file.getPath().replaceAll("\\\\", "/");
            Long time = file.lastModified();
            ClassEntity classEntity = mapClassEntity.get(path);
            if (classEntity != null && classEntity.sign.equals(time + "")) {
                continue;
            }
            String code;
            if (pwd != null && iv != null) {
                byte[] bytes = AES_CBC_128.aesDe(FileTools.readByte(file), pwd, iv);
                code = new String(bytes, Config.encoding);
            } else {
                code = FileTools.readString(file);
            }
            String className = ClassLoad.extractPublicClassName(code);

            if (className.contains("InMemory")) {
                log.t("排除类", className, "无法获取类名或者是非class。");
                ClassEntity classEntity0 = new ClassEntity();
                classEntity0.sign = time + "";
                classEntity0.code = code;
                classEntity0.clazz = null;
                classEntity0.iv = iv;
                classEntity0.pwd = pwd;
                classEntity0.filePath = path;
                mapClassEntity.put(path, classEntity0);
            } else {
                fileMap.put(className, file);
                sourcesMap.put(className, code);
            }
        }
        if (sourcesMap.size() > 0 && fileMap.size() > 0) {
            return loadFiles(sourcesMap, fileMap, pwd, iv, mapClassEntity);
        } else {
            return new ArrayList<>();
        }
    }

    public List<ClassEntity> loadFiles(Map<String, String> sourcesMap, Map<String, File> fileMap, String pwd, String iv, Map<String, ClassEntity> mapClassEntity) throws Exception {
        List<ClassEntity> listClassEntity = new ArrayList<>();
        Map<String, Class<?>> classMap = ClassLoad.compileJavaCode(sourcesMap);
        for (Map.Entry<String, Class<?>> stringClassEntry : classMap.entrySet()) {
            Class<?> aClass = stringClassEntry.getValue();
            File file = fileMap == null ? null : fileMap.get(stringClassEntry.getKey());
            String code = sourcesMap.get(stringClassEntry.getKey());
            String path = file == null ? null : file.getPath().replaceAll("\\\\", "/");
            Long time = file == null ? null : file.lastModified();
            if (aClass == null || file == null) {
                continue;
            }
            ClassEntity classEntity = mapClassEntity.get(path);
            if (classEntity == null) {
                classEntity = new ClassEntity();
                mapClassEntity.put(path, classEntity);
            }
            classEntity.sign = time + "";
            classEntity.code = code;
            classEntity.clazz = aClass;
            classEntity.iv = iv;
            classEntity.pwd = pwd;
            classEntity.filePath = path;
            listClassEntity.add(classEntity);
        }
        return listClassEntity;
    }

    public Map<String, Object> getMapObject() {
        return mapObject0;
    }

    public RunRes request0(Request request, Response response) {
        RunRes runRes = null;
        Object[] objects;
        objects = PublicData.context.get();
        if (objects == null) {
            runRes = new RunRes();
            objects = new Object[]{
                    runRes,
                    gzbJson,
                    log,
                    request,
                    response,
                    null  // 请求参数
            };
            PublicData.context.set(objects);
        } else {
            objects[3] = request;
            objects[4] = response;
            runRes = (RunRes) objects[0];
            runRes.setData(null);
        }
        runRes.setState(200);
        String key = ClassTools.webPathFormat(request.getUri());
        String metName = request.getMethod();
        HttpMapping[] httpMappings = mapHttpMapping0.get(key);
        if (httpMappings == null) {
            return runRes.setState(404);
        }
        int index = -1;
        try {
            for (int i = 0; i < met.length; i++) {
                if (metName.equals(met[i])) {
                    if (httpMappings[i] == null) {
                        return runRes.setState(404);//目的是检查静态资源
                    }
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return runRes.setState(404);
            }
            if (httpMappings[index].header.size() > 0) {
                response.setHeaders(new HashMap<>(httpMappings[index].header));
            }
            if (httpMappings[index].isCrossDomainOrigin) {
                String host = request.getOrigin();
                if (host == null) {
                    host = request.getDomain();
                }
                response.setHeader("access-control-allow-origin", host);
            }

            if (httpMappings[index].semaphore != null && !httpMappings[index].semaphore.tryAcquire()) {
                return runRes.setState(200).setData(gzbJson.fail("请求量超过限流阈值，请稍后重试"));
            }
            Map<String, List<Object>> parar = request.getParameter();
            objects[5] = parar;
            for (DecoratorEntity decoratorEntity : httpMappings[index].start) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        //log.d("request", key, request.getMethod(), parar, 200, "请求被调用前拦截");
                        return runRes1;
                    } else {
                        objects[0] = runRes1;
                        runRes = runRes1;
                        if (runRes1.getData() != null) {
                            Object[] newArray = Arrays.copyOf(objects, objects.length + 1);
                            newArray[newArray.length - 1] = runRes1.getData();
                            objects = newArray;
                        }
                    }
                }
            }
            Object obj02 = httpMappings[index].httpMappingFun._gzb_call_x01(httpMappings[index].id, mapObject0, request, response, parar, gzbJson, log, objects);
            runRes.setData(obj02);
            for (DecoratorEntity decoratorEntity : httpMappings[index].end) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        //log.d("request", key, request.getMethod(), parar, 200, "请求被调用后拦截");
                        return runRes1;
                    } else {
                        objects[0] = runRes1;
                        runRes = runRes1;
                    }
                }
            }
        } catch (Exception e) {
            log.e("框架层错误日志",
                    key,
                    request.getMethod(),
                    request.getParameter(),
                    e);
            return runRes.setState(200).setData(gzbJson.error("访问出现错误"));
        } finally {
            //限流如果开启 解除占用
            if (index > -1 && httpMappings[index] != null && httpMappings[index].semaphore != null) {
                httpMappings[index].semaphore.release();
            }
        }

        return runRes;
    }

    public RunRes request(Request request, Response response) {
        GzbTiming gzbTiming = new GzbTiming();
        gzbTiming.record();
        RunRes runRes = null;
        Object[] objects;
        objects = PublicData.context.get();
        if (objects == null) {
            runRes = new RunRes();
            objects = new Object[]{
                    runRes,
                    gzbJson,
                    log,
                    request,
                    response,
                    null  // 请求参数
            };
            PublicData.context.set(objects);
        } else {
            objects[3] = request;
            objects[4] = response;
            runRes = (RunRes) objects[0];
            runRes.setData(null);
        }
        gzbTiming.record("线程变量");
        String key = ClassTools.webPathFormat(request.getUri());
        gzbTiming.record("格式化请求地址");
        long start = System.nanoTime();
        HttpMapping[] httpMappings = mapHttpMapping0.get(key);
        if (httpMappings == null) {
            return runRes.setState(404);
        }
        long end = System.nanoTime();
        gzbTiming.record("端点获取");
        int index = -1;
        String metName = request.getMethod();
        try {
            for (int i = 0; i < met.length; i++) {
                if (metName.equals(met[i])) {
                    if (httpMappings[i] == null) {
                        return runRes.setState(404);//目的是检查静态资源
                    }
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return runRes.setState(404);
            }
            gzbTiming.record("请求方法");
            if (httpMappings[index].header.size() > 0) {
                response.setHeaders(new HashMap<>(httpMappings[index].header));
            }
            if (httpMappings[index].isCrossDomainOrigin) {
                String host = request.getOrigin();
                if (host == null) {
                    host = request.getDomain();
                }
                response.setHeader("access-control-allow-origin", host);
            }
            gzbTiming.record("协议头");
            if (httpMappings[index].semaphore != null && !httpMappings[index].semaphore.tryAcquire()) {
                return runRes.setState(200).setData(gzbJson.fail("请求量超过限流阈值，请稍后重试"));
            }
            gzbTiming.record("限流");
            Map<String, List<Object>> parar = request.getParameter();
            objects[5] = parar;
            for (DecoratorEntity decoratorEntity : httpMappings[index].start) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        //log.d("request", key, request.getMethod(), parar, 200, "请求被调用前拦截");
                        return runRes1;
                    } else {
                        objects[0] = runRes1;
                        runRes = runRes1;
                        if (runRes1.getData() != null) {
                            Object[] newArray = Arrays.copyOf(objects, objects.length + 1);
                            newArray[newArray.length - 1] = runRes1.getData();
                            objects = newArray;
                        }
                    }
                }
            }
            gzbTiming.record("装饰器-前");
            Object obj02 = httpMappings[index].httpMappingFun._gzb_call_x01(httpMappings[index].id, mapObject0, request, response, parar, gzbJson, log, objects);
            gzbTiming.record("端点调用");
            runRes.setData(obj02);
            for (DecoratorEntity decoratorEntity : httpMappings[index].end) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        //log.d("request", key, request.getMethod(), parar, 200, "请求被调用后拦截");
                        return runRes1;
                    } else {
                        objects[0] = runRes1;
                        runRes = runRes1;
                    }
                }
            }
            gzbTiming.record("装饰器-后");
        } catch (Exception e) {
            log.e("框架层错误日志",
                    key,
                    request.getMethod(),
                    request.getParameter(),
                    e);
            return runRes.setState(200).setData(gzbJson.error("访问出现错误"));
        } finally {
            //限流如果开启 解除占用
            if (index > -1 && httpMappings[index] != null && httpMappings[index].semaphore != null) {
                httpMappings[index].semaphore.release();
            }
        }

        log.t((end - start), gzbTiming.read());
        /// 后台统计 汇总
        AtomicInteger atomicInteger = reqInfo.computeIfAbsent(gzbTiming.all / 1000, k -> new AtomicInteger());
        atomicInteger.addAndGet(1);
        return runRes;
    }

    public RunRes request2(Request request, Response response) {
        GzbTiming gzbTiming = new GzbTiming();
        gzbTiming.record();
        RunRes runRes = new RunRes();
        Object[] objects;
        runRes.setState(200);
        String key = ClassTools.webPathFormat(request.getUri());
        String metName = request.getMethod();
        HttpMapping[] httpMappings = mapHttpMapping0.get(key);
        if (httpMappings == null) {
            return runRes.setState(404);
        }
        gzbTiming.record("获取端点");
        int index = -1;
        try {
            for (int i = 0; i < met.length; i++) {
                if (metName.equals(met[i])) {
                    if (httpMappings[i] == null) {
                        return runRes.setState(404);
                    }
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return runRes.setState(404);
            }
            gzbTiming.record("确定方法");

            if (httpMappings[index].semaphore != null && !httpMappings[index].semaphore.tryAcquire()) {
                return runRes.setState(200).setData(gzbJson.fail("请求量超过限流阈值，请稍后重试"));
            }

            gzbTiming.record("限流");
            if (httpMappings[index].header.size() > 0) {
                response.setHeaders(new HashMap<>(httpMappings[index].header));
            }
            if (httpMappings[index].isCrossDomainOrigin) {
                String host = request.getOrigin();
                if (host == null) {
                    host = request.getDomain();
                }
                response.setHeader("access-control-allow-origin", host);
            }
            gzbTiming.record("预设协议");
            Map<String, List<Object>> parar = request.getParameter();

            objects = PublicData.context.get();
            if (objects == null) {
                objects = new Object[]{
                        runRes,
                        gzbJson,
                        log,
                        request,
                        response,
                        parar  // 请求参数
                };
                PublicData.context.set(objects);
            } else {
                objects[3] = request;
                objects[4] = response;
                objects[5] = parar;
            }
            gzbTiming.record("线程变量");
            for (DecoratorEntity decoratorEntity : httpMappings[index].start) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        //log.d("request", key, request.getMethod(), parar, 200, "请求被调用前拦截");
                        return runRes1;
                    } else {
                        objects[0] = runRes1;
                        runRes = runRes1;
                        if (runRes1.getData() != null) {
                            Object[] newArray = Arrays.copyOf(objects, objects.length + 1);
                            newArray[newArray.length - 1] = runRes1.getData();
                            objects = newArray;
                        }
                    }
                }
            }
            gzbTiming.record("装饰器-前");
            runRes.setData(httpMappings[index].httpMappingFun._gzb_call_x01(httpMappings[index].id, mapObject0, request, response, parar, gzbJson, log, objects));
            gzbTiming.record("端点调用");
            for (DecoratorEntity decoratorEntity : httpMappings[index].end) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        //log.d("request", key, request.getMethod(), parar, 200, "请求被调用后拦截");
                        return runRes1;
                    } else {
                        objects[0] = runRes1;
                        runRes = runRes1;
                    }
                }
            }
            gzbTiming.record("装饰器-后");
        } catch (Exception e) {
            log.e("框架层错误日志",
                    key,
                    request.getMethod(),
                    request.getParameter(),
                    e);
            return runRes.setState(500).setData(gzbJson.error("访问出现错误"));
        } finally {
            //限流如果开启 解除占用
            if (index > -1 && httpMappings[index] != null && httpMappings[index].semaphore != null) {
                httpMappings[index].semaphore.release();
            }
            /// 函数执行 结束

            /// 输出调试信息
            log.t(gzbTiming.read());
            /// 后台统计 汇总
            AtomicInteger atomicInteger = reqInfo.computeIfAbsent(gzbTiming.all / 1000, k -> new AtomicInteger());
            atomicInteger.addAndGet(1);
        }

        return runRes;
    }

    public static Map<Long, AtomicInteger> reqInfo = new ConcurrentHashMap<>();


    public Class<?> loadController(Class<?> clazz, Object object, Map<String, HttpMapping[]> mapHttpMapping, Map<String, Map<String, String>> mapHttpMappingOld,
                                   List<DecoratorEntity> listDecoratorEntity) throws Exception {
        Controller controller = clazz.getAnnotation(Controller.class);
        Header header = clazz.getAnnotation(Header.class);
        if (controller != null) {
            List<String> listClassPath = new ArrayList<>();
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            CrossDomain crossDomain = clazz.getAnnotation(CrossDomain.class);
            if (requestMapping != null && requestMapping.value().length > 0 && requestMapping.value()[0] != null) {
                for (String string : requestMapping.value()) {
                    if (string != null && !string.isEmpty()) {
                        listClassPath.add(string);
                    }
                }
            }
            if (listClassPath.isEmpty()) {
                listClassPath.add("/");
            }

            Map<String, String> map1 = mapHttpMappingOld.get(clazz.getName());
            if (map1 == null) {
                map1 = new HashMap<>();
                mapHttpMappingOld.put(clazz.getName(), map1);
            }
            Map<String, String> map2 = new HashMap<>(map1);

            //log.d("map1 最初",map1);
            //log.d("map2 最初",map2);

            Method[] methods = ClassTools.getCombinedMethods(clazz);

            for (int j = 0; j < methods.length; j++) {
                Method method = methods[j];
                int id = ClassTools.getSingInt(method, clazz);
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                RequestMapping requestMappingMethod = method.getAnnotation(RequestMapping.class);
                Transaction transaction = method.getAnnotation(Transaction.class);
                DecoratorOpen decoratorOpen = method.getAnnotation(DecoratorOpen.class);
                CrossDomain metCrossDomain = method.getAnnotation(CrossDomain.class);
                Header headerMethod = method.getAnnotation(Header.class);
                Limitation limitation = method.getAnnotation(Limitation.class);
                if (getMapping != null && getMapping.value().length > 0) {
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 0, getMapping.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);

                } else if (postMapping != null && postMapping.value().length > 0) {
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 1, postMapping.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);

                } else if (putMapping != null && putMapping.value().length > 0) {
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 2, putMapping.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);

                } else if (deleteMapping != null && deleteMapping.value().length > 0) {
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 3, deleteMapping.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);

                } else if (requestMappingMethod != null && requestMappingMethod.value().length > 0) {
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 0, requestMappingMethod.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 1, requestMappingMethod.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 2, requestMappingMethod.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);
                    putMapping(id, crossDomain, metCrossDomain, header, headerMethod, clazz, 3, requestMappingMethod.value(), method, object, transaction, decoratorOpen, limitation, listClassPath, mapHttpMapping, listDecoratorEntity, map1, map2);

                }
            }

            for (Map.Entry<String, String> stringStringEntry : map2.entrySet()) {
                map1.remove(stringStringEntry.getKey());
                mapHttpMapping.remove(stringStringEntry.getKey());
                log.t("删除", stringStringEntry.getKey());
            }
            //log.d("map1 最终",map1);
            //log.d("map2 最终",map2);
        }

        return null;
    }

    public void DecoratorAdd(String url, String met, int startOrEnd,
                             List<DecoratorEntity> listDecoratorEntity1, List<DecoratorEntity> listDecoratorEntity) {
        //log.d("isDecorator 0 ",listDecorator);
        Map<String, List<DecoratorEntity>> map1 = new HashMap<>();
        for (DecoratorEntity decoratorEntity : listDecoratorEntity1) {
            if (startOrEnd == 1 && decoratorEntity.decoratorStart != null) {
                for (String string : decoratorEntity.decoratorStart.value()) {
                    int next = -1;
                    for (String string2 : decoratorEntity.decoratorStart.method()) {
                        if (met.equals(string2)) {
                            next = 1;
                            break;
                        }
                    }
                    if (next == 1) {
                        string = ClassTools.webPathFormat(string);
                        boolean suc = false;
                        if (decoratorEntity.decoratorStart.type()) {
                            if (url.contains(string)) {
                                suc = true;
                            }
                        } else {
                            if (url.startsWith(string)) {
                                suc = true;
                            }
                        }
                        if (decoratorEntity.decoratorStart.turn()) {
                            suc = !suc;
                        }
                        if (suc) {
                            List<DecoratorEntity> list001 = map1.computeIfAbsent(decoratorEntity.decoratorStart.sort() + "", k -> new ArrayList<>());
                            list001.add(decoratorEntity);
                        }
                        //log.t("isDecorator start", url, string, startOrEnd, suc, listDecoratorEntity.size());
                    }

                }
            }
            if (startOrEnd == 2 && decoratorEntity.decoratorEnd != null) {
                for (String string : decoratorEntity.decoratorEnd.value()) {
                    int next = -1;
                    for (String string2 : decoratorEntity.decoratorEnd.method()) {
                        if (met.equals(string2)) {
                            next = 1;
                            break;
                        }
                    }
                    if (next == 1) {
                        string = ClassTools.webPathFormat(string);
                        boolean suc = false;
                        if (decoratorEntity.decoratorEnd.type()) {
                            if (url.contains(string)) {
                                suc = true;
                            }
                        } else {
                            if (url.startsWith(string)) {
                                suc = true;
                            }
                        }
                        if (decoratorEntity.decoratorEnd.turn()) {
                            suc = !suc;
                        }
                        if (suc) {
                            List<DecoratorEntity> list001 = map1.computeIfAbsent(decoratorEntity.decoratorEnd.sort() + "", k -> new ArrayList<>());
                            list001.add(decoratorEntity);
                        }
                        //log.t("isDecorator end", url, string, startOrEnd, suc, listDecoratorEntity.size());
                    }

                }
            }


        }
        int x = 0;
        while (true) {
            List<DecoratorEntity> list001 = map1.get(x + "");
            if (list001 == null) {
                break;
            }
            listDecoratorEntity.addAll(list001);
            x++;
        }
        //log.t("listDecoratorEntity", url, met, listDecoratorEntity.size());
    }

    public void putMapping(int id, CrossDomain classCrossDomain, CrossDomain metCrossDomain, Header headerClass, Header headerMethod,
                           Class<?> aClass, int index, String[] metPath, Method method, Object object,
                           Transaction transaction, DecoratorOpen decoratorOpen, Limitation limitation,
                           List<String> listClassPath, Map<String, HttpMapping[]> mapHttpMapping
            , List<DecoratorEntity> listDecoratorEntity, Map<String, String> map1, Map<String, String> map2) {

        for (String string : metPath) {
            if (string == null || string.isEmpty()) {
                string = "/";
            }
            //log.d("object", object, Arrays.toString(object.getClass().getInterfaces()));

            for (String str2 : listClassPath) {
                if (str2 == null || str2.isEmpty()) {
                    continue;
                }
                HttpMapping httpMapping2 = new HttpMapping();
                httpMapping2.sign = ClassTools.getSing(method, aClass);
                httpMapping2.id = id;
                httpMapping2.transaction = transaction != null ? (transaction.simulate() ? 2 : 1) : null;
                httpMapping2.httpMappingFun = (GzbOneInterface) object;
                httpMapping2.met = index;
                httpMapping2.start = new ArrayList<>();
                httpMapping2.end = new ArrayList<>();
                String path = ClassTools.webPathFormat(str2 + "/" + string);
                if (decoratorOpen != null && decoratorOpen.value()) {
                    DecoratorAdd(path, Constant.requestMethod[httpMapping2.met], 1, listDecoratorEntity, httpMapping2.start);
                    DecoratorAdd(path, Constant.requestMethod[httpMapping2.met], 2, listDecoratorEntity, httpMapping2.end);
                }
                if (limitation != null && limitation.value() > 0) {
                    httpMapping2.semaphore = new Semaphore(limitation.value());
                } else {
                    httpMapping2.semaphore = null;
                }

                HttpMapping[] httpMappings = mapHttpMapping.get(path);
                httpMapping2.path = path;
                if (httpMappings != null && httpMappings[index] != null) {
                    if (!httpMappings[index].httpMappingFun.getClass().getName().equals(httpMapping2.httpMappingFun.getClass().getName())) {
                        log.w("HTTP端点", "异常替换", id,
                                "由",
                                httpMappings[index].sign, httpMappings[index].path, met[httpMappings[index].met],
                                "替换为",
                                httpMapping2.sign, httpMapping2.path, met[index]);
                    }
                } else {
                    log.t("HTTP端点", "添加", id, path, met[index], httpMapping2.sign);
                }
                httpMapping2.header = new HashMap<>();
                if (headerClass != null) {
                    for (HeaderItem headerItem : headerClass.item()) {
                        if (headerItem.key().isEmpty() || headerItem.val().isEmpty()) {
                            continue;
                        }
                        httpMapping2.header.put(headerItem.key(), headerItem.val());
                    }
                }
                if (headerMethod != null) {
                    for (HeaderItem headerItem : headerMethod.item()) {
                        if (headerItem.key().isEmpty() || headerItem.val().isEmpty()) {
                            continue;
                        }
                        httpMapping2.header.put(headerItem.key(), headerItem.val());
                    }
                }

                if (classCrossDomain != null) {
                    ClassTools.generateCORSHeaders(classCrossDomain, httpMapping2.header);
                }
                if (metCrossDomain != null) {
                    ClassTools.generateCORSHeaders(metCrossDomain, httpMapping2.header);
                }

                if (httpMapping2.header.get("access-control-allow-origin") != null) {
                    if (httpMapping2.header.get("access-control-allow-origin").equals("origin")) {
                        httpMapping2.isCrossDomainOrigin = true;
                    }
                }
                httpMappings = new HttpMapping[4];
                httpMappings[index] = httpMapping2;
                mapHttpMapping.put(path, httpMappings);
                map1.put(path, "1");
                map2.remove(path);
            }
        }
    }


    //返回对象 并且加入缓存
    public DecoratorEntity loadDecorator(ClassEntity classEntity, List<DecoratorEntity> listDecoratorEntity, Object object) throws Exception {
        Decorator decorator = classEntity.clazz.getAnnotation(Decorator.class);
        DecoratorEntity decoratorEntity = null;
        if (decorator != null) {
            decoratorEntity = new DecoratorEntity();
            try {
                decoratorEntity.call = (GzbOneInterface) object;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            decoratorEntity.fields = ClassTools.getCombinedFields(classEntity.clazz);
            List<String> listNames = new ArrayList<>();
            List<Class<?>> listTypes = new ArrayList<>();
            for (Field field : decoratorEntity.fields) {
                listNames.add(field.getName());
                listTypes.add(field.getType());
            }
            decoratorEntity.fieldNames = listNames.toArray(new String[0]);
            decoratorEntity.fieldTypes = listTypes.toArray(new Class[0]);
            Method[] methods = ClassTools.getCombinedMethods(classEntity.clazz);
            //log.d("loadDecorator 0 ",clazz, Arrays.toString(methods));
            for (int j = 0; j < methods.length; j++) {
                Method method = methods[j];
                String sign = ClassTools.getSing(method, classEntity.clazz);
                int id = ClassTools.getSingInt(method, classEntity.clazz);
                DecoratorStart decoratorStart = method.getAnnotation(DecoratorStart.class);
                DecoratorEnd decoratorEnd = method.getAnnotation(DecoratorEnd.class);
                //log.d("loadDecorator 1 ",decoratorStart, decoratorEnd,decoratorStart != null && decoratorStart.value() != null,decoratorEnd != null && decoratorEnd.value() != null);
                if (decoratorStart != null) {
                    for (String string : decoratorStart.value()) {
                        if (string == null || string.isEmpty()) {
                            continue;
                        }
                        DecoratorEntity decoratorEntity1 = new DecoratorEntity();
                        decoratorEntity1.call = decoratorEntity.call;
                        decoratorEntity1.fields = decoratorEntity.fields;
                        decoratorEntity1.fieldNames = decoratorEntity.fieldNames;
                        decoratorEntity1.fieldTypes = decoratorEntity.fieldTypes;
                        decoratorEntity1.sign = sign;
                        decoratorEntity1.id = id;
                        decoratorEntity1.method = method;
                        decoratorEntity1.decoratorStart = decoratorStart;
                        decoratorEntity1.classEntity = classEntity;
                        listDecoratorEntity.add(decoratorEntity1);
                    }
                }
                if (decoratorEnd != null) {
                    for (String string : decoratorEnd.value()) {
                        if (string == null || string.isEmpty()) {
                            continue;
                        }
                        DecoratorEntity decoratorEntity1 = new DecoratorEntity();
                        decoratorEntity1.call = decoratorEntity.call;
                        decoratorEntity1.fields = decoratorEntity.fields;
                        decoratorEntity1.fieldNames = decoratorEntity.fieldNames;
                        decoratorEntity1.fieldTypes = decoratorEntity.fieldTypes;
                        decoratorEntity1.sign = sign;
                        decoratorEntity1.id = id;
                        decoratorEntity1.method = method;
                        decoratorEntity1.decoratorEnd = decoratorEnd;
                        decoratorEntity1.classEntity = classEntity;
                        listDecoratorEntity.add(decoratorEntity1);
                    }
                }
            }
        }
        return decoratorEntity;
    }

    //返回对象 并且加入缓存
    public List<ClassEntity> loadObjects(List<ClassEntity> listClassEntity, Map<String, Object> mapObject, Class aClass) throws Exception {
        if (listClassEntity.size() < 1) {
            return listClassEntity;
        }
        Map<String, File> fileMap = new HashMap<>();
        Map<String, String> sourcesMap = new HashMap<>();
        String pwd = "", iv = "";
        for (int i = 0; i < listClassEntity.size(); i++) {
            ClassEntity classEntity = listClassEntity.get(i);
            if (classEntity.clazz.getAnnotation(aClass) != null) {
                String code = ClassTools.gen_call_code_v4_all_code(classEntity.clazz, classEntity.code);
                String name = ClassLoad.extractPublicClassName(code);
                fileMap.put(name, new File(classEntity.filePath));
                sourcesMap.put(name, code);
                pwd = classEntity.pwd;
                iv = classEntity.iv;
            }
        }
        if (sourcesMap.size() > 0) {
            List<ClassEntity> list = loadFiles(sourcesMap, fileMap, pwd, iv, mapClassEntity);
            for (ClassEntity classEntity : list) {
                Object obj = ClassTools.putObject(classEntity.clazz, null, mapObject);

                if (aClass == DataBaseEventFactory.class) {
                    EventFactory eventFactory = (EventFactory) mapObject.get(EventFactory.class.getName());
                    eventFactory.register(classEntity.clazz, classEntity.code);
                }
                if (aClass == ThreadFactory.class) {
                    Method[] methods = classEntity.clazz.getMethods();
                    Map<String, Method> mapMethodThreadFactory0 = new ConcurrentHashMap<>(mapMethodThreadFactory);
                    for (int i = 0; i < methods.length; i++) {
                        ThreadInterval threadInterval = methods[i].getDeclaredAnnotation(ThreadInterval.class);
                        if (threadInterval == null) {
                            continue;
                        }
                        startThreadFactory(classEntity.clazz, methods[i], mapObject, threadInterval);
                        String key = ClassTools.getSing(methods[i], classEntity.clazz);
                        mapMethodThreadFactory0.remove(key);
                        mapMethodThreadFactory.put(key, methods[i]);
                    }
                    for (Map.Entry<String, Method> stringMethodEntry : mapMethodThreadFactory0.entrySet()) {
                        mapMethodThreadFactory.remove(ClassTools.getSing(stringMethodEntry.getValue(), classEntity.clazz));
                    }
                }
                if (aClass == Decorator.class) {
                    Map<String, Method> map = mapClassMethod.get(classEntity.clazz.getName());
                    List<DecoratorEntity> listDecoratorEntity2 = new ArrayList<>();
                    loadDecorator(classEntity, listDecoratorEntity2, obj);
                    if (map != null) {
                        for (Map.Entry<String, Method> stringMethodEntry : map.entrySet()) {
                            Iterator<DecoratorEntity> iterator = listDecoratorEntity.iterator();
                            while (iterator.hasNext()) {
                                DecoratorEntity decorator = iterator.next();
                                if (Objects.equals(decorator.sign, stringMethodEntry.getKey())) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                    map = new ConcurrentHashMap<>();
                    for (DecoratorEntity decoratorEntity : listDecoratorEntity2) {
                        listDecoratorEntity.add(decoratorEntity);
                        map.put(decoratorEntity.sign, decoratorEntity.method);
                    }
                    mapClassMethod.put(classEntity.clazz.getName(), map);
                }

            }
        }

        return listClassEntity;
    }

    Map<String, Method> mapMethodThreadFactory = new ConcurrentHashMap<>();

    /// 注意要点
    /// 1 改变方法签名的时候必须修改 num=0 停止正在运行的线程 否则线程无法停止 补救措施就是恢复签名 改为0停止
    /// 2 num=0后会清楚运行线程的状态保存  num=1改为2 新新增一个线程 线程1保存原有状态 线程2是全新线程
    /// 3 方法内部返回非null 会终止进程 并清理状态数据
    /// 4 线程数量不变 方法签名不变 修改代码可以无缝热更新 这是非常稳定的模式
    /// 5 方法内部禁止阻塞 除非你明白你在做什么
    public void startThreadFactory(Class<?> aClass, Method method, Map<String, Object> mapObject, ThreadInterval threadInterval) throws Exception {
        String key = ClassTools.getSing(method, aClass);
        ThreadEntity threadEntity = mapListThreadEntity0.get(key);
        if (threadEntity == null) {
            threadEntity = new ThreadEntity();
            threadEntity.method = method;
            threadEntity.result = new LinkedList<>();
            threadEntity.thread = new LinkedList<>();
            threadEntity.lock = new ReentrantLock();
            mapListThreadEntity0.put(key, threadEntity);
        }
        if (threadInterval.async() && threadInterval.num() > 0) {
            log.w("由于异步调度有些风险，所以目前只支持同步调度");
        }
        List<Thread> list = new ArrayList<>();
        int size1 = threadEntity.thread.size();
        threadEntity.lock.lock();
        try {
            //线程全部结束 因为代码更新
            while (!threadEntity.thread.isEmpty()) {
                Thread thread = threadEntity.thread.remove(threadEntity.thread.size() - 1);
                list.add(thread);
                log.d("等待线程结束", key, thread);
            }
        } finally {
            threadEntity.lock.unlock();
        }
        //等待线程结束,不结束的话将会卡住  需要确保不会长时间阻塞
        for (Thread thread : list) {
            if (thread != null) {
                thread.join();
            }
        }
        //结束后 这后边就不存在线程安全问题了

        //如果 调整线程数量为更小值 则清空多余的状态保存
        while (size1 > threadInterval.num()) {
            threadEntity.result.remove(threadEntity.result.size() - 1);
            size1--;
        }
        //准备结果变量 如果不够则填充够为止
        while (threadEntity.result.size() < threadInterval.num()) {
            threadEntity.result.add(new ResultImpl());
        }
        if (threadInterval.num() < 1) {
            mapListThreadEntity0.remove(key);
        } else {
            for (int i = 0; i < threadEntity.result.size(); i++) {
                if (threadEntity.result.get(i) == null) {
                    threadEntity.result.set(i, new ResultImpl());
                }
            }
        }
        //根据设定数量和已启动数量启动线程
        for (int i = threadEntity.thread.size(); i < threadInterval.num(); i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                String key2 = key;
                log.d("线程启动", key2, Thread.currentThread());
                while (true) {
                    try {
                        Tools.sleep(Math.max(threadInterval.value(), 100));
                        ThreadEntity threadEntity0 = mapListThreadEntity0.get(key2);
                        if (threadEntity0.thread.size() < finalI + 1) {
                            log.d("线程正常退出", key2);
                            break;
                        }
                        key2 = ClassTools.getSing(threadEntity0.method, aClass);
                        if (mapMethodThreadFactory.get(key2) == null) {
                            threadEntity0.result.set(finalI, null);
                            threadEntity0.thread.set(finalI, null);
                            log.d("方法签名变化 本线程已经失控 退出", key2);
                            break;
                        }
                        Object[] objects = new Object[]{threadEntity0.result.get(finalI), threadEntity0.lock};
                        int id = ClassTools.getSingInt(method, aClass);
                        GzbOneInterface gzbOneInterface = (GzbOneInterface) mapObject.get(aClass.getName());
                        if (gzbOneInterface == null) {
                            threadEntity0.result.set(finalI, null);
                            threadEntity0.thread.set(finalI, null);
                            log.d("GzbOneInterface 对象不存在", key2);
                            break;
                        }
                        Object obj = gzbOneInterface._gzb_call_x01(id, mapObject0, null, null, new HashMap<>(), gzbJson, log, objects);
                        if (obj != null) {
                            log.d("线程正常退出-释放线程状态-根据调用函数返回值决定", key);
                            //释放占用
                            threadEntity0.result.set(finalI, null);
                            threadEntity0.thread.set(finalI, null);
                            break;
                        }
                    } catch (Exception e) {
                        log.e("线程出现错误，但不会中断", key, e);
                    }
                }
            });
            threadEntity.thread.add(thread);
            thread.start();
        }
    }


}
