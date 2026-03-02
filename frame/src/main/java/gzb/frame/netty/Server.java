package gzb.frame.netty;

import gzb.frame.PublicEntrance;
import gzb.frame.factory.Factory;
import gzb.tools.Config;
import gzb.tools.log.Log;

public class Server {
    public static HTTPServer httpServer;
    public static TCPServer tcpServer;


    public static Factory factory;

    public static void init() throws Exception {
        if (factory==null) {
            factory = PublicEntrance.factory;
            long start = System.currentTimeMillis();
            if (Config.code_type.startsWith("file")) {
                factory.loadJavaDir(Config.codeDir, Config.codePwd, Config.codeIv);
            }
            long end = System.currentTimeMillis();
            Log.log.i("初始化耗时", end - start);
        }
    }

    public static void startTCPServer(int port) throws Exception {
        init();
        tcpServer = new TCPServer();
        tcpServer.start(port);
    }

    public static void startUDPServer(int port) throws Exception {
        init();

    }

    public static void startHTTPServer(int port) throws Exception {
        init();
        httpServer = new HTTPServer();
        httpServer.start(port);
    }

    public static void startWebSocket(int port) throws Exception {
        init();
    }
}
