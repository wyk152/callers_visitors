package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.Visitor;
import com.easymicro.persistence.modular.repository.business.VisitorRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.VisitorService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 访客service接口实现
 * @author LinYingQiang
 * @date 2018-09-19 9:10
 * @qq 961410800
 *
************************************/
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorRepository,Visitor,Long> implements VisitorService {

    @Override
    public void insertOrUpdate(String data) {
        List<Visitor> visitors = JSON.parseArray(data, Visitor.class);
        for (Visitor visitor : visitors) {
            visitor.setAreaCode(visitor.getCompanyCode());
            if (visitor.getTabTime() == null || visitor.getUploadTime() == null) {
                baseRepository.save(visitor);
            } else if (visitor.getTabTime().getTime() != visitor.getUploadTime().getTime()) {
                Visitor update = new Visitor();
                update.setId(visitor.getId());
                update.setAreaCode(visitor.getAreaCode());
                baseRepository.updateSkipNull(Example.of(update),visitor);
            }
        }
    }
}
