package com.easymicro.admin.core.common.constant.dictmap.factory;

import com.easymicro.admin.core.common.constant.factory.ConstantFactory;
import com.easymicro.admin.core.common.constant.factory.IConstantFactory;
import com.easymicro.core.exception.GunsException;
import com.easymicro.admin.core.common.exception.BizExceptionEnum;

import java.lang.reflect.Method;

/**
 * 字典字段的包装器(从ConstantFactory中获取包装值)
 *
 * @author fengshuonan
 * @date 2017-05-06 15:12
 */
public class DictFieldWarpperFactory {

    public static Object createFieldWarpper(Object parameter, String methodName) {
        IConstantFactory constantFactory = ConstantFactory.me();
        try {
            Method method = IConstantFactory.class.getMethod(methodName, parameter.getClass());
            return method.invoke(constantFactory, parameter);
        } catch (Exception e) {
            try {
                Method method = IConstantFactory.class.getMethod(methodName, Long.class);
                return method.invoke(constantFactory, Long.parseLong(parameter.toString()));
            } catch (Exception e1) {
                throw new GunsException(BizExceptionEnum.ERROR_WRAPPER_FIELD);
            }
        }
    }

}
