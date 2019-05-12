package com.easymicro.persistence.modular.repository.business;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.business.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**************************************
 * 用户DAO接口
 *@author LinYingQiang
 *@date 2018-08-28 22:48:41
 *@qq 961410800
 *
************************************/
public interface MemberRepository extends CustomRepository<Member,Long> {


    /**
     * 根据openId和areaCode查找对象
     * @param openId 微信openId
     * @param areaCode 商家areaCode
     */
    @Query("FROM Member m WHERE m.openId = :openId AND m.areaCode = :areaCode")
    Member selectByOpenIdAndAreaCode(@Param("openId") String openId, @Param("areaCode")String areaCode);
}