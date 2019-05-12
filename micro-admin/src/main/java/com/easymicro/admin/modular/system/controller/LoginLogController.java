package com.easymicro.admin.modular.system.controller;

import com.easymicro.admin.core.common.annotion.BussinessLog;
import com.easymicro.admin.core.common.annotion.Permission;
import com.easymicro.admin.core.common.constant.Const;
import com.easymicro.admin.core.common.constant.factory.PageFactory;
import com.easymicro.admin.modular.system.warpper.LogWarpper;
import com.easymicro.core.base.controller.BaseController;
import com.easymicro.persistence.modular.model.system.LoginLog;
import com.easymicro.service.modular.system.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 日志管理的控制器
 *
 * @author fengshuonan
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

    private static String PREFIX = "/system/log/";

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "login_log.html";
    }

    /**
     * 查询登录日志列表
     */
    @RequestMapping("/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String logName) {
        Pageable page = PageFactory.defaultPage();
        Page<LoginLog> p1 = loginLogService.getLoginLogs(page, beginTime, endTime, logName);
        return super.packForBT(p1,LogWarpper.class);
    }

    /**
     * 清空日志
     */
    @BussinessLog("清空登录日志")
    @RequestMapping("/delLoginLog")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delLog() {
        loginLogService.deleteAll();
        return SUCCESS_TIP;
    }
}
