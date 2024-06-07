package com.gem.utils;

import javax.crypto.Cipher;
import java.security.*;

public class RSAKeyUtil {
    //生成RSA密钥对

    /**
     * 生成公钥
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey() throws NoSuchAlgorithmException {
        // 初始化密钥对生成器
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("testPaperSys");
        keyGen.initialize(2048); // 密钥长度，

        // 生成密钥对
        KeyPair keyPair = keyGen.generateKeyPair();

        // 获取公钥和私钥

        return keyPair.getPublic();

    }

    /**
     * 生成私钥
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey getPrivateKey() throws NoSuchAlgorithmException {
        // 初始化密钥对生成器
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("testPaperSys");
        keyGen.initialize(2048); // 密钥长度，可以调整，但一般推荐至少2048位

        // 生成密钥对
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        return privateKey;
    }


    //对pdf文件进行加密
    public static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("testPaperSys");
        cipher.init(Cipher.ENCRYPT_MODE, RSAKeyUtil.getPublicKey());
        return cipher.doFinal(data);
    }


    //对加密文件进行解密
    public static byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("testPaperSys");
        cipher.init(Cipher.DECRYPT_MODE,RSAKeyUtil.getPrivateKey());
        return cipher.doFinal(data);
    }
}