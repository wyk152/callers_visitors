package com.easymicro.core.base.tips;

/**
 * 返回给前台的错误提示
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:05:22
 */
public class ErrorTip extends Tip {

    public ErrorTip(int code, String message,String reason) {
        super();
        this.code = code;
        this.msg = message;
        this.cause=reason;
        this.data=null;
    }

    public ErrorTip( String message) {
        super();
        this.code = -1;
        this.msg = message;
        this.cause="";
        this.data=null;
    }
    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.msg = message;
        this.cause=null;
        this.data=null;
    }
}
