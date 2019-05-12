package com.easymicro.rest.modular.business.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.easymicro.core.https.HttpsUtil;
import com.easymicro.rest.modular.business.wechat.po.WeChatTemplteMsgModel;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeChatUtil {
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    private String appid;

    private String secret;

    static final String AuditKey = "";

    static final String AppointKey = "CHebUlLqLFw0gqYpdH8TR9vgKaSkeG5XzQ7Ni4FfA9o";

    static final String code_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    static final String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?";

    static final String send_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?";

    static final String template_send_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?";

    static final String authorize_url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    
    static final String userinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    Logger logger = Logger.getLogger(WeChatUtil.class);

    public WeChatUtil(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }

    public String authorize(String redirect_uri,String state) {
        String url = authorize_url + "appid=" + appid + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state="+state+"#wechat_redirect";
        return url;

    }

    /**
     * 根据code获得 accessToken
     *
     * @param code
     * @return
     */
    public Map accessTokenByCode(@RequestParam String code) {
        String url = code_access_token_url + "appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params = HttpsUtil.executePost(url,params,Map.class);
        System.out.print("accesstoken:" + params);
        return params;
    }


    /**
     * 获得 accessToken
     *
     * @return
     */
    public Map accessToken() {
        String url = access_token_url + "grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        Map<String, String> params = new HashMap<>();
        params.put("grant_type","client_credential");
        params.put("appid", appid);
        params.put("secret", secret);
        String resut = HttpsUtil.executePost(url,params,null);
        logger.info("url = " + url + "   resut = " + resut);
        Map resultMap = JSON.parseObject(resut, Map.class);
        System.out.print("accesstoken:" + JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 拉取用户信息
     * @param acesstoken
     * @param openid
     * @return
     */
    public Map getuserinfo(String acesstoken, String openid) {
        String url = String.format(userinfo_url, acesstoken, openid);
        String resut = HttpsUtil.executePost(url,"");
        logger.info("url = " + url + "   resut = " + resut);
        Map resultMap = JSON.parseObject(resut, Map.class);
        System.out.print("accesstoken:" + JSONObject.toJSONString(resultMap));
        return resultMap;
    }


    public Map gettickt(String token) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        params.put("type", "jsapi");
        params = HttpsUtil.executePost("https://api.weixin.qq.com/cgi-bin/ticket/getticket", params, Map.class);
        logger.info("获取ticket" + params);
        return params;
    }

    /**
     * 发送消息
     *
     * @param strJson
     * @return
     */
    public Map sendMsg(String strJson) {
        Map tempMap = accessToken();
        String accessToken = (String) tempMap.get("access_token");
        String url = send_url + "access_token=" + accessToken;
        String resut = HttpsUtil.executePost(url, strJson);
        logger.info("url = " + url + "   resut = " + resut);
        Map resultMap = JSON.parseObject(resut, Map.class);
        return resultMap;
    }


    /**
     * 发送模板消息
     *
     * @param openId
     * @param urlStr
     * @param empplateId
     * @param obj
     * @return
     */
    public Map sendTemplateMsg(String openId, String urlStr, String empplateId, JSONObject obj) {
        Map tempMap = accessToken();
        JSONObject json = new JSONObject();
        String touser = openId;//接收者openid　　　　 
        json.put("touser", touser);
        json.put("template_id", empplateId);
        json.put("url", urlStr);
        json.put("data", obj);//模板数据
        String accessToken = (String) tempMap.get("access_token");
        String url = template_send_url + "access_token=" + accessToken;
        String resut =HttpsUtil.executePost(url, JSON.toJSONString(json));
        logger.info("url = " + url + "   resut = " + resut);
        Map resultMap = JSON.parseObject(resut, Map.class);
        return resultMap;
    }
    
    /**
     * 发送模板消息
     *
     * @param openId
     * @param urlStr
     * @return
     */
    public Map sendTemplateMsg(String accessToken, String openId, String urlStr, WeChatTemplteMsgModel msgModel , String AppointKey) {
    	
    	logger.info("==================向openid= "+openId+" 发送了消息=====================");
    	JSONObject obj = new JSONObject();
    	obj.put("first", toJson(msgModel.getFirst()));
    	obj.put("keyword1", toJson(msgModel.getKeyword1()));
    	obj.put("keyword2", toJson(msgModel.getKeyword2()));
    	obj.put("keyword3", toJson(msgModel.getKeyword3()));
    	obj.put("keyword4", toJson(msgModel.getKeyword4()));
    	obj.put("remark", toJson(msgModel.getRmk()));
        
        Map tempMap = accessToken();
        JSONObject json = new JSONObject();
        String touser = openId;//接收者openid　　　　 
        json.put("touser", touser);
        json.put("template_id", AppointKey);
        json.put("url", urlStr);
        json.put("data", obj);//模板数据
        String url = template_send_url + "access_token=" + accessToken;
        String resut = HttpsUtil.executePost(url, JSON.toJSONString(json));
        logger.info("推送微信   url = " + url + "   resut = " + resut);
        Map resultMap = JSON.parseObject(resut, Map.class);
        return resultMap;
    }

	 public void templateMsg(String openid, String url,WeChatTemplteMsgModel msgModel)
     {
         //向用户发送微信消息
//         String first = "您好！您有待审核的任务";//标题
//         String rmk = "请在2个工作日完成审核";//备注
//         String keyword1 = "预约拜访";
//         String keyword2 = DateUtil.dateToStringWithTime2();
//         String keyword3 = "";
         JSONObject json = new JSONObject();
         json.put("first", toJson(msgModel.getFirst()));
         json.put("keyword1", toJson(msgModel.getKeyword1()));
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
         json.put("keyword2", toJson(sdf.format(new Date())));
         json.put("remark", toJson(msgModel.getRmk()));
         sendTemplateMsg(openid, url, AppointKey, json);
     }

    public JSONObject toJson(String value) {
        JSONObject json = new JSONObject();
        json.put("value", value);
        json.put("color", "#173177");//消息字体颜色
        return json;
    }


    public Map<String, String> sign(String url) {
        String token = accessToken().get("access_token").toString();
        String jsapi_ticket = gettickt(token).get("ticket").toString();
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
//        System.out.println(string1);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appid", appid);

        return ret;
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private String create_nonce_str() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


}
