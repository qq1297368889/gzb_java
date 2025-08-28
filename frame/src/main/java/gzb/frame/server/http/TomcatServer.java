package gzb.frame.server.http;

import gzb.frame.factory.ClassFactory;
import gzb.frame.server.http.ws.WebSocket;
import gzb.frame.server.http.ws.WebSocketEndpoint;
import gzb.tools.Config;
import gzb.tools.Tools;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.websocket.server.WsSci;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class TomcatServer {

    public Tomcat tomcat = null;
    public Context ctx = null;
    public ServerContainer serverContainer = null;

    public void startServer() throws Exception {
        String address = Config.get("gzb.system.server.http.address", "0.0.0.0");
        int port = Config.getInteger("gzb.system.server.http.port", 8080);
        int threadMin = Config.getInteger("gzb.system.server.http.thread.min", 20);
        int threadMax = Config.getInteger("gzb.system.server.http.thread.max", 200);
        int maxAwaitNum = Config.getInteger("gzb.system.server.http.await.num", 200);
        int maxAwaitTime = Config.getInteger("gzb.system.server.http.await.time.out", 20000);
        int maxPostSize = Config.getInteger("gzb.system.server.http.postSize", 1024 * 1024 * 10);
        String uriEncoding = Config.get("gzb.system.server.http.uriEncoding", "UTF-8");
        boolean compression = Config.getBoolean("gzb.system.server.http.compression", false);
        String staticDir = Config.get("gzb.system.server.http.static.dir", Config.thisPath());
        String compressionMimeTypes = Config.get("gzb.system.server.http.compressionMimeTypes", "text/html,text/xml,text/plain,text/css,application/javascript,application/json");

        int compressionMinSize = Config.getInteger("gzb.system.server.http.compressionMinSize", 2048);
        boolean secure = Config.getBoolean("gzb.system.server.http.secure", false);
        String scheme = Config.get("gzb.system.server.http.scheme", "http");
        String tmpDir = Config.get("gzb.system.server.http.tmp.dir", Config.tmpPath());

        Tools.fileMkdirsPath(tmpDir);
        startServer(address, port, threadMin, threadMax, maxAwaitNum, maxAwaitTime, staticDir, tmpDir, maxPostSize, uriEncoding, compression, compressionMimeTypes
                , compressionMinSize, secure, scheme);
    }

    public void startServer(String address, Integer port, Integer threadMin, Integer threadMax, Integer maxAwaitNum, Integer maxAwaitTime,
                            String staticDir, String tmpDir, Integer maxPostSize, String uriEncoding, Boolean compression
            , String compressionMimeTypes, Integer compressionMinSize
            , Boolean secure, String scheme) throws LifecycleException, DeploymentException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        tomcat = new Tomcat();
        tomcat.setPort(port);
        // 设置临时目录
        tomcat.setBaseDir(tmpDir);
        Connector connector = tomcat.getConnector();// 获取连接器
        // 允许访问的来源地址
        connector.setProperty("address", address);
        // 最小空闲线程数
        connector.setProperty("minSpareThreads", threadMin.toString());
        // 最大线程数
        connector.setProperty("maxThreads", threadMax.toString());
        // 最大等待队列长度 (当所有线程都在处理请求时，等待连接的最大数量)
        connector.setProperty("acceptCount", maxAwaitNum.toString());
        // 连接超时时间 (等待客户端完成 TCP 握手的时间，单位毫秒)
        connector.setProperty("connectionTimeout", maxAwaitTime.toString());
        // 最大 POST 请求大小 (用于文件上传等)，单位字节
        if (maxPostSize == -1) {
            connector.setMaxPostSize(Integer.MAX_VALUE);
        } else {
            connector.setMaxPostSize(maxPostSize);
        }
        // URI 编码 (用于解码 URL 中的参数)
        connector.setURIEncoding(uriEncoding); // No dedicated setter found in common use

        // 是否启用 GZIP 压缩
        connector.setProperty("compression", compression.toString());
        // 启用 GZIP 压缩的 MIME 类型
        connector.setProperty("compressionMimeTypes", compressionMimeTypes);
        // 启用 GZIP 压缩的最小响应大小
        connector.setProperty("compressionMinSize", compressionMinSize.toString());
        // 是否启用安全连接 (HTTPS)
        connector.setSecure(secure); // Dedicated setter (takes boolean)
        // 连接器使用的协议 scheme (http 或 https)
        connector.setScheme(scheme); // Dedicated setter
        // 每个连接上允许的最大Keep-Alive请求数
        connector.setProperty("maxKeepAliveRequests", "100");
        // Keep-Alive 超时时间 (毫秒)
        connector.setProperty("keepAliveTimeout", "20000");

        // 3. 创建 Web 应用上下文
        ctx = tomcat.addContext("", staticDir);
        ctx.setAllowCasualMultipartParsing(true);

        // 替换默认的 Session 管理器（更彻底）
        ctx.setManager(new SessionManager());

        // 设置会话超时时间
        // 启用 WebSocket 支持
        ctx.addServletContainerInitializer(new WsSci(), Collections.emptySet());

        // 5. 启动服务器
        tomcat.start();
        ctx.setSessionTimeout(10);
        serverContainer = (ServerContainer) ctx.getServletContext().getAttribute(ServerContainer.class.getName());

        //添加默认 servlet
        addServlet(GzbServlet.class);
        //添加 ws支持
        addWebSocket("/ws3",WebSocket.class);
        System.out.println("已加载映射信息  "+Handle.classFactory.mapClass.mapping0.size()+"个");
        System.out.println("server 1.0.0 started on port " + port);
    }

    public void addServlet(String reqPath, Class<?> servletClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String gzbServletName = servletClass.getName();
        Tomcat.addServlet(ctx, gzbServletName, (Servlet) servletClass.getDeclaredConstructor().newInstance());
        ctx.addServletMappingDecoded(reqPath, gzbServletName);
    }

    private void addServlet(Class<?> servletClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 检查类是否有 @WebServlet 注解
        if (servletClass.isAnnotationPresent(WebServlet.class)) {
            WebServlet webServletAnnotation = servletClass.getAnnotation(WebServlet.class);
            // 获取注解中的映射路径
            String[] urlPatterns = webServletAnnotation.urlPatterns();
            if (urlPatterns.length == 0) {
                urlPatterns = webServletAnnotation.value();
            }
            // 创建 Servlet 实例
            Servlet servlet = (Servlet) servletClass.getDeclaredConstructor().newInstance();
            // 获取 Servlet 名称
            String servletName = servletClass.getName();
            // 注册 Servlet
            Tomcat.addServlet(ctx, servletName, servlet);
            // 添加 Servlet 映射
            for (String urlPattern : urlPatterns) {
                ctx.addServletMappingDecoded(urlPattern, servletName);
            }
        }
    }

    public void addWebSocket(String reqPath, Class<?>aClass) throws DeploymentException {
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(aClass, reqPath).build();
        serverContainer.addEndpoint(config);
    }

    public void addWebSocket(Class<?> wsClass) throws DeploymentException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (wsClass.isAnnotationPresent(ServerEndpoint.class)) {
            ServerEndpoint serverEndpoint = wsClass.getAnnotation(ServerEndpoint.class);
            if (serverEndpoint == null || serverEndpoint.value() == null || serverEndpoint.value().isEmpty()) {
                return;
            }
            ServerEndpointConfig config = ServerEndpointConfig.Builder.create(wsClass, serverEndpoint.value()).build();
            serverContainer.addEndpoint(config);
        }
    }

}

