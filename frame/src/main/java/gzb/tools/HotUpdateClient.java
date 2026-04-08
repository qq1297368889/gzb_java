package gzb.tools;

import gzb.frame.factory.ClassLoad;
import gzb.frame.factory.ClassTools;
import gzb.tools.http.HTTP_V3;
import gzb.tools.log.Log;

import java.io.File;
import java.util.List;

public class HotUpdateClient {
    public static void push(String javaCodeDir, String pwd, String iv, String serverUrl) throws Exception {
        List<File> list = FileTools.subFileAll(new File(javaCodeDir), 2, ".java");
        push(list, new String[]{pwd}, new String[]{iv}, new String[]{serverUrl},false);
    }
    public static void push(String javaCodeDir, String pwd, String iv, String serverUrl,boolean verify) throws Exception {
        List<File> list = FileTools.subFileAll(new File(javaCodeDir), 2, ".java");
        push(list, new String[]{pwd}, new String[]{iv}, new String[]{serverUrl},verify);
    }

    public static void push(String javaCodeDir, String[] pwd, String[] iv, String[] serverUrl) throws Exception {
        List<File> list = FileTools.subFileAll(new File(javaCodeDir), 2, ".java");
        push(list, pwd, iv, serverUrl,false);
    }

    public static void push(List<File> list, String[] pwd, String[] iv, String[] serverUrl, boolean verify) throws Exception {
        if (iv.length != pwd.length || iv.length != serverUrl.length) {
            throw new RuntimeException("加密信息和服务器地址不配套");
        }
        HTTP_V3 httpV3 = new HTTP_V3();
        for (int i = 0; i < list.size(); i++) {
            byte[] bytes = FileTools.readByte((list.get(i)));
            String className = null;
            if (verify) {
                Class<?> aClass = ClassLoad.compileJavaCode(new String(bytes));
                if (aClass == null) {
                    Log.log.w("类编译出错", list.get(i));
                    continue;
                }
                className = aClass.getName();
            } else {
                className = ClassTools.extractPublicClassName(new String(bytes));
            }
            for (int i1 = 0; i1 < pwd.length; i1++) {
                if (pwd[i1] != null && iv[i1] != null) {
                    bytes = AES_CBC_128.aesEn(FileTools.readByte((list.get(i))), pwd[i1], iv[i1]);
                }
                if (bytes != null && bytes.length > 0) {
                    String base64 = Tools.textBase64Encoder(bytes);
                    String sign = Tools.textToMd5(base64 + (pwd[i1] == null ? "" : pwd[i1]) + (iv[i1] == null ? "" : iv[i1]));
                    String postData = "name=" + className + "&file=" + Tools.textBase64Encoder(bytes) + "&sign=" + sign;
                    httpV3.post(serverUrl[i1], postData);
                    if (httpV3.getResponseCode() != 200 || !httpV3.asString().contains("类已就绪")) {
                        Log.log.w("上传失败", list.get(i));
                    } else {
                        Log.log.i("上传成功", list.get(i));
                    }
                } else {
                    Log.log.w("文件处理失败", list.get(i));
                }
            }

        }
    }
}
