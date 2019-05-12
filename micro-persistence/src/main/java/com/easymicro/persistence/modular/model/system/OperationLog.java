package com.easymicro.persistence.modular.model.system;

import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


/**************************************
 * 操作日志
 * @author LinYingQiang
 * @date 2018-08-09 18:23
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "sys_operation_log")
public class OperationLog extends BaseEntity {

    /**
     * 日志类型
     */
    private String logtype;
    /**
     * 日志名称
     */
    private String logname;
    /**
     * 用户id
     */
    private Long userid;
    /**
     * 类名称
     */
    private String classname;
    /**
     * 方法名称
     */
    private String method;
    /**
     * 是否成功
     */
    private String succeed;
    /**
     * 备注
     */
    private String message;

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
}
