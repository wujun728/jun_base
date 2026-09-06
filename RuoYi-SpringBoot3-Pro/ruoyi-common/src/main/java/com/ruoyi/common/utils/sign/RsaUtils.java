package com.ruoyi.common.utils.sign;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密解密
 *
 * @author ruoyi
 **/
public class RsaUtils {
    // Rsa 私钥
    public static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9XDIDItpuGLlJ"+
            "5R2BxV0Z8O5oWYIs5LQwKAqm+Us0+HuWn+VeiPhcRFJJCDOHvAEEgieWrK1hmA5k"+
            "Cr6F1Kegoz6GETvK2jArvuR1tnDjXxfErcvH4l+3b7mXpLGxkAT3XoEnjpToQrxC"+
            "kAGr20s/WtQpOjBPKAXLpJaUGX5Fu6VFU9u3ibSAU5tcz96Hlkib3uEuxBwC5p1D"+
            "Qqpv7TaewXZuZWIG0wwrZiPohHBf1hX43xyOwld5J2xUbAX98a0nbEqr8VcTiuu0"+
            "sr30ErDUvuu7Y+jq1kohU4ZmBmetFfojD+8ypJr4WdPGB+w1kNxY6pnwr5qpDth2"+
            "94bO9mRzAgMBAAECggEBAJF3BrBkENpswcIjcLRlEi1AaVTeFeM42bb4u54jegO6"+
            "Mu617HTf0bLHhVK3KybFZR66gYD9K8ACGGP/4PZcM11yqjBBguZFEKY6YbSPr07r"+
            "mQ2s2RO3MgJvoGn+ycZ2tWn2Pk9N99QomAimKbKEptyHgN4e5keYnkMfL9Gbd+ZG"+
            "enRoCo+VOsr9jR11ila2g//KeeV4o5dpUhapwd2HXl9LAwCbN4iUTcTq3jDH8MTm"+
            "GdnZpWaLqakDp9BdnAAbX3o5CeD6SjHzQSwUmFZwRUVf4HwQ/+tS6F2mw3x0S216"+
            "RWfyzmVycaUzTj8dOU3CW8+jSGyBqT+1VYWiAgY0d9ECgYEA5zejWtNkVwgvk2Qi"+
            "DgiyGNjkzOpQpwCtMUYM+QSGysSstErEdcnfMoe6eYkiFNGgBdl3PLrQJwau27/9"+
            "Wqa/MzvNYNTnWPy25aqW66ZOjFTBsIkAa0ViKajtpucIKNvaXSOwlCPoD5yTY4MI"+
            "IH8zIevQZOeNb1tkHJPvbOGJL0kCgYEA0agKHXZzPmY8KeH0y7g6xiGS/ooxZgxy"+
            "B5Zp9UvtqQE5CafE5yd60oTzCW060dOLbgaqRZiuWbFJ0zpI6ChaYTMYxdvrcQWz"+
            "L0GPk9NEe3GRT0ruWYbJkfpeIMUezLKfiueAuJ0Vmy3x8AxuO528jH6B7xvoE0Pe"+
            "0+SUcQqGadsCgYA0qcCEPGe7RvsHGCSFi8d8z1H1tlzeXNIVyf3EbhqBbqBjhDAR"+
            "IAS9TprTeb+QfFp1Wp3E8Evex6/mD2mWTyp3ceSKbJOw+gZycxNi4wM7BUcEfX/h"+
            "7vC3ymkuvapnHAQ1eJ6Mb0042RHc9YhRVod/72UMxoy5U1iPBcbfxtLnOQKBgEh6"+
            "TwTgbfakYSgZdQb4KVlVQfu8ylb89m6pEPg7x20lfxJXbTp763nbfClGGY9wEkN3"+
            "CmYE4kEfiOX8wDeBu7zebTH5VOs9jTRI9dmkr4f9Or6uqLdKYWSVqPSrMHqTRZQ/"+
            "c8BejZmXyIuzwGfbn9Lx6PlALHp8fAvEeYyalt0BAoGAC9QL212Gipj9x9tIoSpb"+
            "itMA6+ut7nGEjr0/geSbe2Y0R5qk2QPk5SMPYZ/kZBZ0DTcRkIIGJ2x+MAUjn+1C"+
            "NAggJZlqValgVXZVIkZQHKYxbdKOjI3W/4LSFn/7RHqx1N9oBgZdFmWBJYuRBrff"+
            "IgZ9skWurSupHYqTKNA49Hc=";

    /**
     * 私钥解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String text) throws Exception {
        return decryptByPrivateKey(privateKey, text);
    }

    /**
     * 公钥解密
     *
     * @param publicKeyString 公钥
     * @param text            待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyString 私钥
     * @param text             待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text             待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString 公钥
     * @param text            待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return 生成后的公私钥信息
     */
    public static RsaKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        return new RsaKeyPair(publicKeyString, privateKeyString);
    }

    /**
     * RSA密钥对对象
     */
    public static class RsaKeyPair {
        private final String publicKey;
        private final String privateKey;

        public RsaKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }
}