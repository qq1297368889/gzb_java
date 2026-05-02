package gzb.sdk;

import java.io.IOException;
import java.util.List;

public interface CacheSdk {
     byte[] sendSync(String url, String params);

     void sendSync(String url, String params, BaseSdk.Call call);

     void sendSync(String url, String params, BaseSdk.Call call, boolean flush) ;
     List<byte[]> sendSync(String url, String params, int pip);
     boolean ping() throws IOException;

     boolean put(String key, String val, int sec) throws IOException;

     boolean del(String... key) throws IOException ;

     String get(String... key) throws IOException ;

     List<String> getAll(String... key) throws IOException ;

     List<String> get(int pip, String key) throws IOException;

     boolean produce(String data) throws IOException ;

     BaseSdk.Message consume(int time) throws IOException ;


     String confirm(long id) throws IOException;

     void close();
}
