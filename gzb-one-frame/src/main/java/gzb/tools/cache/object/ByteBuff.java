package gzb.tools.cache.object;

import gzb.tools.log.Log;
import io.netty.util.ReferenceCounted;

/// 类非线程安全 如果哪个大聪明非要多线程共享 请加锁

/// 我设想的使用场景是存在于 线程空间 避免不必要的GC 复用内存 规避自动回收
public class ByteBuff {
    byte[] bytes;
    int this_index = 0;
    public ByteBuff(){
        this(128);
    }
    public ByteBuff(int size){
        bytes=new byte[size];
    }
    /// 读取所有数据
    public byte[] get() {
        return get(0, this_index);
    }

    /// 读取指定位置
    public byte[] get(int start, int _size) {
        if (start + _size > this_index) {
            Log.log.w("读取越界，返回NULL", "总长度", this_index, "起始位置", start, "读取长度", _size);
            return null;
        }
        byte[] bytes0 = new byte[_size];
        System.arraycopy(bytes, start, bytes0, 0, _size);
        return bytes0;
    }

    /// 写入到内存
    public ByteBuff write(byte[] data) {
        if (data == null) {
            return this;//忽略空的
        }
        return write(data, 0, data.length);
    }

    /// 写入到内存
    public ByteBuff write(byte[] data, int start, int size) {
        if (data == null) {
            return this;//忽略空的
        }
        int newLength = size + this_index;
        if (newLength > bytes.length) {
            byte[] bytes1 = new byte[newLength + 1024]; //扩容策略  无所谓 他是一次性开销 主要用于复用 而不是频繁创建 具体策略参考类介绍
            System.arraycopy(bytes, 0, bytes1, 0, bytes.length);
            bytes = bytes1; //扩容完成
        }
        System.arraycopy(data, start, bytes, this_index, size);//数组拷贝
        this_index += size;
        return this;
    }

    /// 初始化下标
    public ByteBuff setIndex(int index) {
        this_index = index;
        return this;
    }

    /// 重建缓冲区 根据阈值决定
    public ByteBuff setMemorySize(int size) {
        bytes = new byte[size];
        return this;
    }

    /// 内存大小
    public int getMemorySize() {
        return bytes.length;
    }

    /// 指针位置
    public int getThisIndex() {
        return this_index;
    }

}
