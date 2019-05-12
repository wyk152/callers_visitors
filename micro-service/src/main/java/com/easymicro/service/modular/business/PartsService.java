package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.Parts;
import com.easymicro.service.core.IService;

/**************************************
 * 外设service接口
 * @author LinYingQiang
 * @date 2018-09-19 14:17
 * @qq 961410800
 *
************************************/
public interface PartsService extends IService<Parts,Long> {

    /**
     * 数据同步
     */
    void insertOrUpdate(String data);
}
