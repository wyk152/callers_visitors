package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.Employee;
import com.easymicro.service.core.IService;

/**************************************
 * 员工Service接口
 * @author LinYingQiang
 * @date 2018-09-10 11:47
 * @qq 961410800
 *
************************************/
public interface EmployeeService extends IService<Employee,Long> {

    /**
     * 数据同步员工接口
     */
    void insertOrUpdate(String data);

    Employee findByUid(Long id);
}
