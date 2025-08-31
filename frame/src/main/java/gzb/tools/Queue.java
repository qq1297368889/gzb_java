package gzb.tools;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Queue<T> {
    private final Deque<T> deque = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final int capacity;
    private final Condition notFull = lock.newCondition();

    public Queue() {
        capacity = Integer.MAX_VALUE;
    }

    public Queue(int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = maxSize;
    }

    public int size() {
        lock.lock();
        try {
            return deque.size();
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return deque.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public void put(T... ts) {
        if (ts == null) throw new NullPointerException();
        lock.lock();
        try {
            for (T t : ts) {
                while (deque.size() >= capacity) {
                    notFull.await();
                }
                deque.addLast(t);
                notEmpty.signalAll(); // 唤醒所有等待的消费者
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            throw new RuntimeException("Interrupted during put", e);
        } finally {
            lock.unlock();
        }
    }

    public void add(T... ts) {
        put(ts);
    }

    public List<T> take(int batchSize) throws InterruptedException {
        if (batchSize <= 0) throw new IllegalArgumentException("Batch size must be positive");
        lock.lockInterruptibly();
        try {
            // 等待队列中有足够元素或至少有一个元素
            while (deque.size() < Math.min(batchSize, 1)) {
                notEmpty.await();
            }

            List<T> batch = new ArrayList<>(Math.min(batchSize, deque.size()));
            int count = 0;
            while (!deque.isEmpty() && count < batchSize) {
                batch.add(deque.removeFirst());
                count++;
            }

            // 唤醒所有生产者，避免在批量消费后有生产者仍在等待
            if (count > 0) {
                notFull.signalAll();
            }

            return batch;
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (deque.isEmpty()) {
                notEmpty.await();
            }
            T item = deque.removeFirst();
            notFull.signal(); // 只需要唤醒一个生产者
            return item;
        } finally {
            lock.unlock();
        }
    }

    // 兼容老写法
    public T read() {
        try {
            return take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null; // 保持原有行为，但建议调用者检查中断状态
        }
    }

    // 新增方法：非阻塞获取元素
    public T poll() {
        lock.lock();
        try {
            return deque.pollFirst();
        } finally {
            lock.unlock();
        }
    }

    // 新增方法：带超时的获取元素
    public T poll(long timeoutMillis) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            if (deque.isEmpty()) {
                notEmpty.awaitNanos(timeoutMillis * 1_000_000);
                if (deque.isEmpty()) return null;
            }
            T item = deque.removeFirst();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}