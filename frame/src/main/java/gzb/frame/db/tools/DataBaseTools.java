package gzb.frame.db.tools;

import gzb.frame.db.v2.BaseDao;
import gzb.frame.db.v2.DataBase;
import gzb.tools.thread.GzbThreadLocal;

import java.util.ArrayList;
import java.util.List;

public class DataBaseTools {
    public static List<DataBase> readList(Object...dao){
        if (dao==null||dao.length==0) {
            return new ArrayList<>(0);
        }
        List<DataBase> dataBasesList = new ArrayList<>(dao.length);
        for (int i = 0; i < dao.length; i++) {
            if (dao[i] instanceof BaseDao) {
                DataBase dataBase=((BaseDao<?>) dao[i]).getDataBase();
                boolean next=true;
                for (int n = 0; n < dataBasesList.size(); n++) {
                    if (dataBasesList.get(n).dataBaseConfig.getSign().equals(dataBase.dataBaseConfig.getSign())) {
                        next=false;
                        break;
                    }
                }
                if (next) {
                    dataBasesList.add(dataBase);
                }
            }
        }
        return dataBasesList;
    }
    public static void openTransaction(List<DataBase> list, GzbThreadLocal.Entity entity, boolean simulate){
        for (DataBase dataBase : list) {
            dataBase.openTransaction(entity,simulate);
        }
    }
    public static void endTransaction(List<DataBase> list, GzbThreadLocal.Entity entity){
        for (DataBase dataBase : list) {
            dataBase.endTransaction(entity);
        }
    }
    public static void commitTransaction(List<DataBase> list, GzbThreadLocal.Entity entity){
        for (DataBase dataBase : list) {
            dataBase.commit(entity);
        }
    }
    public static void rollbackTransaction(List<DataBase> list, GzbThreadLocal.Entity entity){
        for (DataBase dataBase : list) {
            dataBase.rollback(entity);
        }
    }



}
