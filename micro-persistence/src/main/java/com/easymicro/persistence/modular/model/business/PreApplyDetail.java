package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**************************************
 * 预约详情表
 * @author LinYingQiang
 * @date 2018-09-10 9:54
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_pre_apply_detail")
public class PreApplyDetail extends BaseEntity {

    /**
     * 申请人ID
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * UUID
     */
    @Column(name = "apply_id")
    private String applyid;

    /**
     * 用户手机号
     */
    @Column(name = "utel")
    private String utel;

    /**
     * 	预约人员名称
     */
    @Column(name = "uname")
    private String uname;

    /**
     * 加入时间
     */
    @Column(name = "jointime")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date jointime;

    /**
     * 	0 未授权 1 授权成功 3 平台微信预约的授权结果上传成功
     */
    @Column(name = "rstatus")
    private Integer rstatus;

    /**
     * 关联访客记录id
     */
    @Column(name = "accessrecordid")
    private Integer accessrecordid;

    /**
     * 来源：0 本地 1 线上获取
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 商家编码
     */
    @Column(name = "area_code")
    private String areacode;

    /**
     * 	临时卡号
     */
    @Column(name = "card_num")
    private String cardnum;

    /**
     * 标记时间（当数据更新时自动更新）
     * tabtime = uploadtime  证明记录已经被上传
     */
    @Column(name = "tab_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date tabtime;

    /**
     * 	上传标记(本地上传到平台的时间)
     *
     * 	tabtime = uploadtime  证明记录已经被上传
     */
    @Column(name = "uploadtime")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date uploadtime;

    /**
     * 邀请人openid
     */
    @Column(name = "inviter_open_id")
    private String inviterOpenid;//邀请人openId

    /**
     * 响应消息 邀约时，访客拒绝时的回复
     */
    @Column(name = "resp_msg")
    private String respMsg;

    /**
     * 员工发起邀约，访客是否同意
     */
    @Column(name = "is_confirm")
    private Integer isConfirm;

    /**
     * 是否已经给访客发过推送提醒
     */
    @Column(name = "is_push")
    private Integer isPush;

    /**
     * 父表Group类中的syncId字段
     */
    @Column(name = "sync_id")
    private String syncid;

    /**
     * 是否活动 0-否,1-是
     */
    @Column(name = "is_active")
    private Integer isActive = 0;


    @Transient
    private String companycode;

    @Transient
    private Member wxuser;

    public Member getWxuser() {
        return wxuser;
    }

    public void setWxuser(Member wxuser) {
        this.wxuser = wxuser;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Date getJointime() {
        return jointime;
    }

    public void setJointime(Date jointime) {
        this.jointime = jointime;
    }

    public Integer getRstatus() {
        return rstatus;
    }

    public void setRstatus(Integer rstatus) {
        this.rstatus = rstatus;
    }

    public Integer getAccessrecordid() {
        return accessrecordid;
    }

    public void setAccessrecordid(Integer accessrecordid) {
        this.accessrecordid = accessrecordid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
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

    public String getInviterOpenid() {
        return inviterOpenid;
    }

    public void setInviterOpenid(String inviterOpenid) {
        this.inviterOpenid = inviterOpenid;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public Integer getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(Integer isConfirm) {

        this.isConfirm = isConfirm;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

    public String getSyncid() {
        return syncid;
    }

    public void setSyncid(String syncid) {
        this.syncid = syncid;
    }


}
