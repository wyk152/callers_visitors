package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.persistence.modular.model.business.WxyyStep;
import com.easymicro.service.core.IService;

import java.util.List;

/**************************************
 * 微信预约生命周期Service接口
 * @author LinYingQiang
 * @date 2018-09-11 9:37
 * @qq 961410800
 *
************************************/
public interface WxyyStepService extends IService<WxyyStep,Long> {

    /**
     * 保存微信预约生命周期
     */
    WxyyStep insert(String syncId,String applyId,String wxyyremark,Integer wxyystatus,Integer yytype,String areacode);

    /**
     * 数据同步
     */
    void insertOrUpdate(String data);
}
