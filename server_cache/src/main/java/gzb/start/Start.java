package gzb.start;

import com.action.CacheData;

/// 注意 玩具 demo  不要认真 只是验证主体逻辑
public class Start {
    public static void main(String[] args) throws Exception {
        //创建两个缓存数据库
        CacheData.create(2);
        //启动服务器
        Application.run();
    }
}
