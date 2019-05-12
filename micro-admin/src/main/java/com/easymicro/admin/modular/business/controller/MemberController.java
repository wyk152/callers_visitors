package com.easymicro.admin.modular.business.controller;

import com.easymicro.admin.core.common.constant.factory.PageFactory;
import com.easymicro.core.base.controller.BaseController;
import com.easymicro.core.base.tips.Tip;
import com.easymicro.service.modular.business.MemberService;
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
import com.easymicro.persistence.modular.model.business.Member;

import java.util.Iterator;

/**************************************
 * 用户控制器
 *@author LinYingQiang
 *@date 2018-08-12 18:18
 *@qq 961410800
 *
 ************************************/
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

    private static String PREFIX = "/business/member/";


    @Autowired
    private MemberService memberService;


    /**
     * 跳转到用户首页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "index.html";
    }

    /**
     * 跳转到用户添加页面
     */
    @RequestMapping(value = "/add_page")
    public String addPage() {
        return PREFIX + "add_page.html";
    }

    /**
     * 跳转到用户编辑页面
     */
    @RequestMapping(value = "/edit_page/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Member item = this.memberService.find(id);
        model.addAttribute("item", item);
        return PREFIX + "edit_page.html";
    }

    /**
     * 查询用户列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(String condition) {
        Pageable page = PageFactory.defaultPage();
        Page<Member> p1 = memberService.findAll((Specification<Member>) (root,query,builder)->{
            return null;
        },page);
        return super.packForBT(p1);
    }

    /**
     * 新增用户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Tip add(Member item) {
        memberService.insert(item);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Tip edit(Member item) {
        memberService.updateSkipNull(item);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String ids) {
        Iterable<String> splitIds = Splitter.on(",").split(ids);
        Iterator<String> iterator = splitIds.iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            if (NumberUtils.isCreatable(id)) {
                memberService.delete(Long.valueOf(id));
            }
        }
        return SUCCESS_TIP;
    }

    /**
     * 用户excel导出
     */
    @RequestMapping(value = "export")
    public ResponseEntity<byte[]> export() {


        return null;
    }

}
