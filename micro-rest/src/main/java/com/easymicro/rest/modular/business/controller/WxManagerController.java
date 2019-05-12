package com.easymicro.rest.modular.business.controller;

import com.easymicro.core.support.StrKit;
import com.easymicro.persistence.modular.model.business.Member;
import com.easymicro.persistence.modular.model.business.Merchant;
import com.easymicro.rest.modular.business.wechat.WeChatUtil;
import com.easymicro.service.modular.business.MerchantService;
import com.easymicro.service.modular.business.WeChatService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**************************************
 * 微信
 *@author LinYingQiang
 *@date 2018-06-28 19:19
 *@qq 961410800
 *
 ************************************/
@CrossOrigin(origins = "*", maxAge = 7200)
@RestController
@RequestMapping("/api/wxmanager")
public class WxManagerController {



    @Autowired
    private WeChatService weChatService;

    @Autowired
    private MerchantService merchantService;

    /**
     * 转发到微信获取code请求
     *
     * @param areaCode 商家唯一编码
     */
    @RequestMapping("/authorize")
    public void redirect(String redirect_uri, String areaCode, String end, HttpServletResponse response) throws UnsupportedEncodingException {
        String url = "";
        if (StrKit.isBlank(areaCode)) {

            areaCode = "C100003_1000";
        }
        String[] param = areaCode.split("!");
        String code = param[0];
        redirect_uri += "?areaCode=" + areaCode;
        if (StrKit.notBlank(end)) {
            redirect_uri += "#/" + end;
        }
        url = weChatService.getRedirectUrl(code, URLEncoder.encode(redirect_uri,"utf-8"));
        System.out.println(" 重定向url:  "+url);

        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户openId
     */
    @RequestMapping(value = "/getopenid", method = RequestMethod.POST,
            produces = {"application/json"}, consumes = {"application/json"})
    public Object getopenid(@RequestBody Map<String, String> params) {
        Map<String, Object> rst = new HashMap<>();
        String areacode = params.get("areacode");
        String code = params.get("code");
        if (StringUtils.isNotBlank(areacode) && StringUtils.isNotBlank(code)) {
            Member member = weChatService.getWechatUser(areacode, code);
            if (member != null) {
                rst.put("code", 0);
                rst.put("headimgurl", member.getPhoto());
                rst.put("openid", member.getOpenId());
            } else {
                rst.put("code", 100);
            }
        } else {
            rst.put("code", 100);
        }
        return rst;
    }

    /**
     * 微信签名
     */
    @RequestMapping(value = "/getwxsign", method = RequestMethod.POST, produces = {
            "application/json"}, consumes = {"application/json"})
    @ResponseBody
    public Map getwxsign(@RequestBody Map maps, HttpServletResponse respone) {

        String areacode = maps.get("areacode").toString();

        Merchant merchant = merchantService.getByAreaCode(areacode);
        Map<String, String> map = new HashMap<>();
        if (merchant != null) {
            //查询微信配置
            String appid = merchant.getAppId();// "wxf17685d8dbdc304f";
            String secret = merchant.getAppScrect(); //"e37549e185efb903ffde149f2881159e";

            WeChatUtil weChatUtil = new WeChatUtil(appid, secret);
            weChatUtil.setAppid(appid);
            weChatUtil.setSecret(secret);
            map = weChatUtil.sign(maps.get("url").toString());
        }
        return map;
    }
}
