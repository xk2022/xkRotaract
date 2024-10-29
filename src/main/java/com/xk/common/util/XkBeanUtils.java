package com.xk.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * XkBeanUtils 工具类
 * 提供对象属性拷贝的通用方法，包括单个对象和集合的属性拷贝
 *
 * @author yuan
 */
public class XkBeanUtils extends BeanUtils {

    /**
     * 单个对象的属性拷贝
     *
     * @param source 数据源对象
     * @param targetSupplier 目标对象的供应商（提供目标对象的实例化方法，例如：UserVO::new）
     * @param <S> 数据源类型
     * @param <T> 目标类型
     * @return 拷贝后的目标对象，如果源对象为 null，则返回 null
     */
    public static <S, T> T copyProperties(S source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 集合数据的拷贝（默认字段拷贝）
     *
     * @param sources 数据源列表
     * @param target 目标类::new（例如：UserVO::new）
     * @param <S> 数据源类型
     * @param <T> 目标类型
     * @return 拷贝后的目标对象列表
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 带回调函数的集合数据拷贝（可自定义字段拷贝规则）
     *
     * @param sources 数据源列表
     * @param target 目标类::new（例如：UserVO::new）
     * @param callBack 回调函数，用于自定义字段拷贝规则
     * @param <S> 数据源类型
     * @param <T> 目标类型
     * @return 拷贝后的目标对象列表
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, XkBeanUtilsCallBack<S, T> callBack) {
        if (sources == null || sources.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            BeanUtils.copyProperties(source, t);
            if (callBack != null) {
                // 回调函数，用于自定义字段拷贝
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }
}
