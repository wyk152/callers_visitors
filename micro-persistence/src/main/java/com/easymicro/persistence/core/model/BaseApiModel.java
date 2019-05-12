package com.easymicro.persistence.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**************************************
 * 基础实体类
 * @author LinYingQiang
 * @date 2018-08-09 10:51
 * @qq 961410800
 *
************************************/
@MappedSuperclass
public class BaseApiModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 平台表主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_id")
    private Long primaryId;

    /**
     * 公司编码
     */
    @Column(name = "company_code")
    private String companyCode;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 商家编号
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 区域名称
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 终端编码
     */
    @Column(name = "terminal_code")
    private String terminalCode;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tab_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date tabTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date uploadTime;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "native_create_date")
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date nativeCreateDate;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "native_update_date")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date nativeUpdateDate;

    @Column(name = "operate_status")
    private Integer operateStatus; //0新增  1修改  2删除 3查询


    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public Date getTabTime() {
        return tabTime;
    }

    public void setTabTime(Date tabTime) {
        this.tabTime = tabTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Date getNativeCreateDate() {
        return nativeCreateDate;
    }

    public void setNativeCreateDate(Date nativeCreateDate) {
        this.nativeCreateDate = nativeCreateDate;
    }

    public Date getNativeUpdateDate() {
        return nativeUpdateDate;
    }

    public void setNativeUpdateDate(Date nativeUpdateDate) {
        this.nativeUpdateDate = nativeUpdateDate;
    }

    public Integer getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(Integer operateStatus) {
        this.operateStatus = operateStatus;
    }


}
