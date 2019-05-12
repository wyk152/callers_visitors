package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.Parts;
import com.easymicro.persistence.modular.repository.business.PartsRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.PartsService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 外设service实现
 * @author LinYingQiang
 * @date 2018-09-19 14:17
 * @qq 961410800
 *
************************************/
@Service
public class PartsServiceImpl extends ServiceImpl<PartsRepository,Parts,Long> implements PartsService {

    @Override
    public void insertOrUpdate(String data) {
        List<Parts> parts = JSON.parseArray(data, Parts.class);

        for (Parts part : parts) {
            part.setAreaCode(part.getCompanyCode());
            if (part.getTabTime() == null || part.getUploadTime() == null) {
                baseRepository.save(part);
            } else if (part.getTabTime().getTime() != part.getUploadTime().getTime()) {
                Parts update = new Parts();
                update.setId(part.getId());
                update.setAreaCode(part.getAreaCode());
                baseRepository.updateSkipNull(Example.of(update), part);
            }
        }
    }
}
