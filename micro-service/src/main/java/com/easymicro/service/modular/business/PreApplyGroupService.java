package com.easymicro.service.modular.business;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.Member;
import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.persistence.modular.model.business.PreApplyGroup;
import com.easymicro.service.core.IService;
/**************************************
 * 预约申请主表Service接口
 * @author LinYingQiang
 * @date 2018-09-10 10:22
 * @qq 961410800
 *
************************************/
public interface PreApplyGroupService extends IService<PreApplyGroup,Long> {

    /**
     * 代预约
     */
    void agentApply(PreApplyGroup preapplygoup, PreApplyDetail preapplygoupdetail);

    /**
     * 个人预约
     */
    void personalApply(PreApplyGroup preapplygoup, Member wxuser);

    /**
     * 团队预约提交修改状态
     */
    void teamSubmitApply(PreApplyGroup preapplygoup);

    /**
     * 根据syncId查找预约主表
     */
    PreApplyGroup selectBySyncId(String syncId, String areaCode);

    /**
     * 根据活动id查找对应的预约主表信息
     */
    PreApplyGroup selectByActivityId(Long activityId);

    /**
     * 同步接口
     */
    void insertOrUpdate(String data);

    /**
     * 查询审批状态
     */
    JSON searchResult(Long id);
}
