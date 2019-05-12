package com.easymicro.service.modular.system;

import com.easymicro.core.datascope.DataScope;
import com.easymicro.persistence.modular.model.system.User;
import com.easymicro.service.core.IService;

import java.util.List;
import java.util.Map;

/**************************************
 * 用户Service接口
 * @author LinYingQiang
 * @date 2018-08-09 21:40
 * @qq 961410800
 *
************************************/
public interface UserService extends IService<User,Long> {

    /**
     * 修改用户状态
     */
    void setStatus(Long userId,int status);

    /**
     * 修改密码
     */
    void changePwd(Long userId,String pwd);

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime, Long deptid);

    /**
     * 设置用户的角色
     */
    int setRoles(Long userId, String roleIds);

    /**
     * 通过账号获取用户
     */
    User getByAccount(String account);
}
