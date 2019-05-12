package com.easymicro.model;

/**************************************
 * 定义实体类字段
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/
public class FieldModel {

    /**
     * 英文名称
     */
    private String enName;

    /**
     * 英文别名(用于上传 or 富文本)
     */
    private String aliasName;

    /**
     * 中文名称
     */
    private String cnName;

    /**
     * 字段类型
     */
    private String fieldType;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public String toString() {
        return "FieldModel{" +
                "enName='" + enName + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", fieldType=" + fieldType +
                '}';
    }
}
