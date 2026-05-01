package gzb.tools.cache.object;

import java.util.Arrays;

public class ArrayBuffCache {

    public static int cache_num = 50; //禁止嵌套太多
    public static int buff_size_def = 1024;
    public static int buff_size_min = 128;
    public static int buff_size_max = 1024 * 20;

    public static class Entity {
        public ArrayBuff[] objects =null;
        public int[] states =null;
        /**
         * 申请一个预分配的对象
         * 该方法调用后 必须调用close 否则会出现意外情况
         */
        public int open() {
            int index=-1;
            if (states==null) {
                states=new int[1];
                objects=new ArrayBuff[1];
                index=0;
            }else{
                for (int i = 0; i < states.length; i++) {
                    if (states[i] == 0) {
                        index=i;
                        break;
                    }
                }
                if (index==-1 && states.length<cache_num) {
                    int[]states1=new int[states.length+1];
                    ArrayBuff[]objects1=new ArrayBuff[states.length+1];
                    System.arraycopy(states, 0, states1, 0, states.length);
                    System.arraycopy(objects, 0, objects1, 0, objects.length);
                    states=states1;
                    objects=objects1;
                    index= states.length-1;
                }
            }
            states[index]=1;
            return index;
        }

        public ArrayBuff get(int index) {
            if (index < 0) {//超出max 不在缓存范围内 直接返回新对象
                return  new ArrayBuff(buff_size_min);
            }
            if (objects[index] == null) {
                objects[index] = new ArrayBuff(buff_size_def);
            }
            return objects[index];//返回引用
        }

        public void close(int index) {
            if (index < 0) {
                return;
            }
            if (objects[index].length() > buff_size_max) {
                objects[index] = null;
            } else {
                objects[index].setLength(0);
            }
            states[index] = 0;
        }

        //框架兜底 考虑是否使用 他是开销 但是或许有必要
        public void closeAll() {
            Arrays.fill(states, 0);
        }
    }


}
