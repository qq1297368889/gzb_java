package gzb.frame.server.tcp;

import gzb.tools.DateTime;
import gzb.tools.FileTools;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Test {

    public static void main(String[] args) {
  /*      byte[]data0="hello".getBytes(StandardCharsets.UTF_8);
        ByteBuf data1 = Tools.loadByteBuf(data0);
        Log.log.i(Tools.readByteBuf(data1));
        ByteTools gzbPacketHandler = new ByteTools();
        ByteBuf res1=gzbPacketHandler.createDataPacket(data1);
        Log.log.i(Tools.readByteBuf(res1));
        //广场刷新次数
*/
        List<File>list= FileTools.subFileAll(new File("E:\\codes_20220814\\ec2025v617"),2,".js");
        for (int i = 0; i < list.size(); i++) {
            File file=list.get(i);
            System.out.println(i+" / "+list.size()+ " "+file);
            if (new DateTime(file.lastModified()).toString().contains("2024-11")) {
                System.err.println("\n\n\n\n\n"+file);
            }
        }
        System.out.println("end");
    }
}
