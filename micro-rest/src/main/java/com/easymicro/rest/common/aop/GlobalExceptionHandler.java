package com.easymicro.rest.common.aop;


import com.easymicro.core.aop.BaseControllerExceptionHandler;
import com.easymicro.core.base.tips.ErrorTip;
import com.easymicro.rest.common.exception.BizExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午3:19:56
 */
@ControllerAdvice
public class GlobalExceptionHandler extends BaseControllerExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 拦截所有异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorTip exception(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return new ErrorTip(BizExceptionEnum.SERVER_ERROR.getCode(), BizExceptionEnum.SERVER_ERROR.getMessage());
    }
}
