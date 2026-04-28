package gzb.frame.netty.action.cache.tools;

import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;

public class ResponseData {
    public static String send(long sid, int code, Object...data) {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        int index = entity.stringBuilderCacheEntity.open();
        try {
            StringBuilder sb = entity.stringBuilderCacheEntity.get(index);
            sb.append(sid).append(",");
            sb.append(code);
            if (data != null) {
                for (Object datum : data) {
                    if (datum==null) {
                        sb.append(",");
                        sb.append("null");
                    }else if (datum instanceof String){
                        sb.append(",");
                        sb.append(datum);
                    }else{
                        sb.append(",");
                        sb.append(Tools.toJson0(datum));
                    }
                }
            }
            return sb.toString();
        } finally {
            entity.stringBuilderCacheEntity.close(index);
        }
    }

    public static String send(long sid, int code) {

        return send(sid, code, (Object[]) null);
    }
}
