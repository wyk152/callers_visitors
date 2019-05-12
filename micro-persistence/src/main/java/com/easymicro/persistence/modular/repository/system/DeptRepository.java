package com.easymicro.persistence.modular.repository.system;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.system.Dept;
import com.easymicro.persistence.modular.model.system.User;

import java.util.List;

/**************************************
 * 部门DAO接口
 * @author LinYingQiang
 * @date 2018-08-09 20:09
 * @qq 961410800
 *
************************************/
public interface DeptRepository extends CustomRepository<Dept,Long> {

    /**
     * 根据pids查询
     * @param ids '%[', #{deptid}, ']%'
     */
    List<Dept> findDeptsByPidsLike(String ids);
}
