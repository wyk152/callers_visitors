package com.easymicro.util;


import com.easymicro.model.FieldModel;
import com.easymicro.persistence.core.generator.FieldComment;
import com.easymicro.persistence.core.generator.FieldType;
import com.easymicro.persistence.core.model.BaseEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**************************************
 *
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/
public class ClazzUtil {

    /**
     * 解析Class信息
     */
    public static List<FieldModel> parse(Class<? extends BaseEntity> clazz) {
        List<FieldModel> fieldModels = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        Field[] fields = superclass.getDeclaredFields();
        //解析父类字段信息
        for (Field field : fields) {
            Optional<FieldModel> optional = pareseField(field);
            optional.ifPresent(fieldModels::add);
        }
        fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Optional<FieldModel> optional = pareseField(field);
            optional.ifPresent(fieldModels::add);
        }
        return fieldModels;
    }

    /**
     * 大写第一个字母
     */
    public static String upCaseFirstName(String name) {
        String firstChar = name.substring(0,1).toUpperCase();
        return firstChar.concat(name.substring(1, name.length()));
    }

    /**
     * 小写第一个字母
     */
    public static String lowerCaseFirstName(String name) {
        String firstChar = name.substring(0, 1).toLowerCase();
        return firstChar.concat(name.substring(1, name.length()));
    }

    /**
     * 解析Filed转化FieldModel
     */
    public static Optional<FieldModel> pareseField(Field field) {
        FieldModel fieldModel = null;//初始化
        FieldComment annotation = field.getAnnotation(FieldComment.class);
        if (annotation != null) {
            fieldModel = new FieldModel();
            String fieldName = field.getName();//获取英文名称
            String cnName = annotation.name();//注解标识中文名称
            FieldType fieldType = annotation.fieType();//字段类型
            if (fieldType.equals(FieldType.IMG)) {
                fieldModel.setAliasName("alias" + upCaseFirstName(fieldName));
            } else if (fieldType.equals(FieldType.FILE)) {
                fieldModel.setAliasName("alias" + upCaseFirstName(fieldName));
            } else if (fieldType.equals(FieldType.RICHTEXT)) {
                fieldModel.setAliasName("alias" + upCaseFirstName(fieldName));
            } else {
                fieldModel.setAliasName(fieldName);
            }
            fieldModel.setEnName(fieldName);
            fieldModel.setCnName(cnName);
            fieldModel.setFieldType(fieldType.name());
        }
        return Optional.ofNullable(fieldModel);
    }
}
