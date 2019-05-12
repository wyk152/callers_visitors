package com.easymicro.service.modular.system;

import com.easymicro.persistence.modular.model.system.LoginLog;
import com.easymicro.service.core.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**************************************
 * 登陆日志Service接口
 *@author LinYingQiang
 *@date 2018-08-10 21:44
 *@qq 961410800
 *
************************************/
public interface LoginLogService extends IService<LoginLog,Long> {

    /**
     * 获取登录日志列表
     */
    Page<LoginLog> getLoginLogs(Pageable page, String beginTime, String endTime, String logName);

}
