package gzb.tools.log;

import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.FileTools;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class LogConfig {
    ///0 显示但不保存 1 保存但不显示 2 不显示也不保存 3 显示且保存
    public static Integer[] lvConfig = new Integer[]{0, 0, 0, 0, 0};
    public static String[] lvColour = new String[]{"\u001B[90m", "\u001B[32m", "\u001B[94m", "\u001B[33m", "\u001B[31m", "\u001B[0m"};
    public static String[] lvNames = new String[]{"trace", "debug", "info ", "warn ", "error"};

    public static String lastDeleteDateStr = "";
    public static int days = 30;

    public static void loadConfig(File []logFile) {
        String path=Config.get("gzb.log.path");
        if (path==null) {
            System.out.println(Config.config);
            throw new NullPointerException("log path is null");
        }
        if (!path.endsWith("/")) {
            while (path.endsWith("\\")) {
                path=path.substring(0, path.length()-1);
            }
            path+="/";
        }
        for (int i = 0; i < LogConfig.lvNames.length; i++) {
            LogConfig.lvConfig[i] = Config.getInteger("gzb.log." + LogConfig.lvNames[i].trim(), 0);
            if (logFile[i] == null) {
                logFile[i] = new File(path+ LogConfig.lvNames[i].trim());
            }
            if (!logFile[i].exists()) {
                if (!FileTools.mkdir(logFile[i])) {
                    HOOK.err0.println("loadConfig 创建日志目录失败:" + logFile[i].getPath());
                }
            }
        }
        days = Config.getInteger("gzb.log.save.days", 30);
        try {
            deleteOldLogFiles(logFile);
        } catch (Exception e) {
            e.printStackTrace();//日志类不调日志类 只能输出了 日志类原则上不允许报错
        }
    }


    public static void deleteOldLogFiles(File []logFile) throws Exception {
        DateTime dateTime = new DateTime();
        String dateStr = dateTime.formatDateTime("yyyy/MM/dd");
        //确保每天运行一次
        if (!lastDeleteDateStr.equals(dateStr)) {
            lastDeleteDateStr = dateStr;
            long time0 = 1000L * 60 * 60 * 24 * days;
            dateTime.operation(-time0);
            for (File logDir : logFile) {
                //只删除 前一天的 如果前边有 说明程序运行中断 不管删除
                String path = logDir.getPath().trim() + File.separator + getDay(dateTime) + ".log";
                File file = new File(path).getParentFile();
                if (file.exists()) {
                    List<File> list = FileTools.subFileAll(file, 3);
                    for (File file1 : list) {
                        file1.delete();
                    }
                    list = FileTools.subFileAll(file, 2);
                    for (File file1 : list) {
                        file1.delete();
                    }
                    file.delete();
                }
            }
        }
    }
    public static String getDay(DateTime dateTime) {
        if (dateTime == null) {
            dateTime = new DateTime();
        }
        return dateTime.formatDateTime("yyyy" + File.separator + "MM" + File.separator + "dd" + File.separator + "HH");
    }

}
