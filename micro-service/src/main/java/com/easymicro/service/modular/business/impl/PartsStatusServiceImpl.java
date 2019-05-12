package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.PartsStatus;
import com.easymicro.persistence.modular.repository.business.PartsStatusRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.PartsStatusService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 外设状态service接口
 * @author LinYingQiang
 * @date 2018-09-19 14:27
 * @qq 961410800
 *
************************************/
@Service
public class PartsStatusServiceImpl extends ServiceImpl<PartsStatusRepository,PartsStatus,Long> implements PartsStatusService {


    @Override
    public void insertOrUpdate(String data) {

        List<PartsStatus> partsStatusList = JSON.parseArray(data, PartsStatus.class);

        for (PartsStatus partsStatus : partsStatusList) {
            partsStatus.setAreaCode(partsStatus.getCompanyCode());
            if (partsStatus.getTabTime() == null || partsStatus.getUploadTime() == null) {
                baseRepository.save(partsStatus);
            } else if (partsStatus.getTabTime().getTime() != partsStatus.getUploadTime().getTime()) {
                PartsStatus update = new PartsStatus();
                update.setId(partsStatus.getId());
                update.setAreaCode(partsStatus.getAreaCode());
                baseRepository.updateSkipNull(Example.of(update), partsStatus);
            }
        }
    }
}
