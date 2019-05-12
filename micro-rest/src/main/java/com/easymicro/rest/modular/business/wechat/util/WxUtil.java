package com.easymicro.rest.modular.business.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.easymicro.core.https.HttpsUtil;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WxUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxUtil.class);


    private final static String PUSH_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";//微信推送链接

    private final static String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=";//微信推送链接


//    public static void main(String[] args){
//       String token= getWxAccessToken("wxf17685d8dbdc304f","e37549e185efb903ffde149f2881159e");
//        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        JSONObject map = PushTemplateUtil.appointment("owO-_vg7uHXTs41xOu4-WSVZhPXI","http://www.baidu.com","推送体验","申请",format.format(new Date()),"微信昵称",format.format(new Date()));
////        String token="19_50M9_QU59Yb9BUqALZk9qqWRWerhIAMZSx7XULI_q01YpzAnozm7fZxuZ3zQT1a_-VqtrkGorszGu67vsIcfxFiic_IXduaFAZN-fevHVT45b4lUn9Ujz4lVQgZCjPWlurItEVsawAV8-MJ1VMDdAIACTK";
//
//     System.out.println(map.toString());
//       int code = sendMsg(token,map);
//
//    }


    public static  Integer sendMsg(String accessToke ,JSONObject map) {
        try {
            String result = HttpsUtil.executePost(PUSH_URL + accessToke, map.toString());
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
