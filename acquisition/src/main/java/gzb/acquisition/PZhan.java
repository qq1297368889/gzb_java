package gzb.acquisition;

import com.google.gson.Gson;
import gzb.tools.AES_ECB_128;
import gzb.tools.GzbMap;
import gzb.tools.Tools;
import gzb.tools.http.HTTP;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PZhan {
    static String[] aes_keys = new String[]{
            "6eIZ4cxM5pqzUXcF",
            "84UZNK33cSVylz6Y",
            "jeSWRcTwHyAKwJDB",
            "i1hvJx9vuRt5zEBS",
            "1Yy1KOa75R7cnmkg",
            "4MVTQQAJlMpUIAiL",
            "T0RVp7KIPamrtQ33",
            "8HbPxhX6fjhhhwok",
            "ugvseZc5Kkj8ecmV",
            "G7i3OPcfNhBnAYpc",
    };
    static String key = "肥宅";
    static int page = 1;
    static int pageSize = 10;
    static int sortType = 2;
    static String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZHNDb2RlIjoiREZIIiwic2l0ZUlkIjoxLCJleHAiOjE3NTQxMDAxMjJ9.kD1c9ZEKlUEhD-sgz0MBVwkU3wSw80D88CHltuvUrhE";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String res = search(key, page, pageSize, sortType,false);
        System.out.println(new Gson().fromJson(res, Map.class));
    }

    public static String search(String key, int page, int pageSize, int sortType, boolean print) throws UnsupportedEncodingException {
        String post_url = "https://api.3a5mhx6f3e75.xyz/fast-endecode/main/request";
        String post_data = "";

        HTTP http = new HTTP();
        http.headers.put("Content-Type", "application/json");
        http.headers.put("jwttoken", jwt);
        long time = System.currentTimeMillis();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("accurate", 1);
        map1.put("explore", false);
        map1.put("exploreGroupId", null);
        map1.put("groupId", null);
        map1.put("keyword", key);
        map1.put("keywordType", 0);
        map1.put("page", page);
        map1.put("pageSize", pageSize);
        map1.put("sortType", sortType);

        Map<String, Object> map0 = new HashMap<>();
        map0.put("uri", "cms/vod/search");
        map0.put("method", 2);
        map0.put("params", map1);

        post_data = new Gson().toJson(map0);
        if (print) {
            System.out.println("post_data " + post_data);
        }
        String aes_key = aes_keys[Integer.parseInt((time % 10) + "")];
        if (print) {
            System.out.println("aes_key " + aes_key);
        }
        String b64_01 = AES_ECB_128.aesECBEn(post_data, aes_key);
        if (b64_01 == null) {
            return null;
        }
        if (print) {
            System.out.println("b64_01 " + b64_01);
        }
        post_data = b64_01
                .replaceAll("_", "=")
                .replaceAll("-", "+");
        if (print) {
            System.out.println("data " + post_data);
        }
        String res = http.httpPostByteString(post_url, "{\"data\":\"" + post_data + "\",\"time\":" + time + "}");
        if (print) {
            System.out.println("res " + res);
        }
        String resTime = Tools.textMid(res, "\"time\":", "}", 1);
        String resData = Tools.textMid(res, "\"data\":\"", "\"", 1);
        String res_aes_key = aes_keys[Integer.parseInt((Long.parseLong(resTime) % 10) + "")];
        if (print) {
            System.out.println("res_aes_key " + res_aes_key);
        }
        String res_text = AES_ECB_128.aesECBDe(resData, res_aes_key);
        if (print) {
            System.out.println("res_text " + res_text);
        }
        return res_text;
    }

}
