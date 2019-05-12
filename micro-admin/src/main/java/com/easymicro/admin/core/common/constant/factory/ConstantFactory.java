package com.easymicro.admin.core.common.constant.factory;

import com.easymicro.admin.core.common.constant.cache.Cache;
import com.easymicro.admin.core.common.constant.cache.CacheKey;
import com.easymicro.admin.core.common.constant.state.ManagerStatus;
import com.easymicro.admin.core.common.constant.state.MenuStatus;
import com.easymicro.admin.core.log.LogObjectHolder;
import com.easymicro.core.support.StrKit;
import com.easymicro.core.util.Convert;
import com.easymicro.core.util.SpringContextHolder;
import com.easymicro.core.util.ToolUtil;
import com.easymicro.persistence.modular.model.system.*;
import com.easymicro.service.modular.system.*;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 常量的生产工厂
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

    private RoleService roleService = SpringContextHolder.getBean(RoleService.class);
    private DeptService deptService = SpringContextHolder.getBean(DeptService.class);
    private DictService dictService = SpringContextHolder.getBean(DictService.class);
    private UserService userService = SpringContextHolder.getBean(UserService.class);
    private MenuService menuService = SpringContextHolder.getBean(MenuService.class);

    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    /**
     * 根据用户id获取用户名称
     *
     * @author stylefeng
     * @Date 2017/5/9 23:41
     */
    @Override
    public String getUserNameById(Long userId) {
        User user = userService.find(userId);
        if (user != null) {
            return user.getName();
        } else {
            return "--";
        }
    }

    /**
     * 根据用户id获取用户账号
     *
     * @author stylefeng
     * @date 2017年5月16日21:55:371
     */
    @Override
    public String getUserAccountById(Long userId) {
        User user = userService.find(userId);
        if (user != null) {
            return user.getAccount();
        } else {
            return "--";
        }
    }

    /**
     * 通过角色ids获取角色名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.ROLES_NAME + "'+#roleIds")
    public String getRoleName(String roleIds) {
        if (StringUtils.isBlank(roleIds)) {
            return "";
        }
        Iterable<String> roles = Splitter.on(",").split(roleIds);
        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            if (NumberUtils.isCreatable(role)) {
                Role roleObj = roleService.find(Long.valueOf(role));
                if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
                    sb.append(roleObj.getName()).append(",");
                }
            }

        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 通过角色id获取角色名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    public String getSingleRoleName(Long roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleService.find(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getName();
        }
        return "";
    }

    /**
     * 通过角色id获取角色英文名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_TIP + "'+#roleId")
    public String getSingleRoleTip(Long roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleService.find(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getTips();
        }
        return "";
    }

    /**
     * 获取部门名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
    public String getDeptName(Long deptId) {
        Dept dept = deptService.find(deptId.longValue());
        if (ToolUtil.isNotEmpty(dept) && ToolUtil.isNotEmpty(dept.getFullname())) {
            return dept.getFullname();
        }
        return "";
    }

    /**
     * 获取菜单的名称们(多个)
     */
    @Override
    public String getMenuNames(String menuIds) {
        Integer[] menus = Convert.toIntArray(menuIds);
        StringBuilder sb = new StringBuilder();
        for (Integer menu : menus) {
            Menu menuObj = menuService.find(Long.valueOf(menu));
            if (ToolUtil.isNotEmpty(menuObj) && ToolUtil.isNotEmpty(menuObj.getName())) {
                sb.append(menuObj.getName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 获取菜单名称
     */
    @Override
    public String getMenuName(Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            return "";
        } else {
            Menu menu = menuService.find(menuId);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取菜单名称通过编号
     */
    @Override
    public String getMenuNameByCode(String code) {
        if (ToolUtil.isEmpty(code)) {
            return "";
        } else {
            Menu param = new Menu();
            param.setCode(code);
            Menu menu = menuService.findOne(Example.of(param));
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取字典名称
     */
    @Override
    public String getDictName(Long dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            Dict dict = dictService.find(dictId);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }

    /**
     * 根据字典名称和字典中的值获取对应的名称
     */
    @Override
    public String getDictsByName(String name, Integer val) {
        Dict temp = new Dict();
        temp.setName(name);
        Dict dict = dictService.findOne(Example.of(temp));
        if (dict == null) {
            return "";
        } else {
            Dict tpl = new Dict();
            tpl.setPid(dict.getId());
            Iterable<Dict> dicts = dictService.findAll(Example.of(tpl));
            for (Dict item : dicts) {
                if (item.getNum() != null && item.getNum().equals(val)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 获取性别名称
     */
    @Override
    public String getSexName(Integer sex) {
        return getDictsByName("性别", sex);
    }

    /**
     * 获取用户登录状态
     */
    @Override
    public String getStatusName(Integer status) {
        return ManagerStatus.valueOf(status.intValue());
    }

    /**
     * 获取菜单状态
     */
    @Override
    public String getMenuStatusName(Integer status) {
        return MenuStatus.valueOf(status.intValue());
    }

    /**
     * 查询字典
     */
    @Override
    public List<Dict> findInDict(Long id) {
        if (ToolUtil.isEmpty(id)) {
            return null;
        } else {
            Dict dict = new Dict();
            dict.setPid(id);
            Iterable<Dict> dicts = dictService.findAll(Example.of(dict));
            List<Dict> dicts1 = new ArrayList<>();
            Iterator<Dict> iterator = dicts.iterator();
            while (iterator.hasNext()) {
                dicts1.add(iterator.next());
            }
            return dicts1;
        }
    }

    /**
     * 获取被缓存的对象(用户删除业务)
     */
    @Override
    public String getCacheObject(String para) {
        return LogObjectHolder.me().get().toString();
    }

    /**
     * 获取子部门id
     */
    @Override
    public List<Long> getSubDeptId(Long deptid) {
        List<Dept> depts = deptService.findAll((Specification<Dept>) (root,query,builder) ->{
            Predicate p1 = builder.like(root.get("pids").as(String.class), "%[" + deptid + "]%");
            return builder.and(p1);
        });
        ArrayList<Long> deptids = new ArrayList<>();

        if(depts != null && depts.size() > 0){
            for (Dept dept : depts) {
                deptids.add(dept.getId());
            }
        }

        return deptids;
    }

    /**
     * 获取所有父部门id
     */
    @Override
    public List<Long> getParentDeptIds(Long deptid) {
        Dept dept = deptService.find(deptid);
        String pids = dept.getPids();
        String[] split = pids.split(",");
        ArrayList<Long> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            parentDeptIds.add(Long.valueOf(StrKit.removeSuffix(StrKit.removePrefix(s, "["), "]")));
        }
        return parentDeptIds;
    }


}
