package com.easymicro.rest.modular.business.async;

/**************************************
 *
 * @author LinYingQiang
 * @date 2018-04-27 14:46
 * @qq 961410800
 *
************************************/
public interface BusService<T> {

    void execute(T t);
}
