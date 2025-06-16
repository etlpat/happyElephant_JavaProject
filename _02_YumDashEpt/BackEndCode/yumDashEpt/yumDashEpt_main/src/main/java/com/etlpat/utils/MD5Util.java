package com.etlpat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5 加密工具类
 */
public class MD5Util {

    /**
     * 将字符串转换为 MD5 哈希值
     *
     * @param input 原始字符串
     * @return MD5 哈希值的十六进制表示
     */
    public static String getMD5String(String input) {
        try {
            // 获取 MD5 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 计算哈希值
            byte[] messageDigest = md.digest(input.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }


    /**
     * 验证字符串与 MD5 哈希是否匹配
     *
     * @param input   原始字符串
     * @param md5Hash 要比较的 MD5 哈希值
     * @return 如果匹配返回 true，否则返回 false
     */
    public static boolean match(String input, String md5Hash) {
        String inputHash = getMD5String(input);
        return inputHash.equals(md5Hash);
    }
}