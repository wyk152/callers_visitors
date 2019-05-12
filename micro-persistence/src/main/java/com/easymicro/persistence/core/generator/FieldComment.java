package com.easymicro.persistence.core.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**************************************
 *用于代码生成器,用于字段上注解
 *@author LinYingQiang
 *@date 2018-08-11 21:42
 *@qq 961410800
 *
************************************/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldComment {

    String name();

    FieldType fieType() default FieldType.TEXT;

}
