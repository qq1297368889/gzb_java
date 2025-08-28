package gzb.frame.server.http.ws;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
@ClientEndpoint
public class WebSocketSuper extends WebSocket{
    public static void main(String[] args) {
        WebSocketSuper webSocketSuper = new WebSocketSuper();
        webSocketSuper.test2();
    }
    public void test1(){
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = URI.create("ws://localhost:2080/ws3");
            Session session = container.connectToServer(WebSocketSuper.class, uri);
            System.out.println(session);
            Thread.sleep(1000*3);
            send(session,"test001".getBytes());
            Thread.sleep(1000*3);
            System.out.println("receiveText 1 "+" "+receiveText(session,10000));
            Thread.sleep(1000*3);
            sendText(session,"test002x00");
            Thread.sleep(1000*3);
            System.out.println("receiveText 1 "+" "+receiveText(session,10000));
            Thread.sleep(1000*3);

            Thread.sleep(1000*3);

            // 可以在这里进行消息发送和接收操作
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void test2(){
        // 创建一个 HttpClient 实例
        try {
            // 创建 WebSocket 容器
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            // 连接到 WebSocket 服务器
            WsClientExample client = new WsClientExample();
            container.connectToServer(client, new URI("ws://localhost:2080/ws3"));
            // 保持主线程存活一段时间，以便接收消息
            Thread.sleep(88800);
        } catch (DeploymentException | IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
