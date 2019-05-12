package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.persistence.modular.model.business.WxyyStep;
import com.easymicro.persistence.modular.repository.business.WxyyStepRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.WxyyStepService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**************************************
 * 微信预约生命周期Service实现
 * @author LinYingQiang
 * @date 2018-09-11 9:38
 * @qq 961410800
 *
************************************/
@Service
public class WxyyStepServiceImpl extends ServiceImpl<WxyyStepRepository,WxyyStep,Long> implements WxyyStepService {

    @Override
    public WxyyStep insert(String syncId, String applyId, String wxyyremark, Integer wxyystatus, Integer yytype, String areacode) {
        WxyyStep wxyystep = new WxyyStep();
        wxyystep.setSyncid(syncId);
        wxyystep.setXxyyitemid(applyId);
        wxyystep.setWxyyremark(wxyyremark);
        wxyystep.setWxyystatus(wxyystatus);
        wxyystep.setYytype(yytype);
        wxyystep.setAreacode(areacode);
        return baseRepository.save(wxyystep);
    }

    @Override
    public void insertOrUpdate(String data) {
        List<WxyyStep> wxyySteps = JSON.parseArray(data, WxyyStep.class);
        for (WxyyStep wxyystep : wxyySteps) {
            wxyystep.setAreacode(wxyystep.getCompanycode());
            WxyyStep query = new WxyyStep();
            query.setId(wxyystep.getId());
            query.setAreacode(wxyystep.getAreacode());
            Optional<WxyyStep> optional = baseRepository.findOne(Example.of(query));
            if (optional.isPresent()) {
                baseRepository.updateSkipNull(Example.of(query), wxyystep);
            }else{
                baseRepository.save(wxyystep);
            }
        }
    }
}
