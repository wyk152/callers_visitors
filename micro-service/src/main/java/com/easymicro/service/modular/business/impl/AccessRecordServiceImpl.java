package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.AccessRecord;
import com.easymicro.persistence.modular.repository.business.AccessRecordRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.AccessRecordService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 访客service实现类
 * @author LinYingQiang
 * @date 2018-09-19 12:07
 * @qq 961410800
 *
************************************/
@Service
public class AccessRecordServiceImpl extends ServiceImpl<AccessRecordRepository,AccessRecord,Long> implements AccessRecordService {
    @Override
    public void insertOrUpdate(String data) {
        List<AccessRecord> accessRecords = JSON.parseArray(data, AccessRecord.class);
        for (AccessRecord accessRecord : accessRecords) {
            accessRecord.setAreaCode(accessRecord.getCompanyCode());
            if(accessRecord.getTabTime() == null || accessRecord.getUploadTime() == null){
                baseRepository.save(accessRecord);
            } else if (accessRecord.getTabTime().getTime() != accessRecord.getUploadTime().getTime()) {
                AccessRecord update = new AccessRecord();
                update.setAreaCode(accessRecord.getAreaCode());
                update.setId(accessRecord.getId());
                baseRepository.updateSkipNull(Example.of(update), accessRecord);
            }
        }
    }
}
