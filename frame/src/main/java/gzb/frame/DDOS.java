/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame;

import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.http.HTTP;
import gzb.tools.http.HTTP_V3;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DDOS {
    static AtomicInteger start_ddos = new AtomicInteger(0);//线程启动数量  已准备好

    public static void main(String[] args) {
        String url;
        byte[] bytes;

        //http://127.0.0.1:8080/test/test1?sysUsersAcc=n01
        //http://127.0.0.1:2082/test/test1?sysUsersAcc=n01

        url = "http://127.0.0.1:2081/test/test1?sysUsersAcc=n01";
        //url = "http://127.0.0.1:2082/test/test1?p1=1&p2=2&p3=3.1&p4=4&p5=5&p6=6&p7=7&p8=8&p9=9&p10=10&p11=11&p12=12&p13=13";
        bytes = "n01".getBytes(Config.encoding);//   sysUsersAcc  name msg
        //start("gzb-zero web框架压测", 6, url, 0, null, 10000 * 1000, bytes);


    }

    /**
     * 测试api正确性
     *
     * @param url         请求地址
     * @param met         请求类型 GET POST PUT DELETE
     * @param data        请求参数
     * @param headers     请求协议头
     * @param files       请求需要上传的文件
     * @param successData 如果API响应包含该值 视为成功
     * @return 测试结果 true or false
     */
    public static boolean testApi(String url, String met, String data, Map<String, String> headers, Map<String, List<File>> files, byte[] successData) {

        byte[] bytes = null;
        try {
            bytes = new HTTP_V3().request(url, met, data, headers, files, 10000L).asByte();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bytes == null) {
            return false;
        }
        boolean res = Tools.bytesContains(bytes, successData);
        System.out.println(res + ":" + url + " -> " + data + " -> " + Arrays.toString(bytes));
        return res;
    }

    public static void start(String name, int threadNum, String url, String met, String data, Map<String, String> headers, Map<String, List<File>> files, int requestNum, byte[] successData) {
        AtomicLong start_ok = new AtomicLong(0);//线程启动数量  已准备好
        AtomicLong this_success_request_num = new AtomicLong(0);//请求次数 成功
        AtomicLong this_fail_request_num = new AtomicLong(0);//请求次数 失败
        AtomicLong this_request_time_all = new AtomicLong(0);//微秒 耗时 汇总
        if (name == null) {
            name = "test_" + System.nanoTime();
        }
        System.out.println("任务名称 " + name + " 线程数量 " + threadNum + " 压测总次数 " + requestNum
                + " url " + url + " met " + met + " data " + data
                + " 成功响应包含 " + Arrays.toString(successData));
        List<AtomicLong> list0 = new CopyOnWriteArrayList<>(); //微秒 最大请求耗时
        for (int i = 0; i < threadNum; i++) {
            list0.add(new AtomicLong(0));
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    HTTP_V3 http = new HTTP_V3();
                    byte[] bytes = null;
                    try {
                        bytes = http.request(url, met, data, headers, files, 10000L).asByte();
                        System.out.println(finalI + " 线程名" + " " + getName() + " " +
                                "预请求结果内容：" + Arrays.toString(bytes)
                                + "请求是否包含successData：" + Tools.bytesContains(bytes, successData));
                        start_ok.incrementAndGet(); // 原子递增
                        while (start_ddos.get() == 0) {
                            Tools.sleep(1);
                        }
                        for (int i1 = 0; i1 < requestNum; i1++) {
                            if (start_ddos.get() == 0) {
                                break;
                            }
                            long s01 = System.nanoTime();
                            bytes = http.request(url, met, data, headers, files, 10000L).asByte();
                            long e01 = System.nanoTime();
                            long res = (e01 - s01);
                            if (res > list0.get(finalI).get()) {
                                list0.get(finalI).addAndGet(res);
                            }
                            this_request_time_all.addAndGet(res);//微秒
                            if (Tools.bytesContains(bytes, successData)) {
                                this_success_request_num.incrementAndGet();
                            } else {
                                this_fail_request_num.incrementAndGet();
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //System.out.println(finalI+" "+"线程名"+" "+getName()+" "+ "压测完成");
                }
            }.start();
        }
        int mm = 0;
        long all_num_lao = 0;
        long all_time_lao = 0;
        long suc_num_lao = 0;
        long fail_num_lao = 0;

        long max_max = 0;
        while (true) {
            if (start_ok.get() == threadNum) {
                System.out.println("线程启动完毕 0.1秒后开始请求");
                Tools.sleep(100);
                start_ddos.addAndGet(1);
                break;
            }
        }
        while (true) {
            long cz = 0L;
            long t001 = System.currentTimeMillis();
            do {
                Tools.sleep(1);
                cz = System.currentTimeMillis() - t001;
            } while (cz < 1000);

            long max = 0;
            for (AtomicLong atomicLong : list0) {
                long num = atomicLong.getAndSet(0);
                if (num > max) {
                    max = num;
                }
            }
            if (max > max_max) {
                max_max = max;
            }
            long all_time_this = this_request_time_all.get();
            long suc_num = this_success_request_num.get();
            long fail_num = this_fail_request_num.get();
            long all_num_this = suc_num + fail_num;

            mm++;
            String str = "";
            str += "进度" + mm + "秒,";
            str += "精确时间" + cz + "毫秒,";

            str += "平均(全局)" + (all_num_this / mm) + "次,";
            str += "成功(全局)" + suc_num + "次,";
            str += "失败(全局)" + fail_num + "次,";
            str += "请求(全局)" + all_num_this + "次,";

            str += "成功(最近)" + "=" + (suc_num - suc_num_lao) + "次,";
            str += "失败(最近)" + (fail_num - fail_num_lao) + "次,";
            str += "请求(最近)" + (all_num_this - all_num_lao) + "次,";

            str += "汇总耗时(最近)" + ((all_time_this - all_time_lao) / 1000) + "微秒,";
            if (all_num_this - all_num_lao>0 && all_time_this - all_time_lao>0) {
                str += "平均耗时(最近)" + ((all_time_this - all_time_lao) / (all_num_this - all_num_lao) / 1000) + "微秒,";
            }else{
                str += "平均耗时(最近)0微秒,";
            }

            str += "最大耗时(最近)" + (max / 1000) + "微秒,";
            if (all_num_this>0) {
                str += "平均耗时(全局)" + (all_time_this / all_num_this / 1000) + "微秒,";
            }

            str += "汇总耗时(全局)" + (all_time_this / 1000) + "微秒,";
            str += "最大耗时(全局)" + (max_max / 1000) + "微秒";
            System.out.println(str);
            if (all_num_this >= requestNum) {
                start_ddos.set(0);
                break;
            }
            all_num_lao = all_num_this;
            all_time_lao = all_time_this;
            suc_num_lao = suc_num;
            fail_num_lao = fail_num;
        }
        System.out.println("运行完毕");
    }
}
