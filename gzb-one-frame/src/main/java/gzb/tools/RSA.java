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
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    public static void main(String[] args) {
        RSAEntity rsaEntity = generateKeyPair();
        String originalData = "Hello, RSA Encryption!";
        byte[] data = originalData.getBytes(Config.encoding);

        // 加密
        byte[] encryptedData = encrypt(data, rsaEntity.pub);
        System.out.println("加密后的数据（Base64 编码）: " + Base64.getEncoder().encodeToString(encryptedData));

        // 解密
        byte[] decryptedData = decrypt(encryptedData, rsaEntity.pri);
        System.out.println("解密后的数据: " + new String(decryptedData));


    }

    public static final RSAEntity generateKeyPair() {
        try {
            // 生成密钥对
            KeyPair keyPair = RSATools.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将公钥和私钥转换为字符串
            String publicKeyString = RSATools.publicKeyToString(publicKey);
            String privateKeyString = RSATools.privateKeyToString(privateKey);
            return new RSAEntity(publicKeyString, privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static final String encryptBase64(byte[] data, String publicKeyString){
        return Base64.getEncoder().encodeToString(encrypt(data, publicKeyString))
                .replaceAll("=","_")
                .replaceAll("\\+","-");
    }
    public static final byte[] decryptBase64(String data, String publicKeyString){
        return decrypt(Base64.getDecoder().decode(data.replaceAll("-","+").replaceAll("_","=").getBytes(Config.encoding)), publicKeyString);
    }
    public static final byte[] encrypt(byte[] bytes, String publicKeyString) {
        try {
            PublicKey restoredPublicKey = RSATools.stringToPublicKey(publicKeyString);
            return RSATools.encrypt(bytes, restoredPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final byte[] decrypt(byte[] bytes, String privateKeyString) {
        try {
            PrivateKey restoredPrivateKey = RSATools.stringToPrivateKey(privateKeyString);
            return RSATools.decrypt(bytes, restoredPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


class RSATools {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public static void main(String[] args) {
        try {
            String originalData = "Hello, RSA Encryption!";
            byte[] data = originalData.getBytes(Config.encoding);

            // 生成密钥对
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将公钥和私钥转换为字符串
            String publicKeyString = publicKeyToString(publicKey);
            String privateKeyString = privateKeyToString(privateKey);

            // 将字符串转换回公钥和私钥对象
            PublicKey restoredPublicKey = stringToPublicKey(publicKeyString);
            PrivateKey restoredPrivateKey = stringToPrivateKey(privateKeyString);

            // 加密
            byte[] encryptedData = encrypt(data, restoredPublicKey);
            System.out.println("加密后的数据（Base64 编码）: " + Base64.getEncoder().encodeToString(encryptedData));

            // 解密
            byte[] decryptedData = decrypt(encryptedData, restoredPrivateKey);
            System.out.println("解密后的数据: " + new String(decryptedData));

            // 调用伪代码的加解密函数
            byte[] result = enorde(data, "examplePwd");
            System.out.println("伪代码加解密结果: " + new String(result));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成 RSA 密钥对
     *
     * @return 包含公钥和私钥的 KeyPair 对象
     * @throws NoSuchAlgorithmException 如果指定的算法不可用
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 使用公钥加密数据
     *
     * @param data      待加密的数据
     * @param publicKey 公钥
     * @return 加密后的数据字节数组
     * @throws Exception 如果加密过程中出现异常
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 使用私钥解密数据
     *
     * @param encryptedData 加密后的数据
     * @param privateKey    私钥
     * @return 解密后的数据字节数组
     * @throws Exception 如果解密过程中出现异常
     */
    public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    /**
     * 伪代码实现的加解密函数，实际中 pwd 不能直接用于 RSA 加解密，这里只是示例结构
     *
     * @param data 待处理的数据
     * @param pwd  密码（示例参数，实际 RSA 不这么用）
     * @return 处理后的数据字节数组
     * @throws Exception 如果处理过程中出现异常
     */
    public static byte[] enorde(byte[] data, String pwd) throws Exception {
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 加密
        byte[] encryptedData = encrypt(data, publicKey);
        // 解密
        return decrypt(encryptedData, privateKey);
    }

    /**
     * 将公钥转换为 Base64 编码的字符串
     *
     * @param publicKey 公钥
     * @return Base64 编码的公钥字符串
     */
    public static String publicKeyToString(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 将私钥转换为 Base64 编码的字符串
     *
     * @param privateKey 私钥
     * @return Base64 编码的私钥字符串
     */
    public static String privateKeyToString(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 将 Base64 编码的公钥字符串转换为 PublicKey 对象
     *
     * @param publicKeyString Base64 编码的公钥字符串
     * @return PublicKey 对象
     * @throws Exception 如果转换过程中出现异常
     */
    public static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将 Base64 编码的私钥字符串转换为 PrivateKey 对象
     *
     * @param privateKeyString Base64 编码的私钥字符串
     * @return PrivateKey 对象
     * @throws Exception 如果转换过程中出现异常
     */
    public static PrivateKey stringToPrivateKey(String privateKeyString) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }


}