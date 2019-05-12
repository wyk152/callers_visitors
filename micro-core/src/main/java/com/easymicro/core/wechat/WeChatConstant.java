package com.easymicro.core.wechat;

public interface WeChatConstant {

    /**
     * 用户同意授权，获取code接口
     */
    String URL_GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&response_type=code&scope=snsapi_userinfo&state=STATE&redirect_uri=REDIRECT_URI#wechat_redirect";

    /**
     * 拉取特殊网页TOKEN,与基础支持中的access_token不同
     */
    String URL_SPECIAL_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 获取用户信息接口
     */
    String URL_GET_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 获取微信ACCESS_TOKEN
     */
    String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
}
