package com.easymicro.persistence.modular.model.business;

import com.alibaba.fastjson.annotation.JSONField;

import com.easymicro.persistence.core.generator.ClassComment;
import com.easymicro.persistence.core.generator.FieldComment;
import com.easymicro.persistence.core.model.BaseEntity;
import com.easymicro.persistence.modular.model.system.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**************************************
 * 商家实体
 * @author LinYingQiang
 * @date 2018-08-13 9:15
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_merchant")
@ClassComment(name = "商家")
public class Merchant extends BaseEntity {

    /**
     * 短信APP_KEY
     */
    @Column(name = "app_key",columnDefinition = "varchar(255) DEFAULT NULL COMMENT '短信APP_KEY'")
    private String appKey;

    /**
     * 短信PUBLIC_KEY
     */
    @Column(name = "public_key",columnDefinition = "varchar(255) DEFAULT NULL COMMENT '短信PUBLIC_KEY'")
    private String publicKey;

    /**
     * 微信APP_SCRECT
     */
    @Column(name = "app_screct",columnDefinition = "varchar(255) DEFAULT NULL COMMENT '微信APP_SCRECT'")
    private String appScrect;

    /**
     * 区域编码
     */
    @Column(name = "area_code",columnDefinition = "varchar(50) DEFAULT NULL COMMENT '商家编码'")
    @FieldComment(name = "商家编码")
    private String areaCode;

    /**
     * 微信APP_ID
     */
    @Column(name = "app_id",columnDefinition = "varchar(255) DEFAULT NULL COMMENT '微信APP_ID'")
    @FieldComment(name = "微信APP_ID")
    private String appId;

    /**
     * 商家名称
     */
    @Column(name = "name",columnDefinition = "varchar(50) DEFAULT NULL COMMENT '商家名称'")
    @FieldComment(name = "商家名称")
    private String name;

    /**
     * 商家地址
     */
    @Column(name = "address",columnDefinition = "varchar(255) DEFAULT NULL COMMENT '商家地址'")
    @FieldComment(name = "商家地址")
    private String address;

    /**
     * 商家电话
     */
    @Column(name = "tel",columnDefinition = "varchar(20) DEFAULT NULL COMMENT '商家电话'")
    @FieldComment(name = "商家电话")
    private String tel;

    /**
     * 备注
     */
    @Column(name = "tips",columnDefinition = "text DEFAULT NULL COMMENT '备注'")
    @FieldComment(name = "备注")
    private String tips;

    /**
     * 创建用户
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "create_user_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private User createUser;

    /**
     * 绑定用户
     */
    @OneToMany(mappedBy = "merchant",fetch = FetchType.LAZY)
    @JSONField(serialize = false)
    private Set<User> bindUsers;

    /**
     * 省份
     */
    @Column(name = "province",columnDefinition = "varchar(20) DEFAULT NULL COMMENT '省份'")
    @FieldComment(name = "省份")
    private String province;

    /**
     * 调用微信API所需的access_token
     */
    @Column(name = "access_token",columnDefinition = "varchar(255) DEFAULT NULL COMMENT '调用微信API所需的access_token'")
    @FieldComment(name = "调用微信API所需的access_token")
    private String accessToken;

    /**
     * access_token过期时间
     */
    @Column(name = "expires_time",columnDefinition = "bigint(11) DEFAULT NULL COMMENT 'access_token过期时间'")
    @FieldComment(name = "access_token过期时间")
    private Long expiresTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAppScrect() {
        return appScrect;
    }

    public void setAppScrect(String appScrect) {
        this.appScrect = appScrect;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Set<User> getBindUsers() {
        return bindUsers;
    }

    public void setBindUsers(Set<User> bindUsers) {
        this.bindUsers = bindUsers;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Long expiresTime) {
        this.expiresTime = expiresTime;
    }
}
