package gzb.frame.factory.v4.entity;

import gzb.tools.json.Result;
import gzb.tools.thread.ThreadPool;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class ThreadEntity {
    public List<Thread> thread;
    public List<Result> result;
    public Lock lock;

}
