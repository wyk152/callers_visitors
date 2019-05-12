package com.easymicro.config;

/**************************************
 *页面配置
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/

public class PageConfig extends ContextConfig{

    private EntityConfig entityConfig;

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    @Override
    public void init() {

    }
}
