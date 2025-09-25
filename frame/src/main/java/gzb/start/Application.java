package gzb.start;
import gzb.frame.netty.NettyServer;
import gzb.tools.Config;
import gzb.tools.Tools;
public class Application {
    public static NettyServer server = null;
    public static void main(String[] args) throws Exception {
        run(Application.class);
    }
    public static void run(Class<?>aClass) throws Exception {
        String path = Tools.getProjectRoot(aClass);
        System.setProperty("file.encoding","UTF-8");
        System.setProperty("this.dir", path);
        server = new NettyServer();
        if (Config.WS_PORT>0) {

        }
        if (Config.TCP_PORT>0) {

        }
        if (Config.UDP_PORT>0) {

        }
        if (Config.HTTP_PORT>0) {
            server.startHTTPServer(Config.HTTP_PORT, Math.max(Config.cpu / 10, 1), Config.threadNum);
        }
    }

}
