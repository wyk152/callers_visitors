package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.PartsStatus;
import com.easymicro.service.core.IService;

/**************************************
 * 外设状态service接口
 * @author LinYingQiang
 * @date 2018-09-19 14:26
 * @qq 961410800
 *
************************************/
public interface PartsStatusService extends IService<PartsStatus,Long> {
    /**
     * 数据同步
     */
    void insertOrUpdate(String data);
}
