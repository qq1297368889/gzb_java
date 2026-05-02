package com.system.tools;

import gzb.tools.Tools;
import gzb.tools.thread.GzbThreadLocal;

public class ResponseData {
    public static String send(long sid, int code, Object... data) {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        int index = entity.stringBuilderCacheEntity.open();
        try {
            StringBuilder sb = entity.stringBuilderCacheEntity.get(index);
            sb.append(sid);
            sb.append(",");
            sb.append(code);
            sb.append(",");
            if (data != null) {
                for (Object datum : data) {
                    if (datum == null) {
                        sb.append("4,null");
                        sb.append(",");
                    } else if (datum.getClass() == String.class) {
                        sb.append(((String) datum).length());
                        sb.append(",");
                        sb.append(datum);
                        sb.append(",");
                    } else if (datum.getClass() == Long.class) {
                        sb.append(datum);
                        sb.append(",");
                    } else {
                        String data0 = Tools.toJson0(datum);
                        sb.append(data0.length());
                        sb.append(",");
                        sb.append(data0);
                        sb.append(",");
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
