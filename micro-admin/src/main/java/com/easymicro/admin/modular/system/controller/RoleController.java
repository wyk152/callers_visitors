package com.easymicro.admin.modular.system.controller;


import com.easymicro.admin.core.common.annotion.BussinessLog;
import com.easymicro.admin.core.common.annotion.Permission;
import com.easymicro.admin.core.common.constant.Const;
import com.easymicro.admin.core.common.constant.cache.Cache;
import com.easymicro.admin.core.common.constant.dictmap.RoleDict;
import com.easymicro.admin.core.common.constant.factory.ConstantFactory;
import com.easymicro.admin.core.common.exception.BizExceptionEnum;
import com.easymicro.admin.core.log.LogObjectHolder;
import com.easymicro.admin.modular.system.warpper.RoleWarpper;
import com.easymicro.core.base.controller.BaseController;
import com.easymicro.core.base.tips.Tip;
import com.easymicro.core.cache.CacheKit;
import com.easymicro.core.exception.GunsException;
import com.easymicro.core.node.ZTreeNode;
import com.easymicro.core.util.Convert;
import com.easymicro.core.util.ToolUtil;
import com.easymicro.persistence.modular.model.system.Role;
import com.easymicro.persistence.modular.model.system.User;
import com.easymicro.service.modular.system.RoleService;
import com.easymicro.service.modular.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 *
 * @author fengshuonan
 * @Date 2017年2月12日21:59:14
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static String PREFIX = "/system/role";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 跳转到角色列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/role.html";
    }

    /**
     * 跳转到添加角色
     */
    @RequestMapping(value = "/role_add")
    public String roleAdd() {
        return PREFIX + "/role_add.html";
    }

    /**
     * 跳转到修改角色
     */
    @Permission
    @RequestMapping(value = "/role_edit/{roleId}")
    public String roleEdit(@PathVariable Long roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Role role = this.roleService.find(roleId);
        model.addAttribute(role);
        model.addAttribute("pName", ConstantFactory.me().getSingleRoleName(role.getPid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(role.getDeptid()));
        LogObjectHolder.me().set(role);
        return PREFIX + "/role_edit.html";
    }

    /**
     * 跳转到角色分配
     */
    @Permission
    @RequestMapping(value = "/role_assign/{roleId}")
    public String roleAssign(@PathVariable("roleId") Long roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        model.addAttribute("roleId", roleId);
        model.addAttribute("roleName", ConstantFactory.me().getSingleRoleName(roleId));
        return PREFIX + "/role_assign.html";
    }

    /**
     * 获取角色列表
     */
    @Permission
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String roleName) {
        List<Map<String, Object>> roles = this.roleService.selectRoles(super.getPara("roleName"));
        return super.warpObject(new RoleWarpper(roles));
    }

    /**
     * 角色新增
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "添加角色", key = "name", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip add(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        role.setId(null);
        this.roleService.insert(role);
        return SUCCESS_TIP;
    }

    /**
     * 角色修改
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改角色", key = "name", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip edit(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.updateSkipNull(role);

        //删除缓存
        CacheKit.removeAll(Cache.CONSTANT);
        return SUCCESS_TIP;
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除角色", key = "roleId", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip remove(@RequestParam Long roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }

        //不能删除超级管理员角色
        if (roleId.equals(Const.ADMIN_ROLE_ID)) {
            throw new GunsException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }

        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        this.roleService.delRoleById(roleId);

        //删除缓存
        CacheKit.removeAll(Cache.CONSTANT);
        return SUCCESS_TIP;
    }

    /**
     * 查看角色
     */
    @RequestMapping(value = "/view/{roleId}")
    @ResponseBody
    public Tip view(@PathVariable Long roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.find(roleId);
        return SUCCESS_TIP;
    }

    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    //@BussinessLog(value = "配置权限", key = "roleId,ids", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip setAuthority(@RequestParam("roleId") Long roleId, @RequestParam("ids") String ids) {
        if (ToolUtil.isOneEmpty(roleId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.setAuthority(roleId, ids);
        return SUCCESS_TIP;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeList")
    @ResponseBody
    public List<ZTreeNode> roleTreeList() {
        List<ZTreeNode> roleTreeList = this.roleService.roleTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeListByUserId/{userId}")
    @ResponseBody
    public List<ZTreeNode> roleTreeListByUserId(@PathVariable Long userId) {
        User theUser = this.userService.find(userId);
        String roleid = theUser.getRoleid();
        if (ToolUtil.isEmpty(roleid)) {
            List<ZTreeNode> roleTreeList = this.roleService.roleTreeList();
            return roleTreeList;
        } else {
            String[] strArray = Convert.toStrArray(",", roleid);
            List<ZTreeNode> roleTreeListByUserId = this.roleService.roleTreeListByRoleId(strArray);
            return roleTreeListByUserId;
        }
    }

}
