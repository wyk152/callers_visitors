package com.easymicro.service.modular.business;

import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.service.core.IService;

import java.util.List;

/**************************************
 * 预约子表Service接口
 * @author LinYingQiang
 * @date 2018-09-10 10:30
 * @qq 961410800
 *
************************************/
public interface PreApplyDetailService extends IService<PreApplyDetail,Long> {

    /**
     * 根据syncId,areaCode查找预约子表
     */
    List<PreApplyDetail> selectBySyncId(String syncId,String areaCode);

    /**
     * 根据openId,syncId,areaCode
     */
    PreApplyDetail selectByOpenIdAndSyncIdAndAreaCode(String openId,String syncId,String areaCode);

    /**
     * 根据syncId,areaCode
     */
    PreApplyDetail selectBySyncIdAndAreaCode(String syncId,String areaCode);

    /**
     * 数据同步
     */
    List<PreApplyDetail> insertOrUpdate(String data);
}
