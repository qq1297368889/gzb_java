package gzb.tools.cache.object;


import java.util.Arrays;

public class ObjectCache {

    public static class Entity {
        int max = 50; //禁止嵌套太多  大概是50kb 内存 或许可以考虑加大 暂定50
        int buff_size = 1024;
        private StringBuilder[] stringBuilders = new StringBuilder[max];
        private int[] states = new int[max];

        public int open() {
            for (int i = 0; i < states.length; i++) {
                if (states[i] == 0) {
                    states[i] = 1;
                    return i;
                }
            }
            return -1;
        }

        public void close(int index) {
            if (index < 0) {//不对大值容错 传递异常值说明使用错误早点暴漏问题并非坏事
                return;
            }
            states[index] = 0;
            stringBuilders[index].setLength(0);//按规则调用他不可能是null 不做容错
        }

        public StringBuilder get(int index) {
            if (index < 0) {//超出max 不在缓存范围内 直接返回新对象
                return new StringBuilder(128);
            }
            if (stringBuilders[index] == null) {
                stringBuilders[index] = new StringBuilder(buff_size);
            }
            return stringBuilders[index];//返回引用
        }

        //框架兜底 考虑是否使用 他是开销 但是或许有必要
        public void closeAll() {
            Arrays.fill(states, 0);
        }
    }

    public static final ThreadLocal<ObjectCache.Entity> SB_CACHE0 = ThreadLocal.withInitial(ObjectCache.Entity::new);

}
