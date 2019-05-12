package com.easymicro.persistence.modular.model.business;


import com.easymicro.persistence.core.model.BaseApiModel;
import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**************************************
 * 员工信息对象
 * @author LinYingQiang
 * @date 2018-09-10 11:34
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_employee")
public class Employee extends BaseApiModel {

    @Column(name = "id")
    private Long id;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickname;

    /**
     * 邮箱 | 登录帐号
     */
    @Column(name = "email")
    private String email;

    /**
     * 密码
     */
    @Column(name = "pawd")
    private transient String pswd;

    /**
     * 最后登录时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 员工状态 1:有效，0:禁止登录
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 员工性别 0：女 1：男 2：其他
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 出生日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 住址
     */
    @Column(name = "address")
    private String address;

    /**
     * 身份证号码
     */
    @Column(name = "id_num")
    private String idNum;

    /**
     * 身份证头像图片地址
     */
    @Column(name = "photo")
    private String photo;

    /**
     * 身份证头像图片base64
     */
    @Column(name = "photo_base64")
    private String photoBase64;

    /**
     * 身份证签发机构
     */
    @Column(name = "issuing")
    private String issuing;

    /**
     * 身份证签发时间
     */
    @Column(name = "validity_date_start")
    private String validityDateStart;

    /**
     * 身份证过期时间
     */
    @Column(name = "validity_date_end")
    private String validityDateEnd;

    /**
     * 手机号码
     */
    @Column(name = "phone")
    private String phone;


    /**
     * 座机号码
     */
    @Column(name = "telephone")
    private String telephone;

    /**
     * 员工门禁卡编号
     */
    @Column(name = "ecard_num")
    private String ecardNum;

    /**
     * 员工门禁卡有效截止时间
     */
    @Column(name = "ecard_end_time")
    private String ecardEndTime;

    /**
     * 同步员工ID
     */
    @Column(name = "sync_id")
    private Integer syncid;


    /**
     * 数据状态，0：自动识别（新增或更新） 1：新增 2：更新 3：删除
     */
    @Column(name = "sta")
    private Integer sta;

    /**
     * 被预约时的提示
     */
    @Column(name = "tip")
    private String tip;


    @Transient
    private String companyNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getIssuing() {
        return issuing;
    }

    public void setIssuing(String issuing) {
        this.issuing = issuing;
    }

    public String getValidityDateStart() {
        return validityDateStart;
    }

    public void setValidityDateStart(String validityDateStart) {
        this.validityDateStart = validityDateStart;
    }

    public String getValidityDateEnd() {
        return validityDateEnd;
    }

    public void setValidityDateEnd(String validityDateEnd) {
        this.validityDateEnd = validityDateEnd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEcardNum() {
        return ecardNum;
    }

    public void setEcardNum(String ecardNum) {
        this.ecardNum = ecardNum;
    }

    public String getEcardEndTime() {
        return ecardEndTime;
    }

    public void setEcardEndTime(String ecardEndTime) {
        this.ecardEndTime = ecardEndTime;
    }

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }

    public Integer getSta() {
        return sta;
    }

    public void setSta(Integer sta) {
        this.sta = sta;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

}
