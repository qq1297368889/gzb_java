package gzb.start;
import gzb.frame.netty.NettyServer;
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
        server.startServer();
    }

}
