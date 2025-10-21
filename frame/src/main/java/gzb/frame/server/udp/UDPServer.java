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

import gzb.frame.server.udp.entity.Packet;
import gzb.tools.Config;
import gzb.tools.Queue;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.ThreadPool;

import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UDPServer {
    public ThreadPool threadPool;
    //public BlockingQueue<Packet> waitHandle = new LinkedBlockingQueue<>(10000*20);
    //public BlockingQueue<Packet> waitSend = new LinkedBlockingQueue<>(10000*20);
    public Queue<Packet> waitHandle = new Queue<>();
    public Queue<Packet> waitSend = new Queue<>();
    public Map<String, Packet> waitConfirm = new ConcurrentHashMap<>();
    public DatagramSocket serverSocket;
    public UDP udp = new UDP();
public static Log log= Log.log;
    public void start(int port) {
        start(port, 0, 0);
    }

    public void start(int port, int handleThreadMinNum, int handleThreadMaxNum) {
        int num = Tools.getCPUNum();
        if (handleThreadMinNum == 0) {
            handleThreadMinNum = num * 3;
        }
        if (handleThreadMaxNum == 0) {
            handleThreadMaxNum = num * 10;
        }
        threadPool = new ThreadPool();

        try {
            serverSocket = new DatagramSocket(port);
            log.i("start UDP[" + port + "]....");
            threadPool.startService(num, "ServerRead", new ServerRead(this));
            threadPool.startService(num, "ServerSend", new ServerSend(this));
            threadPool.execute(  new ServerHandle(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class ServerSend extends Thread {
    public UDPServer server;

    public ServerSend(UDPServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<Packet> list = server.waitSend.take(100);
                for (Packet packet : list) {
                    try {
                        packet.setType(1);
                        server.udp.send(server.serverSocket, packet);
                    } catch (Exception e) {
                        server.waitSend.add(packet);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ServerHandle extends Thread {
    public UDPServer server;

    public ServerHandle(UDPServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<Packet> list = server.waitHandle.take(100);
                for (Packet packet : list) {
                    try {
                        //这里是时机业务类后边可能会通过反射调用对应控制器
                        packet.setBytes(("server " + new String(packet.getBytes())).getBytes());
                        server.waitSend.put(packet);
                    } catch (Exception e) {
                        server.waitSend.add(packet);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ServerRead extends Thread {
    public UDPServer server;

    public ServerRead(UDPServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Packet packet = server.udp.read(server.serverSocket);
                if (packet != null) {
                    packet.setType(1);
                    server.waitHandle.put(packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

