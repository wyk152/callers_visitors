package com.easymicro.core.util;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**************************************
 * 对象工具类
 *@author LinYingQiang
 *@date 2018-08-10 21:21
 *@qq 961410800
 *
 ************************************/

public class ObjectUtils {

    /**
     * 将集合中的对象转化Map -> key-value
     */
    public static <T> List<Map<String, Object>> toMaps(List<T> data) {
        List<Map<String, Object>> rstList = new ArrayList<>();
        if (data != null && data.size() > 0) {
            rstList = data.stream().map(ObjectUtils::toMaps).collect(Collectors.toList());
        }
        return rstList;
    }

    public static <T> Map<String, Object> toMaps(T t) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz =  t.getClass();
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
        try {
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                Method method = descriptor.getReadMethod();
                Class<?> type = descriptor.getPropertyType();
                if (type.equals(Integer.class)) {
                    map.put(name, method.invoke(t));
                }else if (type.equals(Long.class)) {
                    map.put(name, method.invoke(t));
                }else if (type.equals(Short.class)) {
                    map.put(name, method.invoke(t));
                }else if (type.equals(Double.class)) {
                    map.put(name, method.invoke(t));
                }else if (type.equals(String.class)) {
                    map.put(name, method.invoke(t));
                }else if (type.equals(Byte.class)) {
                    map.put(name, method.invoke(t));
                }else if (type.equals(Boolean.class)) {
                    map.put(name, method.invoke(t));
                }else if (type.equals(Character.class)) {
                    map.put(name, method.invoke(t));
                }
            }
        } catch (Exception e) {

        }
        return map;
    }

}
