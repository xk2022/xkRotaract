package com.xk.common.util;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class GenericUpdateService<T> {

    public <S> T updateEntity(T entity, S request) {
        // 获取 request 中为 null 的属性名称
        String[] nullPropertyNames = getNullPropertyNames(request);

        // 仅复制非 null 属性
        XkBeanUtils.copyPropertiesAutoConvert(request, entity, nullPropertyNames);

        return entity;
    }

    private <S> String[] getNullPropertyNames(S source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }
}
