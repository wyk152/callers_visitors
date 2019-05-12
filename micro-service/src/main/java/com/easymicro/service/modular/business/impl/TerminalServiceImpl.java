package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.Terminal;
import com.easymicro.persistence.modular.repository.business.TerminalRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.TerminalService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 终端service实现
 * @author LinYingQiang
 * @date 2018-09-19 12:16
 * @qq 961410800
 *
************************************/
@Service
public class TerminalServiceImpl extends ServiceImpl<TerminalRepository,Terminal,Long> implements TerminalService {

    @Override
    public void insertOrUpdate(String data) {
        List<Terminal> terminals = JSON.parseArray(data, Terminal.class);
        for (Terminal terminal : terminals) {
            terminal.setAreacode(terminal.getCompanycode());
            if (terminal.getTabTime() == null || terminal.getUploadTime() == null) {
                baseRepository.save(terminal);
            } else if (terminal.getTabTime().getTime() != terminal.getUploadTime().getTime()) {
                Terminal update = new Terminal();
                update.setId(terminal.getId());
                update.setAreacode(terminal.getAreacode());
                baseRepository.updateSkipNull(Example.of(update), terminal);
            }
        };
    }
}
