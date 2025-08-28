package gzb.acquisition;

import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import java.io.File;
import com.google.gson.Gson;
import gzb.tools.Tools;
import java.util.*;

public class Test {
    static Log log = new LogImpl();

    public static void main(String[] args) throws Exception {
        // test();
        search("真实");
    }
    public static void search(String key) throws Exception {
        String basePath = "E:/00/maomi/2025/";
        List<String>list=new ArrayList<>();
                String[]titles=Tools.fileReadArray(new File(basePath+"titles.txt"),"utf-8","\n");
        String[]video_ids=Tools.fileReadArray(new File(basePath+"video_id.txt"),"utf-8","\n");
        for (int i = 0; i < titles.length; i++) {
            if (titles[i]==null|| titles[i].isEmpty()) {
                continue;
            }
            if (titles[i].contains(key)) {
                list.add(video_ids[i]);
                Tools.m3u8ToMp4(basePath+video_ids[i],basePath+video_ids[i]+".mp4",0);
            }
        }
        System.out.println(list);


    }
    public static void test() throws Exception {
        String basePath = "E:/00/maomi/2025/";
        List<String>list= Tools.fileSubNames(new File(basePath),3);
        System.out.println(list);
        StringBuilder allTitle= new StringBuilder();
        StringBuilder allId= new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String path1=basePath+list.get(i);
            String jsonString= Tools.fileReadString(path1+"/data.json");
            Map map=new Gson().fromJson(jsonString,Map.class);
            String title=map.get("title").toString();
            System.out.println(title +"  "+i+" "+list.size());
            allTitle.append(title).append("\n");
            allId.append(list.get(i)).append("\n");
        }
        Tools.fileSaveString(basePath+"titles.txt", allTitle.toString(),false);
        Tools.fileSaveString(basePath+"video_id.txt", allId.toString(),false);
    }
}
