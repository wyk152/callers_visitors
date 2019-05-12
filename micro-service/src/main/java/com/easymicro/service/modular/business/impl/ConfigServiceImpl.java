package com.easymicro.service.modular.business.impl;

import com.easymicro.persistence.modular.model.business.Config;
import com.easymicro.persistence.modular.repository.business.ConfigRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.ConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * Service实现类
 *@author LinYingQiang
 *@date 2018-08-12 17:17
 *@qq 961410800
 *
 ************************************/
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigRepository,Config,Long> implements ConfigService {

    @Override
    public String getPlatformDomain() {
        String hql = "SELECT c FROM Config c WHERE c.configKey = 'DOMAIN_NAME'";
        List<Config> configs = baseRepository.queryHQL(hql);
        if (configs != null && configs.size() == 1) {
            return configs.get(0).getConfigValue();
        }
        return null;
    }

    @Override
    public String getWeChatRedirectUrl() {
        String hql = "SELECT c FROM Config c WHERE c.configKey = 'WX_REDIRECT'";
        List<Config> configs = baseRepository.queryHQL(hql);
        if (configs != null && configs.size() == 1) {
            return configs.get(0).getConfigValue();
        }
        return null;
    }

    @Override
    public String getReceiveTemplateId() {
        String hql = "SELECT c FROM Config c WHERE c.configKey = 'WX_RECEIVE_TEMPLATE_ID'";
        List<Config> configs = baseRepository.queryHQL(hql);
        if (configs != null && configs.size() == 1) {
            return configs.get(0).getConfigValue();
        }
        return null;
    }

    @Override
    public String getVistorResponseMsgTemplateId() {
        String hql = "SELECT c FROM Config c WHERE c.configKey = 'WX_VISTORRESPONSEMSG_TEMPLATE_ID'";
        List<Config> configs = baseRepository.queryHQL(hql);
        if (configs != null && configs.size() == 1) {
            return configs.get(0).getConfigValue();
        }
        return null;
    }

    @Override
    public String getApplyResultTemplateId() {
        String hql = "SELECT c FROM Config c WHERE c.configKey = 'WX_APPLYRESULT_TEMPLATE_ID'";
        List<Config> configs = baseRepository.queryHQL(hql);
        if (configs != null && configs.size() == 1) {
            return configs.get(0).getConfigValue();
        }
        return null;
    }

    @Override
    public String getActivityTemplateId() {
        String hql = "SELECT c FROM Config c WHERE c.configKey = 'WX_ACTIVITE_TEMPLATE_ID'";
        List<Config> configs = baseRepository.queryHQL(hql);
        if (configs != null && configs.size() == 1) {
            return configs.get(0).getConfigValue();
        }
        return null;
    }
}