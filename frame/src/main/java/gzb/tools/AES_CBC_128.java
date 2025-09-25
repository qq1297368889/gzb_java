/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
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
        byte[] b0 = content.getBytes(Charset.forName(Config.encoding));
        byte[] b1 = aesEn(b0, pwd, iv);
        return Tools.textBase64Encoder(b1);
    }

    public static final String aesDe(String content, String pwd, String iv) {
        byte[] b0 = Tools.textBase64DecoderByte(content);
        byte[] b1 = aesDe(b0, pwd, iv);
        return new String(b1, Charset.forName(Config.encoding));
    }


}
