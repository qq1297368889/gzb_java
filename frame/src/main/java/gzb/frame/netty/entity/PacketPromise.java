package gzb.frame.netty.entity;

import java.util.Arrays;
/// /users/find,1,哈哈哈哈  这是数据格式
public class PacketPromise {
    /// 路由地址
    public String url;
    //请求方法 对应 GET POST PUT DELETE
    public String method;
    /// 数据类型（0-表单，1-JSON，2-BYTE FILE）
     public int type;
    /// 数据内容
    /// type = 2 在data中的格式是 参数名/文件名/文件类型/md5/文件流长度/文件byte内容 ..... 循环读取
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