package com.easymicro.admin.modular.business.controller;

import com.easymicro.admin.core.common.constant.factory.PageFactory;
import com.easymicro.core.base.controller.BaseController;
import com.easymicro.core.base.tips.Tip;
import com.easymicro.service.modular.business.ConfigService;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.easymicro.persistence.modular.model.business.Config;

import java.util.Iterator;

/**************************************
 * 控制器
 *@author LinYingQiang
 *@date 2018-08-12 18:18
 *@qq 961410800
 *
 ************************************/
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {

    private static String PREFIX = "/business/config/";


    @Autowired
    private ConfigService configService;


    /**
     * 跳转到首页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "index.html";
    }

    /**
     * 跳转到添加页面
     */
    @RequestMapping(value = "/add_page")
    public String addPage() {
        return PREFIX + "add_page.html";
    }

    /**
     * 跳转到编辑页面
     */
    @RequestMapping(value = "/edit_page/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Config item = this.configService.find(id);
        model.addAttribute("item", item);
        return PREFIX + "edit_page.html";
    }

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(String condition) {
        Pageable page = PageFactory.defaultPage();
        Page<Config> p1 = configService.findAll((Specification<Config>) (root,query,builder)->{
            return null;
        },page);
        return super.packForBT(p1);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Tip add(Config item) {
        configService.insert(item);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Tip edit(Config item) {
        configService.updateSkipNull(item);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String ids) {
        Iterable<String> splitIds = Splitter.on(",").split(ids);
        Iterator<String> iterator = splitIds.iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            if (NumberUtils.isCreatable(id)) {
                configService.delete(Long.valueOf(id));
            }
        }
        return SUCCESS_TIP;
    }

    /**
     * excel导出
     */
    @RequestMapping(value = "export")
    public ResponseEntity<byte[]> export() {


        return null;
    }

}
