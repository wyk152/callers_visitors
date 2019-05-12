package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.BlackList;
import com.easymicro.persistence.modular.repository.business.BlackListRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.BlackService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 黑名单实现
 * @author LinYingQiang
 * @date 2018-09-19 11:37
 * @qq 961410800
 *
************************************/
@Service
public class BlackServiceImpl extends ServiceImpl<BlackListRepository,BlackList,Long> implements BlackService {

    @Override
    public void insertOrUpdate(String data) {
        List<BlackList> blackLists = JSON.parseArray(data, BlackList.class);
        for (BlackList blackList : blackLists) {
            blackList.setAreaCode(blackList.getCompanyCode());
            if (blackList.getTabTime() == null || blackList.getUploadTime() == null) {
                baseRepository.save(blackList);
            } else if (blackList.getTabTime().getTime() != blackList.getUploadTime().getTime()) {
                BlackList update = new BlackList();
                update.setAreaCode(blackList.getAreaCode());
                update.setId(blackList.getId());
                baseRepository.updateSkipNull(Example.of(update), blackList);
            }
        }
    }
}
