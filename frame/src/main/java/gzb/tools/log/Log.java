package gzb.tools.log;

public interface Log {
    public void print(int index, Object... log);
    public void d(Object... log);
    public void i(Object... log);
    public void w(Object... log);
    public void e(Object... log);
    public void t(Object... log);
    public void s(String sql,long start,long end);
}
