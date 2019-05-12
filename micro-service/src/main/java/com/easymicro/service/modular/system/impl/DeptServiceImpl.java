package com.easymicro.service.modular.system.impl;

import com.easymicro.core.node.ZTreeNode;
import com.easymicro.core.util.ObjectUtils;
import com.easymicro.persistence.modular.model.system.Dept;
import com.easymicro.persistence.modular.repository.system.DeptRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.DeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**************************************
 * 部门Service实现
 * @author LinYingQiang
 * @date 2018-08-10 0:39
 * @qq 961410800
 *
************************************/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptRepository,Dept,Long> implements DeptService {

    @Override
    public void deleteDept(Long deptId) {
        Dept dept = baseRepository.getOne(deptId);
        if (dept != null) {
            Dept deptTpl = new Dept();
            deptTpl.setPids("%[" + dept.getId() + "]%");
            Example<Dept> deptExample = Example.of(deptTpl);
            List<Dept> depts = findAll(deptExample);
            depts.forEach(this::delete);
            delete(dept);
        }
    }

    @Override
    public List<ZTreeNode> tree() {
        List<Dept> depts = findAll();
        List<ZTreeNode> zTreeNodes = new ArrayList<>(depts.size());
        depts.forEach(d -> {
            ZTreeNode tmp = new ZTreeNode();
            if (d.getPid() == null || d.getPid().equals(0)) {
                tmp.setIsOpen(true);
            }else{
                tmp.setIsOpen(false);
            }
            tmp.setId(d.getId());
            tmp.setName(d.getSimplename());
            tmp.setpId(d.getPid().longValue());
            zTreeNodes.add(tmp);
        });
        return zTreeNodes;
    }

    @Override
    public List<Map<String, Object>> list(String condition) {
        List<Dept> depts = findAll((Specification<Dept>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(condition)) {
                Predicate p1 = builder.like(root.get("simplename").as(String.class), "%" + condition + "%");
                Predicate p2 = builder.like(root.get("fullname").as(String.class), "%" + condition + "%");
                Predicate predicate = builder.or(p1, p2);
                predicates.add(predicate);
            }
            return builder.and(predicates.stream().toArray(Predicate[]::new));
        }, new Sort(Sort.Direction.ASC, "num"));

        return depts.stream().map(ObjectUtils::toMaps).collect(Collectors.toList());
    }

    @Override
    public List<Long> selectByPids(Long id) {
        List<Dept> depts = baseRepository.findDeptsByPidsLike("%[" + id + "]%");
        return depts.stream().map(Dept::getId).collect(Collectors.toList());
    }
}
