package gzb.start;

import com.frame.entity.SysFile;
import gzb.tools.DateTime;
import gzb.tools.OnlyId;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        SysFile sysFile = new SysFile();
        sysFile.setSysFilePath(Tools.getRandomString(12));
        sysFile.setSysFileMd5(Tools.getRandomString(32));
        sysFile.setSysFileTime(new DateTime().toLocalDateTime());
        sysFile.setSysFileType(Tools.getRandomString(7));
        sysFile.setSysFileId(OnlyId.getDistributed());
        List<SysFile>list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(sysFile);
        }
        long start, end, size = 0;
        for (int i = 0; i < 1; i++) {
            size=0;
            start=System.currentTimeMillis();
            for (int i1 = 0; i1 < 1; i1++) {
                size+=Tools.toJson(list).length();
            }
            end=System.currentTimeMillis();
            Log.log.i("1",end-start,size,Tools.toJson(list));
            size=0;
            start=System.currentTimeMillis();
            for (int i1 = 0; i1 < 1; i1++) {
               size+=list.toString().length();
            }
            end=System.currentTimeMillis();
            Log.log.i("2",end-start,size,list.toString());
        }
        System.out.println(list.toString());
        System.out.println(Tools.toJson(list));
    }
}

