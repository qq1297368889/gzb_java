package com.frame.test;
import gzb.frame.annotation.*;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("test")
@Header(item = {@HeaderItem(key = "Content-Type", val = "application/json")})
public class Test {
    public static void main(String[] args) {
        List<Users>list=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(users);
        }

        System.out.println("toJsonV2 "+(Tools.toJson0(list)));
        for (int i = 0; i < 1000; i++) {
            long start=System.currentTimeMillis();
            for (int i1 = 0; i1 < 100000; i1++) {
                Tools.toJsonV2(list);
            }
            long end=System.currentTimeMillis();
            System.out.println("toJsonV2 "+(end-start));
            start=System.currentTimeMillis();
            for (int i1 = 0; i1 < 100000; i1++) {
                Tools.toJson(list);
            }
            end=System.currentTimeMillis();
            System.out.println("toJson "+(end-start));
            start=System.currentTimeMillis();
            for (int i1 = 0; i1 < 100000; i1++) {
                Tools.toJson0(list);
            }
            end=System.currentTimeMillis();
            System.out.println("toJson0 "+(end-start));
        }
    }
    public Test(){
        Log.log.i("new Test");
    }

    public static final Users users = new Users();

    static {
        users.setUsersId(8215438L);
        users.setUsersName("vpeGb2SNo8Xk");
        users.setUsersPassword("az1Amb2aVJsP");
        users.setUsersEmail("nWV3qO6qW0zZ@gzb.com");
        users.setUsersTime(LocalDateTime.now());
        users.setUsersAge(12);
    }
    public static List<Map<String, Object>> scanAllThreadLocals() {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            // 1. 获取所有线程
            Thread[] threads = new Thread[Thread.activeCount() * 2];
            int count = Thread.enumerate(threads);

            // 2. 反射获取 ThreadLocalMap 字段
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);

            for (int i = 0; i < count; i++) {
                Thread t = threads[i];
                if (t == null) continue;

                Map<String, Object> threadData = new HashMap<>();
                threadData.put("threadName", t.getName());
                threadData.put("threadId", t.getId());

                // 3. 拿到该线程的 ThreadLocalMap 对象
                Object threadLocalMap = threadLocalsField.get(t);
                if (threadLocalMap != null) {
                    List<String> entries = new ArrayList<>();

                    // 4. 反射遍历 ThreadLocalMap 内部的 table 数组
                    Field tableField = threadLocalMap.getClass().getDeclaredField("table");
                    tableField.setAccessible(true);
                    Object[] table = (Object[]) tableField.get(threadLocalMap);

                    for (Object entry : table) {
                        if (entry != null) {
                            // Entry 继承自 WeakReference，其 referent 是 ThreadLocal 对象
                            // 其 value 字段存储了真正的缓存数据
                            Field valueField = entry.getClass().getDeclaredField("value");
                            valueField.setAccessible(true);
                            Object value = valueField.get(entry);

                            if (value != null) {
                                entries.add(value.getClass().getName() + " -> " + value.toString());
                            }
                        }
                    }
                    threadData.put("locals", entries);
                }
                result.add(threadData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static final byte[] HELLO_WORD = "Hello, World!".getBytes(Config.encoding);
    /// 测试基本性能
    @EventLoop
    @RequestMapping("hello")
    public Object hello() {
        return HELLO_WORD;
    }

    /// 测试传参性能  + 序列化性能
    @EventLoop
    @RequestMapping("users/1")
    public Object users() {
        return users;
    }

    @EventLoop
    @RequestMapping("users/2")
    public Object users(Long usersId, String usersName, String usersPassword, String usersEmail, Integer usersAge) {
        return new Users(
                usersId == null ? 8215438L : usersId,
                usersName == null ?"vpeGb2SNo8Xk" : usersName,
                usersPassword == null ? "az1Amb2aVJsP": usersPassword,
                usersEmail == null ? "nWV3qO6qW0zZ@gzb.com" : usersEmail,
                usersAge == null ? 12: usersAge,
                null //not time
        );
    }
    @EventLoop
    @RequestMapping("users/3")
    public Object users(Users users) {
        return users;
    }

    /// 序列化性能
    @EventLoop
    @RequestMapping("users/array/1")
    public Object usersArray1(int size) {
        List<Users> usersList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            usersList.add(users);
        }
        return usersList;
    }


}
