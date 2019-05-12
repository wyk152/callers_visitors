package com.easymicro.core.base.tips;

/**
 * 返回给前台的提示（最终转化为json形式）
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午11:58:00
 */
public abstract class Tip {

     Integer code;
     String msg;
     String cause;
     Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Tip() {
        this.code = code;
        this.msg = msg;
        this.cause = cause;
        this.data = data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
