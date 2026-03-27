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

package gzb.frame.db;

import gzb.frame.language.Template;
import gzb.tools.Config;
import gzb.tools.Queue;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.ServiceThread;
import gzb.tools.thread.ThreadPool;
import gzb.tools.thread.ThreadPoolV3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncFactory{

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


    public ThreadPoolV3 threadPool = null;
    public Log log = Log.log;
    public int batchSize;
    public int batchAwait;
    public int queueSize;
    public Lock lock;
    public Map<String, ConcurrentLinkedQueue<Result>> cacheMap;
    public DataBase dataBase;

    public AsyncFactory(DataBase dataBase, int threadNum, int batchSize, int batchAwait, int queueSize) {
        this.dataBase = dataBase;
        this.batchSize = batchSize;
        this.batchAwait = batchAwait;
        this.queueSize = queueSize;
        if (threadNum < 1) {
            log.w("警告，后台异步线程数小于1，所有异步操作将无法执行,如需要请至少设置为1，请检查配置文件中的 db.mysql.数据库名.async.thread.num=0");
        } else {
            lock = new ReentrantLock();
            cacheMap = new ConcurrentHashMap<>();
            startThread(threadNum);
        }

    }

    public void startThread(int threadNum) {
        ServiceThread.start(threadNum, "db-async-factory", () -> {
            while (true) {
                try {
                    if (!execMapSql(cacheMap)) {
                        Tools.sleep(batchAwait);
                    }
                } catch (Exception e) {
                    log.e("AsyncFactory", Thread.currentThread().getName(), e);
                }
            }
        });
        //创建等同于 后台异步线程数的线程池  允许大量积压 因为可能同时批量失败
        threadPool = new ThreadPoolV3(queueSize,threadNum, Config.cpu,85.0,true);
    }

    public boolean execMapSql(Map<String, ConcurrentLinkedQueue<Result>> cacheMap) throws SQLException {
        boolean runNum = false;
        for (String sql : cacheMap.keySet()) {
            ConcurrentLinkedQueue<Result> queue = null;
            lock.lock();
            try {
                queue = cacheMap.get(sql);
                if (queue != null) {
                    cacheMap.put(sql, new ConcurrentLinkedQueue<Result>());
                }
            } finally {
                lock.unlock();
            }

            if (queue != null && !queue.isEmpty()) {
                runNum = true;
                Connection connection = dataBase.getConnection();
                PreparedStatement preparedStatement = null;
                try {
                    connection.setAutoCommit(false);
                    preparedStatement = connection.prepareStatement(sql);
                    List<Result> list = new ArrayList<>(batchSize);
                    long start = System.currentTimeMillis();
                    long end = 0;
                    int i = 0;
                    int num = 0;
                    int callBackSuccess = 0;
                    while (true) {
                        i++;
                        try {
                            Result result = queue.poll();
                            if (result == null) {
                                break;
                            }
                            list.add(result);
                            for (int i1 = 0; i1 < result.objects.length; i1++) {
                                preparedStatement.setObject(i1 + 1, result.objects[i1]);
                            }
                            if (result.success != null) {
                                callBackSuccess++;
                            }
                            preparedStatement.addBatch();
                            num++;
                            if (i % batchSize == 0 || queue.isEmpty()) {
                                preparedStatement.executeBatch();
                                connection.commit();
                                end = System.currentTimeMillis();
                                log.d(Template.THIS_LANGUAGE[10], num, Template.THIS_LANGUAGE[11],  Template.THIS_LANGUAGE[12], end - start,  Template.THIS_LANGUAGE[13], sql);
                                num = 0;
                                if (callBackSuccess > 0) {
                                    List<Result> finalList = list;
                                    if (!threadPool.execute(() -> {
                                        for (Result result1 : finalList) {
                                            if (result1.success == null) {
                                                continue;
                                            }
                                            try {
                                                result1.success.run();
                                            } catch (Exception e) {
                                                log.e( Template.THIS_LANGUAGE[14], result1);
                                            }
                                        }
                                    })) {
                                        log.e(Template.THIS_LANGUAGE[15], list);
                                    }
                                }
                                list = new ArrayList<>(batchSize);
                                callBackSuccess = 0;
                                start = System.currentTimeMillis();
                            }

                        } catch (SQLException e) {
                            rollbackFun(connection,list,preparedStatement,e);
                        }
                    }
                } finally {
                    dataBase.close(null, preparedStatement);
                }

            }
        }
        return runNum;
    }
    private void rollbackFun(Connection connection,List<Result> list,PreparedStatement preparedStatement, SQLException e) throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
        for (int n = 0; n < list.size(); n++) {
            Result result = list.get(n);
            try {
                for (int k = 0; k < result.objects.length; k++) {
                    preparedStatement.setObject(k + 1, result.objects[k]);
                }
                preparedStatement.execute();
                if (!threadPool.execute(result.success)) {
                    log.e("异步执行成功，但是回调队列溢出-2");
                }
            } catch (Exception e2) {
                log.e("异步批量执行SQL 失败", result.sql, result.objects, e);
                if (result.fail == null) {
                    log.e("异步执行失败", e);
                } else {
                    if (!threadPool.execute(result.fail)) {
                        log.e("异步执行失败，并且回调队列溢出", e);
                    }
                }
            }
        }
        connection.setAutoCommit(false);
    }
    public int add(String sql, Object[] parar) {
        if (parar == null) {
            parar = new Object[0];
        }
        return add(new Result(sql, parar));
    }
    public int add(Result result) {
        ConcurrentLinkedQueue<Result> queue = cacheMap.get(result.sql);
        if (queue == null) {
            lock.lock();
            try {
                queue = cacheMap.get(result.sql);
                if (queue == null) {
                    queue = new ConcurrentLinkedQueue<>();
                    cacheMap.put(result.sql, queue);
                }
            } finally {
                lock.unlock();
            }
        }
        queue.add(result);
        return 1;
    }
}
