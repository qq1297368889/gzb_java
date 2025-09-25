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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class OnlyId {
    private static long serverName;
    private static long subId;
    private static long subTime;
    static {
        serverName = Config.getInteger("gzb.system.server.name", 1);
        subId = 0;
        subTime = 0;
    }

    public static final long workerIdBits = 10L;
    public static final long sequenceBits = 12L;
    public static final long workerIdShift = sequenceBits;
    public static final long timestampShift = workerIdBits + sequenceBits;
    public static final long maxSequence = ~(-1L << sequenceBits);
    public static final long epoch = 1735689600000L;

    // 位数配置
    public static Long getDistributed(String key) {
        long currentTimestamp = 0;
        Lock lock = LockFactory.getLock(key);
        lock.lock();
        try {
            currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp < subTime) {
                throw new RuntimeException("时间回退");
            }
            if (currentTimestamp == subTime) {
                subId = (subId + 1) & maxSequence;
                if (subId == 0) {
                    // 序列号溢出，等待下一毫秒
                    while (currentTimestamp <= subTime) {
                        currentTimestamp = System.currentTimeMillis();
                    }
                }
            } else {
                subId = 0L;
            }
            subTime = currentTimestamp;
        } finally {
            lock.unlock();
        }
        // 使用位运算组合ID
        return ((currentTimestamp - epoch) << timestampShift) |
                (serverName << workerIdShift) |
                subId;
    }


    public static String getDistributedString() {
        return getDistributedString("OnlyId-getDistributed");
    }

    public static Long getDistributed() {
        return getDistributed("OnlyId-getDistributed");
    }


    public static String getDistributedString(String key) {
        return getDistributed(key).toString();
    }


}
