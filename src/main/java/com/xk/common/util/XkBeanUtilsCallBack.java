package com.xk.common.util;

/**
 * 回调接口，用于自定义属性拷贝的行为
 *
 * @param <S> 数据源类型
 * @param <T> 目标类型
 */
@FunctionalInterface
public interface XkBeanUtilsCallBack<S, T> {

    /**
     * 自定义属性拷贝逻辑
     *
     * @param source 数据源对象
     * @param target 目标对象
     */
    void callBack(S source, T target);

}
