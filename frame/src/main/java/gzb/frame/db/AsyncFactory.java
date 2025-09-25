package gzb.frame.db;

import gzb.tools.Config;
import gzb.tools.OnlyId;
import gzb.tools.Tools;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheMap;
import gzb.tools.log.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncFactory {

    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(AsyncFactory.class));

        DataBase db = DataBaseFactory.getDataBase(
                "frame", "127.0.0.1", "3306", "root", "root",
                "com.mysql.cj.jdbc.Driver", true,
                12, 3000, 200, 5000, 6
        );
        String sql = "INSERT INTO frame.sys_users_login_log" +
                "(sys_users_login_log_id, sys_users_login_log_ip, sys_users_login_log_desc, sys_users_login_log_time, " +
                "sys_users_login_log_uid, sys_users_login_log_token, sys_users_login_log_mac) VALUES " +
                "(?,?,?,?,?,?,?);";
        String sql2 = "INSERT INTO frame.sys_users_login_log" +
                "(sys_users_login_log_id, sys_users_login_log_ip, sys_users_login_log_desc, sys_users_login_log_time, " +
                "sys_users_login_log_2uid, sys_users_login_log_token, sys_users_login_log_mac) VALUES " +
                "(?,?,?,?,?,?);";

        AsyncFactory asyncFactory = db.getAsyncFactory();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            asyncFactory.add(sql, new Object[]{OnlyId.getDistributed(), null, i + "-desc", null, null, null, null});
        }
        long end = System.currentTimeMillis();
        asyncFactory.log.d("异步插入数据库10000条 耗时", end - start, "总耗时", System.currentTimeMillis() - start);
        while (true) {
            Tools.sleep(1000);
            asyncFactory.log.d("异步插入数据库10000条 耗时", end - start, "总耗时", System.currentTimeMillis() - start);
        }
    }

    public Log log = Config.log;
    public int debugNum = 0;
    public int batchSize = 1000;
    public int batchAwait = 200;
    public Lock lock = new ReentrantLock();
    public Lock transactionLock = new ReentrantLock();
    public Map<String, ConcurrentLinkedQueue<Object[]>> cacheMap = new ConcurrentHashMap<>();
    public Map<String, Map<String, ConcurrentLinkedQueue<Object[]>>> transactionMap = new ConcurrentHashMap<>();
    public DataBase dataBase;
    public Thread[] threads;

    public AsyncFactory(DataBase dataBase, int threadNum, int batchSize, int batchAwait) {
        this.dataBase = dataBase;
        this.batchSize = batchSize;
        this.batchAwait = batchAwait;
        threads = new Thread[threadNum];
        if (threadNum<1) {
            log.w("警告，后台异步线程数小于1，所有异步操作将无法执行,如需要请至少设置为1，请检查配置文件中的 db.mysql.数据库名.async.thread.num=0");
        }else{
            startThread(threadNum);
        }
    }

    public void startThread(int threadNum) {
        for (int i = 0; i < threadNum; i++) {
            threads[i] = new Thread(() -> {
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
            threads[i].setName("AsyncFactory-" + i);
            threads[i].start();
        }
    }

    public boolean execMapSql(Map<String, ConcurrentLinkedQueue<Object[]>> cacheMap) throws SQLException {
        boolean runNum = false;
        for (String sql : cacheMap.keySet()) {
            ConcurrentLinkedQueue<Object[]> queue = null;
            lock.lock();
            try {
                queue = cacheMap.remove(sql);
            } finally {
                lock.unlock();
            }
            if (queue != null && queue.size() > 0) {
                runNum = true;
                Connection connection = dataBase.getConnection();
                int size = queue.size();
                PreparedStatement preparedStatement = null;
                try {
                    connection.setAutoCommit(false);
                    preparedStatement = connection.prepareStatement(sql);

                        List<Object[]> list = new ArrayList<>();
                        long start = System.currentTimeMillis();
                        long end = 0;
                        int i = 0;
                        int num = 0;
                        while (true) {
                            i++;
                            size--;
                            try {
                                Object[] parameter = queue.poll();
                                if (parameter == null) {
                                    break;
                                }
                                list.add(parameter);
                                for (int i1 = 0; i1 < parameter.length; i1++) {
                                    preparedStatement.setObject(i1 + 1, parameter[i1]);
                                }
                                preparedStatement.addBatch();
                                debugNum++;
                                num++;
                                if (i % batchSize == 0 || size == 0) {
                                    preparedStatement.executeBatch();
                                    connection.commit();
                                    end = System.currentTimeMillis();
                                    list.clear();
                                    log.d("异步批量执行SQL", num, "条", "耗时", end - start, "毫秒", sql);
                                    num = 0;
                                    start = System.currentTimeMillis();
                                }

                            } catch (SQLException e) {
                                log.e("异步批量执行SQL 失败,尝试筛选错误源", sql, e);
                                connection.rollback();
                                List<Object[]> list2 = new ArrayList<>();
                                connection.setAutoCommit(true);
                                for (int n = 0; n < list.size(); n++) {
                                    try {
                                        for (int k = 0; k < list.get(n).length; k++) {
                                            preparedStatement.setObject(k + 1, list.get(n)[k]);
                                        }
                                        preparedStatement.execute();
                                    } catch (Exception e2) {
                                        list2.add(list.get(n));
                                    }
                                }
                                connection.setAutoCommit(false);
                                if (list2.size() > 0) {
                                    log.e("异步批量执行SQL 重试失败", sql, list2);
                                }
                            }

                        }
                } finally {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                }

            }
        }
        return runNum;
    }

    public boolean execMapSqlTransaction(Map<String, ConcurrentLinkedQueue<Object[]>> cacheMap) throws SQLException {
        Connection connection = null;
        if (cacheMap.size() == 0) {
            return true;
        }
        Map<String, ConcurrentLinkedQueue<Object[]>> cacheMap0 = new HashMap<>();
        PreparedStatement preparedStatement = null;
        try {
            connection = dataBase.getConnection();
            connection.setAutoCommit(false);
            for (String sql : cacheMap.keySet()) {
                ConcurrentLinkedQueue<Object[]> queue = null;
                transactionLock.lock();
                try {
                    queue = cacheMap.remove(sql);
                } finally {
                    transactionLock.unlock();
                }
                if (queue != null) {
                    int size = queue.size();
                    int i = 0;
                    if (size > 0) {
                        size--;
                        i++;
                        ConcurrentLinkedQueue<Object[]> queue2 = new ConcurrentLinkedQueue<>();
                        cacheMap0.put(sql, queue2);
                        preparedStatement = connection.prepareStatement(sql);
                        while (true) {
                            Object[] parameter = queue.poll();
                            if (parameter == null) {
                                break;
                            }
                            queue2.add(parameter);
                            for (int i1 = 0; i1 < parameter.length; i1++) {
                                preparedStatement.setObject(i1 + 1, parameter[i1]);
                            }
                            preparedStatement.addBatch();
                            if (i % batchSize == 0 || size == 0) {
                                int[] res = preparedStatement.executeBatch();
                                log.d("res", res);
                            }
                        }
                    }

                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            log.e("事务执行错误", e, "未执行数据", cacheMap, "已执行数据", cacheMap0);
            return false;
        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
                log.d("preparedStatement", "close");
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return true;
    }

    public int add(String sql, Object[] parar) {
        ConcurrentLinkedQueue<Object[]> queue = cacheMap.get(sql);
        if (queue == null) {
            lock.lock();
            try {
                if (queue == null) {
                    queue = new ConcurrentLinkedQueue<>();
                    cacheMap.put(sql, queue);
                }
            } finally {
                lock.unlock();
            }
        }
        queue.add(parar);
        return 1;
    }

    public int addTransaction(String key, String sql, Object[] parar) {
        Map<String, ConcurrentLinkedQueue<Object[]>> map = transactionMap.get(key);
        if (map == null) {
            transactionLock.lock();
            try {
                if (map == null) {
                    map = new ConcurrentHashMap<>();
                    transactionMap.put(key, map);
                }
            } finally {
                transactionLock.unlock();
            }
        }
        ConcurrentLinkedQueue<Object[]> list = map.get(sql);
        if (list == null) {
            transactionLock.lock();
            try {
                if (list == null) {
                    list = new ConcurrentLinkedQueue<>();
                    map.put(sql, list);
                }
            } finally {
                transactionLock.unlock();
            }
        }
        list.add(parar);
        return 1;
    }

    //给框架调用的 所以不会忘记删除
    public boolean commitTransaction(String key) throws SQLException {
        Map<String, ConcurrentLinkedQueue<Object[]>> map = transactionMap.remove(key);
        if (map == null) {
            return true;
        }
        return execMapSqlTransaction(map);
    }
}
