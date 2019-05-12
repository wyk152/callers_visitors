package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.TerminalParts;
import com.easymicro.persistence.modular.repository.business.TerminalPartsRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.TerminalPartsService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 终端外设service实现
 * @author LinYingQiang
 * @date 2018-09-19 14:08
 * @qq 961410800
 *
************************************/
@Service
public class TerminalPartsServiceImpl extends ServiceImpl<TerminalPartsRepository,TerminalParts,Long> implements TerminalPartsService {

    @Override
    public void insertOrUpdate(String data) {
        List<TerminalParts> terminalPartsList = JSON.parseArray(data, TerminalParts.class);
        for (TerminalParts terminalParts : terminalPartsList) {
            terminalParts.setAreaCode(terminalParts.getCompanyCode());
            if (terminalParts.getTabTime() == null || terminalParts.getUploadTime() == null) {
                baseRepository.save(terminalParts);
            } else if (terminalParts.getTabTime().getTime() != terminalParts.getUploadTime().getTime()) {
                TerminalParts update = new TerminalParts();
                update.setId(terminalParts.getId());
                update.setAreaCode(terminalParts.getAreaCode());
                baseRepository.updateSkipNull(Example.of(update), terminalParts);
            }
        }
    }
}
