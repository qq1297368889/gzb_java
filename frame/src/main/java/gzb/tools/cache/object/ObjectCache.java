package gzb.tools.cache.object;

import java.util.Arrays;

public class ObjectCache {

    public static int cache_num = 50; //禁止嵌套太多
    public static int buff_size_def = 1024;
    public static int buff_size_min = 128;
    public static int buff_size_max = 1024 * 20;

    public static class Entity {
        private Object[] objects = new Object[cache_num];
        private int[] states = new int[cache_num];

        /**
         * 申请一个预分配的对象
         * 该方法调用后 必须调用close 否则会出现意外情况
         */
        public int open() {
            for (int i = 0; i < states.length; i++) {
                if (states[i] == 0) {
                    states[i] = 1;
                    return i;
                }
            }
            return -1;

        }

        public StringBuilder get(int index) {
            if (index < 0) {//超出max 不在缓存范围内 直接返回新对象
                return new StringBuilder(buff_size_min);
            }
            if (objects[index] == null) {
                objects[index] = new StringBuilder(buff_size_def);
            }
            return (StringBuilder) objects[index];//返回引用
        }

        //不能混用 getByteBuff get  不过我自己用的 我自己知道
        public ByteBuff getByteBuff(int index) {
            if (index < 0) {//超出max 不在缓存范围内 直接返回新对象
                return new ByteBuff(buff_size_min);
            }
            if (objects[index] == null) {
                objects[index] = new ByteBuff(buff_size_def);
            }
            return (ByteBuff) objects[index];//返回引用
        }

        public void close(int index) {
            if (index < 0) {//不对大值容错 传递异常值说明使用错误早点暴漏问题并非坏事
                return;
            }
            if (objects[index].getClass() == StringBuilder.class) {
                StringBuilder stringBuilder = (StringBuilder) objects[index];
                if (stringBuilder.length() > buff_size_max) {
                    objects[index] = null;
                } else {
                    stringBuilder.setLength(0);
                }
            } else if (objects[index].getClass() == ByteBuff.class) {
                ByteBuff byteBuff = (ByteBuff) objects[index];
                if (byteBuff.getMemorySize() > buff_size_max) {
                    objects[index] = null;
                } else {
                    byteBuff.setIndex(0);
                }
            }
            states[index] = 0;
        }

        //框架兜底 考虑是否使用 他是开销 但是或许有必要
        public void closeAll() {
            Arrays.fill(states, 0);
        }
    }

    /// 针对高频场景这个必要性非常大
    public static final ThreadLocal<ObjectCache.Entity> SB_CACHE0 = ThreadLocal.withInitial(ObjectCache.Entity::new);
    public static final ThreadLocal<ObjectCache.Entity> SB_BYTE_BUFF = ThreadLocal.withInitial(ObjectCache.Entity::new);

}
