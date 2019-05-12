package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 访客
 * @author LinYingQiang
 * @date 2018-09-19 9:03
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_visitor")
public class Visitor extends BaseApiModel {

    @Column(name = "id")
    private Long id;

    /** 访客名称 */
    @Column(name = "name")
    private String name;

    /** 性别 */
    @Column(name = "sex")
    private String sex;

    /** 民族（编号见民族列表） */
    @Column(name = "nation")
    private String nation;

    /** 出生年月日 */
    @Column(name = "birthday")
    private String birthday;

    /** 住址 */
    @Column(name = "address")
    private String address;

    /** 身份证号 */
    @Column(name = "idnum")
    private String idnum;

    /** 身份证照片路径（以身份证号为照片名称） */
    @Column(name = "photo")
    private String photo;

    @Column(name = "site_photo")
    private String sitePhoto;

    /** 签发机关 */
    @Column(name = "issuing")
    private String issuing;

    /** 有效期，开始时间 */
    @Column(name = "validity_date_start")
    private String validitydatestart;

    /** 有效期，结束时间 */
    @Column(name = "validity_date_end")
    private String validitydateend;

    /** 访客手机号码 */
    @Column(name = "phone")
    private String phone;

    /** 预约开始时间 **/
    @Column(name = "start_time")
    private String startTime;

    /** 预约结束时间 **/
    @Column(name = "end_time")
    private String endTime;

    @Column(name = "pin_yin")
    private String pinYin;

    //上传公安网时间
    @Column(name = "upload_police_time")
    private String uploadPoliceTime;

    //上传公安网状态 1成功 0 失败
    @Column(name = "update_police_status")
    private Integer uploadPoliceStatus;

    //上传公安网结果
    @Column(name = "upload_police_result")
    private String uploadPoliceResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSitePhoto() {
        return sitePhoto;
    }

    public void setSitePhoto(String sitePhoto) {
        this.sitePhoto = sitePhoto;
    }

    public String getIssuing() {
        return issuing;
    }

    public void setIssuing(String issuing) {
        this.issuing = issuing;
    }

    public String getValiditydatestart() {
        return validitydatestart;
    }

    public void setValiditydatestart(String validitydatestart) {
        this.validitydatestart = validitydatestart;
    }

    public String getValiditydateend() {
        return validitydateend;
    }

    public void setValiditydateend(String validitydateend) {
        this.validitydateend = validitydateend;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getUploadPoliceTime() {
        return uploadPoliceTime;
    }

    public void setUploadPoliceTime(String uploadPoliceTime) {
        this.uploadPoliceTime = uploadPoliceTime;
    }

    public Integer getUploadPoliceStatus() {
        return uploadPoliceStatus;
    }

    public void setUploadPoliceStatus(Integer uploadPoliceStatus) {
        this.uploadPoliceStatus = uploadPoliceStatus;
    }

    public String getUploadPoliceResult() {
        return uploadPoliceResult;
    }

    public void setUploadPoliceResult(String uploadPoliceResult) {
        this.uploadPoliceResult = uploadPoliceResult;
    }
}
