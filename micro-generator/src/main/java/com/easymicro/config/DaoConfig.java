package com.easymicro.config;

/**************************************
 *
 *@author LinYingQiang
 *@date 2018-08-12 16:16
 *@qq 961410800
 *
 ************************************/

public class DaoConfig extends ContextConfig {


    private String prefixFileName = "Repository";

    private EntityConfig entityConfig;

    public String getPrefixFileName() {
        return prefixFileName;
    }

    public void setPrefixFileName(String prefixFileName) {
        this.prefixFileName = prefixFileName;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    @Override
    public void init() {
        //1:设置包
        basePackage = "com.easymicro.persistence.modular.repository.business";
        //2:设置导入
        imports.add("com.easymicro.persistence.core.repository.CustomRepository");
        imports.add("com.easymicro.persistence.modular.model.business." + entityConfig.getClazzName());
    }
}
