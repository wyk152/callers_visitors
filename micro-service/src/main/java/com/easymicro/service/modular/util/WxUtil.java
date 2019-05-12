package com.easymicro.service.modular.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WxUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxUtil.class);


    private final static String PUSH_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";//微信推送链接

    private final static String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=";//微信推送链接



    public static  Integer sendMsg(String accessToke ,Map map) {
        try {
            String result = HttpClient.send(PUSH_URL + accessToke, map);
            logger.info("推送结果  :   "  +result);
            if(result!=null&&!"".equals(result.trim())){
                JSONObject object =JSONObject.parseObject(result);
                Integer code = object.getInteger("errcode");
                return code;
            }
            return -1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getWxAccessToken(String appid,String appsecret){
        String accessToken="";
        String url = GET_ACCESS_TOKEN_URL+appid+"&secret="+appsecret;
        String result = HttpClient.send(url);
        logger.info("获取access_token请求结果"+result);
        if(result !=null&&!"".equals(result)){
            JSONObject data = JSONObject.parseObject(result);
            accessToken =data.getString("access_token");
        }
        return accessToken;
    }


}
