package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.AccessRecord;
import com.easymicro.service.core.IService;

/**************************************
 * 访客Service接口
 * @author LinYingQiang
 * @date 2018-09-19 12:07
 * @qq 961410800
 *
************************************/
public interface AccessRecordService extends IService<AccessRecord,Long> {

    /**
     * 数据同步
     */
    void insertOrUpdate(String data);
}
