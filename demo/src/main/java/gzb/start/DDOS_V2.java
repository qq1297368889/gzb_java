package gzb.start;

import gzb.frame.DDOSV2;
import gzb.tools.Tools;

public class DDOS_V2 {

    public static void main(String[] args) {
        String url="http://192.168.10.101:2080/test/api0/get1";
        String met="GET";
        String data="";
        DDOSV2 ddosv2 = new DDOSV2(1, 10000 * 1000) {
            //被多线程调用所以需要这样
            final ThreadLocal<Integer>data0=new ThreadLocal<Integer>();
            @Override
            public String getUrl() {
                int data1=Tools.getRandomInt(10000,99999);
                data0.set(data1);
                return url+"?message="+ data1;
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
                return new String(res).contains(data0.get().toString());
            }
        };
        ddosv2.start();
    }
}
