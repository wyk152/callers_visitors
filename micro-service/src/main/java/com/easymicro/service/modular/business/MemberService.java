package com.easymicro.service.modular.business;

import com.easymicro.service.core.IService;
import com.easymicro.persistence.modular.model.business.Member;

import java.util.List;

/**************************************
 * 用户Service接口
 *@author LinYingQiang
 *@date 2018-08-12 16:16
 *@qq 961410800
 *
 ************************************/
public interface MemberService extends IService<Member,Long> {

    /**
     * 根据openId和areaCode查找对象
     * @param openId 微信openId
     * @param areaCode 商家areaCode
     */
    Member selectByOpenIdAndAreaCode(String openId,String areaCode);

    /**
     * 查询微信申请子表所有用户
     */
    List<Member> selectWxUserByApplyDetail(String syncId,String areaCode);

    Member selectWxUserByUid(Long uid, String areaCode);

    /**
     * 根据手机号码查询实体
     * @param tel
     * @param areaCode
     * @return
     */
    Member selectWxUserByTel(String tel, String areaCode);



}