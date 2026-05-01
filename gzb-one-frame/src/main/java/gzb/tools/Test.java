package gzb.tools;

public class Test {
    public static void main(String[] args) {
        for (int n = 0; n < 100; n++) {
            int size=0;
            long start=System.currentTimeMillis();
            for (int i = 0; i < 10000 * 10; i++) {
                size+= Tools.escapeJsonString(Tools.getRandomString(12)).length();
            }
            long end=System.currentTimeMillis();
            System.out.println(size+"  "+(end-start));
        }

    }
}
