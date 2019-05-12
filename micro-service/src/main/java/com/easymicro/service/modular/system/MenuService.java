package com.easymicro.service.modular.system;

import com.easymicro.core.node.MenuNode;
import com.easymicro.core.node.ZTreeNode;
import com.easymicro.persistence.modular.model.system.Menu;
import com.easymicro.service.core.IService;

import java.util.List;
import java.util.Map;

/**************************************
 * 菜单Service接口
 *@author LinYingQiang
 *@date 2018-08-10 22:22
 *@qq 961410800
 *
 ************************************/
public interface MenuService extends IService<Menu,Long> {

    /**
     * 删除菜单
     */
    void delMenu(Long menuId);

    /**
     * 删除菜单包含所有子菜单
     */
    void delMenuContainSubMenus(Long menuId);

    /**
     * 根据条件查询菜单
     */
    List<Map<String, Object>> selectMenus(String condition,String level);

    /**
     * 根据条件查询菜单
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 获取菜单列表树
     */
    List<ZTreeNode> menuTreeList();

    /**
     * 获取菜单列表树
     */
    List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds);

    /**
     * 删除menu关联的relation
     */
    int deleteRelationByMenu(Long menuId);

    /**
     * 获取资源url通过角色id
     */
    List<String> getResUrlsByRoleId(Long roleId);

    /**
     * 根据角色获取菜单
     */
    List<MenuNode> getMenusByRoleIds(List<Long> roleIds);
}
