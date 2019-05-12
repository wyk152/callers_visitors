package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.Employee;
import com.easymicro.persistence.modular.repository.business.EmployeeRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.EmployeeService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**************************************
 * 员工service实现类
 * @author LinYingQiang
 * @date 2018-09-10 11:48
 * @qq 961410800
 *
************************************/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeRepository,Employee,Long> implements EmployeeService {


    @Override
    public void insertOrUpdate(String data) {
        List<Employee> employees = JSON.parseArray(data, Employee.class);
        for (Employee employee : employees) {
            employee.setAreaCode(employee.getCompanyNum());
            if (employee.getTabTime() == null || employee.getUploadTime() == null) {
                baseRepository.save(employee);
            } else if (employee.getTabTime().getTime() != employee.getUploadTime().getTime()) {
                Employee update = new Employee();
                update.setAreaCode(employee.getAreaCode());
                update.setId(employee.getId());
                baseRepository.updateSkipNull(Example.of(update),employee);
            }
        }
    }

    @Override
    public Employee findByUid(Long id){
        Employee employee = new Employee();
        employee.setId(id);
        Optional<Employee> optional = baseRepository.findOne(Example.of(employee));
        return optional.orElse(null);
    }
}
