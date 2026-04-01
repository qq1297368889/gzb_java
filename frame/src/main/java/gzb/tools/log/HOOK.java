package gzb.tools.log;

import java.io.*;
public class HOOK extends PrintStream {
    public static final PrintStream out0 = System.out;
    public static final PrintStream err0 = System.err;
    int lv = 0;
    public HOOK(int lv) {
        super(new ByteArrayOutputStream());
        this.lv = lv;
    }

    public void gzbPrintln(String x) {
        out0.println(x);
    }
    public void println0(Object x) {
        Object message = (x == null) ? "" : x;
        LogThread.logThread.addLog(lv, null, new Object[]{"HOOK", message});
    }
    @Override
    public void println(Object x) {
       println0(x);
    }

    @Override
    public void println(String x) {
        println0(x);
    }

    @Override
    public void println(int x) {
        println0(x);
    }

    @Override
    public void println(char x) {
        println0(x);
    }

    @Override
    public void println(long x) {
        println0(x);
    }

    @Override
    public void println(float x) {
        println0(x);
    }

    @Override
    public void println(char[] x) {
        println0(x);
    }

    @Override
    public void println(double x) {
        println0(x);
    }

    @Override
    public void println(boolean x) {
        println0(x);
    }

    @Override
    public void println() {
        println0(null);
    }

    @Override
    public void print(Object x) {
        println0(x);
    }

    @Override
    public void print(String x) {
        println0(x);
    }

    @Override
    public void print(int x) {
        println0(x);
    }

    @Override
    public void print(char x) {
        println0(x);
    }

    @Override
    public void print(long x) {
        println0(x);
    }

    @Override
    public void print(float x) {
        println0(x);
    }

    @Override
    public void print(char[] x) {
        println0(x);
    }

    @Override
    public void print(double x) {
        println0(x);
    }

    @Override
    public void print(boolean x) {
        println0(x);
    }


}