package gzb.start;

import gzb.frame.netty.handler.HTTPHandler;
import gzb.tools.Config;
import gzb.tools.log.Log;

/// 注意 玩具 demo  不要认真 只是验证主体逻辑
public class Start {
    public static void main(String[] args) throws Exception {
        //启动服务器
        Application.run();
    }
}
