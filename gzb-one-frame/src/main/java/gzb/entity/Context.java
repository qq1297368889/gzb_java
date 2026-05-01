package gzb.entity;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;

import java.util.List;
import java.util.Map;

public class Context {
    Integer depth;
    Request this_request;
    Response this_response;
    Map<String, List<Object>> this_requestMap;
}
