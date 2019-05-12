package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.Department;
import com.easymicro.persistence.modular.repository.business.DepartmentRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.DepartmentService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 部门service实现类
 * @author LinYingQiang
 * @date 2018-09-19 14:34
 * @qq 961410800
 *
************************************/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentRepository,Department,Long> implements DepartmentService {

    @Override
    public void insertOrUpdate(String data) {
        List<Department> departments = JSON.parseArray(data, Department.class);
        for (Department department : departments) {
            department.setAreaCode(department.getCompanyCode());
            if (department.getTabTime() == null || department.getUploadTime() == null) {
                baseRepository.save(department);
            } else if (department.getTabTime().getTime() != department.getUploadTime().getTime()) {
                Department update = new Department();
                update.setId(department.getId());
                update.setAreaCode(department.getAreaCode());
                baseRepository.updateSkipNull(Example.of(update), department);
            }
        }
    }
}
