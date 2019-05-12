package com.easymicro.persistence.modular.model.system;

import com.easymicro.persistence.core.model.BaseEntity;
import com.easymicro.persistence.modular.model.business.Merchant;

import javax.persistence.*;
import java.util.Date;

/**************************************
 * 管理员表
 * @author LinYingQiang
 * @date 2018-08-09 18:27
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {

    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * md5密码盐
     */
    private String salt;
    /**
     * 名字
     */
    private String name;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
    private Integer sex;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 角色id
     */
    private String roleid;
    /**
     * 部门id
     */
    private Long deptid;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
    private Integer status;

    /**
     * 身份,  0-平台 ,1-商家
     */
    private Integer identity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "merchant_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private Merchant merchant;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public Long getDeptid() {
        return deptid;
    }

    public void setDeptid(Long deptid) {
        this.deptid = deptid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public String toString() {
        return "User{" +
                "avatar='" + avatar + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", roleid='" + roleid + '\'' +
                ", deptid=" + deptid +
                ", status=" + status +
                ", id=" + id +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                '}';
    }
}
