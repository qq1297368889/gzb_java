package gzb.frame.server.http.ws;

import gzb.frame.server.http.ws.entity.WSEntity;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/ws3")
public class WebSocketEndpoint extends Endpoint {
    private Session session;
    private static final Map<Session, WSEntity> mapSession = new ConcurrentHashMap<>();
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        System.out.println("onOpen: " + session);
        new WSEntity(session).putMap(mapSession);
        WebSocketEndpoint webSocketEndpoint = this;
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                webSocketEndpoint.onMessage(message);
            }
        });
        // 只添加处理二进制消息的处理器
        session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
            @Override
            public void onMessage(ByteBuffer message) {
                byte[] bytes = new byte[message.remaining()];
                message.get(bytes);
                webSocketEndpoint.onMessage(bytes);
            }
        });
    }
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("onClose: " + session);
        new WSEntity(session).removeMap(mapSession);
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        System.out.println("onError: " + session);
        close();
    }

    public void onMessage(byte[] bytes) {
        WSEntity wsEntity = mapSession.get(session);
        if (wsEntity == null) {
            return;
        }
        if (wsEntity.getCountDownLatch()!=null){
            wsEntity.getCountDownLatch().countDown();
            wsEntity.setCountDownLatch(null);
            wsEntity.setCountDownLatchDataByte(bytes);
            wsEntity.setCountDownLatchDataString(null);
        }
        wsEntity.setReceiveTimestamp(System.currentTimeMillis());
        System.out.println("onMessage byte: " + Arrays.toString(bytes));
        sendMessage("长度：" + (bytes.length));
    }

    public void onMessage(String message) {
        WSEntity wsEntity = mapSession.get(session);
        if (wsEntity == null) {
            return;
        }
        if (wsEntity.getCountDownLatch()!=null){
            wsEntity.getCountDownLatch().countDown();
            wsEntity.setCountDownLatch(null);
            wsEntity.setCountDownLatchDataString(message);
            wsEntity.setCountDownLatchDataByte(null);
        }
        wsEntity.setReceiveTimestamp(System.currentTimeMillis());
        System.out.println("onMessage text: " + message);
        sendMessage("长度：" + (message.length()));
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Map<Session, WSEntity> getAllSession() {
        return mapSession;
    }

    public void close() {
        close(session);
    }

    public void close(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int sendMessageByte(Session session, byte[] data) {
        System.out.println("sendMessage: " + Arrays.toString(data));
        WSEntity wsEntity = mapSession.get(session);
        if (wsEntity == null) {
            return -1;
        }
        wsEntity.setSendTimestamp(System.currentTimeMillis());
        session.getAsyncRemote().sendBinary(ByteBuffer.wrap(data));
        return 1;
    }

    public int sendMessageByte(byte[] data) {
        return sendMessageByte(session, data);
    }

    // mapSession 中 发送成功的会被删除 留下的是发送失败
    public int sendMessageByte(byte[] data, Map<Session, WSEntity> mapSession) {
        int count = 0;
        Iterator<Map.Entry<Session, WSEntity>> iterator = mapSession.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Session, WSEntity> entry = iterator.next();
            if (sendMessageByte(entry.getKey(), data) > -1) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    // listSession 中 发送成功的会被删除 留下的是发送失败
    public int sendMessageByte(byte[] data, List<Session> listSession) {
        int count = 0;
        Iterator<Session> iterator = listSession.iterator();
        while (iterator.hasNext()) {
            Session session1 = iterator.next();
            if (sendMessageByte(session1, data) > -1) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    public int sendMessage(Session session, String message) {
        System.out.println("sendMessage: " + message);
        WSEntity wsEntity = mapSession.get(session);
        if (wsEntity == null) {
            return -1;
        }
        wsEntity.setSendTimestamp(System.currentTimeMillis());
        session.getAsyncRemote().sendText(message);
        return 1;
    }


    public int sendMessage(String message) {
        return sendMessage(session, message);
    }

    // mapSession 中 发送成功的会被删除 留下的是发送失败
    public int sendMessage(String message, Map<Session, WSEntity> mapSession) {
        int count = 0;
        Iterator<Map.Entry<Session, WSEntity>> iterator = mapSession.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Session, WSEntity> entry = iterator.next();
            if (sendMessage(entry.getKey(), message) > -1) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    // listSession 中 发送成功的会被删除 留下的是发送失败
    public int sendMessage(String message, List<Session> listSession) {
        int count = 0;
        Iterator<Session> iterator = listSession.iterator();
        while (iterator.hasNext()) {
            Session session1 = iterator.next();
            if (sendMessage(session1, message) > -1) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }
    public String sendMessageAndWaitReply(String message,long timeout) {
        CountDownLatch latch = new CountDownLatch(1);
        sendMessage(message);
        WSEntity wsEntity = mapSession.get(session);
        try {
            if (latch.await(timeout,TimeUnit.SECONDS)) {
                if (wsEntity.getCountDownLatchDataString()!=null) {
                    return wsEntity.getCountDownLatchDataString();
                }
                if (wsEntity.getCountDownLatchDataByte()!=null) {
                    return new String(wsEntity.getCountDownLatchDataByte());
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            wsEntity.setCountDownLatchDataByte(null);
            wsEntity.setCountDownLatchDataString(null);
            wsEntity.setCountDownLatch(null);
        }
    }
    public byte[] sendMessageAndWaitReply(byte[] bytes,long timeout) {
        CountDownLatch latch = new CountDownLatch(1);
        sendMessageByte(bytes);
        WSEntity wsEntity = mapSession.get(session);
        try {
            if (latch.await(timeout,TimeUnit.SECONDS)) {
                if (wsEntity.getCountDownLatchDataByte()!=null) {
                    return wsEntity.getCountDownLatchDataByte();
                }
                if (wsEntity.getCountDownLatchDataString()!=null) {
                    return wsEntity.getCountDownLatchDataString().getBytes();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            wsEntity.setCountDownLatchDataByte(null);
            wsEntity.setCountDownLatchDataString(null);
            wsEntity.setCountDownLatch(null);
        }
    }

}
