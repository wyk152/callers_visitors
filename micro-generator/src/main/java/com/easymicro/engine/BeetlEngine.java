package com.easymicro.engine;

import com.easymicro.config.*;
import com.easymicro.core.util.ToolUtil;
import com.easymicro.persistence.core.model.BaseEntity;
import com.sun.javafx.PlatformUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**************************************
 *
 *@author LinYingQiang
 *@date 2018-08-11 23:23
 *@qq 961410800
 *
 ************************************/

public class BeetlEngine {

    private GroupTemplate groupTemplate;


    private static EntityConfig entityConfig = new EntityConfig();
    private static DaoConfig daoConfig = new DaoConfig();
    private static ServiceConfig serviceConfig = new ServiceConfig();
    private static ServiceImplConfig serviceImplConfig = new ServiceImplConfig();
    private static ControllerConfig controllerConfig = new ControllerConfig();
    private static PageConfig pageConfig = new PageConfig();


    public BeetlEngine(Class<? extends BaseEntity> clazz) {
        Properties properties = new Properties();
        properties.put("RESOURCE.root", "");
        properties.put("DELIMITER_STATEMENT_START", "<%");
        properties.put("DELIMITER_STATEMENT_END", "%>");
        properties.put("HTML_TAG_FLAG", "##");
        Configuration cfg = null;
        try {
            cfg = new Configuration(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
        groupTemplate = new GroupTemplate(resourceLoader, cfg);
        groupTemplate.registerFunctionPackage("tool", new ToolUtil());

        //1:指定Entity实体配置
        entityConfig.setTarget(clazz);

        //3:复制数据
        daoConfig.setEntityConfig(entityConfig);
        serviceConfig.setEntityConfig(entityConfig);
        serviceImplConfig.setEntityConfig(entityConfig);
        controllerConfig.setEntityConfig(entityConfig);
        pageConfig.setEntityConfig(entityConfig);

        //2:启动所有config类init方法
        entityConfig.init();
        daoConfig.init();
        serviceConfig.init();
        serviceImplConfig.init();
        controllerConfig.init();
        pageConfig.init();

    }

    protected void configTemplate(Template template) {
        template.binding("controller", controllerConfig);
        template.binding("entity", entityConfig);
        template.binding("dao", daoConfig);
        template.binding("service", serviceConfig);
        template.binding("serviceImpl", serviceImplConfig);
        template.binding("page", pageConfig);
    }


    public void generateFile(String template, String filePath) {
        Template pageTemplate = groupTemplate.getTemplate(template);
        configTemplate(pageTemplate);
        if (PlatformUtil.isWindows()) {
            filePath = filePath.replaceAll("/+|\\\\+", "\\\\");
        } else {
            filePath = filePath.replaceAll("/+|\\\\+", "/");
        }
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            pageTemplate.renderTo(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void build() {
        //生成repository代码
        generateFile("/template/repository/repository.btl",
                daoConfig.getFilePath() + entityConfig.getClazzName() + daoConfig.getPrefixFileName() + ".java");
        System.out.println("生成repository接口成功");
        generateFile("/template/service/service.btl",
                serviceConfig.getFilePath() + entityConfig.getClazzName() + serviceConfig.getPrefixFileName() + ".java");
        System.out.println("生成service接口成功");
        generateFile("/template/service/serviceimpl.btl",
                serviceImplConfig.getFilePath() + entityConfig.getClazzName() + serviceImplConfig.getPrefixFileName() + ".java");
        System.out.println("生成serviceImpl实现成功");
        generateFile("/template/controller/controller.btl",
                controllerConfig.getFilePath() + entityConfig.getClazzName() + controllerConfig.getPrefixFileName() + ".java");
        System.out.println("生成controller实现成功");
        generateFile("/template/page/index.btl",
                pageConfig.getFilePath() + "/page/" + entityConfig.getLowerCaseClassName() + "/index.html");
        System.out.println("生成index_page成功");
        generateFile("/template/page/index_js.btl",
                pageConfig.getFilePath() + "/js/" + entityConfig.getLowerCaseClassName() + "/index.js");
        System.out.println("生成index_js成功");
        generateFile("/template/page/add_page.btl",
                pageConfig.getFilePath() + "/page/" + entityConfig.getLowerCaseClassName() + "/add_page.html");
        System.out.println("生成add_page成功");
        generateFile("/template/page/edit_page.btl",
                pageConfig.getFilePath() + "/page/" + entityConfig.getLowerCaseClassName() + "/edit_page.html");
        System.out.println("生成edit_page成功");
        generateFile("/template/page/page_info_js.btl",
                pageConfig.getFilePath() + "/js/" + entityConfig.getLowerCaseClassName() + "/page_info.js");
        System.out.println("生成page_info_js成功");
    }
}
