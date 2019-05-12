package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.BlackList;
import com.easymicro.service.core.IService;

/**************************************
 * 黑名单service接口
 * @author LinYingQiang
 * @date 2018-09-19 11:36
 * @qq 961410800
 *
************************************/
public interface BlackService extends IService<BlackList,Long> {

    /**
     * 数据同步
     */
    void insertOrUpdate(String data);
}
