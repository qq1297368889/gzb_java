package gzb.frame.server;

import com.frame.dao.SysLogDao;
import com.frame.dao.impl.SysLogDaoImpl;
import com.frame.entity.SysLog;
import gzb.tools.DateTime;
import gzb.tools.Tools;
import gzb.tools.http.HTTPV2;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import org.apache.tools.ant.taskdefs.Sleep;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServerTest {
    static int open = 0;
    static int serverPid = 8700;

    public static void main(String[] args) throws Exception {

        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(ServerTest.class));
        String url1 = "http://127.0.0.1:2080/test/test1?name=lisi";
        String url2 = "http://127.0.0.1:8080/test/test1?name=lisi";
        action_test(300000, 3,
                20260, 27876,
                "自研框架", "spring",
                url1, url2);

    }

    private static final SystemInfo si = new SystemInfo();
    private static final OperatingSystem os = si.getOperatingSystem();
    private static final CentralProcessor processor = si.getHardware().getProcessor();

    public static int findPidByName(String processName) {
        List<OSProcess> processes = os.getProcesses();
        for (OSProcess process : processes) {
            if (process.getName().toLowerCase().contains(processName.toLowerCase())) {
                return process.getProcessID();
            }
        }
        return -1; // 未找到进程
    }

    /**
     * 获取指定进程的 CPU 占用率。
     *
     * @param pid 进程的 ID
     * @param mm 采样花费时间
     * @return 进程的 CPU 占用率，如果进程不存在则返回 -1
     */
    public static double getProcessCpuLoad(int pid, int mm) {
        // 第一次获取进程快照
        OSProcess process = os.getProcess(pid);
        if (process == null) {
            System.err.println("未找到指定 PID 的进程：" + pid);
            return -1.0;
        }

        long oldUptime = process.getUpTime();
        long oldKernelTime = process.getKernelTime();
        long oldUserTime = process.getUserTime();

        Tools.sleep(mm);
        // 第二次获取进程快照
        process = os.getProcess(pid);
        if (process == null) {
            // 如果进程在这段时间内结束了
            return -1.0;
        }

        long newUptime = process.getUpTime();
        long newKernelTime = process.getKernelTime();
        long newUserTime = process.getUserTime();

        // 计算时间差
        long upTimeDifference = newUptime - oldUptime;
        long cpuTimeDifference = (newKernelTime - oldKernelTime) + (newUserTime - oldUserTime);

        // 仅在时间差大于 0 时进行计算，避免除以零
        if (upTimeDifference > 0) {
            // CPU 占用率 = (进程 CPU 时间差 / 进程总运行时间差) * 100%
            double cpuLoad = (double) cpuTimeDifference * 100.0 / upTimeDifference;

            // 考虑多核情况，通常需要除以核心数
            double logicalProcessorCount = processor.getLogicalProcessorCount();
            return cpuLoad / logicalProcessorCount;
        }

        return 0.0;
    }
    /**
     * 测试dao层性能
     */
    public static void dao_test() throws Exception {
        Log log = new LogImpl();
        //必须传递路径给框架
        SysLogDao dao = new SysLogDaoImpl();
        SysLog sysLog = new SysLog();
        String time = new DateTime().toString();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            //随机获取一个16位字符串
            sysLog.setSysLogSql(Tools.getRandomString(16))
                    .setSysLogMs(Tools.getRandomLong(10000, 100))
                    .setSysLogTime(time);
            dao.save(sysLog);
        }
        long end = System.currentTimeMillis();
        log.d("插入 1000 单条 同步 耗时", (end - start) + "");

        List<SysLog> list = new ArrayList<>();
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            //随机获取一个16位字符串
            sysLog.setSysLogSql(Tools.getRandomString(16))
                    .setSysLogMs(Tools.getRandomLong(10000, 100))
                    .setSysLogTime(time);
            list.add(sysLog);
        }
        end = System.currentTimeMillis();
        log.d("批量插入 100000 单条 组装 耗时", (end - start) + "");
        start = System.currentTimeMillis();
        dao.saveBatch(list);
        end = System.currentTimeMillis();
        log.d("批量插入 100000 单条 插入 耗时", (end - start) + "");


        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            //随机获取一个16位字符串
            sysLog.setSysLogSql(Tools.getRandomString(16))
                    .setSysLogMs(Tools.getRandomLong(10000, 100))
                    .setSysLogTime(time);
            dao.saveAsync(sysLog);
        }
        end = System.currentTimeMillis();
        log.d("插入 100000 单条 异步 耗时", (end - start) + "", "实际耗时在后台");

        Tools.sleep(1000 * 10);//等待后台线程执行
    }

    /**
     * 公平压力测试对比
     *
     * @param testNum 压测请求次数
     * @param thrMax 压测线程数量
     * @param server1Pid 压测服务器1进程Pid
     * @param server2Pid 压测服务器2进程Pid
     * @param server1Name 压测服务器1名称
     * @param server2Name 压测服务器2名称
     * @param server1Url 压测服务器1请求地址
     * @param server2Url 压测服务器2请求地址
     */
    public static void action_test(
            int testNum, int thrMax,
            int server1Pid, int server2Pid
            , String server1Name
            , String server2Name
            , String server1Url
            , String server2Url) throws IOException, InterruptedException {
        if (server1Name==null) {
            server1Name="server1";
        }
        if (server2Name==null) {
            server2Name="server2";
        }
        if (testNum==0) {
            testNum=10000;
        }
        if (thrMax==0) {
            thrMax=1;
        }
        int reqNum0 = testNum / 10 * 3;
        HTTPV2 http = new HTTPV2();
        if (server1Url!=null) {
            System.out.println("这里是 " + server1Name + " 热身：" + reqNum0 + "次请求");
            pressureTest(server1Pid, thrMax, reqNum0, http, server1Url, null, 0, null, null, null);

            System.out.println("这里是 " + server1Name + " 实测：" + testNum + "次请求");
            pressureTest(server1Pid, thrMax, testNum, http, server1Url, null, 0, null, null, null);
        }
        if (server2Url!=null) {
            System.out.println("这里是 " + server2Name + " 热身：" + reqNum0 + "次请求");
            pressureTest(server2Pid, thrMax, reqNum0, http, server2Url, null, 0, null, null, null);
            System.out.println("这里是 " + server2Name + " 实测：" + testNum + "次请求");
            pressureTest(server2Pid, thrMax, testNum, http, server2Url, null, 0, null, null, null);
        }
    }

    public static void pressureTest(int serverPid, int threadNum, int requestNum, HTTPV2 http, String url, String data, int met, String uploadName, List<File> listFiles, Map<String, String[]> uploadData) throws InterruptedException {
        try {
            http.request(new URL(url), data, met, uploadName, listFiles, uploadData);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        AtomicInteger open = new AtomicInteger(0);
        AtomicInteger start = new AtomicInteger(0);
        AtomicInteger suc = new AtomicInteger(0);
        AtomicInteger err = new AtomicInteger(0);
        AtomicLong time = new AtomicLong(0);
        final long[] maxTime = {0};
        List<Thread> list = new LinkedList<>();
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    start.incrementAndGet(); // 原子递增
                    while (ServerTest.open == 0) {
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            byte[] bytes = null;
                            try {
                                long start = System.currentTimeMillis();
                                bytes = http.request(new URL(url), data, met, uploadName, listFiles, uploadData).toByte();

                                long end = System.currentTimeMillis();
                                if (bytes != null && bytes.length > 0 && new String(bytes).contains("\"code\":\"1\"")) {
                                    suc.incrementAndGet(); // 原子递增
                                } else {
                                    err.incrementAndGet();
                                }
                                long time0 = end - start;
                                if (time0 > maxTime[0]) {
                                    maxTime[0] = time0;
                                }
                                time.addAndGet(time0);
                            } catch (Exception e) {
                                err.incrementAndGet();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.setName("t-" + i);
            thread.start();
            list.add(thread);
        }
        int k = 0;
        int lao = 0;

        while (true) {
            if (start.get() == threadNum) {
                Thread.sleep(10);
                System.out.println("start->");
                ServerTest.open = 1;
                suc.set(0);
                break;
            }
            Thread.sleep(10);
        }
        System.out.println("测试地址:"+url);
        while (true) {
            k++;
            double num = 0.0;
            if (serverPid>0) {
                num = getProcessCpuLoad(serverPid, 1000);
            }else{
                Tools.sleep(1000);
            }
            int lao2 = suc.get();//统计 总请求数量
            System.out.println("服务器CPU占用:" + Tools.doubleTo2(num) + "%," +
                    "已耗时:" + k + "秒," +
                    "成功请求数:" + (lao2) + "次," +
                    "失败请求数:" + err.get() + "次," +
                    "平均每秒请求数:" + (lao2 / k) + "次," +
                    "平均耗时:" + (time.get() / (suc.get() + err.get())) + "毫秒," +
                    "总耗时:" + (time.get()) + "毫秒," +
                    "最大耗时:" + maxTime[0] + "毫秒," +
                    "最近一秒请求数:" + (lao2 - lao) + "次");
            lao = lao2;
            maxTime[0] = 0;
            if (suc.get() > requestNum) {
                System.out.println("开始结束线程");
                for (Thread thread : list) {
                    thread.interrupt();
                    thread.join();
                }
                System.out.println("结束线程完毕");
                break;
            }
        }

    }
}
