package gzb.start;


import com.frame.controller.SysFileController;
import gzb.frame.factory.v5.GzbFactory;

public class Test001 {
    public static void main(String[] args) {
        GzbFactory.loadController(SysFileController.class);
    }
}
