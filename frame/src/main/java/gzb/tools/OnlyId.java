package gzb.tools;

import java.util.concurrent.locks.Lock;

public class OnlyId {
    private static String serverName;
    private static short subId;
    private static long subTime;
    static {
        serverName = Config.get("server.name", "001");
        subId = 0;
        subTime = 0;
    }


    public static void main(String[] args) {
        System.out.println(getDistributed());
    }
    public static final Long getDistributed() {
        String key = "OnlyId-getDistributed";
        Lock lock = LockFactory.getLock(key);
        StringBuilder sb = new StringBuilder();
        lock.lock();
        try {
            subId++;
            long subTime2 = System.currentTimeMillis();
            if (subTime2 != subTime) {
                subId = 0;
            } else {
                if (subId > 999) {
                    subId = 0;
                    Thread.sleep(1);
                    return getDistributed();
                }
            }
            subTime = subTime2;
            sb.append(subTime2);
            if (subId < 10) {
                sb.append("00").append(subId);
            } else if (subId < 100) {
                sb.append("0").append(subId);
            } else if (subId < 1000) {
                sb.append(subId);
            }
            sb.append(serverName);
        } catch (Exception e) {
            e.printStackTrace();
            sb = new StringBuilder().append(getDistributed());
        } finally {
            lock.unlock();
        }
        return Long.valueOf(sb.toString());
    }
    public static final String getDistributedString() {
        String key = "OnlyId-getDistributed";
        Lock lock = LockFactory.getLock(key);
        StringBuilder sb = new StringBuilder();
        lock.lock();
        try {
            subId++;
            long subTime2 = System.currentTimeMillis();
            if (subTime2 != subTime) {
                subId = 0;
            } else {
                if (subId > 999) {
                    subId = 0;
                    Thread.sleep(1);
                    return getDistributedString();
                }
            }
            subTime = subTime2;
            sb.append(subTime2);
            if (subId < 10) {
                sb.append("00").append(subId);
            } else if (subId < 100) {
                sb.append("0").append(subId);
            } else if (subId < 1000) {
                sb.append(subId);
            }
            sb.append(serverName);
        } catch (Exception e) {
            e.printStackTrace();
            sb = new StringBuilder().append(getDistributed());
        } finally {
            lock.unlock();
        }
        return sb.toString();
    }


}
