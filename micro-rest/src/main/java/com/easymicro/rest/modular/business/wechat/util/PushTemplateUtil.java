package com.easymicro.rest.modular.business.wechat.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PushTemplateUtil {

    /**
     *审批结果通知推送模板
     * @param
     */
    public static JSONObject approvalResult(String templateId,String openid,String url,String title,String name,String content){
        JSONObject map = new JSONObject();
        JSONObject data = new JSONObject();
        map.put("touser", openid);
        map.put("template_id", templateId);//模板的id
        map.put("url", url);//跳转的路径
        map.put("topcolor", "#FF0000");
        data.put("first", toJson(title));//标题
        data.put("keyword1",toJson (name));//内容：审批人
        data.put("keyword2", toJson(content));//内容：审批内容
        data.put("remark", toJson("点击查看详情"));//备注
        map.put("data", data);
        return map;
    }

    /**
     *预约审核通知
     * @param openid
     */
    public static JSONObject appointment(String templateId,String openid,String url,String title,String applyContent,String appointmentTime,String applicant,String applyTime){
        JSONObject map =  new JSONObject();
        JSONObject data = new JSONObject();
        map.put("touser", openid);
        map.put("template_id", templateId);//模板的id
        map.put("url", url);//跳转的路径
        map.put("topcolor", "#FF0000");
        data.put("first", toJson(title));//标题
        data.put("keyword1",toJson (applyContent));//内容：申请内容
        data.put("keyword2", toJson(appointmentTime));//内容：预约时间
        data.put("keyword3", toJson(applicant));//内容：申请人
        data.put("keyword4", toJson(applyTime));//内容：申请时间
        data.put("remark", toJson("点击查看详情"));//备注

        map.put("data", data);
        return map;
    }

    /**
     *审核结果通知模板
     * @param openid
     */
    public static JSONObject auditingResult(String templateId,String openid,String url,String title,String content,String result,String auditTime){
        JSONObject map = new JSONObject();
        JSONObject data = new JSONObject();
        map.put("touser", openid);
        map.put("template_id", templateId);//模板的id
        map.put("url", url);//跳转的路径
        map.put("topcolor", "#FF0000");
        data.put("first", toJson(title));//标题
        data.put("keyword1",toJson (result));//内容：审核结果
        data.put("keyword2", toJson(auditTime));//内容：审核时间
        data.put("remark", toJson(content));//备注
        map.put("data", data);
        return map;
    }

    private static JSONObject toJson(String value){
        JSONObject json = new JSONObject();
        json.put("value", value);
        json.put("color", "#173177");//消息字体颜色
        return json;
    }

}
