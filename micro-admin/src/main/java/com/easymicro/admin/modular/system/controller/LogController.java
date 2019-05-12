package com.easymicro.admin.modular.system.controller;

import com.easymicro.admin.core.common.annotion.BussinessLog;
import com.easymicro.admin.core.common.annotion.Permission;
import com.easymicro.admin.core.common.constant.Const;
import com.easymicro.admin.core.common.constant.factory.PageFactory;
import com.easymicro.admin.core.common.constant.state.BizLogType;
import com.easymicro.admin.modular.system.warpper.LogWarpper;
import com.easymicro.core.base.controller.BaseController;
import com.easymicro.core.support.BeanKit;
import com.easymicro.persistence.modular.model.system.OperationLog;
import com.easymicro.service.modular.system.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 日志管理的控制器
 *
 * @author fengshuonan
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

    private static String PREFIX = "/system/log/";

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "log.html";
    }

    /**
     * 查询操作日志列表
     */
    @RequestMapping("/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String logName, @RequestParam(required = false) Integer logType) {
        Page<OperationLog> page = operationLogService.getOperationLogs(PageFactory.defaultPage(), beginTime, endTime, logName, BizLogType.valueOf(logType));
        return super.packForBT(page);
    }

    /**
     * 查询操作日志详情
     */
    @RequestMapping("/detail/{id}")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object detail(@PathVariable Long id) {
        OperationLog operationLog = operationLogService.find(id);
        Map<String, Object> stringObjectMap = BeanKit.beanToMap(operationLog);
        return super.warpObject(new LogWarpper(stringObjectMap));
    }

    /**
     * 清空日志
     */
    @BussinessLog(value = "清空业务日志")
    @RequestMapping("/delLog")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delLog() {
        operationLogService.deleteAll();
        return SUCCESS_TIP;
    }
}
