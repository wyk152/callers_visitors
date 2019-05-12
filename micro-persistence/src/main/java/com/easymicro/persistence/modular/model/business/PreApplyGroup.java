package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**************************************
 * 预约主表记录
 * @author LinYingQiang
 * @date 2018-09-08 11:28
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_pre_apply_group")
public class PreApplyGroup extends BaseEntity {

    /**
     * 员工id
     */
    @Column(name = "uid")
    private Long uid;


    @Transient
    private String companyCode;

    /**
     * 申请人微信openId
     */
    @Column(name = "apply_open_id")
    private String applyopenid;

    /**
     * 商家编码
     */
    @Column(name = "area_code")
    private String areacode;

    /**
     * 商家信息
     */
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    /**
     * 预约原因
     */
    @Column(name = "reason")
    private String reson;

    /**
     * 提交状态：0-全部,1-待审批,2-已审批,3-未通过
     */
    @Column(name = "status",columnDefinition = "tinyint(1) DEFAULT 0")
    private Integer status;

    /**
     * 审批结果
     */
    @Column(name = "response_msg")
    private String responsemsg;

    /**
     * 来源：0 本地 1 线上获取
     */
    @Column(name = "type",columnDefinition = "tinyint(1) DEFAULT 0")
    private Integer type;

    /**
     * 开始时间
     */
    @Column(name = "starttime")
    private String starttime;

    /**
     * 结束时间
     */
    @Column(name = "endtime")
    private String endtime;

    /**
     * 标记时间（当数据更新时自动更新）
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tab_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date tabTime;

    /**
     * 上传时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "uploadtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date uploadtime;

    /**
     * 来访单位
     */
    @Column(name = "unit")
    private String unit;

    /**
     * 是否是团队预约 1 是 0 否
     */
    @Column(name = "is_team")
    private Integer isteam;

    /**
     * 	0 不限制 其他限制并且type=3是活动
     */
    @Column(name = "max_person_num")
    private Integer maxpersonnum;

    /**
     * 加入的人数
     */
    @Column(name = "join_person_num")
    private Integer jionpersonnum;

    /**
     * 城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 0 个人主动发起 1 邀约 2 代预约
     */
    @Column(name = "launch_type")
    private Integer launchType;

    /**
     * 是否活动 0- 不是活动, 1-是活动
     */
    @Column(name = "is_active")
    private Integer isActive;

    /**
     * 主表唯一id,手机端可以发起预约，存在云平台，商家端电脑也可以发起预约，存在商家端，最终数据要汇总到平台的数据库中，如果用主键ID，会有重复情况，所以加了个syncid作为标识
     */
    @Column(name = "sync_id")
    private String syncid;

    /**
     * 存放预约申请子表(个人申请的话只有一个,团队,活动会存在N个)
     */
    @Transient
    private List<PreApplyDetail> preapplygoupdetail;

    /**
     * 存放预约申请人(个人申请的话只有一个,团队,活动会存在N个不同用户)
     */
    @Transient
    private List<Member> members;

    @Transient
    private String cardNum;//卡号

    @Transient
    private PreApplyDetail detail;

    @Transient
    private Integer isJoin;//数据库无此字段，方便查询结果映射,是否加入活动 1 是 0 否

    @Transient
    private String applyId;//根据不同的openId查询出子表的applyId

    @Transient
    private String applyname; //申请人，数据库中没有

    @Transient
    private String toname;//被访人，数据库中没有

    @Transient
    private String fromname;//来访人，数据库中没有

    @Transient
    private Integer applyCount;//数据库无此字段，方便查询结果映射

    /**
     * 活动
     */
    @Column(name = "activity_logo")
    private String activitylogo;

    /**
     * 活动图片的绝对路径,数据库没有，方便前端显示
     */
    @Column(name = "activity_logo_path")
    private String activitylogoAbsolutePath;

    /**
     * 标题
     */
    @Column(name = "activity_title")
    private String activityTitle;

    /**
     * 子标题
     */
    @Column(name = "sub_title")
    private String subtitle;

    @Column(name = "address")
    private String address;

    @Column(name = "organizer")
    private String organizer;

    @Column(name = "content")
    private String content;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getApplyopenid() {
        return applyopenid;
    }

    public void setApplyopenid(String applyopenid) {
        this.applyopenid = applyopenid;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponsemsg() {
        return responsemsg;
    }

    public void setResponsemsg(String responsemsg) {
        this.responsemsg = responsemsg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Date getTabTime() {
        return tabTime;
    }

    public void setTabTime(Date tabTime) {
        this.tabTime = tabTime;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getIsteam() {
        return isteam;
    }

    public void setIsteam(Integer isteam) {
        this.isteam = isteam;
    }

    public Integer getMaxpersonnum() {
        return maxpersonnum;
    }

    public void setMaxpersonnum(Integer maxpersonnum) {
        this.maxpersonnum = maxpersonnum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getLaunchType() {
        return launchType;
    }

    public void setLaunchType(Integer launchType) {
        this.launchType = launchType;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getSyncid() {
        return syncid;
    }

    public void setSyncid(String syncid) {
        this.syncid = syncid;
    }

    public List<PreApplyDetail> getPreapplygoupdetail() {
        return preapplygoupdetail;
    }

    public void setPreapplygoupdetail(List<PreApplyDetail> preapplygoupdetail) {
        this.preapplygoupdetail = preapplygoupdetail;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public PreApplyDetail getDetail() {
        return detail;
    }

    public void setDetail(PreApplyDetail detail) {
        this.detail = detail;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getActivitylogo() {
        return activitylogo;
    }

    public void setActivitylogo(String activitylogo) {
        this.activitylogo = activitylogo;
    }

    public String getActivitylogoAbsolutePath() {
        return activitylogoAbsolutePath;
    }

    public void setActivitylogoAbsolutePath(String activitylogoAbsolutePath) {
        this.activitylogoAbsolutePath = activitylogoAbsolutePath;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(Integer isJoin) {
        this.isJoin = isJoin;
    }

    public String getApplyname() {
        return applyname;
    }

    public void setApplyname(String applyname) {
        this.applyname = applyname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }

    public Integer getJionpersonnum() {
        return jionpersonnum;
    }

    public void setJionpersonnum(Integer jionpersonnum) {
        this.jionpersonnum = jionpersonnum;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }
}
