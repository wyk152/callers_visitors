package com.easymicro.service.modular.system.impl;

import com.easymicro.core.node.MenuNode;
import com.easymicro.core.node.ZTreeNode;
import com.easymicro.core.util.ObjectUtils;
import com.easymicro.persistence.modular.model.system.Menu;
import com.easymicro.persistence.modular.model.system.Relation;
import com.easymicro.persistence.modular.repository.system.MenuRepository;
import com.easymicro.persistence.modular.repository.system.RelationRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**************************************
 * 菜单Service实现类
 *@author LinYingQiang
 *@date 2018-08-10 22:22
 *@qq 961410800
 *
 ************************************/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuRepository,Menu,Long> implements MenuService {

    @Autowired
    private RelationRepository relationRepository;

    @Override
    public void delMenu(Long menuId) {
        //删除菜单
        baseRepository.deleteById(menuId);
        //删除关联的relation
        relationRepository.deleteByRoleid(menuId);
    }

    @Override
    public void delMenuContainSubMenus(Long menuId) {
        Menu menu = find(menuId);
        //删除当前菜单
        if (menu != null) {
            delete(menu);
        }
        //删除所有子菜单
        List<Menu> menus = findAll((Specification<Menu>)
                (root, query, builder) -> builder.like(root.get("pcodes").as(String.class), "%[" + menu.getCode() + "]%"));
        if (menus != null && menus.size() > 0) {
            menus.forEach(this::delete);
        }
    }

    @Override
    public List<Map<String, Object>> selectMenus(String condition, String level) {
        List<Menu> menus = baseRepository.findAll((Specification<Menu>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status = 1
            Predicate predicate = builder.equal(root.get("status").as(Integer.class), 1);
            predicates.add(predicate);
            //name like CONCAT('%',#{condition},'%') or code like CONCAT('%',#{condition},'%')
            if (StringUtils.isNotBlank(condition)) {
                Predicate p1 = builder.like(root.get("name").as(String.class), "%" + condition + "%");
                Predicate p2 = builder.like(root.get("code").as(String.class), "%" + condition + "%");
                predicate = builder.or(p1, p2);
                predicates.add(predicate);
            }
            //levels = #{level}
            if (StringUtils.isNotBlank(level)) {
                predicate = builder.equal(root.get("levels").as(String.class), level);
                predicates.add(predicate);
            }
            return builder.and(predicates.toArray(new Predicate[]{}));
        });
        return ObjectUtils.toMaps(menus);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        List<Relation> relations = relationRepository.findByRoleid(roleId);
        return relations.stream().map(Relation::getMenuid).collect(Collectors.toList());
    }

    @Override
    public List<ZTreeNode> menuTreeList() {
        EntityManager entityManager = baseRepository.getEntityManager();
        String nativeSQL = "SELECT m1.id AS id," +
                "(CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 0 ELSE m2.id END ) AS pId," +
                " m1.name AS name, " +
                "(CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 'true' ELSE 'false' END) as isOpen " +
                "FROM sys_menu m1 " +
                "LEFT join sys_menu m2 ON m1.pcode = m2.code " +
                "ORDER BY m1.id ASC";
        Session session = entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(nativeSQL);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("pId", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("isOpen", StandardBasicTypes.BOOLEAN);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(ZTreeNode.class));
        List<ZTreeNode> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds) {
        String sql = "SELECT m1.id AS id, " +
                "(CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 0 ELSE m2.id END) AS pId, " +
                "m1.name AS name," +
                "(CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 'true' ELSE 'false' END) as isOpen," +
                "(CASE WHEN (m3.id = 0 OR m3.id IS NULL) THEN 'false' ELSE 'true' END) as 'checked' " +
                "FROM sys_menu m1 LEFT JOIN sys_menu m2 ON m1.pcode = m2.code " +
                "LEFT JOIN (SELECT id FROM sys_menu WHERE id IN (:ids)) m3 on m1.id = m3.id " +
                "ORDER BY m1.id ASC";
        Session session = baseRepository.getEntityManager().unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("pId", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("isOpen", StandardBasicTypes.BOOLEAN);
        query.addScalar("checked", StandardBasicTypes.BOOLEAN);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(ZTreeNode.class));
        query.setParameter("ids", menuIds);
        List<ZTreeNode> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public int deleteRelationByMenu(Long menuId) {
        relationRepository.deleteByMenuid(menuId);
        return 0;
    }

    @Override
    public List<String> getResUrlsByRoleId(Long roleId) {
        String sql = "select url from sys_relation rel inner join sys_menu m on rel.menuid = m.id where rel.roleid = :roleId";
        Session session = baseRepository.getEntityManager().unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        query.addScalar("url", StandardBasicTypes.STRING);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("roleId", roleId);
        List resultList = query.getResultList();
        List<String> stringList = new ArrayList<>(resultList.size());
        for (Object o : resultList) {
            if (o instanceof Map) {
                Map<String, Object> row = (Map<String, Object>) o;
                stringList.add((String) row.get("url"));
            }
        }
        return stringList;
    }

    @Override
    public List<MenuNode> getMenusByRoleIds(List<Long> roleIds) {
        String sql = "SELECT " +
                "m1.id AS id," +
                "m1.icon AS icon," +
                "(CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 0 ELSE m2.id END) AS parentId," +
                "m1.name as name," +
                "m1.url as url," +
                "m1.levels as levels," +
                "m1.ismenu as ismenu," +
                "m1.num as num " +
                "FROM sys_menu m1 " +
                "LEFT join sys_menu m2 ON m1.pcode = m2.code " +
                "INNER JOIN " +
                "(SELECT ID FROM sys_menu WHERE ID IN " +
                "(SELECT menuid FROM sys_relation rela WHERE rela.roleid IN (:ids))) m3 ON m1.id = m3.id " +
                "WHERE m1.ismenu = 1 ORDER BY levels,num asc";
        Session session = baseRepository.getEntityManager().unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("icon", StandardBasicTypes.STRING);
        query.addScalar("parentId", StandardBasicTypes.LONG);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.addScalar("url", StandardBasicTypes.STRING);
        query.addScalar("levels", StandardBasicTypes.INTEGER);
        query.addScalar("ismenu", StandardBasicTypes.INTEGER);
        query.addScalar("num", StandardBasicTypes.INTEGER);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(MenuNode.class));
        query.setParameter("ids", roleIds);
        List<MenuNode> resultList = query.getResultList();
        return resultList;
    }
}
