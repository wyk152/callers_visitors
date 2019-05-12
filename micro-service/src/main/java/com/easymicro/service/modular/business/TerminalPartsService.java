package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.TerminalParts;
import com.easymicro.service.core.IService;

/**************************************
 * 终端外设
 * @author LinYingQiang
 * @date 2018-09-19 14:08
 * @qq 961410800
 *
************************************/
public interface TerminalPartsService extends IService<TerminalParts,Long> {
    void insertOrUpdate(String data);
}
