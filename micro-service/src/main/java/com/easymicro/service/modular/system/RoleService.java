package com.easymicro.service.modular.system;

import com.easymicro.core.node.ZTreeNode;
import com.easymicro.persistence.modular.model.system.Role;
import com.easymicro.service.core.IService;

import java.util.List;
import java.util.Map;

/**************************************
 * 角色serivce接口
 *@author LinYingQiang
 *@date 2018-08-11 0:04
 *@qq 961410800
 *
************************************/
public interface RoleService  extends IService<Role,Long>{


    /**
     * 设置某个角色的权限
     */
    void setAuthority(Long roleId, String ids);

    /**
     * 删除角色
     */
    void delRoleById(Long roleId);

    /**
     * 根据条件查询角色列表
     */
    List<Map<String, Object>> selectRoles(String condition);

    /**
     * 删除某个角色的所有权限
     */
    int deleteRolesById(Long roleId);

    /**
     * 获取角色列表树
     */
    List<ZTreeNode> roleTreeList();

    /**
     * 获取角色列表树
     */
    List<ZTreeNode> roleTreeListByRoleId(String[] roleId);
}
