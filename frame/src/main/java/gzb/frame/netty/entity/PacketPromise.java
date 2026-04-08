package gzb.frame.netty.entity;

import java.util.Arrays;
/// /users/find,1,哈哈哈哈  这是数据格式
public class PacketPromise {
    /// 路由地址
    public String url;
    //请求方法 对应 GET POST PUT DELETE
    public String method;
    /// 数据类型（0-表单，1-JSON，2-BYTE,3-FILE）  文件在data中的格式是  文件名/文件类型/文件签名MD5/结束分隔符/文件数据流/结束分隔符
    public int type;
    /// 数据内容
    public byte[] data;

    @Override
    public String toString() {
        return "{" +
                "\"url\":\""+url+"\"," +
                "\"method\":\""+method+"\"," +
                "\"type\":\""+type+"\"," +
                "\"data\":"+ Arrays.toString(data) +"" +
                "}";
    }
}