package gzb.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DES_CBC {
    public static void main(String[] args) {
        String data = "韩国国立现代美术馆.果川.韩国";
        String key = "12345678";
        String iv = "12345678";

        String en = encrypt(data, key, iv);
        String de = decrypt(en, key, iv);

        System.out.println("明文: " + data);
        System.out.println("加密: " + en);
        System.out.println("解密: " + de);
    }

    public static final byte[] encrypt(byte[] bytes, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "DES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(bytes);
        return encrypted;
    }

    public static String encrypt(String data, String key, String iv) {
        try {
            return new String(Base64.getEncoder().encode(encrypt(data.getBytes(), key, iv)), Config.encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final byte[] decrypt(byte[] bytes, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "DES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        return cipher.doFinal(bytes);
    }

    public static String decrypt(String dataEncode, String key, String iv) {
        try {
            return new String(decrypt(Base64.getDecoder().decode(dataEncode), key, iv));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
