package gzb.frame;

import gzb.tools.Tools;
import gzb.tools.http.HTTP_V3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DDOSV2 {

    Map<String, String> headers;
    Map<String, List<File>> files;
    long[][] res_all = null;
    int requestNum;
    boolean stop = true;
    AtomicInteger awaitNum = new AtomicInteger(0);

    //ArrayList<Long>[] distribution = null;

    public DDOSV2(int threadNum, int requestNum) {
        res_all = new long[threadNum][2];
        //distribution=new ArrayList[threadNum];
        this.requestNum = requestNum;
    }

    public boolean ver(byte[] res) {
        return res != null && res.length > 0;
    }

    public String getUrl() {
        return "";
    }

    public String getMet() {
        return "GET";
    }


    public String getBody() {
        return "";
    }

    public long getTimeOut() {
        return 1000 * 10;
    }

    public Map<String, List<File>> getFiles() {
        return files;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] request(boolean print0,HTTP_V3 http) {
        try {
            http.request(getUrl(), getMet(), getBody(), getHeaders(), getFiles(), getTimeOut());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = http.asByte();
        if (print0) {
            System.out.println(Arrays.toString(bytes));
        }
        return bytes;
    }

    public void startThread() {
        for (int i = 0; i < res_all.length; i++) {
            int finalI = i;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    //0成功  1失败 2请求耗时 3最大耗时
                    res_all[finalI] = new long[5];
                    res_all[finalI][0] = 0;
                    res_all[finalI][1] = 0;
                    res_all[finalI][2] = 0;
                    res_all[finalI][3] = 0;
                    res_all[finalI][4] = 0;
                    //distribution[finalI]=new ArrayList<>(requestNum/res_all.length*2);
                    HTTP_V3 http = new HTTP_V3();
                    request(true,http);
                    awaitNum.incrementAndGet();
                    while (stop) {
                        Tools.sleep(1);
                    }
                    while (true) {
                        long start = System.nanoTime();
                        if (ver(request(false,http))) {
                            res_all[finalI][0]++;
                        } else {
                            res_all[finalI][1]++;
                        }
                        long end = System.nanoTime();
                        long time = end - start;
                        res_all[finalI][2] += time;
                        if (time > res_all[finalI][3]) {
                            res_all[finalI][3] = time;
                        }
                        //time = time / 1000 / 1000;
                        //记录分布
                        //distribution[finalI].add(time);
                        if (stop) {
                            break;
                        }
                    }
                    System.out.println("线程结束:" + getName());
                }
            };
            thread.setName("t-" + i);
            thread.start();
        }
    }

    public void print(int sec,long time, long success, long error, long allTime, long maxTime) {
        if (sec<1) {
            sec=1;
        }
        long qps1=success / sec;
        long qps2=error / sec;

        System.out.println("sec:" + sec + " "+time+"ms ok:" +  (qps1) + "/s no:" +  (qps2) + "/s time:" + (allTime / (success+error)/1000) + "us max time:" + (maxTime/1000)+"us");
    }

    public void start() {
        startThread();
        while (true) {
            if (awaitNum.get() == res_all.length) {
                break;
            }
            Tools.sleep(1);
        }
        Tools.sleep(1000);
        stop = false;

        long[] res = new long[10];
        int sec = 0;
        //0成功  1失败 2请求耗时 3最大耗时
        long start = System.currentTimeMillis();
        while (true) {
            sec++;
            Tools.sleep(800);
            long time0=System.currentTimeMillis();
            while (time0 - start < 1000) {
                Tools.sleep(1);
                time0=System.currentTimeMillis();
            }
            long time=time0 - start;
            start = System.currentTimeMillis();
            for (int i = 0; i < res_all.length; i++) {
                long[] longs=res_all[i];
                res[0] += longs[0];
                res[1] += longs[1];
                res[2] += longs[2];
                if (longs[3] > res[3]) {
                    res[3] = longs[3];
                }
                longs[3] = 0;
            }
            print(sec, time,res[0], res[1], res[2], res[3]);
            if (res[0]+res[1]>=requestNum) {
                stop = true;
                Tools.sleep(1000);
                print(sec,time,  res[0], res[1], res[2], res[3]);
                break;
            }

            if (sec==5) {//长时间的话尝试忽略前期数据
                for (int i = 0; i < res_all.length; i++) {
                    long[] longs=res_all[i];
                    longs[0] = 0;
                    longs[1] = 0;
                    longs[2]= 0;
                }
                sec=0;
                System.out.println("新批次------");
            }
            Arrays.fill(res,0);
        }

    }
}
