package com.easymicro.persistence.modular.repository.system;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.system.Relation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**************************************
 * 角色-权限关联DAO接口
 * @author LinYingQiang
 * @date 2018-08-09 20:14
 * @qq 961410800
 *
************************************/
public interface RelationRepository extends CustomRepository<Relation,Long> {

    void deleteByMenuid(Long menuId);

    List<Relation> findByRoleid(Long roleId);

    void deleteByRoleid(Long roleId);
}
