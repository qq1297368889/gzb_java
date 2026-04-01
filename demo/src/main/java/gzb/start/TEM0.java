package gzb.start;

import com.frame.controller.SysFileController;
import com.frame.dao.SysFileDao;
import gzb.frame.db.DataBase;
import gzb.frame.factory.ClassTools;
import gzb.frame.factory.GzbOneInterface;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

import java.util.List;
import java.util.Map;

public class TEM0 extends SysFileController implements GzbOneInterface {

    @Override
    public Object _gzb_call_x01(int _gzb_one_c_id, Map<String, Object> _gzb_one_c_mapObject, Request _g_p_req, Response _g_p_resp, Map<String, List<Object>> _gzb_one_c_requestMap, GzbJson _g_p_gzbJson, Log _g_p_log, Object[] arrayObject) throws Exception {
        return null;
    }

    SysFileDao sysFileDao = null;

    public Object _gzb_call_x02(int id,
                                Map<String, Object> object,
                                Request req,
                                Response res,
                                Map<String, List<Object>> parar,
                                GzbJson json,
                                Log _g_p_log,
                                Object[] arrayObject) throws Exception {
        Object object_return = null;
        java.util.List<Object> t_map_list = null;
        switch (id) {
            case 0: {
                GzbJson result = null;
                String[] field = null;
                String[] symbol = null;
                String[] value = null;
                String[] montage = null;
                String sortField = null;
                String sortType = null;
                Integer page = null;
                Integer limit = null;
                result = json;
                if (sortField == null) {
                    //简单对象
                    t_map_list = parar.get("b");            //如果不是数组 则这样输出
                    if (t_map_list != null && t_map_list.size() > 0 && t_map_list.get(0) != null) {
                        sortField = java.lang.String.valueOf(t_map_list.get(0) instanceof String ? (String) t_map_list.get(0) : t_map_list.get(0).toString());
                    }
                }
                /// 如果开启事务则输出这个
                DataBase dataBase = sysFileDao.getDataBase();
                dataBase.openTransaction(true);//是否模拟
                try {
                    return query(result, field, symbol, value, montage, sortField, sortType, page, limit);
                } catch (Throwable e) {
                    dataBase.rollback();
                    throw e;
                } finally {
                    dataBase.endTransaction();
                }


            }
        }
        ;
        return null;
    }
}
