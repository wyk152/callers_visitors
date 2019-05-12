package com.easymicro.persistence.modular.model.system;

import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 角色和菜单关联表
 * @author LinYingQiang
 * @date 2018-08-09 18:24
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "sys_relation")
public class Relation extends BaseEntity {

    /**
     * 菜单id
     */
    private Long menuid;
    /**
     * 角色id
     */
    private Long roleid;

    public Long getMenuid() {
        return menuid;
    }

    public void setMenuid(Long menuid) {
        this.menuid = menuid;
    }

    public Long getRoleid() {
        return roleid;
    }

    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }
}
