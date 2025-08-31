package gzb.frame.server.http.ws.entity;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WSEntity {
    Session session;
    Long sendTimestamp;
    Long ReceiveTimestamp;
    List<String> data = new ArrayList<>();
    Lock lock=new ReentrantLock();
    CountDownLatch countDownLatch;
    byte[] countDownLatchDataByte;
    String countDownLatchDataString;
    public WSEntity(Session session) {
        this.session = session;
        this.sendTimestamp = 0L;
        this.ReceiveTimestamp = System.currentTimeMillis();
        this.data = new ArrayList<>();
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public byte[] getCountDownLatchDataByte() {
        return countDownLatchDataByte;
    }

    public void setCountDownLatchDataByte(byte[] countDownLatchDataByte) {
        this.countDownLatchDataByte = countDownLatchDataByte;
    }

    public String getCountDownLatchDataString() {
        return countDownLatchDataString;
    }

    public void setCountDownLatchDataString(String countDownLatchDataString) {
        this.countDownLatchDataString = countDownLatchDataString;
    }

    public Long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(Long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public Long getReceiveTimestamp() {
        return ReceiveTimestamp;
    }

    public void setReceiveTimestamp(Long receiveTimestamp) {
        ReceiveTimestamp = receiveTimestamp;
    }

    public WSEntity putMap(Map<Session, WSEntity> map) {
        map.put(session, this);
        return this;
    }
    public WSEntity removeMap(Map<Session, WSEntity> map) {
        return map.remove(session);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
