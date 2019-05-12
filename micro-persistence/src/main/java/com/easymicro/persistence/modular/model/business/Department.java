package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 部门
 * @author LinYingQiang
 * @date 2018-09-19 14:31
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_department")
public class Department extends BaseApiModel {

    /**
     * 部门ID
     */
    @Column(name = "id")
    private Integer id;

    /**
     * 部门名称
     */
    @Column(name = "value")
    private String value;

    /**
     * 父级部门ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 同步部门ID
     */
    @Column(name = "sync_id")
    private Integer syncid;

    /**
     * 同步部门父级ID
     */
    @Column(name = "sync_parent_id")
    private Integer syncParentId;

    /**
     * 企业编号
     */
    @Column(name = "company_num")
    private String companyNum;

    /**
     * 数据状态，0：自动识别（新增或更新） 1：新增 2：更新 3：删除
     */
    @Column(name = "sta")
    private String sta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }

    public Integer getSyncParentId() {
        return syncParentId;
    }

    public void setSyncParentId(Integer syncParentId) {
        this.syncParentId = syncParentId;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }
}
