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

import gzb.frame.ThreadLocalData;
import gzb.frame.annotation.*;
import gzb.frame.db.ForeignKeyFactory;
import gzb.frame.factory.*;
import gzb.frame.factory.v4.entity.ClassEntity;
import gzb.frame.factory.v4.entity.DecoratorEntity;
import gzb.frame.factory.v4.entity.HttpMapping;
import gzb.frame.factory.v4.entity.ThreadEntity;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.frame.server.http.entity.RunRes;
import gzb.tools.*;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbCache;
import gzb.tools.json.GzbJson;
import gzb.tools.json.GzbJsonImpl;
import gzb.tools.json.ResultImpl;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class FactoryImpl implements Factory {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(FactoryImpl.class));
        FactoryImpl factory = new FactoryImpl();
    }

    public Map<String, Map<String, Method>> mapClassMethod = new ConcurrentHashMap<>();
    public static Log log = Config.log;
    public Map<String, ClassEntity> mapClassEntity = new ConcurrentHashMap<>();
    public Map<String, HttpMapping[]> mapHttpMapping0 = new ConcurrentHashMap<>();
    public Map<String, Object> mapObject0 = new ConcurrentHashMap<>();
    List<DecoratorEntity> listDecoratorEntity = new ArrayList<>();
    public Map<String, Map<String, String>> mapHttpMappingOld0 = new ConcurrentHashMap<>();
    public static int serverState = -1;
    public String[] met = Constant.requestMethod;
    public Map<String, ThreadEntity> mapListThreadEntity0 = new ConcurrentHashMap<>();


    public void loadJavaDir(String classDir, String pwd, String iv) {
        //加入内置对象
        mapObject0.put(Log.class.getName(), Config.log);
        mapObject0.put(LogImpl.class.getName(), Config.log);


        //JSON对象
        GzbJson gzbJson = new GzbJsonImpl();
        mapObject0.put(GzbJson.class.getName(), gzbJson);
        mapObject0.put(GzbJsonImpl.class.getName(), gzbJson);
        mapObject0.put(ForeignKeyFactory.class.getName(), new ForeignKeyFactory(mapObject0,log));


        //缓存对象
        mapObject0.put(Cache.gzbCache.getClass().getName(), Cache.gzbCache);
        mapObject0.put(GzbCache.class.getName(), Cache.gzbCache);


        startFileScanning(classDir, pwd, iv);
        while (serverState == -1) {
            Tools.sleep(1);
        }
    }

    public void startFileScanning(String classDir, String pwd, String iv) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    List<File> listFile = new ArrayList<>();
                    String[] arr1 = classDir.split(",");
                    for (int i = 0; i < arr1.length; i++) {
                        arr1[i] = Tools.pathFormat(arr1[i].trim());
                        if (arr1[i].isEmpty()) {
                            continue;
                        }
                        if (new File(arr1[i]).isDirectory()) {
                            List<File> list = Tools.fileSub(arr1[i], 2, ".java");
                            listFile.addAll(list);
                        } else {
                            log.w("代码目录不存在或错误", arr1[i]);
                        }
                    }
                    List<ClassEntity> listClassEntity = new ArrayList<>();
                    for (File file : listFile) {
                        ClassEntity classEntity = loadFile(file, pwd, iv, mapClassEntity);
                        if (classEntity != null) {
                            listClassEntity.add(classEntity);
                        }
                    }
                    if (!listClassEntity.isEmpty()) {
                        for (ClassEntity classEntity : listClassEntity) {
                            loadService(classEntity, mapObject0);
                        }
                        for (ClassEntity classEntity : listClassEntity) {
                            loadControllerObject(classEntity, mapObject0);
                        }
                        for (ClassEntity classEntity : listClassEntity) {
                            loadDataBaseEventFactory(classEntity, mapObject0);
                        }
                        for (ClassEntity classEntity : listClassEntity) {
                            Map<String, Method> map = mapClassMethod.get(classEntity.clazz.getName());
                            List<DecoratorEntity> listDecoratorEntity2= new ArrayList<>();
                            loadDecorator(classEntity, listDecoratorEntity2);
                            if (map!=null) {
                                for (Map.Entry<String, Method> stringMethodEntry : map.entrySet()) {
                                    Iterator<DecoratorEntity> iterator = listDecoratorEntity.iterator();
                                    while (iterator.hasNext()) {
                                        DecoratorEntity decorator=iterator.next();
                                        if (Objects.equals(decorator.sign, stringMethodEntry.getKey())) {
                                            iterator.remove();
                                        }
                                    }
                                }
                            }
                            map=new ConcurrentHashMap<>();
                            for (DecoratorEntity decoratorEntity : listDecoratorEntity2) {
                                listDecoratorEntity.add(decoratorEntity);
                                map.put(decoratorEntity.sign,decoratorEntity.method);
                            }
                            mapClassMethod.put(classEntity.clazz.getName(),map);
                        }
                        //log.d("listDecoratorEntity 1 ",listDecoratorEntity.size(),listDecoratorEntity);
                        for (Map.Entry<String, Object> stringObjectEntry : mapObject0.entrySet()) {
                            ClassTools.classInject(stringObjectEntry.getValue(), null, mapObject0);
                        }
                        for (ClassEntity classEntity : listClassEntity) {
                            loadThreadInterval(classEntity, mapObject0);
                        }
                        for (Map.Entry<String, ClassEntity> stringClassEntityEntry : mapClassEntity.entrySet()) {
                            ClassEntity classEntity = stringClassEntityEntry.getValue();
                            loadController(classEntity.clazz, mapObject0.get(classEntity.clazz.getName()), mapHttpMapping0, mapHttpMappingOld0, listDecoratorEntity);
                        }
                    }

                } catch (Exception e) {
                    log.e(e);
                }
                serverState = 1;
                Tools.sleep(1000 * 2);
            }


        });
        thread.start();
    }

    public List<ClassEntity> loadFolder(String folder, String pwd, String iv, Map<String, ClassEntity> mapClassEntity) throws Exception {
        List<ClassEntity> listClassEntity = new ArrayList<>();
        File file = new File(folder);
        if (!file.exists() || !file.isDirectory()) {
            log.e("编译 代码目录不存在", file.getPath());
            return listClassEntity;
        }
        List<File> list = Tools.fileSub(folder, 2, ".java");
        for (File file1 : list) {
            ClassEntity classEntity = loadFile(file1, pwd, iv, mapClassEntity);
            if (classEntity != null) {
                listClassEntity.add(classEntity);
            }
        }
        return listClassEntity;
    }

    public ClassEntity loadFile(File file1, String pwd, String iv, Map<String, ClassEntity> mapClassEntity) throws Exception {
        Long time = file1.lastModified();
        String path = file1.getPath().replaceAll("\\\\", "/");
        ClassEntity classEntity = mapClassEntity.get(path);
        if (classEntity != null && classEntity.sign.equals(time + "")) {
            return null;
        } else {
            String code;
            if (pwd != null && iv != null) {
                byte[] bytes = AES_CBC_128.aesDe(FileTools.readByte(file1), pwd, iv);
                code = new String(bytes, Config.encoding);
            } else {
                code = FileTools.readString(file1);
            }
            Class<?> aClass = ClassLoad.compileJavaCode(code);
            if (aClass == null) {
                return null;
            } else {
                classEntity = new ClassEntity();
                classEntity.sign = time + "";
                classEntity.code = code;
                classEntity.clazz = aClass;
                classEntity.iv = iv;
                classEntity.pwd = pwd;
                classEntity.filePath = path;
                mapClassEntity.put(classEntity.filePath, classEntity);
                return classEntity;
            }
        }
    }

    public Map<String, Object> getMappingMap() {
        // 先转换为原生类型，编译器会警告
        Map rawMap = mapHttpMapping0;
        // 再将原生类型转换为目标类型，编译器无法阻止
        return (Map<String, Object>) rawMap;
    }

    public RunRes request(Request request, Response response) {
        /// 函数执行 开始
        /// 初始化 开始
        RunRes runRes = new RunRes();
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
                if (metName.toUpperCase().equals(met[i])) {
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
            /// 初始化 结束
            /// 预设协议头 预设  后续可以覆盖 开始
            if (httpMappings[index].header != null && !httpMappings[index].header.isEmpty()) {
                response.setHeaders(new HashMap<>(httpMappings[index].header));
            }
            if (httpMappings[index].isCrossDomainOrigin) {
                String host = request.getOrigin();
                if (host == null) {
                    host = request.getDomain();
                }
                response.setHeader("access-control-allow-origin", host);
            }
            /// 预设协议头 预设  后续可以覆盖 结束
            /// 限流器 开始
            if (httpMappings[index].semaphore !=null) {
                boolean state = httpMappings[index].semaphore.tryAcquire();
                if (!state) {
                    return runRes.setState(200).setData("{\"code\":2,\"message\":\"请求量超过限流阈值，请稍后重试\"}");
                }
            }
            /// 限流器 结束
            /// 请求参数获取 开始
            Map<String, List<Object>> parar = request.getParameter();
            /// 请求参数获取 结束
            /// 内置对象创建 开始
            Object[] objects = new Object[]{runRes};//, request.getSession()
            ThreadLocalData.this_request.set(request);
            ThreadLocalData.this_response.set(response);
            ThreadLocalData.this_requestMap.set(parar);
            //log.d("parar",parar);
            /// 内置对象创建 结束
            /// 装饰器(调用前) 开始
            for (DecoratorEntity decoratorEntity : httpMappings[index].start) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, objects, decoratorEntity.isOpenTransaction);
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

            /// 装饰器(调用前) 结束

            /// 调用映射端点函数 开始
            Object obj02 = httpMappings[index].httpMappingFun._gzb_call_x01(httpMappings[index].id, mapObject0, request, response, parar, objects, httpMappings[index].isOpenTransaction);
            runRes.setData(obj02);
            /// 调用映射端点函数 结束
            /// 装饰器(调用后) 开始
            for (DecoratorEntity decoratorEntity : httpMappings[index].end) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, objects, decoratorEntity.isOpenTransaction);
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
            /// 装饰器(调用后) 结束
        } catch (Exception e) {
            log.e("框架层错误日志",
                    key,
                    request.getMethod(),
                    request.getParameter(),
                    e);
            return runRes.setState(500).setData("{\"code\":3,\"message\":\"访问出现错误\"}");
        } finally {
            //限流如果开启 解除占用
            if (index>-1 && httpMappings[index]!=null && httpMappings[index].semaphore != null) {
                httpMappings[index].semaphore.release();
            }
            ThreadLocalData.this_request.remove();
            ThreadLocalData.this_response.remove();
            ThreadLocalData.this_requestMap.remove();
            ThreadLocalData.depth.remove();

            /// 函数执行 结束
            /// 输出调试信息
        }

        return runRes;
    }

    public RunRes request0(Request request, Response response) {
        long[] times = new long[15];
        /// 函数执行 开始
        times[0] = System.nanoTime();
        /// 初始化 开始
        RunRes runRes = new RunRes();
        runRes.setState(200);
        String key = ClassTools.webPathFormat(request.getUri());
        String metName = request.getMethod();
        HttpMapping[] httpMappings = mapHttpMapping0.get(key);

        if (httpMappings == null) {
            return runRes.setState(404);
        }
        Semaphore semaphore = null;
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
            /// 初始化 结束
            times[1] = System.nanoTime();
            /// 预设协议头 预设  后续可以覆盖 开始
            if (httpMappings[index].header != null && !httpMappings[index].header.isEmpty()) {
                response.setHeaders(new HashMap<>(httpMappings[index].header));
            }
            if (httpMappings[index].isCrossDomainOrigin) {
                String host = request.getOrigin();
                if (host == null) {
                    host = request.getDomain();
                }
                response.setHeader("access-control-allow-origin", host);
            }
            /// 预设协议头 预设  后续可以覆盖 结束
            times[2] = System.nanoTime();
            /// 限流器 开始
            if (httpMappings[index].semaphore !=null) {
                boolean state = httpMappings[index].semaphore.tryAcquire();
                if (!state) {
                    return runRes.setState(200).setData("{\"code\":2,\"message\":\"请求量超过限流阈值，请稍后重试\"}");
                }
            }
            /// 限流器 结束
            times[3] = System.nanoTime();


            /// 请求参数获取 开始
            Map<String, List<Object>> parar = request.getParameter();
            /// 请求参数获取 结束
            times[4] = System.nanoTime();

            /// 内置对象创建 开始
            Object[] objects = new Object[]{runRes};//, request.getSession()
            ThreadLocalData.this_request.set(request);
            ThreadLocalData.this_response.set(response);
            ThreadLocalData.this_requestMap.set(parar);

            /// 内置对象创建 结束
            times[5] = System.nanoTime();
            /// 装饰器(调用前) 开始
            for (DecoratorEntity decoratorEntity : httpMappings[index].start) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, objects, decoratorEntity.isOpenTransaction);
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
            /// 装饰器(调用前) 结束
            times[6] = System.nanoTime();

            //调用映射端点
            /// 调用映射端点函数 开始
            Object obj02 = httpMappings[index].httpMappingFun._gzb_call_x01(httpMappings[index].id, mapObject0, request, response, parar, objects, httpMappings[index].isOpenTransaction);
            runRes.setData(obj02);
            /// 调用映射端点函数 结束
            times[7] = System.nanoTime();
            /// 装饰器(调用后) 开始
            for (DecoratorEntity decoratorEntity : httpMappings[index].end) {
                RunRes runRes1 = (RunRes) decoratorEntity.call._gzb_call_x01(decoratorEntity.id, mapObject0, request, response, parar, objects, decoratorEntity.isOpenTransaction);
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
            /// 装饰器(调用后) 结束
            times[8] = System.nanoTime();
        } catch (Exception e) {
            log.e("框架层错误日志",
                    key,
                    request.getMethod(),
                    request.getParameter(),
                    e);
            return runRes.setState(500).setData("{\"code\":3,\"message\":\"访问出现错误\"}");
        } finally {
            //限流如果开启 解除占用
            if (index>-1 && httpMappings[index]!=null && httpMappings[index].semaphore != null) {
                httpMappings[index].semaphore.release();
            }
            ThreadLocalData.this_request.remove();
            ThreadLocalData.this_response.remove();
            ThreadLocalData.this_requestMap.remove();
            ThreadLocalData.depth.remove();
            /// 函数执行 结束
            times[9] = System.nanoTime();
            Long res = (times[9] - times[0]) / 1000;
            /// 后台统计 汇总
            AtomicInteger atomicInteger = reqInfo.computeIfAbsent(res, k -> new AtomicInteger());
            atomicInteger.addAndGet(1);
            /// 输出调试信息
            log.t(
                    "初始化", times[1] - times[0], "纳秒",
                    "预设协议头", times[2] - times[1], "纳秒",
                    "限流器", times[3] - times[2], "纳秒",
                    "请求参数获取", times[4] - times[3], "纳秒",
                    "内置对象创建", times[5] - times[4], "纳秒",
                    "装饰器(调用前)(包含事物处理)", times[6] - times[5], "纳秒",
                    "调用映射端点函数(包含事物处理)", times[7] - times[6], "纳秒",
                    "装饰器(调用后)(包含事物处理)", times[8] - times[7], "纳秒",
                    "全流程执行耗时", res, "微秒"
            );
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
        log.t("listDecoratorEntity", url, met, listDecoratorEntity.size(), listDecoratorEntity);
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
                httpMapping2.isOpenTransaction = transaction != null && transaction.value();
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
                    httpMapping2.semaphore=new Semaphore(limitation.value());
                } else {
                    httpMapping2.semaphore=null;
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
                httpMapping2.header.put("content-type", ContentType.json);
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
    public DecoratorEntity loadDecorator(ClassEntity classEntity, List<DecoratorEntity> listDecoratorEntity) throws Exception {
        Decorator decorator = classEntity.clazz.getAnnotation(Decorator.class);
        DecoratorEntity decoratorEntity = null;
        if (decorator != null) {
            decoratorEntity = new DecoratorEntity();
            Class<?> aClass = ClassTools.gen_call_code_v4_class(classEntity.clazz, classEntity.code);
            if (aClass == null) {
                return decoratorEntity;
            }
            try {
                decoratorEntity.call = (GzbOneInterface) aClass.getDeclaredConstructor().newInstance();

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
    public Object loadService(ClassEntity classEntity, Map<String, Object> mapObject) throws Exception {
        Service service = classEntity.clazz.getAnnotation(Service.class);
        Object object = null;
        if (service != null) {
            //classEntity.clazz = gen_code(classEntity.clazz, classEntity.code);
            object = ClassTools.putObject(classEntity.clazz, service.value(), mapObject);
        }
        return object;
    }
    //返回对象 并且加入缓存
    public Object loadDataBaseEventFactory(ClassEntity classEntity, Map<String, Object> mapObject) throws Exception {
        DataBaseEventFactory dataBaseEventFactory = classEntity.clazz.getAnnotation(DataBaseEventFactory.class);
        Object object = null;
        if (dataBaseEventFactory != null) {
             classEntity.clazz = ClassTools.gen_call_code_v4_class(classEntity.clazz, classEntity.code);
            ForeignKeyFactory foreignKeyFactory = (ForeignKeyFactory)mapObject.get(ForeignKeyFactory.class.getName());
            foreignKeyFactory.register(classEntity.clazz,classEntity.code);
        }
        return object;
    }

    public Object loadControllerObject(ClassEntity classEntity, Map<String, Object> mapObject) throws Exception {
        Controller controller = classEntity.clazz.getAnnotation(Controller.class);
        Object object = null;
        if (controller != null) {
            classEntity.clazz = ClassTools.gen_call_code_v4_class(classEntity.clazz, classEntity.code);
            object = ClassTools.putObject(classEntity.clazz, controller.value(), mapObject);

        }
        return object;
    }

    //返回对象 并且加入缓存
    public Object loadThreadInterval(ClassEntity classEntity, Map<String, Object> mapObject) throws Exception {
        ThreadFactory threadInterval = classEntity.clazz.getAnnotation(ThreadFactory.class);
        Object object = null;
        if (threadInterval != null) {
            classEntity.clazz = ClassTools.gen_call_code_v4_class(classEntity.clazz, classEntity.code);
            object = ClassTools.putObject(classEntity.clazz, null, mapObject);
            Method[] methods = classEntity.clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                startThreadFactory(i, classEntity.clazz, methods[i], (GzbOneInterface) object, mapObject);
            }
        }
        return object;
    }

    /// 注意要点
    /// 1 改变方法签名的时候必须修改 num=0 停止正在运行的线程 否则线程无法停止 补救措施就是恢复签名 改为0停止
    /// 2 num=0后会清楚运行线程的状态保存  num=1改为2 新新增一个线程 线程1保存原有状态 线程2是全新线程
    /// 3 方法内部返回非null 会终止进程 并清理状态数据
    /// 4 线程数量不变 方法签名不变 修改代码可以无缝热更新 这是非常稳定的模式
    /// 5 方法内部禁止阻塞 除非你明白你在做什么

    public void startThreadFactory(int id, Class<?> aClass, Method method, GzbOneInterface gzbOneInterface
            , Map<String, Object> mapObject) throws Exception {
        ThreadInterval threadInterval = method.getDeclaredAnnotation(ThreadInterval.class);
        Transaction transaction = method.getDeclaredAnnotation(Transaction.class);
        if (threadInterval == null) {
            return;
        }
        String key = ClassTools.getSing(method, aClass);
        ThreadEntity threadEntity = mapListThreadEntity0.get(key);
        if (threadEntity == null) {
            threadEntity = new ThreadEntity();
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
                log.d("线程启动", key, Thread.currentThread());
                while (true) {
                    try {
                        Tools.sleep(Math.max(threadInterval.value(), 100));
                        ThreadEntity threadEntity0 = mapListThreadEntity0.get(key);
                        if (threadEntity0.thread.size() < finalI + 1) {
                            log.d(key, "线程正常退出");
                            break;
                        }

                        Object[] objects = new Object[]{threadEntity0.result.get(finalI), threadEntity0.lock};
                        Object obj = gzbOneInterface._gzb_call_x01(id, mapObject0, null, null, new HashMap<>(), objects, transaction != null && transaction.value());
                        if (obj != null) {
                            log.d("线程正常退出-释放线程状态-根据调用函数返回值决定", key);
                            //释放占用
                            threadEntity0.result.set(finalI, null);
                            threadEntity0.thread.set(finalI, null);
                            break;
                        }
                    } catch (Exception e) {
                        log.e("线程出现错误，但不会中断", key, Thread.currentThread(), e);
                    }
                }
            });
            threadEntity.thread.add(thread);
            thread.start();
        }
    }


}
