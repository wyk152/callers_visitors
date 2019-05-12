package com.easymicro.service.modular.business;

import com.easymicro.service.core.IService;
import com.easymicro.persistence.modular.model.business.Config;

/**************************************
 * Service接口
 *@author LinYingQiang
 *@date 2018-08-12 16:16
 *@qq 961410800
 *
 ************************************/
public interface ConfigService extends IService<Config,Long> {

    /**
     * 获取平台地址
     */
    String getPlatformDomain();

    /**
     * 平台微信地址
     */
    String getWeChatRedirectUrl();

    /**
     * 收到推送的审批 微信模块
     */
    String getReceiveTemplateId();

    /**
     * 微信访客反馈消息模板
     */
    String getVistorResponseMsgTemplateId();

    /**
     * 审批结果的推送 微信模板
     */
    String getApplyResultTemplateId();

    /**
     * 活动审批消息模版
     */
    String getActivityTemplateId();
}