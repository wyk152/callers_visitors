package com.easymicro.admin.modular.system.controller;

import com.easymicro.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@Controller
@RequestMapping("/blackboard")
public class BlackboardController extends BaseController {


    /**
     * 跳转到黑板
     */
    @RequestMapping("")
    public String blackboard(Model model) {

        return "/blackboard.html";
    }
}
