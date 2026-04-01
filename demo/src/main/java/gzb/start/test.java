package gzb.start;

import com.alibaba.fastjson2.JSON;
import com.frame.dao.SysFileDao;
import com.frame.dao.impl.SysFileDaoImpl;
import com.frame.entity.SysFile;
import com.frame.test.Test;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.Resource;
import gzb.frame.db.DataBase;
import gzb.frame.factory.ClassTools;
import gzb.frame.factory.GzbEntityInterface;
import gzb.tools.DateTime;
import gzb.tools.Queue;
import gzb.tools.Tools;
import gzb.tools.thread.GzbThreadLocal;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class test {
    public static void main(String[] args) throws Exception {
    Class<?>class1= Test.class;
        String code=ClassTools.gen_call_code_v4(class1,null);
        System.out.println(code);
    }

    public static void main0(String[] args) throws Exception {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
        Queue<String> queue0 = new Queue<String>();

        for (int n = 0; n < 10; n++) {
            Thread thread = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    test(queue);
                }
            });
            thread.start();
        }
    }

    public static void test(Queue<String> queue) {
        int num = 10000 * 5;
        int size = 0;
        long start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            queue.add("test");
        }
        long end = System.nanoTime();
        System.out.println("add size " + size + " 总耗时 " + ((end - start) / 1000 / 1000) + "毫秒 平均耗时 " + ((end - start) / (num)) + "纳秒");
        start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            size += queue.poll().length();
        }
        end = System.nanoTime();
        System.out.println("poll size " + size + " 总耗时 " + ((end - start) / 1000 / 1000) + "毫秒 平均耗时 " + ((end - start) / (num)) + "纳秒");


    }

    public static void test(ConcurrentLinkedQueue<String> queue) {
        int num = 10000 * 5;
        int size = 0;
        long start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            queue.add("test");
        }
        long end = System.nanoTime();
        System.out.println("size " + size + " 总耗时 " + ((end - start) / 1000 / 1000) + "毫秒 平均耗时 " + ((end - start) / (num)) + "纳秒");
        start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            size += queue.poll().length();
        }
        end = System.nanoTime();
        System.out.println("size " + size + " 总耗时 " + ((end - start) / 1000 / 1000) + "毫秒 平均耗时 " + ((end - start) / (num)) + "纳秒");


    }

}

class c_15876325051994112 {

    // --- Class Variables ---
    @Resource
    SysFileDao sysFileDao;

    public String _gzb_tem_001(String acc, String pwd, com.frame.entity.SysUsers sysUsers) throws Exception {
        GzbThreadLocal.Entity entity1 = GzbThreadLocal.context.get();
        int index0=entity1.stringBuilderCacheEntity.open();
        StringBuilder sb =entity1.stringBuilderCacheEntity.get(index0);
        try {
            sb.append("<html>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n<table>\r\n    <tr>\r\n        ");
            System.out.println(acc);
            System.out.println(pwd);
            System.out.println(sysUsers);
            List<SysFile> list = sysFileDao.query(new SysFile());
            for (SysFile sysFile : list) {
                sb.append("\r\n        <td>");
                sb.append(sysFile.getSysFileId());
                sb.append("</td>\r\n        ");
            }
            sb.append("\r\n    </tr>\r\n</table>\r\n<body>\r\n<script></script>\r\n</body>\r\n</html>");
            return sb.toString();
        }finally {
            entity1.stringBuilderCacheEntity.close(index0);
        }
    }

}