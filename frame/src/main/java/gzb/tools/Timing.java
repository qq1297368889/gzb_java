package gzb.tools;

import gzb.exception.GzbException0;

public class Timing {
    public static void main(String[] args) throws InterruptedException {
        Timing timing = new Timing();
        timing.record();
        Thread.sleep(10);
        timing.record();
        Thread.sleep(10);
        timing.record();
        Thread.sleep(10);
        timing.record();
        System.out.println(timing.read("测试"));
         timing = new Timing();
        timing.record("测试1");
        Thread.sleep(10);
        timing.record("测试2");
        Thread.sleep(10);
        timing.record("测试3");
        Thread.sleep(10);
        timing.record("测试4");
        System.out.println(timing.read());
    }

    String[] titles;
    long[] times;
    int index = 0;
    public long all;
    public Timing() {
        this(16);
    }

    public Timing(int size) {
        if (size>0) {
            times = new long[size];
            titles = new String[size];
        }

    }

    public void record() {
        record(null);
    }

    public void record(String title) {
        if (times==null) {
            return;
        }
        times[index] = System.nanoTime();
        titles[index] = title;
        index++;
    }

    public String read() {
        return read(titles);
    }

    public String read(String... titles) {
        if (times==null) {
            return null;
        }
        if (titles == null || times.length < 2) {
            throw new GzbException0("计时标题和计时数据不匹配");
        }
        StringBuilder data = new StringBuilder();
        int x = 0;
        for (int i = 1; i < times.length; i++) {
            if (times[i] < 1) {
                break;
            }
            String title;
            if (i > titles.length - 1) {
                title = "计时" + x;
            } else {
                title = titles[i];
            }
            x++;
            long time0 = times[i] - times[i - 1];
            all += time0;
            data.append(title).append(":").append(time0).append(", ");
        }
        data.append("总计:").append(all);
        return data.toString();
    }
}
