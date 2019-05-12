package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.VisitReasons;
import com.easymicro.persistence.modular.repository.business.VisitReasonsRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.VisitReasonsService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 来访事由service实现
 * @author LinYingQiang
 * @date 2018-09-19 14:43
 * @qq 961410800
 *
 ************************************/
@Service
public class VisitReasonsServiceImpl extends ServiceImpl<VisitReasonsRepository, VisitReasons, Long> implements VisitReasonsService {
    @Override
    public void insertOrUpdate(String data) {
        List<VisitReasons> list = JSON.parseArray(data, VisitReasons.class);
        for (VisitReasons visitReasons : list) {
            visitReasons.setAreaCode(visitReasons.getCompanyCode());
            if (visitReasons.getTabTime() == null || visitReasons.getUploadTime() == null) {
                baseRepository.save(visitReasons);
            } else if (visitReasons.getTabTime().getTime() != visitReasons.getUploadTime().getTime()) {
                VisitReasons update = new VisitReasons();
                update.setId(visitReasons.getId());
                update.setAreaCode(visitReasons.getAreaCode());
                baseRepository.updateSkipNull(Example.of(update), visitReasons);
            }
        }
    }
}
