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

package gzb.frame.db.v2;
import gzb.frame.language.Template;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.ServiceThread;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncFactory {

    public static class Result {
        public String sql;
        public Object[] objects;
        public Runnable fail;
        public Runnable success;

        public Result(String sql, Object[] objects) {
            this.sql = sql;
            this.objects = objects;
        }

        public Result(String sql, Object[] objects, Runnable fail, Runnable success) {
            this.sql = sql;
            this.objects = objects;
            this.fail = fail;
            this.success = success;
        }
    }


    public Log log = Log.log;
    public int batchSize;
    public int batchAwait;
    public int queueSize;
    //public Map<String, ConcurrentLinkedQueue<Result>> cacheMap;
    public Map<String, LinkedBlockingQueue<Result>> cacheMap;
    public DataBase dataBase;

    public AsyncFactory(DataBase dataBase, int threadNum, int batchAwait, int queueSize) {
        this.dataBase = dataBase;
        this.batchAwait = batchAwait;
        this.queueSize = queueSize;
        if (threadNum < 1) {
            log.w("warn async thread num < 1");
        } else {
            cacheMap = new ConcurrentHashMap<>();
            startThread(threadNum);
        }
        batchSize= dataBase.dataBaseConfig.batch_size;
    }

    public void startThread(int threadNum) {
        ServiceThread.start(threadNum, "AsyncFactory", () -> {
            log.d("start async thread",Thread.currentThread().getName());
            List<Result> list = new ArrayList<>(batchSize);
            List<Object[]> list_objects = new ArrayList<>(batchSize);
            while (true) {
                try {
                    if (!execMapSql(cacheMap,list,list_objects)) {
                        Tools.sleep(batchAwait);
                    }
                } catch (Exception e) {
                    log.e("AsyncFactory", Thread.currentThread().getName(), e);
                }
            }
        });
    }
    public AtomicInteger all=new AtomicInteger(0);
    public boolean execMapSql(Map<String, LinkedBlockingQueue<Result>> cacheMap, List<Result> list, List<Object[]> list_objects)  {

        boolean runNum = false;
        for (String sql : cacheMap.keySet()) {
            list.clear();
            list_objects.clear();
            LinkedBlockingQueue<Result> queue = cacheMap.get(sql);
            if (queue.drainTo(list, batchSize)>0) {
                all.addAndGet(list.size());
                runNum = true;
                int callBackSuccess = 0;
                for (Result result : list) {
                    list_objects.add(result.objects);
                    if (result.success != null) {
                        callBackSuccess++;
                    }
                }
                try {
                    ///事物内执行
                    dataBase.execute(sql, list_objects,true);
                    if (callBackSuccess > 0) {
                        for (Result result1 : list) {
                            if (result1.success == null) {
                                continue;
                            }
                            try {
                                result1.success.run();
                            } catch (Exception e) {
                                log.e(Template.THIS_LANGUAGE[14], result1);
                            }
                        }
                    }
                }catch (Exception e) {
                    log.e("AsyncFactory",sql, e);
                    //尝试筛选
                    for (Result result1 : list) {
                        try {
                            dataBase.execute(result1.sql, result1.objects);
                            if (result1.success!=null) {
                                try {
                                    result1.success.run();
                                }catch (Exception e3){
                                    log.e(Template.THIS_LANGUAGE[14], result1.sql, result1.objects, e3);
                                }
                                result1.success=null;
                            }
                        }catch (Exception e2) {
                            if (result1.fail!=null) {
                                try {
                                    result1.fail.run();
                                }catch (Exception e3){
                                    log.e(Template.THIS_LANGUAGE[10], result1.sql, result1.objects, e3);
                                }
                                result1.fail=null;
                            }else{
                                log.e("AsyncFactory",sql, result1.objects,e,e2);
                            }
                        }
                    }
                }
            }
        }
        return runNum;
    }
    public int add(String sql, Object[] parar) {
        if (parar == null) {
            parar = new Object[0];
        }
        return add(new Result(sql, parar));
    }
    Lock lock=new ReentrantLock();
    public int add(Result result) {
        LinkedBlockingQueue<Result> queue = cacheMap.get(result.sql);
        if (queue == null) {
            lock.lock();
            try {
                queue = cacheMap.get(result.sql);
                if (queue == null) {
                    queue = new LinkedBlockingQueue<>(queueSize);
                    cacheMap.put(result.sql, queue);
                }
            } finally {
                lock.unlock();
            }
        }
        try {
            queue.put(result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
}
