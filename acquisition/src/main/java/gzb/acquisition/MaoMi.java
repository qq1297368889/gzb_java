package gzb.acquisition;

import com.google.gson.Gson;
import gzb.tools.*;
import gzb.tools.http.HTTP;
import gzb.tools.thread.ThreadPool;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MaoMi {

    // 解密方法
    public static String decrypt(String encryptedData, String keyBase64, String ivBase64, String suffix) throws Exception {
        // 解码密钥和初始IV
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        byte[] originalIvBytes = Base64.getDecoder().decode(ivBase64);

        // 构建最终IV (originalIV + suffix)
        String ivWithSuffix = new String(originalIvBytes, StandardCharsets.UTF_8) + suffix;
        IvParameterSpec ivSpec = new IvParameterSpec(ivWithSuffix.getBytes(StandardCharsets.UTF_8));

        // 解码Base64密文
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        byte[] ciphertext;
        // 检查是否包含OpenSSL格式头
        if (encryptedBytes.length >= 16 && startsWithSaltHeader(encryptedBytes)) {
            // 提取盐和实际密文 (OpenSSL格式)
            byte[] salt = new byte[8];
            System.arraycopy(encryptedBytes, 8, salt, 0, 8);
            ciphertext = new byte[encryptedBytes.length - 16];
            System.arraycopy(encryptedBytes, 16, ciphertext, 0, ciphertext.length);
        } else {
            // 直接使用完整密文 (非OpenSSL格式)
            ciphertext = encryptedBytes;
        }

        // 创建AES密钥
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // 初始化解密器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // 执行解密
        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // 检查是否以OpenSSL的Salted__前缀开头
    private static boolean startsWithSaltHeader(byte[] data) {
        String header = new String(data, 0, 8, StandardCharsets.UTF_8);
        return header.equals("Salted__");
    }


    public static String keyBase64 = "SWRUSnEwSGtscHVJNm11OGlCJU9PQCF2ZF40SyZ1WFc=";
    public static String ivBase64 = "JDB2QGtySDdWMg==";
    public static String secretKey = "D7hGKHnWThaECaQ3ji4XyAF3MfYKJ53M";
    public static String sign_key = "JkI2OG1AJXpnMzJfJXUqdkhVbEU0V2tTJjFKNiUleG1VQGZO";
    public static String suffix = "123456";


    public static void main(String[] args) {
        try {
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("this.dir", Tools.getProjectRoot(MaoMi.class));



            List<Map<String, Object>>list=readList(1);

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> stringObjectMap = list.get(i);
                String video_url = sginUrl(stringObjectMap.get("video_url").toString(),
                        "https://tma.qianchengwandian.top");
                System.out.println(video_url);

            }
            System.out.println(list);
            if (list!=null) {
                return ;
            }
            int page = 1;
            int runMax = 5;
            Map<String, String> map = new ConcurrentSkipListMap<>();
            AtomicInteger runState = new AtomicInteger(0);
            ThreadPool.pool.startThread(1, "maomi-jiankong-" + 1, true, () -> {
                while (true) {

                    System.out.println(map);
                    Tools.sleep(1000);
                }
            });
            while (true) {
                if (runState.get()<runMax) {
                    int finalPage = page;
                    runState.getAndIncrement();
                    ThreadPool.pool.startThread(1, "maomi-" + runState.get(), true, () -> {
                        try {
                            start01(finalPage, map);
                            runState.getAndDecrement();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    page++;
                }
                Tools.sleep(1000, 2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //启动下载 某页
    public static void start01(int page, Map<String, String> printLogMap) throws Exception {
        List<Map<String, Object>> list = readList(page);
        HTTP http = new HTTP();

        for (int i = 0; i < list.size(); i++) {
            printLogMap.put(page+"-页面进度", (i + 1) + "/" + list.size());
            Map<String, Object> stringObjectMap = list.get(i);
            String imgUrl = "https://jpg.tlxxw.cc" + stringObjectMap.get("thumb") + ".txt?size=500x281";
            String video_url = sginUrl(stringObjectMap.get("video_url").toString(), "https://tma.qianchengwandian.top");
            String basePath = "E:/00/maomi/2025/" + (stringObjectMap.get("id").toString().split("\\.")[0]) + "/";
            new File(basePath).mkdirs();
            Tools.fileSaveString(basePath + "data.json", new Gson().toJson(stringObjectMap), false);
            String base641 = http.httpGetString(imgUrl);
            byte[] bytes = Tools.textBase64DecoderByte(base641.replaceAll("data:image/jpeg;base64,", ""));

            Tools.fileSaveByte(basePath + "image." + Tools.textMid(base641, "data:image/", ";base64", 1),
                    bytes, false);
            Tools.m3u8Download(video_url, basePath, 1, 50,
                    printLogMap,page+"-视频进度");
        }
        System.out.println(page + "  end 结束");
    }

    //签名算法
    public static String sginUrl(String url, String baseUrl) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int time = new DateTime().operation(100).toStampInt();
        String k1 = secretKey + url + time;
        String sginUrl = baseUrl + url
                + "?wsSecret=" + Tools.toMd5(k1.getBytes())
                + "&wsTime=" + time;
        // sginUrl = sginUrl.replaceAll("//", "/");
        return sginUrl;
    }

    //读取视频列表
    public static List<Map<String, Object>> readList(int page) throws Exception {
        String encryptedData = null;
        String decryptedData = null;
        String data1 = new HTTP().httpGetString("https://mjson.szaction.cc/data/list/base-shipin-dsp-" + page + ".js");
        encryptedData = Tools.textMid(data1, "\"data\":\"", "\"", 1);
        suffix = Tools.textMid(data1, "\"suffix\":\"", "\"", 1);
        encryptedData = encryptedData.replaceAll("\\\\/", "/");
        decryptedData = decrypt(encryptedData, keyBase64, ivBase64, suffix);
        decryptedData = Tools.unicodeToString(decryptedData).replaceAll("\\\\/", "/");
        Map<String, Object> map = new Gson().fromJson(decryptedData, Map.class);
        Map<String, Object> map2 = (Map<String, Object>) map.get("list");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map2.get("data");
        return list;
    }
}