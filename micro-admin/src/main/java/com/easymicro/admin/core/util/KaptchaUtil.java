package com.easymicro.admin.core.util;

import com.easymicro.admin.config.properties.GunsProperties;
import com.easymicro.core.util.SpringContextHolder;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(GunsProperties.class).getKaptchaOpen();
    }
}