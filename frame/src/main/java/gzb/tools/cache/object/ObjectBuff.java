package gzb.tools.cache.object;
/// 环形 缓冲区有些缺陷感觉无法接收  搁置
public class ObjectBuff {
    Object[]objects;
    int this_index=0;
    int size=10;
    public ObjectBuff(int size){
        objects=new Object[size];
    }
}
