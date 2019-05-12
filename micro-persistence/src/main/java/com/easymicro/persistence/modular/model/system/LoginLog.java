package com.easymicro.persistence.modular.model.system;

import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 登陆日志表
 * @author LinYingQiang
 * @date 2018-08-09 18:10
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "sys_login_log")
public class LoginLog extends BaseEntity {

    /**
     * 日志名称
     */
    private String logname;
    /**
     * 管理员id
     */
    private Long userid;
    /**
     * 是否执行成功
     */
    private String succeed;
    /**
     * 具体消息
     */
    private String message;
    /**
     * 登录ip
     */
    private String ip;

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getSucceed() {
        return succeed;
    }

    public void setSucceed(String succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
