package com.easymicro.persistence.core.generator;

/**************************************
 * 用户FieldComment上,
 *@author LinYingQiang
 *@date 2018-08-11 21:49
 *@qq 961410800
 *
************************************/
public enum FieldType {

    ID("id",1),//主键字段
    TEXT("text",0),//普通文本,输出input
    IMG("img",0),//图片上传插件,输出fileupload
    FILE("file",0),//文件上传插件,输出fileupload
    RICHTEXT("richtext",0),//富文本插件
    SELECT("select",0), //下拉选项
    CREATETIME("createtime",1),//创建时间
    UPDATETIME("updatetime",1),//创建时间
    VERSION("verison",1), //版本
    OTHER("other", 0);//其他

    /**
     * 名称,如果解析class field的name相同表示命中
     */
    private String name;

    /**
     * 是否要检查  0-不检查,1-检查
     */
    private Integer checked;

    FieldType(String name,Integer checked){
        this.name = name;
        this.checked = checked;
    }

    public Integer getChecked() {
        return checked;
    }

    public String getName() {
        return name;
    }
}
