package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.Visitor;
import com.easymicro.service.core.IService;

/**************************************
 * 访客service接口
 * @author LinYingQiang
 * @date 2018-09-19 9:09
 * @qq 961410800
 *
************************************/
public interface VisitorService extends IService<Visitor,Long> {

    /**
     * 同步接口新增访客
     */
    void insertOrUpdate(String data);
}
