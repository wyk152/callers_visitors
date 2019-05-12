package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.PreApplyGroup;
import com.easymicro.service.core.IService;
import com.easymicro.persistence.modular.model.business.Merchant;

/**************************************
 * 商家Service接口
 *@author LinYingQiang
 *@date 2018-08-12 16:16
 *@qq 961410800
 *
 ************************************/
public interface MerchantService extends IService<Merchant,Long> {

    /**
     * 分配用户到公司
     * @param id
     * @param userIds
     */
    void assign(Long id, String userIds);

    /**
     * 根据areaCode 查询商家
     * @param areaCode
     */
    Merchant getByAreaCode(String areaCode);



    String getAccessToken(String  areaCode);

    String  updateAccessToken(String areaCode);
}