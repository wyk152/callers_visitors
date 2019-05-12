package com.easymicro.config;

import com.easymicro.model.FieldModel;
import com.easymicro.persistence.core.generator.ClassComment;
import com.easymicro.persistence.core.model.BaseEntity;
import com.easymicro.util.ClazzUtil;

import java.util.List;

/**************************************
 * 解析Class存储信息
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/

public class EntityConfig extends ContextConfig {


    /**
     * 定义需要生成的实体类
     */
    private Class<? extends BaseEntity> target;

    /**
     * 中文业务名称
     */
    private String businessName;

    /**
     * 业务CLass名称
     */
    private String clazzName;

    /**
     * 业务Class名称,第一个字母小写
     */
    private String lowerCaseClassName;

    /**
     * 字段模型
     */
    private List<FieldModel> fieldModels;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<FieldModel> getFieldModels() {
        return fieldModels;
    }

    public void setFieldModels(List<FieldModel> fieldModels) {
        this.fieldModels = fieldModels;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public Class<? extends BaseEntity> getTarget() {
        return target;
    }

    public void setTarget(Class<? extends BaseEntity> target) {
        this.target = target;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getLowerCaseClassName() {
        return lowerCaseClassName;
    }

    public void setLowerCaseClassName(String lowerCaseClassName) {
        this.lowerCaseClassName = lowerCaseClassName;
    }

    @Override
    public void init() {
        //3:解析类
        fieldModels = ClazzUtil.parse(target);
        clazzName = target.getSimpleName();
        lowerCaseClassName = ClazzUtil.lowerCaseFirstName(clazzName);

        ClassComment annotation = target.getAnnotation(ClassComment.class);
        if (annotation != null) {
            businessName = annotation.name();
        }
    }
}
