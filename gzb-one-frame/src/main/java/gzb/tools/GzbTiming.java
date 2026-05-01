package gzb.tools;


public class GzbTiming {
    public static void main(String[] args) {
        GzbTiming timing = new GzbTiming();
        timing.record();
        for (int i = 0; i < 1000000; i++) {
            String s = "hello" + i;
        }
        timing.record("循环1000000次字符串连接");
        System.out.println(timing.read());
    }

    StringBuilder message = new StringBuilder(1024);
    public long time = 0;
    public long all = 0;

    public void record() {
        time = System.nanoTime();
    }

    public void record(String msg) {
        long end = System.nanoTime();
        long t01 = end - time;
        all += t01;
        message.append(msg).append(" : ").append(t01).append("ns,");
        time = System.nanoTime();
    }

    //输出
    public String read() {
        message.append("总: ").append(all).append("ns");
        return message.toString();
    }

    public GzbTiming reset() {
        this.time = 0;
        this.all = 0;
        return this;
    }
}
