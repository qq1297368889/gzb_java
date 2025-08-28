package gzb.start;
import gzb.frame.server.http.TomcatServer;
import gzb.tools.Tools;

public class Application {

    public static TomcatServer server = new TomcatServer();
    public static void main(String[] args) throws Exception {
        run(Application.class);
    }
    public static void run(Class<?>aClass) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(aClass));
        server.startServer();
    }

}
