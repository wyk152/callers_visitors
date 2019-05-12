package com.easymicro.config;

import java.util.ArrayList;
import java.util.List;

/**************************************
 * 生成器主配置
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/

public abstract class ContextConfig {

    /**
     * 文件存放路径
     */
    private String filePath = "D://TEMPLATE//";

    /**
     * 需要导入的包
     */
    protected List<String> imports = new ArrayList<>();

    /**
     * 定义Entity包路径
     */
    protected String basePackage;

    public String getFilePath() {
        return filePath;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public abstract void init();
}
