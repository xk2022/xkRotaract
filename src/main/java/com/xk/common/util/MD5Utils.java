package com.xk.common.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by yuan on 2018/07/04
 */
public class MD5Utils {

    /**
     * MD5加密類
     *
     * @param str 要加密的字符串
     * @return 加密後的字符串
     */
    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位加密
//            return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密解密演算法 執行一次加密,兩次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    /**
     *  生成8位長度的salt值
      */
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }


    // 靜態加密測試
    public static void main(String[] args) {
        System.out.println(code("123456"));
        String s = new String("wong2wai3dah123");
        String s1 = new String("wong2wai3dah123");
        System.out.println("原始:" + s);
        System.out.println("MD5後:" + code(s));
        System.out.println("MD5後:" + code(s1));
        // System.out.println("加密的:" + convertMD5(s));
        // System.out.println("解密的:" + convertMD5(convertMD5(s)));
    }

}
