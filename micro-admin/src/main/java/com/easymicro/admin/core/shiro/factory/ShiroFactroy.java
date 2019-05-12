package com.easymicro.admin.core.shiro.factory;

import com.easymicro.admin.core.common.constant.factory.ConstantFactory;
import com.easymicro.admin.core.common.constant.state.ManagerStatus;
import com.easymicro.admin.core.shiro.ShiroUser;
import com.easymicro.core.util.Convert;
import com.easymicro.core.util.SpringContextHolder;
import com.easymicro.persistence.modular.model.system.User;
import com.easymicro.service.modular.system.MenuService;
import com.easymicro.service.modular.system.UserService;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class ShiroFactroy implements IShiro {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    public static IShiro me() {
        return SpringContextHolder.getBean(IShiro.class);
    }

    @Override
    public User user(String account) {

        User user = userService.getByAccount(account);

        // 账号不存在
        if (null == user) {
            throw new CredentialsException();
        }
        // 账号被冻结
        if (user.getStatus() != ManagerStatus.OK.getCode()) {
            throw new LockedAccountException();
        }
        return user;
    }

    @Override
    public ShiroUser shiroUser(User user) {
        ShiroUser shiroUser = new ShiroUser();

        shiroUser.setId(user.getId());
        shiroUser.setAccount(user.getAccount());
        shiroUser.setDeptId(user.getDeptid());
        shiroUser.setDeptName(ConstantFactory.me().getDeptName(user.getDeptid()));
        shiroUser.setName(user.getName());
        shiroUser.setIdentity(user.getIdentity());
        shiroUser.setMerchant(user.getMerchant());

        Integer[] roleArray = Convert.toIntArray(user.getRoleid());
        List<Long> roleList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<String>();
        for (int roleId : roleArray) {
            roleList.add(Long.valueOf(roleId));
            roleNameList.add(ConstantFactory.me().getSingleRoleName(Long.valueOf(roleId)));
        }
        shiroUser.setRoleList(roleList);
        shiroUser.setRoleNames(roleNameList);

        return shiroUser;
    }

    @Override
    public List<String> findPermissionsByRoleId(Long roleId) {
        return menuService.getResUrlsByRoleId(roleId);
    }

    @Override
    public String findRoleNameByRoleId(Long roleId) {
        return ConstantFactory.me().getSingleRoleTip(roleId);
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName) {
        String credentials = user.getPassword();

        // 密码加盐处理
        String source = user.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
