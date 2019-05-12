package com.easymicro.rest.modular.business.async.business;

import com.easymicro.core.util.DateUtil;
import com.easymicro.persistence.modular.model.business.Employee;
import com.easymicro.persistence.modular.model.business.Merchant;
import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.persistence.modular.model.business.PreApplyGroup;
import com.easymicro.rest.modular.business.async.BusService;
import com.easymicro.rest.modular.business.async.event.SyncEvent;
import com.easymicro.rest.modular.business.wechat.WeChatUtil;
import com.easymicro.rest.modular.business.wechat.po.WeChatTemplteMsgModel;
import com.easymicro.service.modular.business.ConfigService;
import com.easymicro.service.modular.business.EmployeeService;
import com.easymicro.service.modular.business.MemberService;
import com.easymicro.service.modular.business.MerchantService;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
public class SyncPushWxMsgComponent implements BusService<SyncEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncPushWxMsgComponent.class);

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    @Subscribe
    public void execute(SyncEvent syncEvent) {
        PreApplyGroup preApplyGroup = syncEvent.getPreApplyGroup();
        PreApplyDetail preApplyDetail = syncEvent.getPreApplyDetail();
        if (preApplyDetail.getUploadtime()== null && preApplyGroup.getStatus() != null && preApplyGroup.getStatus() == 2) {
            if (StringUtils.isNotBlank(preApplyDetail.getOpenId())) {
                Merchant merchant = merchantService.getByAreaCode(preApplyDetail.getAreacode());
                Employee employee = new Employee();
                employee.setId(preApplyGroup.getUid());
                employee.setAreaCode(preApplyGroup.getAreacode());
                employee = employeeService.findOne(Example.of(employee));
                WeChatUtil weChatUtil = new WeChatUtil(merchant.getAppId(),merchant.getAppScrect());
                String url =  configService.getWeChatRedirectUrl();//微信授权回调
                if (preApplyGroup.getIsActive() == 1) {
                    WeChatTemplteMsgModel msgModel = new WeChatTemplteMsgModel();
                    msgModel.setFirst("您申请参加\""+preApplyGroup.getActivityTitle()+"\" 审核通过了");
                    msgModel.setKeyword1("通过");
                    msgModel.setKeyword2(DateUtil.dateToStringWithTime());
                    msgModel.setKeyword3("请您准时参加活动，谢谢~");
                    String redirect_uri = configService.getPlatformDomain()
                            + "/wxmanager/authorize?redirect_uri=" + url + "&areacode=" + preApplyDetail.getAreacode() +"!2!"+ preApplyDetail.getSyncid()+"&end=joinList/-1";

                    String accesstoken = merchant.getAccessToken();
                    Map map = weChatUtil.sendTemplateMsg(accesstoken, preApplyDetail.getOpenId(),redirect_uri , msgModel,configService.getActivityTemplateId());

                    if ("40001".equals(map.get("errcode")) || "40002".equals(map.get("errcode"))) {
                        accesstoken = merchantService.updateAccessToken(merchant.getAreaCode());
                         weChatUtil.sendTemplateMsg(accesstoken, preApplyDetail.getOpenId(),redirect_uri , msgModel,configService.getActivityTemplateId());
                    }

                    }else{
                    WeChatTemplteMsgModel msgModel = new WeChatTemplteMsgModel();
                    if(preApplyGroup.getLaunchType() != null && preApplyGroup.getLaunchType() == 1){//邀约
                        msgModel.setFirst("您好！"+employee.getNickname()+"邀请您来访~");
                    }
                    else {
                        msgModel.setFirst("您好！您的预约申请通过了");
                    }
                    msgModel.setKeyword1(employee != null ? employee.getNickname():"管理员");
                    msgModel.setKeyword2(preApplyGroup.getResponsemsg());
                    String redirect_uri = configService.getPlatformDomain()
                            + "/wxmanager/authorize?redirect_uri=" + url + "&areacode=" + preApplyDetail.getAreacode()+"!2!"+ preApplyDetail.getSyncid()+"&end=results";

                    String accesstoken = merchant.getAccessToken();
                    Map map = weChatUtil.sendTemplateMsg(accesstoken, preApplyDetail.getOpenId(),redirect_uri , msgModel,configService.getApplyResultTemplateId());

                    if ("40001".equals(map.get("errcode")) || "40002".equals(map.get("errcode"))) {
                        accesstoken = merchantService.updateAccessToken(merchant.getAreaCode());
                        weChatUtil.sendTemplateMsg(accesstoken, preApplyDetail.getOpenId(),redirect_uri , msgModel,configService.getApplyResultTemplateId());
                    }



                }
            }
        }
    }
}
