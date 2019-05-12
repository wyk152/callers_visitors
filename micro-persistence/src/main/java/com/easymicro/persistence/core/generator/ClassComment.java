package com.easymicro.persistence.core.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**************************************
 * 标注在类上的注解,
 *@author LinYingQiang
 *@date 2018-08-11 22:02
 *@qq 961410800
 *
************************************/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassComment {


    String name();

}
