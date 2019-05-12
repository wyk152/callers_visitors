package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.persistence.modular.model.business.PreApplyGroup;
import com.easymicro.persistence.modular.repository.business.PreApplyDetailRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.PreApplyDetailService;
import com.easymicro.service.modular.business.PreApplyGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 * 预约字表Service实现类
 * @author LinYingQiang
 * @date 2018-09-10 10:32
 * @qq 961410800
 *
 ************************************/
@Service
public class PreApplyDetailServiceImpl extends ServiceImpl<PreApplyDetailRepository, PreApplyDetail, Long> implements PreApplyDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreApplyDetailService.class);

    @Autowired
    private PreApplyGroupService preApplyGroupService;

    @Override
    public List<PreApplyDetail> selectBySyncId(String syncId, String areaCode) {
        PreApplyDetail detail = new PreApplyDetail();
        detail.setSyncid(syncId);
        detail.setAreacode(areaCode);
        return findAll(Example.of(detail));
    }

    @Override
    public PreApplyDetail selectByOpenIdAndSyncIdAndAreaCode(String openId, String syncId, String areaCode) {
        PreApplyDetail detail = new PreApplyDetail();
        detail.setSyncid(syncId);
        detail.setAreacode(areaCode);
        detail.setOpenId(openId);
        return findOne(Example.of(detail));
    }


    @Override
    public PreApplyDetail selectBySyncIdAndAreaCode( String syncId, String areaCode) {
        PreApplyDetail detail = new PreApplyDetail();
        detail.setSyncid(syncId);
        detail.setAreacode(areaCode);
        return findOne(Example.of(detail));
    }

    @Override
    public List<PreApplyDetail> insertOrUpdate(String data) {
        List<PreApplyDetail> preApplyDetails = JSON.parseArray(data, PreApplyDetail.class);
        boolean isHandler = true;
        for (PreApplyDetail preApplyDetail : preApplyDetails) {
            PreApplyGroup preApplyGroup = preApplyGroupService.selectBySyncId(preApplyDetail.getSyncid(), preApplyDetail.getAreacode());
            if (preApplyGroup == null) {
                isHandler = false;
                LOGGER.error("areaCode = " + preApplyDetail.getCompanycode() + "  syncid= " + preApplyDetail.getSyncid() + "主表数据还未接收到，子表数据暂不接收！");
                break;
            }
        }
        if (isHandler) {
            for (PreApplyDetail preApplyDetail : preApplyDetails) {
                preApplyDetail.setAreacode(preApplyDetail.getCompanycode());
                preApplyDetail.setId(null);
                if (preApplyDetail.getTabtime() == null || preApplyDetail.getUploadtime() == null) {
                    baseRepository.save(preApplyDetail);
                } else if (preApplyDetail.getTabtime() .getTime() != preApplyDetail.getUploadtime().getTime()) {
                    PreApplyDetail update = new PreApplyDetail();
                    update.setApplyid(preApplyDetail.getApplyid());
                    update.setAreacode(preApplyDetail.getAreacode());
                    baseRepository.updateSkipNull(Example.of(update), preApplyDetail);
                }
            }
        }
        return preApplyDetails;
    }
}
