package com.easymicro.persistence.modular.repository.business;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.business.Merchant;

/**************************************
 * 商家DAO接口
 *@author LinYingQiang
 *@date 2018-08-13 22:38:58
 *@qq 961410800
 *
************************************/
public interface MerchantRepository extends CustomRepository<Merchant,Long> {

    /**
     * 根据areaCode 查询商家
     * @param appId
     */
    Merchant getByAreaCode(String appId);

}