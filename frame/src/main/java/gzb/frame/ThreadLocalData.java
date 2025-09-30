package gzb.frame;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ThreadLocalData {
    //这个不用担心泄露 泄露也没关系 省的下次 == null  用于数据库事件
    public static final ThreadLocal<Integer> depth = new ThreadLocal<>();
    public static final ThreadLocal<Request> this_request = new ThreadLocal<>();
    public static final ThreadLocal<Response> this_response = new ThreadLocal<>();
    public static final ThreadLocal<Map<String, List<Object>>> this_requestMap = new ThreadLocal<>();


}
