package gzb.frame.factory.v5;

import java.util.List;
import java.util.Map;

public class TypeConversion {
    public void send(String msg){

    }
    public String parseString(List<Object>list,boolean notNull){
        if (list==null||list.size()==0) {
            if (notNull) {
                send("参数不允许为null");
            }
            return null;
        }
        return null;
    }
}
