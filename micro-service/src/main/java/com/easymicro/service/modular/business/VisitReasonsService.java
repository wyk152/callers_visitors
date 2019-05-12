package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.VisitReasons;
import com.easymicro.service.core.IService;

/**************************************
 * 来访事由service接口
 * @author LinYingQiang
 * @date 2018-09-19 14:43
 * @qq 961410800
 *
************************************/
public interface VisitReasonsService extends IService<VisitReasons,Long> {
    /**
     * 同步接口
     */
    void insertOrUpdate(String data);
}
