package com.lx.framework.tool.startup.utils;



import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Base64;


/**
 * @author xin.liu
 * @description 基于AES的加密、解密工具类
 * @date 2023-04-10  17:24
 * @Version 1.0
 */
public class AESUtil {


    /**
     * 密钥 需要前端和后端保持一致
     */
    public static final String KEY = "ca43274076ad485e88659b193e51f001";


    /**
     * 算法
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";




    /**
     * @description    解密
     * @param encrypt  密文
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/10 17:28
     */
    public static String aesDecrypt(String encrypt) {
        return aesDecrypt(encrypt, KEY);
    }


    /**
     * @description     加密
     * @param content   原文
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/10 17:28
     */
    public static String aesEncrypt(String content) {
        return aesEncrypt(content, KEY);
    }



    /**
     * @description 将byte[]转为各种进制的字符串
     * @param bytes
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return: java.lang.String    转换后的字符串
     * @author xin.liu
     * @date 2023/4/10 17:29
     */
    public static String binary(byte[] bytes, int radix){
        // 这里的1代表正数
        return new BigInteger(1, bytes).toString(radix);
    }



    /**
     * @description   base 64 encode
     * @param bytes  待编码的byte[]
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/10 17:30
     */
    public static String base64Encode(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * @description         base 64 decode
     * @param base64Code    待解码的base 64 code
     * @return: byte[]      解码后的byte[]
     * @author xin.liu
     * @date 2023/4/10 17:30
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return isEmpty(base64Code) ? null : Base64.getDecoder().decode(base64Code);
    }



    /**
     * @description         AES加密
     * @param content       待加密的内容
     * @param encryptKey    加密密钥
     * @return: byte[]      加密后的byte[]
     * @author xin.liu
     * @date 2023/4/10 17:30
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }



    /**
     * @description         AES加密为base64 code
     * @param content       待加密的内容
     * @param encryptKey    加密密钥
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/10 17:31
     */
    public static String aesEncrypt(String content, String encryptKey)  {
        try {
            return base64Encode(aesEncryptToBytes(content, encryptKey));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



    /**
     * @description          AES解密
     * @param encryptBytes   encryptBytes 待解密的byte[]
     * @param decryptKey     decryptKey 解密密钥
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/10 17:31
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes,"utf-8");
    }


    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */

    /**
     * @description         将base64 code AES解密
     * @param encryptStr    待解密的base64 code
     * @param decryptKey    解密密钥
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/10 17:32
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) {
        try {
            return isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * @description 判断字符串是否为空
     * @param str
     * @return: boolean
     * @author xin.liu
     * @date 2023/4/10 17:35
     */
    public static boolean isEmpty(String str){
       return str == null || str.isEmpty();
    }



    /**
     * 测试
     */
    public static void main(String[] args) throws Exception {
        String content = "460002199905213416";
        System.out.println("加密前：" + content);
        System.out.println("加密密钥和解密密钥：" + AESUtil.KEY);
        String encrypt = aesEncrypt(content, AESUtil.KEY);
        System.out.println("加密后：" + encrypt);
        String decrypt = aesDecrypt(encrypt,AESUtil.KEY);
        System.out.println("解密后：" + decrypt);
    }

}
