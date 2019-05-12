package com.easymicro.rest.modular.business.wechat.util;

public class WxResponseCode {

    Integer code;
    String msg;

    WxResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static String getMsg(Integer code) {
        String msg = "成功";

        switch (code) {
            case 0:
                msg = "推送成功";
                break;
            case 40001:
                msg = "access_token不合法";
                break;
            case 40002:
                msg = "不合法的凭证类型";
                break;
            case 40003:
                msg = "不合法的OpenID，请确认OpenID（该用户）是否已关注公众号，或是否是其他公众号的OpenID";
                break;
            case 40014:
                msg = "不合法的access_token，请开发者认真比对access_token的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口";
                break;
            case 42001:
                msg = "access_token超时，请检查access_token的有效期，请参考基础支持-获取access_token中，对access_token的详细机制说明";
                break;
            case 43004:
                msg = "需要接收者关注公众号";
                break;
            case 45009:
                msg = "接口调用超过限制";
                break;
            case 48001:
                msg = "api 功能未授权，请确认公众号已获得该接口，可以在公众平台官网 - 开发者中心页中查看接口权限";
                break;
            case 48004:
                msg = "api 接口被封禁，请登录 mp.weixin.qq.com 查看详情";
                break;
            case 50005:
                msg = "用户未关注公众号";
                break;
        }
        return msg;
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

}
