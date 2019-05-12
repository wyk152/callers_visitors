package com.easymicro.persistence.modular.model.system;

import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 角色表
 * @author LinYingQiang
 * @date 2018-08-09 18:26
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity {

    /**
     * 序号
     */
    private Integer num;
    /**
     * 父角色id
     */
    private Long pid;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 部门名称
     */
    private Long deptid;
    /**
     * 提示
     */
    private String tips;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDeptid() {
        return deptid;
    }

    public void setDeptid(Long deptid) {
        this.deptid = deptid;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
