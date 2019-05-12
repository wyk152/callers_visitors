package com.easymicro.rest.modular.business.async.business;

import com.easymicro.core.util.DateUtil;
import com.easymicro.persistence.modular.model.business.Member;
import com.easymicro.persistence.modular.model.business.Merchant;
import com.easymicro.persistence.modular.model.business.PreApplyGroup;
import com.easymicro.rest.modular.business.async.BusService;
import com.easymicro.rest.modular.business.wechat.WeChatUtil;
import com.easymicro.rest.modular.business.wechat.po.WeChatTemplteMsgModel;
import com.easymicro.service.modular.business.ConfigService;
import com.easymicro.service.modular.business.MemberService;
import com.easymicro.service.modular.business.MerchantService;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**************************************
 * 预约成功推送微信模板信息
 * @author LinYingQiang
 * @date 2018-09-11 9:49
 * @qq 961410800
 *
 ************************************/
@Component
public class PushWxTemplateMsgComponent implements BusService<PreApplyGroup> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushWxTemplateMsgComponent.class);

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ConfigService configService;

    @Override
    @Subscribe
    public void execute(PreApplyGroup preApplyGroup) {
        Merchant merchant = merchantService.getByAreaCode(preApplyGroup.getAreacode());
        if (merchant != null) {
            try {
                Member member = memberService.selectWxUserByUid(preApplyGroup.getUid(), preApplyGroup.getAreacode());
                if (member != null && StringUtils.isNotBlank(member.getOpenId())) {
                    LOGGER.info("=======个人申请预约=========审批人已经绑定微信，发送微信推送=======================");
                    WeChatUtil weChatUtil = new WeChatUtil(merchant.getAppId(), merchant.getAppScrect());
                    WeChatTemplteMsgModel msgModel = new WeChatTemplteMsgModel();
                    msgModel.setFirst("您好！您有一个待审核的任务");
                    msgModel.setKeyword1(preApplyGroup.getReson());
                    msgModel.setKeyword2(preApplyGroup.getStarttime() + " 至" + preApplyGroup.getEndtime());
                    msgModel.setKeyword3(member.getName());
                    msgModel.setKeyword4(DateUtil.dateToStringWithTime());
                    // msgModel.setRmk("如有需要,请重新申请.");
                    String url = configService.getWeChatRedirectUrl();//微信授权回调
                    String redirect_uri = configService.getPlatformDomain()
                            + "/wxmanager/authorize?redirect_uri=" + url + "&areacode=" + preApplyGroup.getAreacode() + "!2!" + preApplyGroup.getSyncid() + "&end=examination";
                    //System.out.println("====================="+redirect_uri);


                    String accesstoken = merchant.getAccessToken();
                    Map map = weChatUtil.sendTemplateMsg(accesstoken,
                            member.getOpenId(), redirect_uri, msgModel, configService.getReceiveTemplateId());

                    if ("40001".equals(map.get("errcode")) || "40002".equals(map.get("errcode"))) {
                        accesstoken = merchantService.updateAccessToken(merchant.getAreaCode());
                        map = weChatUtil.sendTemplateMsg(accesstoken,
                                member.getOpenId(), redirect_uri, msgModel, configService.getReceiveTemplateId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("微信模板消息发送错误", e);
            }

        } else {
            LOGGER.info("!!!!!!!" + preApplyGroup.getAreacode() + "   没有配置微信appid,无法发送模板消息!!!!!!!!!!");
        }
    }
}
