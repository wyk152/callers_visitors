package com.easymicro.admin.modular.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.easymicro.admin.core.common.constant.factory.PageFactory;
import com.easymicro.core.base.controller.BaseController;
import com.easymicro.core.base.tips.ErrorTip;
import com.easymicro.core.base.tips.Tip;
import com.easymicro.core.https.HttpsUtil;
import com.easymicro.core.wechat.WeChatConstant;
import com.easymicro.persistence.modular.model.system.User;
import com.easymicro.service.modular.business.MerchantService;
import com.easymicro.service.modular.system.UserService;
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
import com.easymicro.persistence.modular.model.business.Merchant;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**************************************
 * 商家控制器
 *@author LinYingQiang
 *@date 2018-08-12 18:18
 *@qq 961410800
 *
 ************************************/
@Controller
@RequestMapping("/merchant")
public class MerchantController extends BaseController {

    private static String PREFIX = "/business/merchant/";


    @Autowired
    private MerchantService companyService;

    @Autowired
    private UserService userService;


    /**
     * 跳转到商家首页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "index.html";
    }

    /**
     * 跳转到商家添加页面
     */
    @RequestMapping(value = "/add_page")
    public String addPage() {
        return PREFIX + "add_page.html";
    }

    /**
     * 跳转到商家编辑页面
     */
    @RequestMapping(value = "/edit_page/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Merchant item = this.companyService.find(id);
        model.addAttribute("item", item);
        return PREFIX + "edit_page.html";
    }

    /**
     * 跳转到商家分配账号
     */
    @RequestMapping(value = "/assign_page/{id}")
    public String assginPage(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        Merchant item = this.companyService.find(id);
        model.addAttribute("selected", JSONObject.toJSONString(item.getBindUsers()));
        List<User> users = userService.findAll((Specification<User>) (root, query, builder) -> {
            Predicate identity = builder.equal(root.get("identity").as(Integer.class), 1);
            Predicate companyIsNull = builder.isNull(root.get("merchant"));
            query.where(identity, companyIsNull);
            return null;
        });
        model.addAttribute("unselected", JSONObject.toJSONString(users));
        return PREFIX + "assign_page.html";
    }

    /**
     * 跳转到商家分配账号
     */
    @RequestMapping(value = "/assign")
    @ResponseBody
    public Object assign(Long id, String userIds) {
        companyService.assign(id,userIds);
        return SUCCESS_TIP;
    }

    /**
     * 查询商家列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(String condition) {
        Pageable page = PageFactory.defaultPage();
        Page<Merchant> p1 = companyService.findAll((Specification<Merchant>) (root, query, builder)->{
            return null;
        },page);
        return super.packForBT(p1);
    }

    /**
     * 新增商家
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Tip add(Merchant item) {
        companyService.insert(item);
        return SUCCESS_TIP;
    }

    /**
     * 修改商家
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Tip edit(Merchant item) {
        companyService.updateSkipNull(item);
        return SUCCESS_TIP;
    }

    /**
     * 删除商家
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String ids) {
        Iterable<String> splitIds = Splitter.on(",").split(ids);
        Iterator<String> iterator = splitIds.iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            if (NumberUtils.isCreatable(id)) {
                companyService.delete(Long.valueOf(id));
            }
        }
        return SUCCESS_TIP;
    }

    /**
     * 验证微信
     */
    @RequestMapping(value = "/valid")
    @ResponseBody
    public Object valid(Long companyId) {
        if (companyId != null) {
            Merchant company = companyService.find(companyId);
            if (company != null) {
                String appId = company.getAppId();
                String appScrect = company.getAppScrect();
                Map<String, String> params = new HashMap<>();
                params.put("appid", company.getAppId());
                params.put("secret", company.getAppScrect());
                params.put("grant_type", "client_credential");
                String requestUrl = WeChatConstant.URL_ACCESS_TOKEN
                        .replaceAll("APPID", company.getAppId())
                        .replaceAll("APPSECRET", company.getAppScrect());
                Map<String, Object> jsonMap = HttpsUtil.executePost(requestUrl, params, Map.class);
                //验证是否合法
                if (jsonMap.containsKey("errcode")) {
                    Tip error = new ErrorTip(500,"errorcode:"+jsonMap.get("errcode")+"msg:"+jsonMap.get("errmsg").toString());
                    return error;
                }
            }
        }
        return SUCCESS_TIP;
    }

    /**
     * 商家excel导出
     */
    @RequestMapping(value = "export")
    public ResponseEntity<byte[]> export() {


        return null;
    }

}
