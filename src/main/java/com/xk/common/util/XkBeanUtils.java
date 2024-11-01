package com.xk.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * XkBeanUtils 工具類
 * 提供對象屬性拷貝的通用方法，包括單個對象和集合的屬性拷貝
 *
 * @author yuan
 */
public class XkBeanUtils {

    /**
     * 單個對象的屬性拷貝，支援不同型別的常見轉換
     *
     * @param source         資料來源對象
     * @param targetSupplier 目標對象的供應商（提供目標對象的實例化方法，例如：UserVO::new）
     * @param <S>            資料來源類型
     * @param <T>            目標類型
     * @return 拷貝後的目標對象，如果來源對象為 null，則返回 null
     */
    public static <S, T> T copyProperties(S source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        T target = targetSupplier.get();

        // 使用 Spring 的 BeanUtils 進行基本屬性拷貝
        BeanUtils.copyProperties(source, target);

        // 自動型別轉換
        copyPropertiesAutoConvert(source, target);
        return target;
    }

    /**
     * 單個對象的屬性拷貝，支援不同型別的常見轉換，並排除指定的 null 屬性。
     *
     * @param source             資料來源對象
     * @param target             資料目標對象
     * @param nullPropertyNames  要排除的屬性名稱集（null 或者特定排除的屬性名稱）
     */
    public static void copyPropertiesAutoConvert(Object source, Object target, String... nullPropertyNames) {
        if (source == null || target == null) {
            return;
        }

        String[] excludedProperties = getNullPropertyNames(source, nullPropertyNames);
        BeanUtils.copyProperties(source, target, excludedProperties);

        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            try {
                Object sourceValue = sourceField.get(source);

                for (Field targetField : targetFields) {
                    targetField.setAccessible(true);

                    if (sourceField.getName().equals(targetField.getName()) &&
                            sourceValue != null &&
                            !isExcluded(sourceField.getName(), excludedProperties) &&
                            StringUtils.isNotBlank(String.valueOf(sourceValue))) {

                        convertAndSet(target, sourceValue, targetField, sourceField);
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 獲取來源對象中所有 null 值的屬性名稱，並合併傳入的排除屬性。
     */
    private static String[] getNullPropertyNames(Object source, String... additionalExcludes) {
        Set<String> emptyNames = new HashSet<>();

        // 找出所有值為 null 的屬性名稱
        for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(source.getClass())) {
            try {
                Field field = getFieldRecursively(source.getClass(), pd.getName());
                if (field != null) {
                    field.setAccessible(true);
                    if (field.get(source) == null) {
                        emptyNames.add(pd.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 加入額外的排除屬性
        if (additionalExcludes != null) {
            for (String exclude : additionalExcludes) {
                emptyNames.add(exclude);
            }
        }

        return emptyNames.toArray(new String[0]);
    }

    /**
     * 遞歸查找指定名稱的欄位，包括父類中的欄位
     */
    private static Field getFieldRecursively(Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 如果當前類找不到欄位，遞歸查找父類
            return getFieldRecursively(clazz.getSuperclass(), fieldName);
        }
    }


    private static boolean isExcluded(String fieldName, String[] excludedProperties) {
        for (String exclude : excludedProperties) {
            if (fieldName.equals(exclude)) {
                return true;
            }
        }
        return false;
    }

    private static void convertAndSet(Object target, Object sourceValue, Field targetField, Field sourceField)
            throws IllegalAccessException {
        if (targetField.getType().equals(String.class) && sourceField.getType().equals(Long.class)) {
            targetField.set(target, sourceValue.toString());
        } else if (targetField.getType().equals(Long.class) && sourceField.getType().equals(String.class)) {
            targetField.set(target, Long.valueOf((String) sourceValue));
        } else if (targetField.getType().equals(String.class) && sourceField.getType().equals(Integer.class)) {
            targetField.set(target, sourceValue.toString());
        } else if (targetField.getType().equals(Integer.class) && sourceField.getType().equals(String.class)) {
            targetField.set(target, Integer.valueOf((String) sourceValue));
        } else if (targetField.getType().equals(String.class) && sourceField.getType().equals(Boolean.class)) {
            targetField.set(target, sourceValue.toString());
        } else if (targetField.getType().equals(Boolean.class) && sourceField.getType().equals(String.class)) {
            targetField.set(target, Boolean.parseBoolean((String) sourceValue));
        }
    }

    /**
     * 集合數據的拷貝
     *
     * @param sources 數據源列表
     * @param target 目標類::new（例如：UserVO::new）
     * @param <S> 數據源類型
     * @param <T> 目標類型
     * @return 拷貝後的目標對象列表
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        if (sources == null || sources.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = copyProperties(source, target);  // 使用單個對象拷貝並自動轉換
            list.add(t);
        }
        return list;
    }

    /**
     * 帶回調函數的集合數據拷貝
     *
     * @param sources 數據源列表
     * @param target 目標類::new（例如：UserVO::new）
     * @param callBack 回調函數，用於自定義字段拷貝規則
     * @param <S> 數據源類型
     * @param <T> 目標類型
     * @return 拷貝後的目標對象列表
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, XkBeanUtilsCallBack<S, T> callBack) {
        if (sources == null || sources.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = copyProperties(source, target);  // 單個對象拷貝
            if (callBack != null) {
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }
}
