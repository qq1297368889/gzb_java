package gzb.frame;

import gzb.exception.GzbException0;
import gzb.tools.GzbNumber;
import gzb.tools.Tools;

public class Test {
    public static void main(String[] args) {

        try {
            GzbNumber.parseLong("1000h");
            GzbNumber.parseInteger("1000h");
            GzbNumber.parseShort("1000h");
            GzbNumber.parseFloat("1000h");
            GzbNumber.parseDouble("1000h");
            GzbNumber.parseByte("1000h");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(Tools.getExceptionInfo(e));
        }
    }


}
