package gzb.frame.server.http.ws;

import javax.websocket.*;

// 定义一个 WebSocket 客户端端点
@ClientEndpoint
public class WsClientExample extends WebSocket{

    // 会话对象，用于与服务器进行通信
    private Session session;

    // 连接建立时的回调方法
    @OnOpen
    public void onOpen(Session session,EndpointConfig endpointConfig) {
        super.onOpen(session,endpointConfig);
        startHeartbeat(session);
    }

    // 接收到服务器消息时的回调方法
    @OnMessage
    public void onMessage(Session session,String message) {
        super.onMessage(session,message.getBytes());
    }

    // 接收到服务器消息时的回调方法
    @OnMessage
    public void onMessage(Session session,byte[] message) {
        super.onMessage(session,message);
    }

    // 连接关闭时的回调方法
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session,closeReason);
        stopHeartbeat(session);
    }

    // 连接出错时的回调方法
    @OnError
    public void onError(Session session,Throwable error) {
        super.onError(session,error);
        stopHeartbeat(session);
    }
}