package com.easymicro.rest.modular.business.async.event;

import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.persistence.modular.model.business.PreApplyGroup;

/**************************************
 * 数据同步预约接口时发送消息
 * @author LinYingQiang
 * @date 2018-09-19 15:20
 * @qq 961410800
 *
************************************/
public class SyncEvent {

    private PreApplyGroup preApplyGroup;

    private PreApplyDetail preApplyDetail;

    public PreApplyGroup getPreApplyGroup() {
        return preApplyGroup;
    }

    public void setPreApplyGroup(PreApplyGroup preApplyGroup) {
        this.preApplyGroup = preApplyGroup;
    }

    public PreApplyDetail getPreApplyDetail() {
        return preApplyDetail;
    }

    public void setPreApplyDetail(PreApplyDetail preApplyDetail) {
        this.preApplyDetail = preApplyDetail;
    }
}
