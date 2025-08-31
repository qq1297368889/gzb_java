package gzb.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AES_CBC_128 {
    private final static String AES = "AES";
    private final static byte[] iv_def = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static final byte[] aesEn(byte[] contentByte, String key, String iv) {
        return aesEn(contentByte, key.getBytes(), iv.getBytes());
    }
    public static final byte[] aesEn(byte[] contentByte, byte[] key, byte[] iv) {
        try {
            //初始化一个密钥对象
            SecretKeySpec keySpec = new SecretKeySpec(key, AES);
            //初始化一个初始向量,不传入的话，则默认用全0的初始向量
            byte[] initParam = iv == null ? iv_def : iv;
            IvParameterSpec ivSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(contentByte);
        } catch (Exception e) {
            return null;
        }
    }

    public static final byte[] aesDe(byte[] contentByte, String key, String iv) {
        return aesDe(contentByte, key.getBytes(), iv.getBytes());
    }
    public static final byte[] aesDe(byte[] contentByte, byte[] key, byte[] iv) {
        try {
            //初始化一个密钥对象
            SecretKeySpec keySpec = new SecretKeySpec(key, AES);
            //初始化一个初始向量,不传入的话，则默认用全0的初始向量
            byte[] initParam = iv == null ? iv_def : iv;
            IvParameterSpec ivSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(contentByte);
        } catch (Exception e) {
            return null;
        }

    }


    public static final String aesEn(String content, String pwd, String iv) {
        byte[] b0 = content.getBytes(StandardCharsets.UTF_8);
        byte[] b1 = aesEn(b0, pwd, iv);
        System.out.println(b0);
        System.out.println(b1);
        return Tools.textBase64Encoder(b1);
    }

    public static final String aesDe(String content, String pwd, String iv) {
        byte[] b0 = Tools.textBase64DecoderByte(content);
        byte[] b1 = aesDe(b0, pwd, iv);
        return new String(b1, StandardCharsets.UTF_8);
    }


}
