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

import gzb.frame.PublicEntrance;
import gzb.frame.annotation.*;
import gzb.frame.db.EventFactory;
import gzb.frame.factory.*;
import gzb.entity.ClassEntity;
import gzb.entity.DecoratorEntity;
import gzb.entity.HttpMapping;
import gzb.entity.ThreadEntity;
import gzb.frame.netty.HTTPRequestHandlerV4;
import gzb.frame.netty.NettyServer;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestDefaultImpl;
import gzb.frame.netty.entity.Response;
import gzb.entity.RunRes;
import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.json.GzbJson;
import gzb.tools.json.ResultImpl;
import gzb.tools.log.Log;
import gzb.tools.thread.ThreadPoolV3;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

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
    public Map<String, HttpMapping[]> mapHttpMapping0 = new ConcurrentHashMap<String, HttpMapping[]>();
    public Map<String, Object> mapObject0 = new ConcurrentHashMap<>();
    List<DecoratorEntity> listDecoratorEntity = new ArrayList<>();
    public Map<String, Map<String, String>> mapHttpMappingOld0 = new ConcurrentHashMap<>();
    public static int serverState = -1;
    public String[] met = Constant.requestMethod;
    public Map<String, ThreadEntity> mapListThreadEntity0 = new ConcurrentHashMap<>();

    public GzbJson gzbJson = PublicEntrance.gzbJson;

    public void loadPublicClass() throws Exception {
        //缓存对象
        ClassTools.putObject(Cache.gzbCache.getClass(), null, mapObject0, Cache.gzbCache);
        ClassTools.putObject(Cache.gzbQueue.getClass(), null, mapObject0, Cache.gzbQueue);
        //JSON对象
        ClassTools.putObject(gzbJson.getClass(), null, mapObject0, gzbJson);
        //数据库事件对象
        ClassTools.putObject(PublicEntrance.eventFactory.getClass(), null, mapObject0, PublicEntrance.eventFactory);
        //日志对象
        ClassTools.putObject(Log.log.getClass(), null, mapObject0, Log.log);
        //生成打包命令
      /*  try {
            ClassByteTools.gen_cmd();

            FileTools.mkdir(new File(ClassByteTools.outConfigPath+"agent-extracted-predefined-classes"));
            ClassByteTools.outJni();
            ClassByteTools.outPredefined();
            ClassByteTools.outProxy();
            ClassByteTools.outSerialization();
        } catch (Exception e) {
            System.out.println("打包后这里属于正常错误 " + e.getMessage());
            ClassByteTools.outPath=null;
            ClassByteTools.outConfigPath=null;
        }
        */
        ClassTools.saveSingAll(ClassByteTools.cachePath + "mapping0.config");
    }


    public void loadJavaDir(String classDir, String pwd, String iv) throws Exception {
        File file = new File(Config.thisPath + "/className.config");
        if (file.exists()) {
            loadClassName(FileTools.readArray(file));
            return;
        }
        loadPublicClass();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    List<ClassEntity> listClassEntity = loadFiles(classDir, pwd, iv, mapClassEntity);
                    if (listClassEntity.size() > 0) {
                        load(listClassEntity);
                    }
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
        //System.gc();
    }

    public void loadServerHttp() throws Exception {
        loadPublicClass();
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
        //System.gc();
    }

    public void loadClassName(String[] classNames) throws Exception {
        loadPublicClass();
        List<ClassEntity> listClassEntity = new ArrayList<>();
        for (int i = 0; i < classNames.length; i++) {
            if (classNames[i] == null || classNames[i].length() < 1 || classNames[i].endsWith("_inner_v1")) {
                continue;
            }
            Class<?> clazz = Class.forName(classNames[i]);
            if (clazz.isInterface()) {
                continue;
            }
            ClassEntity classEntity = new ClassEntity();
            classEntity.clazz = clazz;
            classEntity.sign = "-1";
            listClassEntity.add(classEntity);
        }
        load(listClassEntity);
    }


    public void load(List<ClassEntity> listClassEntity) throws Exception {
        if (!listClassEntity.isEmpty()) {
            //有线程安全问题,不过真出问题了 请从自身找原因 总不能我在这都加个锁吧
            if (PublicEntrance.classLoadEvent != null) {
                PublicEntrance.classLoadEvent.eventStart();
                for (ClassEntity entity : listClassEntity) {
                    PublicEntrance.classLoadEvent.eventClassStart(entity.clazz.getName());
                }
            }
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
                loadController(
                        classEntity.clazz,
                        mapObject0.get(classEntity.clazz.getName()),
                        mapHttpMapping0,
                        mapHttpMappingOld0,
                        listDecoratorEntity);
            }
            /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //暂未开发完成 目前 打包成exe 可运行 监听但是丢失了 映射信息 第一选择 保留注解等信息 第二选择 生成新的类保存映射
            for (Map.Entry<String, HttpMapping[]> stringEntry : mapHttpMapping0.entrySet()) {
                for (HttpMapping httpMapping : stringEntry.getValue()) {
                    if (httpMapping == null) {
                        continue;
                    }
                    //log.i(stringEntry.getKey() + ":" + httpMapping.toString());
                }
            }

            //有线程安全问题,不过真出问题了 请从自身找原因 总不能我在这都加个锁吧
            if (PublicEntrance.classLoadEvent != null) {
                for (ClassEntity entity : listClassEntity) {
                    PublicEntrance.classLoadEvent.eventClassEnd(entity.clazz.getName());
                }
                PublicEntrance.classLoadEvent.eventEnd(mapHttpMapping0);
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
            if (classEntity != null && classEntity.sign.equals("-1")) {
                continue;
            }
            String code;
            if (pwd != null && iv != null) {
                byte[] bytes = AES_CBC_128.aesDe(FileTools.readByte(file), pwd, iv);
                code = new String(bytes, Config.encoding);
            } else {
                code = FileTools.readString(file);
            }
            String className = ClassTools.extractPublicClassName(code);

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

    public static final byte[] _fail_json = ("{\"" + Config.stateName + "\":\"" + Config.failVal + "\",\"" + Config.messageName + "\":\"server busy / 服务器 繁忙\"}").getBytes(Config.encoding);
    public static final byte[] _err_json = ("{\"" + Config.stateName + "\":\"" + Config.failVal + "\",\"" + Config.messageName + "\":\"server error / 服务器 异常\"}").getBytes(Config.encoding);
    public static final byte[] _err_404 = ("{\"" + Config.stateName + "\":\"" + Config.failVal + "\",\"" + Config.messageName + "\":\"server the resource does not exist / 服务器 资源不存在\"}").getBytes(Config.encoding);
    public static final byte[] _intercept_400 = ("{\"" + Config.stateName + "\":\"" + Config.failVal + "\",\"" + Config.messageName + "\":\"server the resource does not exist / 服务器 资源不存在\"}").getBytes(Config.encoding);

    public long send(ChannelHandlerContext ctx, byte[] msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(msg));
        response.headers().set("content-length", msg.length);
        ctx.writeAndFlush(response);
        return System.nanoTime();
        //Unpooled.wrappedBuffer(msg)
    }

    //public static final ThreadPool THREAD_POOL = new ThreadPool(Config.bizThreadNum, Config.bizAwaitNum);
    public static final ThreadPoolV3 THREAD_POOL = new ThreadPoolV3(
            Config.cpu * 1000,
            Config.bizThreadNum < 1 ? Config.cpu * 2 : Config.bizThreadNum,
            Config.bizAwaitNum < 1 ? Config.cpu * 500 : Config.bizAwaitNum,
            85.0,
            Config.bizThreadNum < 1);


    public void debug(long[] times, String title) {
        long t01 = (times[2] - times[1]);
        long t02 = (times[1] - times[0]);
        long t03 = (times[2] - times[0]);
        AtomicInteger atomicInteger = HTTPRequestHandlerV4.reqInfo.computeIfAbsent(t03 / 1000, k -> new AtomicInteger());
        atomicInteger.addAndGet(1);
        log.d(title, "总耗时", t03, "netty数据获取", t02, "框架执行", t01);
    }

    //返回 404 需要寻找静态资源
    public void start(ChannelHandlerContext ctx, FullHttpRequest req) {
        long[] times = new long[]{0, 0, 0, 0};
        times[0] = System.nanoTime();
        Request request = new RequestDefaultImpl(ctx, req);
        Response response = request.getResponse();
        String metName = request.getMethod();
        String key = request.getUri();
        times[1] = System.nanoTime();
        HttpMapping[] httpMappings = mapHttpMapping0.get(key);
        if (httpMappings == null) {
            NettyServer.HTTPStaticFileHandler.channelRead0(ctx, req);
            return;
        }
        int index = -1;
        for (int i = 0; i < met.length; i++) {
            if (metName.equals(met[i])) {
                if (httpMappings[i] == null) {
                    NettyServer.HTTPStaticFileHandler.channelRead0(ctx, req);
                    return;
                }
                index = i;
                break;
            }
        }
        if (index == -1) {
            send(ctx, _err_404);
            return;
        }
        HttpMapping httpMapping = httpMappings[index];
        //同步会在事件循环线程执行
        if (httpMapping.async) {
            if (!THREAD_POOL.execute(() -> {
                times[2] = exec(httpMapping, ctx, req, request, response);
                debug(times, "异步");
            })) {
                times[2] = send(ctx, _fail_json);
                debug(times, "繁忙");
            }
        } else {
            times[2] = exec(httpMapping, ctx, req, request, response);
            debug(times, "同步");
        }
    }

    //后续可以考虑 加入线程空间
    public String toKey(String[] keys, Map<String, List<Object>> parar) {
        StringBuilder token = new StringBuilder();
        List<Object> list = null;
        for (String s : keys) {
            list = parar.get(s);
            if (list == null || list.size() == 0) {
                token.append(s).append("_");
            } else {
                for (Object object : list) {
                    token.append(s).append("_").append(object).append("_");
                }
            }
        }
        return token.toString();
    }

    public long exec(HttpMapping httpMapping, ChannelHandlerContext ctx, FullHttpRequest req, Request request, Response response) {
        try {
            long time;
            if (httpMapping.header.size() > 0) {
                response.setHeaders(new HashMap<>(httpMapping.header));
            }
            if (httpMapping.isCrossDomainOrigin) {
                String host = request.getOrigin();
                if (host == null) {
                    host = request.getDomain();
                }
                response.setHeader("access-control-allow-origin", host);
            }
            if (httpMapping.semaphore != null && !httpMapping.semaphore.tryAcquire()) {
                send(ctx, _fail_json);
                return System.nanoTime();
            }
            Map<String, List<Object>> parar = request.getParameter();
            boolean openCache = httpMapping.cacheSecond != null && httpMapping.cacheKey != null;
            String key = null;
            if (openCache) {
                key = toKey(httpMapping.cacheKey, parar);
                byte[] bytes = Cache.gzbCache.getByte(key);
                if (bytes != null) {
                    time = System.nanoTime();
                    response.sendAndFlush(bytes);
                    return time;
                }
            }
            Object[] objects = PublicEntrance.context.get();
            RunRes runRes = null;
            if (objects == null) {
                runRes = new RunRes();
                objects = new Object[]{
                        runRes,
                        gzbJson,
                        log,
                        request,
                        response,
                        parar  // 请求参数
                };
                PublicEntrance.context.set(objects);
            } else {
                objects[3] = request;
                objects[4] = response;
                objects[5] = parar;
                runRes = (RunRes) objects[0];
                runRes.setData(null);
                runRes.setState(200);
            }
            for (DecoratorEntity decoratorEntity : httpMapping.start) {
                log.t("调用 前置装饰器", decoratorEntity.sign);
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        time = System.nanoTime();
                        response.setContentType("application/json;charset=UTF-8");
                        response.sendAndFlush(runRes1.getData());
                        return time;
                    } else {
                        objects[0] = runRes1;
                        if (runRes1.getData() != null) {
                            Object[] newArray = Arrays.copyOf(objects, objects.length + 1);
                            newArray[newArray.length - 1] = runRes1.getData();
                            objects = newArray;
                            runRes1.setData(null);
                        }
                    }
                }
            }
            Object obj02 = httpMapping.httpMappingFun._gzb_call_x01(httpMapping.id, mapObject0, request, response, parar, gzbJson, log, objects);
            runRes.setData(obj02);
            for (DecoratorEntity decoratorEntity : httpMapping.end) {
                log.t("调用 后置装饰器", decoratorEntity.sign);
                RunRes runRes1 = (RunRes) decoratorEntity.call
                        ._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, gzbJson, log, objects);
                if (runRes1 != null) {
                    if (runRes1.getState() != 200) {
                        time = System.nanoTime();
                        response.setContentType("application/json;charset=UTF-8");
                        response.sendAndFlush(runRes1.getData());
                        return time;
                    } else {
                        objects[0] = runRes1;
                    }
                }
            }
            time = System.nanoTime();
            response.sendAndFlush(runRes.getData());
            if (openCache) {
                if (runRes.getData() instanceof String) {
                    Cache.gzbCache.setByte(key, ((String) runRes.getData()).getBytes(Config.encoding), httpMapping.cacheSecond);
                } else if (runRes.getData() instanceof byte[]) {
                    Cache.gzbCache.setByte(key, (byte[]) runRes.getData(), httpMapping.cacheSecond);
                } else {
                    Cache.gzbCache.setByte(key, (Tools.toJson(runRes.getData())).getBytes(Config.encoding), httpMapping.cacheSecond);
                }
            }
            return time;
        } catch (Exception e) {
            long time = System.nanoTime();
            send(ctx, _err_json);
            log.e("框架层错误日志",
                    request.getUri(),
                    request.getMethod(),
                    request.getParameter(),
                    e);
            return time;
        } finally {
            //限流如果开启 解除占用
            if (httpMapping.semaphore != null) {
                httpMapping.semaphore.release();
            }
        }
    }

    public static class EntityAnnotation {
        public GetMapping getMapping = null;
        public PostMapping postMapping = null;
        public PutMapping putMapping = null;
        public DeleteMapping deleteMapping = null;
        public RequestMapping requestMappingMethod = null;
        public Transaction transaction = null;
        public DecoratorOpen decoratorOpen = null;
        public CrossDomain metCrossDomain = null;
        public Header headerMethod = null;
        public Limitation limitation = null;
        public EventLoop eventLoop = null;
        public CacheRequest cacheRequest = null;

        public Controller controller = null;
        public Header header = null;
        public RequestMapping requestMapping = null;
        public CrossDomain crossDomain = null;
    }

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
                EntityAnnotation entityAnnotation = new EntityAnnotation();
                entityAnnotation.getMapping = method.getAnnotation(GetMapping.class);
                entityAnnotation.postMapping = method.getAnnotation(PostMapping.class);
                entityAnnotation.putMapping = method.getAnnotation(PutMapping.class);
                entityAnnotation.deleteMapping = method.getAnnotation(DeleteMapping.class);
                entityAnnotation.requestMappingMethod = method.getAnnotation(RequestMapping.class);
                entityAnnotation.transaction = method.getAnnotation(Transaction.class);
                entityAnnotation.decoratorOpen = method.getAnnotation(DecoratorOpen.class);
                entityAnnotation.metCrossDomain = method.getAnnotation(CrossDomain.class);
                entityAnnotation.headerMethod = method.getAnnotation(Header.class);
                entityAnnotation.limitation = method.getAnnotation(Limitation.class);
                entityAnnotation.eventLoop = method.getAnnotation(EventLoop.class);
                entityAnnotation.cacheRequest = method.getAnnotation(CacheRequest.class);


                entityAnnotation.crossDomain = crossDomain;
                entityAnnotation.requestMapping = requestMapping;
                entityAnnotation.controller = controller;
                entityAnnotation.header = header;


                if (entityAnnotation.getMapping != null && entityAnnotation.getMapping.value().length > 0) {
                    putMapping(id, clazz, 0, entityAnnotation.getMapping.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
                } else if (entityAnnotation.postMapping != null && entityAnnotation.postMapping.value().length > 0) {
                    putMapping(id, clazz, 1, entityAnnotation.postMapping.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
                } else if (entityAnnotation.putMapping != null && entityAnnotation.putMapping.value().length > 0) {
                    putMapping(id, clazz, 2, entityAnnotation.putMapping.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
                } else if (entityAnnotation.deleteMapping != null && entityAnnotation.deleteMapping.value().length > 0) {
                    putMapping(id, clazz, 3, entityAnnotation.deleteMapping.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
                } else if (entityAnnotation.requestMappingMethod != null && entityAnnotation.requestMappingMethod.value().length > 0) {

                    putMapping(id, clazz, 0, entityAnnotation.requestMappingMethod.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
                    putMapping(id, clazz, 1, entityAnnotation.requestMappingMethod.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
                    putMapping(id, clazz, 2, entityAnnotation.requestMappingMethod.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
                    putMapping(id, clazz, 3, entityAnnotation.requestMappingMethod.value(), method, object, listClassPath
                            , mapHttpMapping, listDecoratorEntity, map1, map2, entityAnnotation);
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
                        log.t("isDecorator start", url, string, startOrEnd, suc, listDecoratorEntity.size());
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

                        log.t("isDecorator end", url, string, startOrEnd, suc, listDecoratorEntity.size());
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
        log.t("listDecoratorEntity", url, met, listDecoratorEntity.size());
    }

    public void putMapping(int id, Class<?> aClass, int index, String[] metPath, Method method, Object object,
                           List<String> listClassPath, Map<String, HttpMapping[]> mapHttpMapping
            , List<DecoratorEntity> listDecoratorEntity, Map<String, String> map1, Map<String, String> map2,
                           EntityAnnotation entityAnnotation) {

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
                httpMapping2.transaction = entityAnnotation.transaction != null ? (entityAnnotation.transaction.simulate() ? 2 : 1) : null;
                httpMapping2.httpMappingFun = (GzbOneInterface) object;
                httpMapping2.met = index;
                httpMapping2.start = new ArrayList<>();
                httpMapping2.end = new ArrayList<>();
                if (entityAnnotation.cacheRequest != null) {
                    httpMapping2.cacheKey = entityAnnotation.cacheRequest.value();
                    httpMapping2.cacheSecond = entityAnnotation.cacheRequest.second();
                }
                String path = ClassTools.webPathFormat(str2 + "/" + string);
                httpMapping2.path = path;
                if (entityAnnotation.decoratorOpen != null && entityAnnotation.decoratorOpen.value()) {
                    DecoratorAdd(path, Constant.requestMethod[httpMapping2.met], 1, listDecoratorEntity, httpMapping2.start);
                    DecoratorAdd(path, Constant.requestMethod[httpMapping2.met], 2, listDecoratorEntity, httpMapping2.end);
                }
                if (entityAnnotation.limitation != null && entityAnnotation.limitation.value() > 0) {
                    httpMapping2.semaphore = new Semaphore(entityAnnotation.limitation.value());
                } else {
                    httpMapping2.semaphore = null;
                }

                HttpMapping[] httpMappings = mapHttpMapping.get(path);
                if (httpMappings == null) {
                    httpMappings = new HttpMapping[4];
                }
                if (httpMappings[index] != null) {
                    if (httpMappings[index].httpMappingFun != null
                            && !httpMappings[index].httpMappingFun.getClass().getName().equals(httpMapping2.httpMappingFun.getClass().getName())) {
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
                if (entityAnnotation.header != null) {
                    for (HeaderItem headerItem : entityAnnotation.header.item()) {
                        if (headerItem.key().isEmpty() || headerItem.val().isEmpty()) {
                            continue;
                        }
                        httpMapping2.header.put(headerItem.key(), headerItem.val());
                    }
                }
                if (entityAnnotation.headerMethod != null) {
                    for (HeaderItem headerItem : entityAnnotation.headerMethod.item()) {
                        if (headerItem.key().isEmpty() || headerItem.val().isEmpty()) {
                            continue;
                        }
                        httpMapping2.header.put(headerItem.key(), headerItem.val());
                    }
                }

                if (entityAnnotation.crossDomain != null) {
                    ClassTools.generateCORSHeaders(entityAnnotation.crossDomain, httpMapping2.header);
                }
                if (entityAnnotation.metCrossDomain != null) {
                    ClassTools.generateCORSHeaders(entityAnnotation.metCrossDomain, httpMapping2.header);
                }
                if (entityAnnotation.eventLoop != null) {
                    httpMapping2.async = entityAnnotation.eventLoop.async();
                }
                if (httpMapping2.header.get("access-control-allow-origin") != null) {
                    if (httpMapping2.header.get("access-control-allow-origin").equals("origin")) {
                        httpMapping2.isCrossDomainOrigin = true;
                    }
                }

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
                String name = null;
                if (classEntity.code != null) {
                    String code = ClassTools.gen_call_code_v4_all_code(classEntity.clazz, classEntity.code);
                    name = ClassTools.extractPublicClassName(code);
                    sourcesMap.put(name, code);
                }
                if (classEntity.filePath != null && name != null) {
                    fileMap.put(name, new File(classEntity.filePath));
                }
                pwd = classEntity.pwd;
                iv = classEntity.iv;
            }
        }
        List<ClassEntity> list = null;
        if (sourcesMap.size() > 0) {
            list = loadFiles(sourcesMap, fileMap, pwd, iv, mapClassEntity);
            for (ClassEntity classEntity : listClassEntity) {
                if (classEntity.code == null) {
                    list.add(classEntity);
                }
            }
        } else {
            list = listClassEntity;
        }

        for (ClassEntity classEntity : list) {
            if (classEntity.clazz.isInterface()) {
                continue;
            }
            if (classEntity.clazz.getAnnotation(aClass)==null) {
                continue;
            }
            if (aClass == Service.class) {
                Object obj = ClassTools.putObject(classEntity.clazz, null, mapObject);
            }
            if (aClass == Controller.class) {
                Object obj = ClassTools.putObject(classEntity.clazz, null, mapObject);
                //提前生成适配的实体类 目的是触发增强类并编译落盘为编译成二进制做准备  loadObject内置缓存 不会有明显性能问题 且非核心链路

                Map<String, List<Object>> map001 = new HashMap<>();
                for (Method method : classEntity.clazz.getMethods()) {
                    for (Class<?> parameterType : method.getParameterTypes()) {
                        if (parameterType.isPrimitive() || parameterType.getAnnotation(EntityAttribute.class) == null) {
                            continue;
                        }
                        ClassTools.loadObject(parameterType, map001);//触发增强类生成
                    }
                }
                for (Field combinedField : ClassTools.getCombinedFields(classEntity.clazz)) {
                    if (combinedField.getType().isPrimitive() || combinedField.getType().getAnnotation(EntityAttribute.class) == null) {
                        continue;
                    }
                    ClassTools.loadObject(combinedField.getType(), map001);//触发增强类生成
                }
            }
            if (aClass == DataBaseEventFactory.class) {
                Object obj = ClassTools.putObject(classEntity.clazz, null, mapObject);
                EventFactory eventFactory = (EventFactory) mapObject.get(EventFactory.class.getName());
                eventFactory.register(classEntity.clazz, classEntity.code);
            }
            if (aClass == ThreadFactory.class) {
                Object obj = ClassTools.putObject(classEntity.clazz, null, mapObject);
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
                Object obj = ClassTools.putObject(classEntity.clazz, null, mapObject);
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
        return listClassEntity;
    }

    Map<String, Method> mapMethodThreadFactory = new ConcurrentHashMap<>();

    /// 注意要点
    /// 1 改变方法签名的时候必须修改 num=0 停止正在运行的线程 否则线程无法停止 补救措施就是恢复签名 改为0停止 (已克服 无需再修改为0)
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
