/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.tools;

import gzb.tools.log.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Queue<T> {
    private final Deque<T> deque = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition producers = lock.newCondition();//生产者
    private final Condition consumer = lock.newCondition();//消费者

    private final int queue_size;

    public Queue() {
        queue_size = Integer.MAX_VALUE;
    }

    public Queue(int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.queue_size = maxSize;
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

    private void signal(Condition condition, int awaitNum) {
        if (awaitNum <= 0) return;
        if (awaitNum <= 3) {
            for (int i = 0; i < awaitNum; i++) {
                condition.signal();
            }
        } else {
            condition.signalAll();
        }
    }

    private void consumerAwait() throws InterruptedException {

        consumer.await();
    }

    private void producersAwait() throws InterruptedException {

        producers.await();
    }

    //自动背压
    public void add(T... ts) {
        if (ts == null) throw new NullPointerException();
        lock.lock();
        try {
            for (T t : ts) {
                while (deque.size() >= queue_size) {
                    producersAwait();
                }
                deque.addLast(t);
            }
            if (ts.length==1) {
                consumer.signal();
            }else if (ts.length>=1) {
                consumer.signalAll();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            throw new RuntimeException("Interrupted during put", e);
        } finally {
            lock.unlock();
        }
    }

    public boolean offer(T... ts) {
        if (ts == null) return false;
        lock.lock();
        try {
            if (deque.size() + ts.length >= queue_size) {
                return false;
            }
            for (T t : ts) {
                deque.addLast(t);
            }
            consumer.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }


    public List<T> readList(int batchSize) throws InterruptedException {
        if (batchSize <= 0) throw new IllegalArgumentException("Batch size must be positive");
        lock.lockInterruptibly();
        try {
            while (deque.isEmpty()) {
                consumerAwait();
            }
            List<T> batch = new ArrayList<>(batchSize);
            while (!deque.isEmpty() && batch.size() < batchSize) {
                batch.add(deque.removeFirst());
            }
            if (batch.size() > 0) {
                producers.signalAll();
            }
            return batch;
        } finally {
            lock.unlock();
        }
    }

    public T read() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (deque.isEmpty()) {
                consumerAwait();
            }
            T item = deque.removeFirst();
            producers.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public T poll(long time, TimeUnit timeUnit) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            if (deque.isEmpty()) {
                if (!consumer.await(time, timeUnit)) {
                    return null;
                }
                if (deque.isEmpty()) return null;
            }
            T t=deque.removeFirst();
            producers.signalAll();
            return t;
        } finally {
            lock.unlock();
        }
    }

    public T poll() {
        lock.lock();
        try {
            if (deque.isEmpty()) {
                return null;
            }
            T t=deque.removeFirst();
            producers.signalAll();
            return t;
        } finally {
            lock.unlock();
        }
    }
}