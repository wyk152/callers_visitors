package com.easymicro.config;

/**************************************
 * Controller配置信息
 *@author LinYingQiang
 *@date 2018-08-11 22:22
 *@qq 961410800
 *
 ************************************/
public class ControllerConfig extends ContextConfig {

    private EntityConfig entityConfig;

    private String prefixFileName = "Controller";

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public String getPrefixFileName() {
        return prefixFileName;
    }

    public void setPrefixFileName(String prefixFileName) {
        this.prefixFileName = prefixFileName;
    }

    @Override
    public void init() {
        //1:设置包
        basePackage = "com.easymicro.admin.modular.business";
        //2:设置导入
        imports.add("com.easymicro.admin.core.common.constant.factory.PageFactory");
        imports.add("com.easymicro.core.base.controller.BaseController");
        imports.add("com.easymicro.core.base.tips.Tip");
        imports.add("com.easymicro.service.modular.business." + entityConfig.getClazzName() + "Service");
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add("org.springframework.data.domain.Page");
        imports.add("org.springframework.data.domain.Pageable");
        imports.add("org.springframework.data.jpa.domain.Specification");
        imports.add("org.springframework.http.ResponseEntity");
        imports.add("org.springframework.stereotype.Controller");
        imports.add("org.springframework.ui.Model");
        imports.add("org.springframework.web.bind.annotation.PathVariable");
        imports.add("org.springframework.web.bind.annotation.RequestMapping");
        imports.add("org.springframework.web.bind.annotation.ResponseBody");
        imports.add("com.easymicro.persistence.modular.model.business." + entityConfig.getClazzName());
    }
}
