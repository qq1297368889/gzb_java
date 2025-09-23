package gzb.entity;

import gzb.tools.GzbMap;

import java.util.concurrent.locks.Lock;

public class ThreadInfo {
    long start;
    long end;
    Thread thread;
    Lock lock;
    GzbMap gzbMap;
    String name;
    int stop;

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public GzbMap getGzbMap() {
        return gzbMap;
    }

    public void setGzbMap(GzbMap gzbMap) {
        this.gzbMap = gzbMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "start=" + start +
                ", end=" + end +
                ", thread=" + thread +
                ", lock=" + lock +
                ", gzbMap=" + gzbMap +
                ", name='" + name + '\'' +
                ", stop=" + stop +
                '}';
    }
}
