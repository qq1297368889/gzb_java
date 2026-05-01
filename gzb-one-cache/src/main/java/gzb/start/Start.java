package gzb.start;

import com.system.tools.CacheData;
import com.system.tools.QueueData;

public class Start {
    public static void main(String[] args) throws Exception {
        //创建两个缓存数据库
        CacheData.create(2);
        //创建两个队列服务器
        QueueData.create(2);
        //启动服务器
        Application.run();
    }
}
