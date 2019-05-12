package com.easymicro.rest.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**************************************
 *
 *@author LinYingQiang
 *@date 2018-06-29 00:00
 *@qq 961410800
 *
 ************************************/

public class ApiResult<T> {

    public final static Integer SUCCESS = 1000;//成功
    public final static Integer NOLOGIN  = 2000;//未登录
    public final static Integer PARAMERROR  = 3000;//参数错误
    public final static Integer NOPERSONINFO  = 4000;//没有个人信息
    public final static Integer OPERATION_ERROR  = 5000;//业务错误
    public final static Integer ERROR  = 9000;//系统错误

    private static final Logger logger = LoggerFactory.getLogger(ApiResult.class);


    private Integer code = SUCCESS;//默认成功

    /**
     * 返回消息
     */
    private String msg = "成功";

    private String cause;

    /**
     * 返回内容
     */
    private T content;

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     *            the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the content
     */
    public T getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(T content) {
        this.content = content;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public ApiResult<T> getErrorResult(Exception e)
    {
        this.code = ApiResult.ERROR;
        this.msg = "系统错误";
        if(e.getCause() != null &&  e.getCause().getMessage() != null)
        {
            this.cause =  e.getCause().getMessage();
        }
        else
        {
            this.cause = e.getMessage();
        }
        logger.error("接口发生错误", e);
        return this;
    }

}
