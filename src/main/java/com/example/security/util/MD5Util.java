package com.example.security.util;

import java.security.MessageDigest;

public class MD5Util {
    public static void main(String[] args) {
        String pwd = getMD5("99991");
        System.out.println(pwd);
    }

    //生成MD5
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 建立一個md5演算法物件
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 獲得MD5位元組陣列,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 轉換為16進位制字串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 二進位制轉十六進位制
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}