package com.easymicro.service.modular.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easymicro.persistence.modular.constant.business.WxStepEnum;
import com.easymicro.persistence.modular.model.business.Member;
import com.easymicro.persistence.modular.model.business.PreApplyDetail;
import com.easymicro.persistence.modular.model.business.PreApplyGroup;
import com.easymicro.persistence.modular.model.business.WxyyStep;
import com.easymicro.persistence.modular.repository.business.PreApplyDetailRepository;
import com.easymicro.persistence.modular.repository.business.PreApplyGroupRepository;
import com.easymicro.persistence.modular.repository.business.WxyyStepRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.PreApplyGroupService;
import com.easymicro.service.modular.business.WxyyStepService;
import com.easymicro.service.modular.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

/**************************************
 * 预约申请主表Service实现类
 * @author LinYingQiang
 * @date 2018-09-10 10:24
 * @qq 961410800
 *
************************************/
@Service
public class PreApplyGroupServiceImpl extends ServiceImpl<PreApplyGroupRepository,PreApplyGroup,Long> implements PreApplyGroupService {

    @Autowired
    private PreApplyDetailRepository preApplyDetailRepository;

    @Autowired
    private PreApplyGroupRepository preApplyGroupRepository;


    @Autowired
    private WxyyStepService wxyyStepService;


     @Autowired
    private WxyyStepRepository wxyyStepRepository;




    @Override
    public void agentApply(PreApplyGroup preapplygoup, PreApplyDetail preapplygoupdetail) {
        baseRepository.save(preapplygoup);
        preapplygoupdetail.setIsConfirm(0);
        preApplyDetailRepository.save(preapplygoupdetail);
    }

    @Override
    public void personalApply(PreApplyGroup preapplygoup, Member wxuser) {
        baseRepository.save(preapplygoup);
        PreApplyDetail detail = new PreApplyDetail();
        detail.setOpenId(preapplygoup.getApplyopenid());
        detail.setSyncid(preapplygoup.getSyncid());
        detail.setAreacode(preapplygoup.getAreacode());
        detail.setType(0);//线上
        detail.setUname(wxuser.getName());
        detail.setUtel(wxuser.getPhone());
        detail.setJointime(new Date());
        detail.setType(1);//线上
        String applyId = UUID.randomUUID().toString().toUpperCase();
        detail.setApplyid(applyId);
        detail.setIsConfirm(0);
        preApplyDetailRepository.save(detail);
        wxyyStepService.insert(preapplygoup.getSyncid(),applyId, WxStepEnum.APPLY.getName(), WxStepEnum.APPLY.getValue(), WxStepEnum.PERSINAL, detail.getAreacode());
    }

    @Override
    public void teamSubmitApply(PreApplyGroup preapplygoup) {
        String hql = "UPDATE PreApplyGroup p SET p.status = :status WHERE p.syncid = :syncId AND p.areacode = :areaCode";
        Query query = baseRepository.getEntityManager().createQuery(hql);
        query.setParameter("status", preapplygoup.getStatus());
        query.setParameter("syncId", preapplygoup.getSyncid());
        query.setParameter("areaCode", preapplygoup.getAreacode());
        query.executeUpdate();
    }

    @Override
    public PreApplyGroup selectBySyncId(String syncId, String areaCode) {
        PreApplyGroup query = new PreApplyGroup();
        query.setAreacode(areaCode);
        query.setSyncid(syncId);
        return findOne(Example.of(query));
    }

    @Override
    public PreApplyGroup selectByActivityId(Long activityId) {
        String hql = "SELECT g FROM PreApplyGroup g WHERE g.activity.id = :id";
        TypedQuery<PreApplyGroup> query = baseRepository.getEntityManager().createQuery(hql, PreApplyGroup.class);
        query.setParameter("id",activityId);
        return query.getSingleResult();
    }

    @Override
    public void insertOrUpdate(String data) {
        List<PreApplyGroup> preApplyGroups = JSON.parseArray(data, PreApplyGroup.class);
        for (PreApplyGroup preapplygoup : preApplyGroups) {
            preapplygoup.setAreacode(preapplygoup.getCompanyCode());
            preapplygoup.setId(null);
            if(preapplygoup.getTabTime() == null || preapplygoup.getUploadtime() == null){
                baseRepository.save(preapplygoup);
            } else if (preapplygoup.getTabTime() .getTime() != preapplygoup.getUploadtime().getTime()) {
                PreApplyGroup update = new PreApplyGroup();
                update.setSyncid(preapplygoup.getSyncid());
                update.setAreacode(preapplygoup.getAreacode());
                baseRepository.updateSkipNull(Example.of(update), preapplygoup);
            }
        }
    }

    @Override
    public JSON searchResult(Long id) {

        Map map =new HashMap();

        Optional<PreApplyGroup> optional=preApplyGroupRepository.findById(id);
        PreApplyGroup preApplyGroup =optional.get();

        if(preApplyGroup==null){
            return ResultUtil.toJson(10001,"预约不存在","预约不存在",null);
        }

        List<WxyyStep> WxyyStep =wxyyStepRepository.querySQL(" select * from bus_wxyystep where sync_id = '"+preApplyGroup.getSyncid()+"' order by id desc ");

        List list =new ArrayList();

        for (WxyyStep step:WxyyStep){

            JSONObject stepMap =new JSONObject();

            stepMap.put("yytype",step.getYytype());
            stepMap.put("uploadtime",step.getUploadtime());
            stepMap.put("tabtime",step.getTabtime());
            stepMap.put("areacode",step.getAreacode());
            stepMap.put("companycode",step.getCompanycode());
            stepMap.put("visitorrecordid",step.getVisitorrecordid());
            stepMap.put("xxyyitemid",step.getXxyyitemid());
            stepMap.put("syncid",step.getSyncid());
            stepMap.put("wxyyremark",step.getWxyyremark());
            stepMap.put("wxyystatus",step.getWxyystatus());
            stepMap.put("id",step.getId());

            PreApplyDetail preApplyDetail = preApplyDetailRepository.findByApplyid(step.getXxyyitemid());

            stepMap.put("primaryid",preApplyDetail.getId());

            list.add(stepMap);
        }
        map.put("content",list);

        return ResultUtil.toJson(1000,"成功",null,map);
    }
}
