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

package gzb.frame.server.udp;

import gzb.tools.Config;
import gzb.tools.Tools;

import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    public static void main(String[] args) throws Exception {
        long start=System.currentTimeMillis();
        UDPServer server = new UDPServer();
        server.start(1088);
        int num = 36;Tools.getCPUNum();

        AtomicInteger suc = new AtomicInteger(0);
        for (int i = 0; i < num; i++) {
            Thread thread =new Thread(){
                @Override
                public void run() {
                    DatagramSocket client = null;
                    try {
                        client=new DatagramSocket(0);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i1 = 0; i1 < 10000; i1++) {
                        try {
                            UDP.send(client,"127.0.0.1",1088, (Tools.getRandomString(32)+(i1+1)).getBytes(Config.encoding));
                            //UDP.send(client,"123.60.94.141",2600, (Tools.getRandomString(18)+(i1+1)).getBytes(Config.encoding));
                            //System.out.println(new String(UDP.read(client).getBytes(Config.encoding)));
                            suc.incrementAndGet(); // 原子递增
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    long end=System.currentTimeMillis();
                    System.out.println("send end "+(end-start) );
                    //incoming a new work connection for udp proxy, 123.60.94.141:7000
                }
            };
            thread.start();
        }
        int t00=-1;
        int t01=-1;
        int mm=0;
        System.out.println("waitHandle1 "+server.waitHandle.size());
        System.out.println("waitSend1 "+server.waitSend.size());
        while (true){
            mm++;
            int t03=server.waitHandle.size();
            if (t00!=t03){
                t00=t03;
                long end=System.currentTimeMillis();
                System.out.println("waitHandle "+(end-start) +" "+t03+" "+(num));
            }
            int t02=server.waitSend.size();
            if (t01!=t02){
                t01=t02;
                long end=System.currentTimeMillis();
                System.out.println("waitSend "+(end-start) +" "+t02+" "+(num));
            }
            System.out.println("suc "+(suc.get()) +"   mm:"+mm);

            Thread.sleep(1000);
        }

    }
}
