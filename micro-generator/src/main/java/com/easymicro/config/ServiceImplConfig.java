package com.easymicro.config;

/**************************************
 * Service配置信息
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/

public class ServiceImplConfig extends ContextConfig {


    private String prefixFileName = "ServiceImpl";

    private EntityConfig entityConfig;

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public String getPrefixFileName() {
        return prefixFileName;
    }

    public void setPrefixFileName(String prefixFileName) {
        this.prefixFileName = prefixFileName;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }



    @Override
    public void init() {
        //1:设置包
        basePackage = "com.easymicro.service.modular.business.impl";
        //2:设置导入
        imports.add("com.easymicro.persistence.modular.model.business." + entityConfig.getClazzName());
        imports.add("com.easymicro.persistence.modular.repository.business." + entityConfig.getClazzName() +"Repository");
        imports.add("com.easymicro.service.core.ServiceImpl");
        imports.add("com.easymicro.service.modular.business." + entityConfig.getClazzName() + "Service");
        imports.add("org.springframework.stereotype.Service");
    }
}
