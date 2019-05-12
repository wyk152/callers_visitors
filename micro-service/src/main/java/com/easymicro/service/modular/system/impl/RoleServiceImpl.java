package com.easymicro.service.modular.system.impl;

import com.easymicro.core.node.ZTreeNode;
import com.easymicro.core.util.Convert;
import com.easymicro.core.util.ObjectUtils;
import com.easymicro.persistence.modular.model.system.Relation;
import com.easymicro.persistence.modular.model.system.Role;
import com.easymicro.persistence.modular.repository.system.RelationRepository;
import com.easymicro.persistence.modular.repository.system.RoleRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.RoleService;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**************************************
 *角色Service实现
 *@author LinYingQiang
 *@date 2018-08-11 00:00
 *@qq 961410800
 *
 ************************************/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleRepository,Role,Long> implements RoleService {

    @Autowired
    private RelationRepository relationRepository;

    @Override
    public void setAuthority(Long roleId, String ids) {
        // 删除该角色所有的权限
        relationRepository.deleteByRoleid(roleId);
        // 添加新的权限
        Relation relation = null;
        for (Long id : Convert.toLongArray(true, (Object[]) Convert.toStrArray(",", ids))) {
            relation = new Relation();
            relation.setRoleid(roleId);
            relation.setMenuid(id);
            relationRepository.save(relation);
        }
    }

    @Override
    public void delRoleById(Long roleId) {
        //删除角色
        baseRepository.deleteById(roleId);
        //删除该角色所有的权限
        relationRepository.deleteByRoleid(roleId);
    }

    @Override
    public List<Map<String, Object>> selectRoles(String condition) {
        List<Role> roles = baseRepository.findAll((Specification<Role>) (root, query, builder) -> {
            if (StringUtils.isNotBlank(condition)) {
                Predicate name = builder.like(root.get("name").as(String.class), "%" + condition + "%");
                return builder.and(name);
            }
            return null;
        });
        return roles.stream().map(ObjectUtils::toMaps).collect(Collectors.toList());
    }

    @Override
    public int deleteRolesById(Long roleId) {
        relationRepository.deleteByRoleid(roleId);
        return 0;
    }

    @Override
    public List<ZTreeNode> roleTreeList() {
        String sql = "select " +
                "id," +
                "pId," +
                "name," +
                "(case when (pId=0 or pId is null) then 'true' else 'false' end) as 'open' " +
                "from sys_role";
        Session session = relationRepository.getEntityManager().unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("pId", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("open", StandardBasicTypes.BOOLEAN);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(ZTreeNode.class));
        List<ZTreeNode> nodes = query.getResultList();
        session.close();
        return nodes;
    }

    @Override
    public List<ZTreeNode> roleTreeListByRoleId(String[] roleId) {
        String sql = "SELECT " +
                "r.id 'id'," +
                "pId 'pId'," +
                "name AS 'name'," +
                "(CASE WHEN (pId = 0 OR pId IS NULL) THEN 'true' ELSE 'false' END) 'open'," +
                "(CASE WHEN (r1.id = 0 OR r1.id IS NULL) THEN 'false' ELSE 'true' END) 'checked' " +
                "FROM sys_role r " +
                "LEFT JOIN " +
                "(SELECT id FROM sys_role WHERE ID IN (:ids)) r1 ON r.id = r1.id " +
                "ORDER BY pId,num ASC";
        Session session = baseRepository.getEntityManager().unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("pId", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("open", StandardBasicTypes.BOOLEAN);
        query.addScalar("checked", StandardBasicTypes.BOOLEAN);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(ZTreeNode.class));
        //准备参数
        String ids = Joiner.on(",").join(roleId);
        query.setParameter("ids", ids);

        List<ZTreeNode> rstList = query.getResultList();
        session.close();
        return rstList;
    }
}
