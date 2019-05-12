package com.easymicro.persistence.modular.repository.business;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.business.PreApplyDetail;

/**************************************
 * 预约子表Dao接口
 * @author LinYingQiang
 * @date 2018-09-10 10:19
 * @qq 961410800
 *
************************************/
public interface PreApplyDetailRepository extends CustomRepository<PreApplyDetail,Long> {


    PreApplyDetail findByApplyid(String applyid);
}
