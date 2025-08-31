package gzb.frame.server.http.ws;

import com.google.gson.JsonObject;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import javax.websocket.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.*;

public class WebSocket extends Endpoint {
    public static final byte[] HEARTBEAT_MESSAGE = {0X0, 0x1};
    public static final long HEARTBEAT_INTERVAL = 2000; // 心跳间隔，单位：毫秒
    public static final Map<Session, Long> mapSession = new ConcurrentHashMap<>();
    public static final Map<Session, BlockingQueue<byte[]>> mapQueue = new ConcurrentHashMap<>();
    public static final Map<Session, ScheduledFuture<?>> mapScheduledFuture = new ConcurrentHashMap<>();
    public static final Map<Session, ScheduledExecutorService> mapScheduledExecutorService = new ConcurrentHashMap<>();
    public static final Log log = new LogImpl(WebSocket.class);

    public void startHeartbeat(Session session) {
        ScheduledExecutorService scheduledExecutorService = mapScheduledExecutorService.get(session);
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            mapScheduledExecutorService.put(session, scheduledExecutorService);
        }
        mapScheduledFuture.put(session, scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (session == null || !session.isOpen()) {
                close(session);
                return;
            }
            Long lastHeartbeat = mapSession.get(session);
            if (lastHeartbeat!=null && System.currentTimeMillis() - lastHeartbeat > HEARTBEAT_INTERVAL * 3) {
                close(session);
                return;
            }
            send(session, HEARTBEAT_MESSAGE);
        }, 0, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS));
    }


    public void stopHeartbeat(Session session) {
        ScheduledFuture<?> scheduledFuture = mapScheduledFuture.get(session);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        log.i("onOpen: ", session);
        WebSocket webSocket = this;
        mapSession.put(session, System.currentTimeMillis());
        mapQueue.put(session, new LinkedBlockingQueue<>());
        addEve(session, webSocket);
        JsonObject command = new JsonObject();

        command.addProperty("id", 1);
        command.addProperty("method", "Runtime.evaluate");
        JsonObject params = new JsonObject();
        params.addProperty("expression", "(function (){try {try {if (gzb == null) {return \"lib-no\";}}catch (e){return \"lib-no\";}\n" +
                "let data = location.href;\n" +
                "if (data == null){return null;}return data.toString();} catch (e){return e.message;}}());");
        params.addProperty("returnByValue", true); // 要求返回值
        command.add("params", params);
        session.getAsyncRemote().sendText(command.toString());
    }

    public void addEve(Session session, WebSocket webSocket) {
        try {
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    webSocket.onMessage(session, message.getBytes());
                }
            });
            // 只添加处理二进制消息的处理器
            session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
                @Override
                public void onMessage(ByteBuffer message) {
                    byte[] bytes = new byte[message.remaining()];
                    message.get(bytes);
                    webSocket.onMessage(session, bytes);
                }
            });
        } catch (Exception e) {
            log.e("已经设置了消息处理器,(客户端报错正常,无视)", e);
        }

    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        log.d("onClose", session, closeReason);
        close(session);
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        log.e("onError", session, throwable);
        close(session);
    }

    public void close(Session session) {
        stopHeartbeat(session);
        mapSession.remove(session);
        mapQueue.remove(session);
        mapScheduledFuture.remove(session);
        mapScheduledExecutorService.remove(session);
        ScheduledExecutorService executor = mapScheduledExecutorService.remove(session);
        if (executor != null) {
            executor.shutdown();
        }
        if (session.isOpen()){
            try {
                session.close();
            } catch (Exception e) {
                log.e("close error", e);
            }
        }

    }

    public void onMessage(Session session, byte[] message) {
        mapSession.put(session, System.currentTimeMillis());
        if (message != null) {
            if (message.length == HEARTBEAT_MESSAGE.length && Arrays.equals(message, HEARTBEAT_MESSAGE)) {
                log.i("ping", Arrays.toString(message));
                return;
            }
            log.i("onMessage: ", message.length);
        } else {
            log.i("onMessage: null");
            message = new byte[0];
        }
        BlockingQueue<byte[]> queue = mapQueue.get(session);
        if (queue != null) {
            try {
                queue.put(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.e("onMessage error", e);
            } catch (Exception e) {
                log.e("onMessage error", e);
            }
        }
    }

    public void onMessage(Session session, String message) {
        onMessage(session, message.getBytes());
    }

    public byte[] receive(Session session, long time) {
        BlockingQueue<byte[]> queue = mapQueue.get(session);
        if (queue != null) {
            try {
                return queue.poll(time, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.e("receive error", e);
                return null;
            } catch (Exception e) {
                log.e("receive error", e);
            }
        }
        return null;
    }

    public String receiveText(Session session, long time) {
        byte[] bytes = receive(session, time);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    public void send(Session session, byte[] message) {
        log.d("send message.length", message.length);
        try {
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(message));
            mapSession.put(session, System.currentTimeMillis());
        } catch (Exception e) {
            log.e("send error", e);
        }
    }

    public byte[] sendAndReceive(Session session, byte[] message, long time) {
        try {
            send(session, message);
            return receive(session, time);
        } catch (Exception e) {
            log.e("sendAndReceive error", e);
            return null;
        }
    }

    public void sendText(Session session, String message) {
        log.d("sendText", message);
        try {
            session.getAsyncRemote().sendText(message);
            mapSession.put(session, System.currentTimeMillis());
        } catch (Exception e) {
            log.e("sendText error", e);
        }
    }

    public byte[] sendTextAndReceive(Session session, String message, long time) {
        try {
            sendText(session, message);
            return receive(session, time);
        } catch (Exception e) {
            log.e("sendTextAndReceive error", e);
            return null;
        }
    }

}