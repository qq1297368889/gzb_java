package gzb.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsyEntity {
    public Lock lock = new ReentrantLock();
    public List<Object[]> list = new ArrayList<>();
}