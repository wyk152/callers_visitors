package com.easymicro.service.modular.business;


import com.easymicro.persistence.modular.model.business.Member;

/**************************************
 * 微信接口
 *@author LinYingQiang
 *@date 2018-06-28 22:30
 *@qq 961410800
 *
************************************/ 
public interface WeChatService {

    /**
     * 根据areaCode获取redirect_uri
     * @param areaCode 公司唯一编码
     */
    String getRedirectUrl(String areaCode,String url);

    /**
     * 根据商家code和微信code获取用户信息
     * @param areaCode 商家code
     * @param code 微信code
     */
    Member getWechatUser(String areaCode, String code);
}
