package com.easymicro.rest.modular.business.controller;


import com.easymicro.persistence.modular.constant.business.WxStepEnum;
import com.easymicro.persistence.modular.model.business.*;
import com.easymicro.rest.core.ApiResult;
import com.easymicro.rest.core.EncryptUtil;
import com.easymicro.rest.modular.business.async.TopicService;
import com.easymicro.rest.modular.business.async.event.SyncEvent;
import com.easymicro.rest.modular.business.model.*;
import com.easymicro.service.modular.business.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**************************************
 * 数据同步接口
 * @author LinYingQiang
 * @date 2018-09-18 22:03
 * @qq 961410800
 *
 ************************************/
@RestController
@RequestMapping("/api/sync")
public class DataSyncController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private BlackService blackService;
    @Autowired
    private AccessRecordService accessRecordService;
    @Autowired
    private TerminalService terminalService;
    @Autowired
    private TerminalPartsService terminalPartsService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private PartsStatusService partsStatusService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private VisitReasonsService visitReasonsService;
    @Autowired
    private PreApplyGroupService preApplyGroupService;
    @Autowired
    private PreApplyDetailService preApplyDetailService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private WxyyStepService wxyyStepService;


    /**
     * 数据同步
     */
    @RequestMapping(value = "dataSync", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<String> dataSync(@RequestBody SyncModel syncModel, HttpServletRequest request) {
        String sign = syncModel.getSign();
        String className = syncModel.getClassName();
        String data = syncModel.getData();
        String timestamp = syncModel.getTimestamp();
        ApiResult<String> result = new ApiResult<>();
        String msg = this.verification(data, className, timestamp, sign);
        if (StringUtils.isBlank(msg)) {
            data = EncryptUtil.base64decode(data);
            try {
                switch (className) {
                    case "UUser":// 员工
                        employeeService.insertOrUpdate(data);
                        break;
                    case "Visitor":// 访客
                        visitorService.insertOrUpdate(data);
                        break;
                    case "Blacklist":// 黑名单
                        blackService.insertOrUpdate(data);
                        break;
                    case "AccessRecord":// 访客记录
                        accessRecordService.insertOrUpdate(data);
                        break;
                    case "Terminal":// 终端
                        terminalService.insertOrUpdate(data);
                        break;
                    case "TerminalParts":// 终端外设
                        terminalPartsService.insertOrUpdate(data);
                        break;
                    case "Parts":// 外设
                        partsService.insertOrUpdate(data);
                        break;
                    case "PartsStatus":// 外设状态
                        partsStatusService.insertOrUpdate(data);
                        break;
                    case "Department":// 部门
                        departmentService.insertOrUpdate(data);
                        break;
                    case "VisitReasons":// 来访原因
                        visitReasonsService.insertOrUpdate(data);
                        break;
                    case "VisitorCar":// 访问车辆

                        break;
                    case "Res":// 物品
                        break;
                    case "DraginRes":// 携带物品
                        break;
                    case "TakeoutRes":// 部门
                        break;
                    case "SwingCardRecord":// 刷卡记录
                        break;
                    case "Preapplygoup":// 微信申请记录
                        preApplyGroupService.insertOrUpdate(data);
                        break;
                    case "Preapplygoupdetail":// 微信申请详情
                        List<PreApplyDetail> preApplyDetails = preApplyDetailService.insertOrUpdate(data);
                        //发送消息
                        for (PreApplyDetail preApplyDetail : preApplyDetails) {
                            SyncEvent event = new SyncEvent();
                            PreApplyGroup preApplyGroup = preApplyGroupService.selectBySyncId(preApplyDetail.getSyncid(), preApplyDetail.getAreacode());
                            event.setPreApplyGroup(preApplyGroup);
                            event.setPreApplyDetail(preApplyDetail);
                            topicService.post(event);
                        }
                        break;
                    case "Userexpermission":// 审批次数
                        break;
                    case "Wxyystep":// 微信
                        wxyyStepService.insertOrUpdate(data);
                        break;
                    default:
                        result.setCode(ApiResult.PARAMERROR);
                        result.setMsg("未找到对应的类名");
                        break;
                }
            } catch (Exception e) {
                result.setCode(ApiResult.PARAMERROR);
                result.setCause(e.getMessage());
                LOGGER.error("错误：" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            result.setCode(ApiResult.PARAMERROR);
            result.setMsg(msg);
        }

        return result;
    }


    /**
     * 黑名单下载接口
     */
    @RequestMapping(value = "downBlacklist", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<List<Object>> downBlacklist(@RequestBody DownBlacklistModel downBlacklistModel) {
        ApiResult<List<Object>> result = new ApiResult<>();
        if(!StringUtils.isBlank(downBlacklistModel.getDownTime())) {

        }else {
            result.setMsg("downTime 不能为空");
            result.setCode(ApiResult.PARAMERROR);
        }
        return result;
    }

    /**
     * 微信人员下载接口
     */
    @RequestMapping(value = "downWxUser", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<List<Member>> downWxUser(@RequestBody DownWxUserModel downWxUserModel) {
        ApiResult<List<Member>> result = new ApiResult<>();
        if(downWxUserModel.getDownTime() != null && downWxUserModel.getCompanyCode() != null) {
            LOGGER.info("===========DownTime:" + downWxUserModel.getDownTime() + "===========CompanyCode():" + downWxUserModel.getCompanyCode());
            try {
                List<Member> list = memberService.findAll((Specification<Member>) (root, query, builder) ->{
                    List<Predicate> predicates = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date condition = null;
                    try {
                        condition = sdf.parse(downWxUserModel.getDownTime());
                    } catch (ParseException e) {
                        condition = new Date();
                    }
                    predicates.add(builder.greaterThanOrEqualTo(root.get("tabtime").as(Date.class),condition));
                    predicates.add(builder.equal(root.get("areaCode").as(String.class), downWxUserModel.getCompanyCode()));
                    return builder.and(predicates.toArray(new Predicate[0]));
                });
                result.setContent(list);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("发生错误", e);
                result.getErrorResult(e);
            }
        }
        else {
            result.setMsg("下载时间和公司编号  不能为空");
            result.setCode(ApiResult.PARAMERROR);
        }

        return result;
    }

    /**
     * 预约记录下载接口
     */
    @RequestMapping(value = "downWxApply", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<List<PreApplyGroup>> downWxApply(@RequestBody DownWxUserModel downWxUserModel) {
        ApiResult<List<PreApplyGroup>> result = new ApiResult<>();
        if(downWxUserModel.getDownTime() != null && downWxUserModel.getCompanyCode() != null)
        {
            LOGGER.info("===========DownTime:" + downWxUserModel.getDownTime() + "===========CompanyCode():" + downWxUserModel.getCompanyCode());
            try {
                List<PreApplyGroup> list = preApplyGroupService.findAll((Specification<PreApplyGroup>) (root, query, builder) ->{
                    List<Predicate> predicates = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date condition;
                    try {
                        condition = sdf.parse(downWxUserModel.getDownTime());
                    } catch (ParseException e) {
                        condition = new Date();
                    }
                    predicates.add(builder.greaterThanOrEqualTo(root.get("tabTime").as(Date.class),condition));
                    predicates.add(builder.equal(root.get("areacode").as(String.class), downWxUserModel.getCompanyCode()));
                    return builder.and(predicates.toArray(new Predicate[0]));
                });
                if (list != null && list.size() > 0) {
                    list.stream().forEach(group -> {
                        List<PreApplyDetail> preApplyDetails = preApplyDetailService.selectBySyncId(group.getSyncid(), group.getCompanyCode());
                        group.setPreapplygoupdetail(preApplyDetails);
                    });
                }
                result.setContent(list);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("发生错误", e);
                result.getErrorResult(e);
            }
        }
        else
        {
            result.setMsg("下载时间和公司编号  不能为空");
            result.setCode(ApiResult.PARAMERROR);
        }
        return result;
    }

    /**
     * 活动详情下载接口
     */
    @RequestMapping(value = "downWxApplyDetail",method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<List<PreApplyDetail>> downWxApplyDetail(@RequestBody DownWeCharApplyModel downWeCharApplyModel) {
        ApiResult<List<PreApplyDetail>> result = new ApiResult<>();

        if(downWeCharApplyModel.getDownTime() != null && downWeCharApplyModel.getCompanyCode() != null) {
            LOGGER.info("===========DownTime:" + downWeCharApplyModel.getDownTime() + "===========CompanyCode():" + downWeCharApplyModel.getCompanyCode());
            try {
                List<PreApplyDetail>  detailList = preApplyDetailService.findAll((Specification<PreApplyDetail>) (root,query,builder)->{
                    List<Predicate> predicates = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date condition = null;
                    try {
                        condition = sdf.parse(downWeCharApplyModel.getDownTime());
                    } catch (ParseException e) {
                        condition = new Date();
                    }
                    predicates.add(builder.greaterThanOrEqualTo(root.get("tabtime").as(Date.class), condition));
                    predicates.add(builder.equal(root.get("isActive").as(Integer.class), downWeCharApplyModel.getIsActive()));
                    predicates.add(builder.equal(root.get("areacode").as(String.class), downWeCharApplyModel.getCompanyCode()));
                    return builder.and(predicates.toArray(new Predicate[0]));
                });
                for(PreApplyDetail preapplygoupdetail:detailList) {
                    Member user = memberService.selectByOpenIdAndAreaCode(preapplygoupdetail.getOpenId(), preapplygoupdetail.getAreacode());
                    preapplygoupdetail.setWxuser(user);
                }
                result.setContent(detailList);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("发生错误", e);
                result.getErrorResult(e);
            }
        }
        else {
            result.setMsg("下载时间和公司编号不能为空");
            result.setCode(ApiResult.PARAMERROR);
        }
        return result;
    }

    /**
     * 更新预约记录接口
     */
    @RequestMapping(value = "updatePreapplygoupdetail",method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public @ResponseBody ApiResult<String> updatePreapplygoupdetail(@RequestBody PreApplyDetail preapplygoupdetail) {
        ApiResult<String> result = new ApiResult<>();
        if(preapplygoupdetail.getAccessrecordid() == null
                || StringUtils.isBlank(preapplygoupdetail.getCardnum())
                || StringUtils.isBlank(preapplygoupdetail.getApplyid())
                || StringUtils.isBlank(preapplygoupdetail.getCompanycode())) {
            result.setCode(ApiResult.PARAMERROR);
            result.setMsg("失败");
            result.setCause("参数不全");
            return result;
        }
        preapplygoupdetail.setAreacode(preapplygoupdetail.getCompanycode());

        PreApplyDetail preApplyDetail = preApplyDetailService.findOne((Specification<PreApplyDetail>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("areacode").as(String.class), preapplygoupdetail.getAreacode()));
            predicates.add(builder.equal(root.get("applyid").as(String.class), preapplygoupdetail.getApplyid()));
            predicates.add(builder.isNull(root.get("cardnum").as(String.class)));
            return builder.and(predicates.toArray(new Predicate[0]));
        });

        if (preApplyDetail == null) {
            result.setCode(ApiResult.PARAMERROR);
            result.setMsg("失败");
            result.setCause("没有查询到记录 或者 该条记录已经获得卡号");
        }else{
            preapplygoupdetail.setId(preApplyDetail.getId());
            preApplyDetailService.updateSkipNull(preapplygoupdetail);
            PreApplyGroup preApplyGroup = preApplyGroupService.selectBySyncId(preApplyDetail.getSyncid(), preApplyDetail.getAreacode());
            //发送微信信息
            if (StringUtils.isNotBlank(preapplygoupdetail.getOpenId())) {
                SyncEvent syncEvent = new SyncEvent();
                syncEvent.setPreApplyDetail(preapplygoupdetail);
                syncEvent.setPreApplyGroup(preApplyGroup);
                topicService.post(syncEvent);
            }
            if(preApplyGroup.getLaunchType() != 1) {//邀约，生命周期中不加 审批通过
                Integer type = preApplyGroup.getIsActive() == 1 ? WxStepEnum.ACTIVE: ((preApplyGroup.getIsteam() == 1) ? WxStepEnum.TEAM : WxStepEnum.PERSINAL);
                Integer status = (type == WxStepEnum.ACTIVE ? WxStepEnum.APPLY.getValue() : WxStepEnum.APPROVAL.getValue());//特殊处理，活动审批 阶段，步骤值仍是1
                wxyyStepService.insert(preApplyGroup.getSyncid(),preapplygoupdetail.getApplyid(), "审批通过",status ,type ,preApplyGroup.getAreacode());
            }
        }

        return result;
    }

    /**
     * 更新员工接口
     */
    @RequestMapping(value = "deleteMember",method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public @ResponseBody ApiResult<String> deleteMember(@RequestBody DeleteMemberModel deleteMemberModel){
        ApiResult<String> result = new ApiResult<>();
        if (deleteMemberModel.getUid() == null || StringUtils.isBlank(deleteMemberModel.getAreaCode())) {
            result.setCode(ApiResult.PARAMERROR);
            result.setMsg("失败");
            result.setCause("参数不全");
            return result;
        }
        Employee delete = new Employee();
        delete.setId(deleteMemberModel.getUid().longValue());
        delete.setAreaCode(deleteMemberModel.getAreaCode());
        Employee employee = employeeService.findOne(Example.of(delete));
        if (employee != null) {
            employeeService.delete(employee);
            Member query = new Member();
            query.setUid(employee.getId());
            query.setAreaCode(employee.getAreaCode());
            Member member = memberService.findOne(Example.of(query));
            if (member != null) {
                member.setUid(null);
                memberService.update(member);
            }
        }
        return result;
    }

    /**
     * 删除黑名单接口
     */
    @RequestMapping(value = "deleteBlack",method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<String> deleteBlack(@RequestBody DeleteBlackModel deleteBlackModel){
        ApiResult<String> result = new ApiResult<>();
        if (deleteBlackModel.getVid() == null || StringUtils.isBlank(deleteBlackModel.getAreaCode()))
        {
            result.setCode(ApiResult.PARAMERROR);
            result.setMsg("失败");
            result.setCause("参数不全");
            return result;
        }
        BlackList query = new BlackList();
        query.setVid(deleteBlackModel.getVid());
        query.setAreaCode(deleteBlackModel.getAreaCode());
        BlackList blackList = blackService.findOne(Example.of(query));
        if (blackList != null) {
            blackService.delete(blackList);
        }
        return result;
    }


    /**
     * 校验推送过来的参数
     */
    public String verification(String data, String tableName, String timestamp, String sign) {
        String msg = null;

        if (StringUtils.isBlank(tableName)) {
            msg = "表名不能为空";
        } else if (StringUtils.isBlank(data)) {
            msg = "数据不能为空";
        } else if (StringUtils.isBlank(timestamp)) {
            msg = "时间不能为空";
        } else {
            String tempSign = EncryptUtil.getMD5Str(data + tableName + timestamp);
            int i = data.length();
            if (!tempSign.equals(sign)) {
                msg = "数据校验失败";
            }
        }
        return msg;
    }
}
