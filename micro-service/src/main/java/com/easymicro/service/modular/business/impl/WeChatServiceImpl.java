package com.easymicro.service.modular.business.impl;

import com.easymicro.core.https.HttpsUtil;
import com.easymicro.core.support.StrKit;
import com.easymicro.core.wechat.WeChatConstant;
import com.easymicro.persistence.modular.model.business.Member;
import com.easymicro.persistence.modular.model.business.Merchant;
import com.easymicro.service.modular.business.ConfigService;
import com.easymicro.service.modular.business.MemberService;
import com.easymicro.service.modular.business.MerchantService;
import com.easymicro.service.modular.business.WeChatService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**************************************
 * 微信服务实现
 *@author LinYingQiang
 *@date 2018-06-28 22:22
 *@qq 961410800
 *
 ************************************/
@Service
public class WeChatServiceImpl implements WeChatService {

    private static  final Logger log= LoggerFactory.getLogger(WeChatServiceImpl.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MemberService memberService;

    @Override
    public String getRedirectUrl(String areaCode,String redirectUrl) {
        //1:获取完整redirect_uri
        if(StrKit.isBlank(redirectUrl)){
             redirectUrl = configService.getWeChatRedirectUrl();
        }
        //2:获取商家APPID
        Merchant merchant = merchantService.getByAreaCode(areaCode);
        //3:组装
        String redirectCodeUrl = WeChatConstant.URL_GET_CODE
                .replaceAll("APPID", merchant.getAppId())
                .replaceAll("REDIRECT_URI",redirectUrl)
                .replaceAll("STATE",areaCode);
        log.info("微信授权url: "+redirectCodeUrl);
        return redirectCodeUrl;
    }

    @Override
    public Member getWechatUser(String areaCode, String code) {
        if (StringUtils.isNotBlank(areaCode) && StringUtils.isNotBlank(code)) {
            Merchant merchant = merchantService.getByAreaCode(areaCode);
            if (merchant != null) {
                Map<String, String> params = new HashMap<>();
                params.put("appid", merchant.getAppId());
                params.put("secret", merchant.getAppScrect());
                params.put("code", code);
                params.put("grant_type", "authorization_code");
                Map<String, Object> jsonMap = HttpsUtil.executePost(WeChatConstant.URL_SPECIAL_TOKEN, params, Map.class);
                if (!jsonMap.containsKey("errcode")) {
                    String accessToken = jsonMap.get("access_token") + "";
                    String openId = jsonMap.get("openid") + "";
                    Member wrapper = new Member();
                    wrapper.setOpenId(openId);
                    wrapper.setAreaCode(areaCode);
                    Member member = memberService.findOne(Example.of(wrapper));
                    params = new HashMap<>();
                    params.put("access_token", accessToken);
                    params.put("openid", openId);
                    params.put("lang", "zh_CN");
                    Map<String, Object> userMap = HttpsUtil.executePost(WeChatConstant.URL_GET_USERINFO, params, Map.class);
                    if (!userMap.containsKey("errcode")) {
                        if (member == null) {
//                            //1:如果用户信息为空,新增
                            member = new Member();
                            String nickName = (String) userMap.get("nickname");
                            nickName = replaceEmoji(nickName);
                            String headimgurl = (String) userMap.get("headimgurl");
                            member.setNickName(nickName);
                            member.setPhoto(headimgurl);
                            member.setOpenId(openId);
                            member.setCreatetime(new Date());
                            member.setAreaCode(areaCode);
                            member.setType(1);
                            member.setIsLogin(0);
                            memberService.insert(member);
                        }else{
                            String nickName = (String) userMap.get("nickname");
                            nickName = replaceEmoji(nickName);
                            String headimgurl = (String) userMap.get("headimgurl");
                            member.setNickName(nickName);
                            member.setPhoto(headimgurl);
                            memberService.update(member);
                        }
                    }
                    return member;
                }
            }
        }
        return null;
    }

    private static boolean hasEmoji(String content) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private static String replaceEmoji(String str) {
        if (!hasEmoji(str)) {
            return str;
        } else {
            str = str.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", " ");
            return str;
        }

    }
}
