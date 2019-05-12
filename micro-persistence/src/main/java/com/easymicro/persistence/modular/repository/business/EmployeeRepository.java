package com.easymicro.persistence.modular.repository.business;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.business.Employee;

/**************************************
 * 员工dao接口
 * @author LinYingQiang
 * @date 2018-09-10 11:43
 * @qq 961410800
 *
************************************/
public interface EmployeeRepository extends CustomRepository<Employee,Long> {



    Employee findByPrimaryId(Long primaryId);
}
