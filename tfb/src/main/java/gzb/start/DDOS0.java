package gzb.start;

import gzb.frame.DDOS;
import gzb.frame.DDOSV2;
import gzb.tools.Tools;
import gzb.tools.http.HTTPV2;

import java.util.concurrent.ThreadLocalRandom;

public class DDOS0 {

    public static void main(String[] args) {
        String url="http://127.0.0.1:8080/fortunes";
        String met="GET";
        String data="";
        DDOSV2 ddosv2 = new DDOSV2(1, 10000 * 1000) {
            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public String getMet() {
                return met;
            }

            @Override
            public String getBody() {
                return data;
            }

            @Override
            public boolean ver(byte[] res) {
                if (res==null || res.length==0) {
                    return false;
                }
                return new String(res).contains("id");
            }
        };
        ddosv2.start();
    }
}
