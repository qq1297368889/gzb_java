package gzb.tools.cache.object;

import java.util.*;

public class MapCache {

    public static int cache_num = 50; //禁止嵌套太多
    public static int buff_size_def = 1024;
    public static int buff_size_min = 128;
    public static int buff_size_max = 1024 * 20;

    public static class Entity {
        public Map<?,?>[] objects =null;
        public int[] states =null;

        public int open() {
            int index = -1;
            if (states == null) {
                states = new int[1];
                objects = new Map<?,?>[1];
                index = 0;
            } else {
                for (int i = 0; i < states.length; i++) {
                    if (states[i] == 0) {
                        index = i;
                        break;
                    }
                }
                if (index == -1 && states.length < cache_num) {
                    int[] states1 = new int[states.length + 1];
                    Map<?,?>[] objects1 = new Map<?,?>[states.length + 1];
                    System.arraycopy(states, 0, states1, 0, states.length);
                    System.arraycopy(objects, 0, objects1, 0, objects.length);
                    states = states1;
                    objects = objects1;
                    index = states.length - 1;
                }
            }
            states[index] = 1;
            return index;
        }
        public <T>T get(int index) {
            if (index < 0) {//超出max 不在缓存范围内 直接返回新对象
                return (T)new HashMap<>(buff_size_min);
            }
            if (objects[index] == null) {
                objects[index] = new HashMap<>(buff_size_def);
            }
            return (T) objects[index];//返回引用
        }

        public void close(int index) {
            if (index < 0) {
                return;
            }
            if (objects[index].size() > buff_size_max) {
                objects[index] = null;
            } else {
                objects[index].clear();
            }
            states[index] = 0;
        }

        //框架兜底 考虑是否使用 他是开销 但是或许有必要
        public void closeAll() {
            Arrays.fill(states, 0);
        }
    }


}
