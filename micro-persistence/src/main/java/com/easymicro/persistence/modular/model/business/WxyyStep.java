package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信预约生命周期
 */
@Entity
@Table(name = "bus_wxyystep")
public class WxyyStep extends BaseEntity {

    /**
     * 微信预约状态 0 被下载 1 授权卡号完成 2 授权成功并上传 3 访客到达 4 主动注销离开 5 超时注销
     */
    @Column(name = "wxyy_status")
    private Integer wxyystatus;

    /**
     * 维系预约当前状态描述
     */
    @Column(name = "wxyy_remark")
    private String wxyyremark;

    /**
     * 同步ID,和预约主表syncid一致
     */
    @Column(name = "sync_id")
    private String syncid;

    /**
     * 微信预约子表ID
     */
    @Column(name = "wxyy_itemid")
    private String xxyyitemid;

    /**
     * 访问记录id
     */
    @Column(name = "visitor_recordid")
    private String visitorrecordid;

    /**
     * 商家编号
     */
    @Column(name = "area_code")
    private String areacode;

    /**
     * 标记时间（当数据更新时自动更新）
     */
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "tabtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date tabtime;

    /**
     * 上传标记
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "uploadtime")
    private Date uploadtime;

    /**
     * 1 个人 2 团队 3 活动
     */
    @Column(name = "yy_type")
    private Integer yytype;

    @Transient
    private String companycode;

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public Integer getWxyystatus() {
        return wxyystatus;
    }

    public void setWxyystatus(Integer wxyystatus) {
        this.wxyystatus = wxyystatus;
    }

    public String getWxyyremark() {
        return wxyyremark;
    }

    public void setWxyyremark(String wxyyremark) {
        this.wxyyremark = wxyyremark == null ? null : wxyyremark.trim();
    }

    public String getSyncid() {
        return syncid;
    }

    public void setSyncid(String syncid) {
        this.syncid = syncid;
    }

    public String getXxyyitemid() {
        return xxyyitemid;
    }

    public void setXxyyitemid(String xxyyitemid) {
        this.xxyyitemid = xxyyitemid == null ? null : xxyyitemid.trim();
    }

    public String getVisitorrecordid() {
        return visitorrecordid;
    }

    public void setVisitorrecordid(String visitorrecordid) {
        this.visitorrecordid = visitorrecordid == null ? null : visitorrecordid.trim();
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode == null ? null : areacode.trim();
    }

    public Date getTabtime() {
        return tabtime;
    }

    public void setTabtime(Date tabtime) {
        this.tabtime = tabtime;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public Integer getYytype() {
        return yytype;
    }

    public void setYytype(Integer yytype) {
        this.yytype = yytype;
    }
}