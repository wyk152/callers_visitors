package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.Department;
import com.easymicro.service.core.IService;

/**************************************
 * 部门service接口
 * @author LinYingQiang
 * @date 2018-09-19 14:34
 * @qq 961410800
 *
************************************/
public interface DepartmentService extends IService<Department,Long> {
    /**
     * 数据同步
     */
    void insertOrUpdate(String data);
}
