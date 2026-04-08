package gzb.frame.db.v2;

public interface Async <T>{
    public Async<T> success();
    public Async<T> fail(T t);
}
