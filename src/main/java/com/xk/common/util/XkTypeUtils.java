package com.xk.common.util;

/**
 * 將 TypeUtils 封裝成一個工具類，它不需要添加任何特定的註解來使其成為一個 Spring Bean。
 * 通常情況下，工具類的設計是無狀態的，直接作為靜態方法使用，因此它不需要被 Spring 容器管理。
 * 只需要按照靜態方法調用的方式引用即可。
 *
 * Created on 2024/10/24.
 *
 * @author yuan
 */
public class XkTypeUtils {

    // 檢查並轉換為 Long
    public static Long parseLongOrNull(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            System.out.println("導入的 value 格式不正確，無法轉換成 Long: " + value);
            throw new IllegalArgumentException("Invalid value format for Long: " + value);
        }
    }

    // 檢查並轉換為 Integer
    public static Integer parseIntegerOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("導入的 value 格式不正確，無法轉換成 Integer: " + value);
            throw new IllegalArgumentException("Invalid value format for Integer: " + value);
        }
    }

    // 檢查並轉換為 Double
    public static Double parseDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("導入的 value 格式不正確，無法轉換成 Double: " + value);
            throw new IllegalArgumentException("Invalid value format for Double: " + value);
        }
    }

    // 檢查並轉換為 Float
    public static Float parseFloatOrNull(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            System.out.println("導入的 value 格式不正確，無法轉換成 Float: " + value);
            throw new IllegalArgumentException("Invalid value format for Float: " + value);
        }
    }

    // 檢查並轉換為 Boolean
    public static Boolean parseBooleanOrNull(String value) {
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        } else {
            System.out.println("導入的 value 格式不正確，無法轉換成 Boolean: " + value);
            throw new IllegalArgumentException("Invalid value format for Boolean: " + value);
        }
    }

    // 檢查並轉換為 Short
    public static Short parseShortOrNull(String value) {
        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            System.out.println("導入的 value 格式不正確，無法轉換成 Short: " + value);
            throw new IllegalArgumentException("Invalid value format for Short: " + value);
        }
    }

    // 檢查並轉換為 Byte
    public static Byte parseByteOrNull(String value) {
        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            System.out.println("導入的 value 格式不正確，無法轉換成 Byte: " + value);
            throw new IllegalArgumentException("Invalid value format for Byte: " + value);
        }
    }

    // 檢查並轉換為 Character (取第一個字符)
    public static Character parseCharOrNull(String value) {
        if (value != null && value.length() == 1) {
            return value.charAt(0);
        } else {
            System.out.println("導入的 value 格式不正確，無法轉換成 Character: " + value);
            throw new IllegalArgumentException("Invalid value format for Character: " + value);
        }
    }

}
