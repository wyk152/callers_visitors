package com.easymicro.service.modular.system;

import com.easymicro.persistence.modular.model.system.OperationLog;
import com.easymicro.service.core.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**************************************
 * 操作日志查询Service接口
 *@author LinYingQiang
 *@date 2018-08-10 23:54
 *@qq 961410800
 *
************************************/
public interface OperationLogService extends IService<OperationLog,Long> {

    /**
     * 获取操作日志列表
     */
    Page<OperationLog> getOperationLogs(Pageable page, String beginTime, String endTime, String logName, String logType);

    void deleteAll();
}
