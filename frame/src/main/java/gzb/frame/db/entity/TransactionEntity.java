package gzb.frame.db.entity;

import gzb.frame.db.DataBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionEntity {
    //数据库事务是否开启 null关闭 1开启 2模拟
    public Integer simulate;
    public  Map<String, List<Object[]>> data;

    public TransactionEntity(Integer simulate) {
        this.simulate = simulate;
        data = new HashMap<>();
    }
}
