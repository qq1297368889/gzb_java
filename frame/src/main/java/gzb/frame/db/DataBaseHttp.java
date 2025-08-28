package gzb.frame.db;

import gzb.entity.EntityClassInfo;
import gzb.entity.SqlTemplate;
import gzb.entity.TableInfo;
import gzb.tools.GzbMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DataBaseHttp implements DataBase {
    @Override
    public Object[] getDefObjectArr() {
        return new Object[0];
    }

    @Override
    public List<TableInfo> getTableInfo() throws Exception {
        return null;
    }

    @Override
    public EntityClassInfo getEntityInfo(Object t) {
        return null;
    }

    @Override
    public SqlTemplate toSelect(String tableName, String[] fields, String[] symbol, String[] value, String[] montage, String sortField, String sortType) {
        return null;
    }

    @Override
    public SqlTemplate toSelect(Object t, int page, int size, String sortField, String sortType) {
        return null;
    }

    @Override
    public SqlTemplate toSave(Object t) throws Exception {
        return null;
    }

    @Override
    public SqlTemplate toSave(Object t, boolean autoId) throws Exception {
        return null;
    }

    @Override
    public SqlTemplate toUpdate(Object t) {
        return null;
    }

    @Override
    public SqlTemplate toDelete(Object t) {
        return null;
    }

    @Override
    public String getOnlyIdDistributedString() {
        return null;
    }

    @Override
    public Long getOnlyIdDistributed() {
        return null;
    }

    @Override
    public int getMaxId(String mapName, String idName) {
        return 0;
    }

    @Override
    public int getOnlyIdNumber(String mapName, String idName) {
        return 0;
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void close(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement) {

    }

    @Override
    public List<GzbMap> selectGzbMap(String sql) throws Exception {
        return null;
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Connection connection) throws Exception {
        return null;
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects) throws Exception {
        return null;
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects, Connection connection) throws Exception {
        return null;
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects, boolean hump) throws Exception {
        return null;
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects, boolean hump, Connection connection) throws Exception {
        return null;
    }

    @Override
    public int runSql(String sql) throws Exception {
        return 0;
    }

    @Override
    public int runSql(String sql, Connection connection) throws Exception {
        return 0;
    }

    @Override
    public int runSql(String sql, Object[] para) throws Exception {
        return 0;
    }

    @Override
    public int runSql(String sql, Object[] para, Connection connection) throws Exception {
        return 0;
    }

    @Override
    public int runSqlBatch(String sql, List<Object[]> list_parameter) throws Exception {
        return 0;
    }

    @Override
    public int runSqlBatch(String sql, List<Object[]> list_parameter, Connection connection) throws Exception {
        return 0;
    }

    @Override
    public int runSqlAsync(String sql, Object[] para) {
        return 0;
    }

    @Override
    public int runSqlAsyncBatch(String sql, List<Object[]> list_parameter) {
        return 0;
    }
}
