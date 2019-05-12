package com.easymicro.rest.modular.business.wechat.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ResultUtil {

   private Integer code;
   private String msg;
   private String cause;
   private Object data;

    public ResultUtil(Integer code, String msg,String cause, Object data) {
        this.code = code;
        this.msg = msg;
        this.cause = cause;
        this.data = data;
    }

    public Integer getCode() {
        return code;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static JSON toJson(Integer code, String msg, String cause, Object data){
        JSONObject object = new JSONObject();
        object.put("code",code);
        object.put("msg",msg);
        object.put("cause",cause);
        object.put("data", data);
        return object;

    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
