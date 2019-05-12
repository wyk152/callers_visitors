package com.easymicro.persistence.modular.model.business;


import com.easymicro.persistence.core.generator.ClassComment;
import com.easymicro.persistence.core.generator.FieldComment;
import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;


/**************************************
 * 用户(微信端)
 * @author LinYingQiang
 * @date 2018-08-28 22:15
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_member")
@ClassComment(name = "用户")
public class Member extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 微信昵称
     */
    @Column(name = "nick_name")
    @FieldComment(name = "微信昵称")
    private String nickName;

    /**
     * 绑定微信
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 姓名
     */
    @Column(name = "name")
    @FieldComment(name = "姓名")
    private String name;

    /**
     * 手机
     */
    @Column(name = "phone")
    @FieldComment(name = "手机")
    private String phone;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    @FieldComment(name = "部门名称")
    private String deptName;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    @FieldComment(name = "公司名称")
    private String companyName;

    /**
     * 公司地址
     */
    @Column(name = "company_address")
    @FieldComment(name = "公司地址")
    private String companyAddress;

    /**
     * 职务
     */
    @Column(name = "duty_name")
    @FieldComment(name = "职务")
    private String dutyName;

    /**
     * 0默认是身份证，1军官证，2护照
     */
    @Column(name = "card_type")
    private Integer cardType;

    /**
     * 证件编号
     */
    @Column(name = "card_no")
    @FieldComment(name = "证件编号")
    private String cardNo;

    /**
     * 车牌号
     */
    @Column(name = "car_no")
    @FieldComment(name = "车牌号")
    private String carNo;

    /**
     * 商家编号
     */
    @Column(name = "area_code")
    @FieldComment(name = "商家编号")
    private String areaCode;

    /**
     * 账号类型  1-普通会员，2-员工
     */
    @Column(name = "type")
    @FieldComment(name = "账号类型")
    private Integer type;

    /**
     * 验证码
     */
    @Transient
    private String telYzm;

    /**
     * 微信头像
     */
    @Column(name = "avatar_url")
    @FieldComment(name = "微信头像")
    private String photo;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_time")
    private Date uploadtime;

    @Column(name = "tab_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tabtime;

    /**
     * 员工Id
     */
    @Column(name = "u_id")
    private Long uid;

    /**
     * 是否登录过 1是 0未登录
     */
    @Column(name = "is_login")
    private Integer isLogin;


    /**
     * 自动回复
     */
    @Column(name = "tip")
    private String tip;

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTelYzm() {
        return telYzm;
    }

    public void setTelYzm(String telYzm) {
        this.telYzm = telYzm;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public Date getTabtime() {
        return tabtime;
    }

    public void setTabtime(Date tabtime) {
        this.tabtime = tabtime;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Integer isLogin) {
        this.isLogin = isLogin;
    }
}
