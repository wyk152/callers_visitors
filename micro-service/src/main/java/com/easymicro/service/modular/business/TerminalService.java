package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.Terminal;
import com.easymicro.service.core.IService;

/**************************************
 * 终端service接口
 * @author LinYingQiang
 * @date 2018-09-19 12:15
 * @qq 961410800
 *
************************************/
public interface TerminalService extends IService<Terminal,Long> {
    /**
     * 数据同步
     */
    void insertOrUpdate(String data);
}
