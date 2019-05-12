package com.easymicro.rest.modular.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.easymicro.core.support.StrKit;
import com.easymicro.core.util.DateUtil;
import com.easymicro.persistence.modular.constant.business.WxStepEnum;
import com.easymicro.persistence.modular.model.business.*;
import com.easymicro.rest.core.ApiResult;
import com.easymicro.rest.modular.business.async.TopicService;
import com.easymicro.rest.modular.business.model.*;
import com.easymicro.rest.modular.business.wechat.WeChatUtil;
import com.easymicro.rest.modular.business.wechat.po.WeChatTemplteMsgModel;
import com.easymicro.rest.modular.business.wechat.util.PushTemplateUtil;
import com.easymicro.rest.modular.business.wechat.util.WxUtil;
import com.easymicro.service.modular.business.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**************************************
 * 个人、团队预约模块
 * @author LinYingQiang
 * @date 2018-09-03 14:27
 * @qq 961410800
 *
 ************************************/
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/wxapply")
public class PreApplyController {


    @Autowired
    private MemberService memberService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private PreApplyGroupService preApplyGroupService;

    @Autowired
    private PreApplyDetailService preApplyDetailService;

    @Autowired
    private WxyyStepService wxyyStepService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private EmployeeService employeeService;


    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 我收到的申请
     * //TODO 优化
     */
    @RequestMapping(value = "/approvelist", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Object> approvelist(@RequestBody WeChatApplyModel appleyModel, HttpServletRequest request) {
        ApiResult<Object> result = new ApiResult<>();

        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");

        Member member = memberService.selectByOpenIdAndAreaCode(openId, areaCode);

        StringBuilder sql = new StringBuilder("select p.id as id, p.createtime as createtime,p.updatetime as updatetime, " +
                "p.uid as uid,p.apply_open_id as applyopenid , p.area_code as areacode ,p.reason as reson, p.status as status, " +
                "p.response_msg as responsemsg,p.type as type,p.starttime as starttime, p.endtime as endtime,p.tab_time as tabTime, " +
                "p.uploadtime as uploadtime,p.unit as unit,p.is_team as isteam ,p.max_person_num as maxpersonnum,p.join_person_num as jionpersonnum, " +
                "p.city as city,p.launch_type as launchType,p.is_active as isActive,p.sync_id as syncid,p.activity_logo as activitylogo," +
                "p.activity_logo_path as activitylogoAbsolutePath,p.activity_title as activityTitle,p.sub_title as subtitle, " +
                "p.address as address,p.organizer as organizer,p.content as content," +
                "(select count(1) from bus_pre_apply_detail d1 where d1.sync_id = p.sync_id and d1.area_code = p.area_code) applyCount, " +
                "(select m.name from bus_member m where m.open_id = p.apply_open_id and m.area_code = p.area_code limit 0,1) applyname, " +
                "(select d2.uname from bus_pre_apply_detail d2 where d2.sync_id = p.sync_id and d2.area_code = p.area_code limit 0,1) fromname, " +
                "(select d3.card_num from bus_pre_apply_detail d3 where d3.sync_id = p.sync_id and d3.area_code = p.area_code limit 0,1) cardNum, " +
                "(select d4.apply_id from bus_pre_apply_detail d4 where d4.sync_id = p.sync_id and d4.area_code = p.area_code limit 0,1) applyId, " +
                "(select e.name  from bus_member e where e.u_id = p.uid and e.area_code = p.area_code limit 0,1) toname " +
                "from bus_pre_apply_group p where p.is_active = 0 ");

        StringBuilder countSql = new StringBuilder("select count(p.id)" +
                "from bus_pre_apply_group p where p.is_active = 0 ");

        if (StringUtils.isNotBlank(areaCode)) {
            sql.append(" and p.area_code = :areacode");
            countSql.append(" and p.area_code = :areacode");
        }

        if (member != null) {
            sql.append(" and p.uid = :uid");
            countSql.append(" and p.uid = :uid");
        }

        if (appleyModel.getStatus() != null && appleyModel.getStatus() != 0L) {
            sql.append(" and p.status = :status");
            countSql.append(" and p.status = :status");
        }

        if (appleyModel.getLaunchType() != null) {
            sql.append(" and p.launch_type = :launchType");
            countSql.append(" and p.launch_type = :launchType");
        }else {
            sql.append(" and p.launch_type in (0,2) ");
            countSql.append(" and p.launch_type in (0,2) ");
            sql.append(" and ( ( is_team =1 and p.status !=0 ) or is_team =0 )  ");
            countSql.append("  and ( ( is_team =1 and p.status !=0 ) or is_team =0 )   ");
        }

        sql.append(" order by p.id desc");

        NativeQuery nativeQuery = preApplyGroupService.entityManager().unwrap(Session.class).createNativeQuery(sql.toString());
        NativeQuery conutQuery = preApplyGroupService.entityManager().unwrap(Session.class).createNativeQuery(countSql.toString());

        if (StringUtils.isNotBlank(areaCode)) {
            nativeQuery.setParameter("areacode", areaCode);
            conutQuery.setParameter("areacode", areaCode);
        }
        if (member != null) {
            nativeQuery.setParameter("uid", member.getUid());
            conutQuery.setParameter("uid", member.getUid());
        }
        if (appleyModel.getStatus() != null && appleyModel.getStatus() != 0L) {
            nativeQuery.setParameter("status", appleyModel.getStatus());
            conutQuery.setParameter("status", appleyModel.getStatus());
        }

        if (appleyModel.getLaunchType() != null) {
            nativeQuery.setParameter("launchType", appleyModel.getLaunchType());
            conutQuery.setParameter("launchType", appleyModel.getLaunchType());
        }
        nativeQuery.addScalar("id", StandardBasicTypes.LONG);
        nativeQuery.addScalar("createtime", StandardBasicTypes.DATE);
        nativeQuery.addScalar("updatetime", StandardBasicTypes.DATE);
        nativeQuery.addScalar("applyopenid", StandardBasicTypes.STRING);
        nativeQuery.addScalar("areacode", StandardBasicTypes.STRING);
        nativeQuery.addScalar("reson", StandardBasicTypes.STRING);
        nativeQuery.addScalar("status", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("responsemsg", StandardBasicTypes.STRING);
        nativeQuery.addScalar("type", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("starttime", StandardBasicTypes.STRING);
        nativeQuery.addScalar("endtime", StandardBasicTypes.STRING);
        nativeQuery.addScalar("tabTime", StandardBasicTypes.DATE);
        nativeQuery.addScalar("uploadtime", StandardBasicTypes.DATE);
        nativeQuery.addScalar("unit", StandardBasicTypes.STRING);
        nativeQuery.addScalar("isteam", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("maxpersonnum", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("jionpersonnum", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("city", StandardBasicTypes.STRING);
        nativeQuery.addScalar("launchType", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("isActive", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("syncid", StandardBasicTypes.STRING);
        nativeQuery.addScalar("activitylogo", StandardBasicTypes.STRING);
        nativeQuery.addScalar("activitylogoAbsolutePath", StandardBasicTypes.STRING);
        nativeQuery.addScalar("activityTitle", StandardBasicTypes.STRING);
        nativeQuery.addScalar("subtitle", StandardBasicTypes.STRING);
        nativeQuery.addScalar("address", StandardBasicTypes.STRING);
        nativeQuery.addScalar("organizer", StandardBasicTypes.STRING);
        nativeQuery.addScalar("content", StandardBasicTypes.STRING);
        nativeQuery.addScalar("applyCount", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("applyname", StandardBasicTypes.STRING);
        nativeQuery.addScalar("fromname", StandardBasicTypes.STRING);
        nativeQuery.addScalar("cardNum", StandardBasicTypes.STRING);
        nativeQuery.addScalar("applyId", StandardBasicTypes.STRING);
        nativeQuery.addScalar("toname", StandardBasicTypes.STRING);

        nativeQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.aliasToBean(PreApplyGroup.class));
        nativeQuery.setFirstResult((appleyModel.getPageNo() - 1) * appleyModel.getPageSize());
        nativeQuery.setMaxResults(appleyModel.getPageSize());
        List<PreApplyGroup> resultList = nativeQuery.getResultList();
        for (PreApplyGroup group:resultList) {
            PreApplyDetail detail = preApplyDetailService.selectBySyncIdAndAreaCode(group.getSyncid(),areaCode);
            group.setDetail(detail);
            List list =memberService.selectWxUserByApplyDetail(group.getSyncid(),areaCode);
            group.setMembers(list);
        }

        Object count = conutQuery.getSingleResult();


        Map resultMap = new HashMap();


        resultMap.put("list", resultList);
        resultMap.put("totalCount", count);
        result.setContent(resultMap);
        return result;
    }


    /**
     * 我申请的预约历史
     */
    @RequestMapping(value = "/myApplyHistoryList", method = RequestMethod.POST, produces = {
            "application/json"}, consumes = {"application/json"})
    public ApiResult<Map> myApplyHistoryList(@RequestBody PageModel pageModel, HttpServletRequest request) {
        ApiResult<Map> result = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");

//        String openId = "owO-_vgJJK3rjcB78lUa0GjsESMU";
//        String areaCode = "C100003_1000";

        QPreApplyGroup _Q_PreApplyGroup = QPreApplyGroup.preApplyGroup;
        QPreApplyDetail _Q_PreApplyDetail = QPreApplyDetail.preApplyDetail;
        QMember _Q_Member = QMember.member;

        BooleanExpression idNotNull = _Q_PreApplyGroup.areacode.eq(areaCode)
                .and(_Q_PreApplyGroup.applyopenid.eq(openId));


        QueryResults<Tuple> queryResults = preApplyGroupService.qslQuery()
                .select(_Q_PreApplyGroup, _Q_PreApplyDetail, _Q_Member)
                .from(_Q_PreApplyGroup)
                .innerJoin(_Q_Member)
                .on(_Q_PreApplyGroup.applyopenid.eq(_Q_Member.openId))
                .innerJoin(_Q_PreApplyDetail)
                .on(_Q_PreApplyGroup.syncid.eq(_Q_PreApplyDetail.syncid))
                .where(idNotNull)
                .orderBy(_Q_PreApplyGroup.createtime.desc())
                .offset(pageModel.getPageNo() - 1)
                .limit(pageModel.getPageSize())
                .fetchResults();

        List<PreApplyGroup> groups = queryResults.getResults().stream().map(tuple -> {
            PreApplyGroup group = tuple.get(_Q_PreApplyGroup);
            PreApplyDetail detail = tuple.get(_Q_PreApplyDetail);
            Member member = tuple.get(_Q_Member);
            Member member2 = memberService.selectWxUserByUid(group.getUid(),areaCode);

            group.setDetail(detail);
            group.setToname(member2.getName());
            List<Member> members = memberService.selectWxUserByApplyDetail(group.getSyncid(), group.getAreacode());
            group.setMembers(members);

            return group;
        }).collect(Collectors.toList());


        Map resultMap = new HashMap();
        resultMap.put("list", groups);
        resultMap.put("totalCount", queryResults.getTotal());
        result.setContent(resultMap);
        return result;
    }


    /**
     * 我申请的预约记录
     * TODO 要进行代码优化
     */
    @RequestMapping(value = "/myApplyList", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Map> myApplyList(@RequestBody WeChatApplyModel appleyModel, HttpServletRequest request) {
        ApiResult<Map> result = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");

        QPreApplyGroup _Q_PreApplyGroup = QPreApplyGroup.preApplyGroup;
        QPreApplyDetail _Q_PreApplyDetail = QPreApplyDetail.preApplyDetail;
        QMember _Q_Member = QMember.member;
        QEmployee _Q_Employee = QEmployee.employee;

        BooleanExpression idNotNull = _Q_PreApplyGroup.id.isNotNull();

        if (appleyModel.getStatus() != null) {
            idNotNull = idNotNull.and(_Q_PreApplyGroup.status.eq(appleyModel.getStatus()));
        }

        if (StringUtils.isNotBlank(openId)) {
            idNotNull = idNotNull.and(_Q_PreApplyGroup.applyopenid.eq(openId));
        }

        if (StringUtils.isNotBlank(areaCode)) {
            idNotNull = idNotNull.and(_Q_PreApplyGroup.areacode.eq(areaCode));
        }

        if (appleyModel.getLaunchType() != null) {
            idNotNull = idNotNull.and(_Q_PreApplyGroup.launchType.eq(appleyModel.getLaunchType()));
        }

        if (appleyModel.getIsteam() != null) {
            idNotNull = idNotNull.and(_Q_PreApplyGroup.isteam.eq(appleyModel.getIsteam()));
        } else {
            idNotNull = idNotNull.and(_Q_PreApplyGroup.isteam.eq(0));
        }

        QueryResults<Tuple> results = preApplyDetailService.qslQuery()
                .select(_Q_PreApplyGroup, _Q_PreApplyDetail, _Q_Member, _Q_Employee)
                .from(_Q_PreApplyGroup)
                .innerJoin(_Q_Member)
                .on(_Q_PreApplyGroup.applyopenid.eq(_Q_Member.openId))
                .innerJoin(_Q_Employee)
                .on(_Q_PreApplyGroup.uid.eq(_Q_Employee.id))
                .leftJoin(_Q_PreApplyDetail)
                .on(_Q_PreApplyGroup.syncid.eq(_Q_PreApplyDetail.syncid))
                .where(idNotNull)
                .orderBy(_Q_PreApplyGroup.createtime.desc())
                .offset(appleyModel.getPageNo() - 1)
                .limit(appleyModel.getPageSize())
                .fetchResults();

        List<PreApplyGroup> groups = results.getResults().stream()
                .map(tuple -> {
                    PreApplyGroup group = tuple.get(_Q_PreApplyGroup);
                    PreApplyDetail detail = tuple.get(_Q_PreApplyDetail);
                    if (detail != null) {
                        //设置申请人
                        group.setFromname(detail.getUname());
                        group.setCardNum(detail.getCardnum());
                        group.setApplyId(detail.getApplyid());
                    }
                    //设置被访问人
                    Member employee =new Member();
                    employee.setUid(group.getUid());

                     employee = memberService.findOne(Example.of(employee));
                    if (employee != null) {
                        group.setToname(employee.getName());
                    }
                    List<Member> members = memberService.selectWxUserByApplyDetail(group.getSyncid(), group.getAreacode());
                    group.setMembers(members);
                    group.setApplyCount(members.size());

                    Member member = tuple.get(_Q_Member);
                    if (member != null) {
                        group.setApplyname(member.getName());
                    }
                    return group;
                }).collect(Collectors.toList());

        long total = results.getTotal();

        Map resultMap = new HashMap();
        resultMap.put("list", groups);
        resultMap.put("totalCount", total);
        result.setContent(resultMap);
        return result;
    }

    /**
     * 发起代预约
     */
    @RequestMapping(value = "/agentApply", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<String> agentApply(@RequestBody AgentApplyModel agentApplyModel, HttpServletRequest request) {
        ApiResult<String> res = new ApiResult<>();

        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");

        String syncId = UUID.randomUUID().toString().toUpperCase();

        PreApplyGroup preapplygoup = new PreApplyGroup();
        Member member=null;
        if(agentApplyModel.getUid()!=null){
            member =memberService.selectWxUserByUid(agentApplyModel.getUid(),areaCode);
           if(member!=null){
               preapplygoup.setUnit(member.getCompanyName());
           }
       }
        preapplygoup.setSyncid(syncId);
        preapplygoup.setApplyopenid(openId);
        preapplygoup.setAreacode(areaCode);
        preapplygoup.setStatus(1);//已提交
        preapplygoup.setIsteam(0);
        preapplygoup.setType(1);//线上
        preapplygoup.setLaunchType(2);
        preapplygoup.setIsActive(0);//非活动
        preapplygoup.setStarttime(agentApplyModel.getStarttime());
        preapplygoup.setEndtime(agentApplyModel.getEndtime());
        preapplygoup.setUid(agentApplyModel.getUid());
        if (StringUtils.isNotBlank(preapplygoup.getStarttime()) && StringUtils.isNotBlank(preapplygoup.getEndtime())) {
            preapplygoup.setStarttime(preapplygoup.getStarttime().replaceAll("/", "-"));
            preapplygoup.setEndtime(preapplygoup.getEndtime().replaceAll("/", "-"));
        }
        preapplygoup.setReson(agentApplyModel.getReson());
        preapplygoup.setUid(agentApplyModel.getUid());
        preapplygoup.setCardNum(agentApplyModel.getCarNum());

        PreApplyDetail preapplygoupdetail = new PreApplyDetail();
        String applyid = UUID.randomUUID().toString().toUpperCase();
        preapplygoupdetail.setApplyid(applyid);
        preapplygoupdetail.setSyncid(syncId);
        preapplygoupdetail.setAreacode(areaCode);
        preapplygoupdetail.setUname(agentApplyModel.getName());
        preapplygoupdetail.setUtel(agentApplyModel.getTel());
        preapplygoupdetail.setType(1);//线上
        preapplygoupdetail.setOpenId(openId);
        preapplygoupdetail.setIsActive(0);//非活动

        preApplyGroupService.agentApply(preapplygoup, preapplygoupdetail);

        wxyyStepService.insert(preapplygoup.getSyncid(), preapplygoupdetail.getApplyid(), "发起了代预约", WxStepEnum.APPLY.getValue(), WxStepEnum.PERSINAL, preapplygoup.getAreacode());

        //推送模版消息

       if(member!=null){
           String appointmentTime = preapplygoup.getStarttime() + " - " + preapplygoup.getEndtime();
           String applyTime = dateFormat.format(preapplygoup.getCreatetime());

           String url = configService.getWeChatRedirectUrl();
           String redirect_uri = configService.getPlatformDomain()
                   + "/wxmanager/authorize?redirect_uri=" + url + "&areaCode=" + areaCode + "!2!" + syncId + "&end=examination";

           JSONObject map = PushTemplateUtil.appointment(configService.getReceiveTemplateId(),member.getOpenId(),redirect_uri, preapplygoup.getActivityTitle(), preapplygoup.getReson(), appointmentTime, agentApplyModel.getName(), applyTime);

           String accesstoken = merchantService.getAccessToken(areaCode);
           Integer code = WxUtil.sendMsg(accesstoken, map);
           if (code == 40001 || code == 40002) {
               accesstoken = merchantService.updateAccessToken(areaCode);
               WxUtil.sendMsg(accesstoken, map);
           }
       }

        return res;
    }


    /**
     * 员工发起邀约
     */
    @RequestMapping(value = "/inviteApply", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<String> inviteApply(@RequestBody InviteApplyModel inviteApplyModel, HttpServletRequest request) {
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        ApiResult<String> res = new ApiResult<>();

        try {
            Member wxuser = memberService.selectByOpenIdAndAreaCode(openId, areaCode);

            if (wxuser == null) {
                res.setCode(ApiResult.OPERATION_ERROR);
                res.setContent("员工信息不存在");
                return res;
            }

            String syncId = UUID.randomUUID().toString().toUpperCase();
            PreApplyGroup preapplygoup = new PreApplyGroup();
            preapplygoup.setSyncid(syncId);
            preapplygoup.setApplyopenid(openId);
            preapplygoup.setAreacode(areaCode);
            preapplygoup.setUnit(wxuser.getCompanyName());
            preapplygoup.setStatus(2);//已提交
            preapplygoup.setIsteam(0);
            preapplygoup.setType(1);//线上
            preapplygoup.setLaunchType(1);
            preapplygoup.setIsActive(0);//非活动
            preapplygoup.setUid(wxuser.getUid());
            if (StringUtils.isNotBlank(inviteApplyModel.getStarttime()) && StringUtils.isNotBlank(inviteApplyModel.getEndtime())) {
                preapplygoup.setStarttime(inviteApplyModel.getStarttime().replaceAll("/", "-"));
                preapplygoup.setEndtime(inviteApplyModel.getEndtime().replaceAll("/", "-"));
            }
            preapplygoup.setReson(inviteApplyModel.getReson());

            PreApplyDetail preapplygoupdetail = new PreApplyDetail();
            preapplygoupdetail.setSyncid(syncId);
            String applyid = UUID.randomUUID().toString().toUpperCase();
            preapplygoupdetail.setApplyid(applyid);
            preapplygoupdetail.setAreacode(areaCode);
            preapplygoupdetail.setUname(inviteApplyModel.getName());
            preapplygoupdetail.setUtel(inviteApplyModel.getTel());
            preapplygoupdetail.setType(1);
            preapplygoupdetail.setIsActive(0);//非活动
            Member user = memberService.selectWxUserByTel(inviteApplyModel.getTel(), areaCode);
            if (user != null) {
                preapplygoupdetail.setOpenId(user.getOpenId());
            }

            preApplyGroupService.agentApply(preapplygoup, preapplygoupdetail);

            wxyyStepService.insert(preapplygoup.getSyncid(), preapplygoupdetail.getApplyid(), "发起了邀约", WxStepEnum.APPLY.getValue(), WxStepEnum.PERSINAL, preapplygoup.getAreacode());

            String appointmentTime = preapplygoup.getStarttime() + " - " + preapplygoup.getEndtime();
            String applyTime = dateFormat.format(preapplygoup.getCreatetime());

            String url = configService.getWeChatRedirectUrl();
            String redirect_uri = configService.getPlatformDomain()
                    + "/wxmanager/authorize?redirect_uri=" + url + "&areaCode=" + areaCode + "!2!" + syncId + "&end=results";


            JSONObject map = PushTemplateUtil.appointment(configService.getReceiveTemplateId(),user.getOpenId(),redirect_uri, preapplygoup.getActivityTitle(), preapplygoup.getReson(), appointmentTime, wxuser.getName(), applyTime);

            String accesstoken = merchantService.getAccessToken(areaCode);
            Integer code = WxUtil.sendMsg(accesstoken, map);
            if (code == 40001 || code == 40002) {
                accesstoken = merchantService.updateAccessToken(areaCode);
                WxUtil.sendMsg(accesstoken, map);
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.getErrorResult(e);
        }

        return res;
    }


    /**
     * 访客发起个人预约
     */
    @RequestMapping(value = "/personalApply", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Map> personalApply(@RequestBody PreApplyGroup preapplygoup, HttpServletRequest request) {
        ApiResult<Map> res = new ApiResult<>();

        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        Member member=null;
        if(preapplygoup.getUid()!=null){
             member =memberService.selectWxUserByUid(preapplygoup.getUid(),areaCode);
            if(member!=null){
                preapplygoup.setUnit(member.getCompanyName());
            }
        }
        String syncId = UUID.randomUUID().toString().toUpperCase();
        preapplygoup.setSyncid(syncId);
        preapplygoup.setApplyopenid(openId);
        preapplygoup.setAreacode(areaCode);
        preapplygoup.setStatus(1);//已提交
        preapplygoup.setIsteam(0);
        preapplygoup.setType(1);//线上
        preapplygoup.setLaunchType(0);
        preapplygoup.setIsActive(0);//默认不是活动
        if (StringUtils.isNotBlank(preapplygoup.getStarttime()) && StringUtils.isNotBlank(preapplygoup.getEndtime())) {
            preapplygoup.setStarttime(preapplygoup.getStarttime().replaceAll("/", "-"));
            preapplygoup.setEndtime(preapplygoup.getEndtime().replaceAll("/", "-"));
        }
        Member wxuser = memberService.selectByOpenIdAndAreaCode(openId, areaCode);

        preApplyGroupService.personalApply(preapplygoup, wxuser);

        //推送模版消息
//        topicService.post(preapplygoup);
        if(member!=null) {
            String url = configService.getWeChatRedirectUrl();
            String redirect_uri = configService.getPlatformDomain()
                    + "/wxmanager/authorize?redirect_uri=" + url + "&areaCode=" + areaCode + "!2!" + syncId + "&end=examination";

            String appointmentTime = preapplygoup.getStarttime() + " - " + preapplygoup.getEndtime();
            String applyTime = dateFormat.format(preapplygoup.getCreatetime());
            JSONObject map = PushTemplateUtil.appointment(configService.getReceiveTemplateId(),member.getOpenId(), redirect_uri, preapplygoup.getActivityTitle(), preapplygoup.getReson(), appointmentTime, wxuser.getName(), applyTime);

            String accesstoken = merchantService.getAccessToken(areaCode);
            Integer code = WxUtil.sendMsg(accesstoken, map);
            if (code == 40001 || code == 40002) {
                accesstoken = merchantService.updateAccessToken(areaCode);
                WxUtil.sendMsg(accesstoken, map);
            }
        }
        return res;
    }


    /**
     * 团队预约
     */
    @RequestMapping(value = "/teamApply", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Object> teamApply(@RequestBody PreApplyGroup preapplygoup, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        Member wxuser = memberService.selectByOpenIdAndAreaCode(openId, areaCode);
        if (this.submitVilate(openId, "1", request)) {

            if(preapplygoup.getUid()!=null){
                Member member =memberService.selectWxUserByUid(preapplygoup.getUid(),areaCode);
                if(member!=null){
                    preapplygoup.setUnit(member.getCompanyName());
                }
            }
            String syncId = UUID.randomUUID().toString().toUpperCase();
            preapplygoup.setSyncid(syncId);
            preapplygoup.setApplyopenid(openId);
            preapplygoup.setAreacode(areaCode);
            preapplygoup.setStatus(0);//已保存，未提交
            preapplygoup.setIsteam(1);
            preapplygoup.setType(1);//线上
            preapplygoup.setLaunchType(0);
            preapplygoup.setIsActive(0);//默认不是活动
            if (StringUtils.isNotBlank(preapplygoup.getStarttime()) && StringUtils.isNotBlank(preapplygoup.getEndtime())) {
                preapplygoup.setStarttime(preapplygoup.getStarttime().replaceAll("/", "-"));
                preapplygoup.setEndtime(preapplygoup.getEndtime().replaceAll("/", "-"));
            }
            preApplyGroupService.insert(preapplygoup);
            wxyyStepService.insert(syncId, null, "创建团队预约", WxStepEnum.CREATED.getValue(), WxStepEnum.TEAM, preapplygoup.getAreacode());
            res.setContent(preapplygoup);


        } else {
            res.setCode(ApiResult.PARAMERROR);
            res.setMsg("请不要重复操作");
        }
        return res;
    }

    /**
     * 团队提交
     */
    @RequestMapping(value = "/teamSubmit", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Object> teamSubmit(@RequestBody PreApplyGroup preapplygoup, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        preapplygoup.setAreacode(areaCode);
        preapplygoup.setStatus(1);//已提交
        if (StringUtils.isNotBlank(preapplygoup.getStarttime()) && StringUtils.isNotBlank(preapplygoup.getEndtime())) {
            preapplygoup.setStarttime(preapplygoup.getStarttime().replaceAll("/", "-"));
            preapplygoup.setEndtime(preapplygoup.getEndtime().replaceAll("/", "-"));
        }
        preApplyGroupService.teamSubmitApply(preapplygoup);
        List<PreApplyDetail> detailList = preApplyDetailService.selectBySyncId(preapplygoup.getSyncid(), areaCode);
        for (PreApplyDetail detail : detailList) {
            wxyyStepService.insert(detail.getSyncid(), detail.getApplyid(), "团队预约提交审批", WxStepEnum.APPLY.getValue(), WxStepEnum.TEAM, areaCode);
        }
        //推送模版消息

        Member member=null;
        if(preapplygoup.getUid()!=null){
            member =memberService.selectWxUserByUid(preapplygoup.getUid(),areaCode);
            if(member!=null){
                preapplygoup.setUnit(member.getCompanyName());
            }
        }

        Member applyMember = memberService.selectByOpenIdAndAreaCode(openId,areaCode);
        if(member!=null) {
            String url = configService.getWeChatRedirectUrl();
            String redirect_uri = configService.getPlatformDomain()
                    + "/wxmanager/authorize?redirect_uri=" + url + "&areaCode=" + areaCode + "!2!" + preapplygoup.getSyncid() + "&end=examination";
            String appointmentTime = preapplygoup.getStarttime() + " - " + preapplygoup.getEndtime();
            if(preapplygoup.getCreatetime()==null){
                preapplygoup.setCreatetime(new Date());
            }
            String applyTime = dateFormat.format(preapplygoup.getCreatetime());
            JSONObject map = PushTemplateUtil.appointment(configService.getReceiveTemplateId(),member.getOpenId(), redirect_uri, preapplygoup.getActivityTitle(), preapplygoup.getReson(), appointmentTime, applyMember.getName(), applyTime);

            String accesstoken = merchantService.getAccessToken(areaCode);
            Integer code = WxUtil.sendMsg(accesstoken, map);
            if (code == 40001 || code == 40002) {
                accesstoken = merchantService.updateAccessToken(areaCode);
                WxUtil.sendMsg(accesstoken, map);
            }
        }
        return res;
    }

    /**
     * 加入团队预约
     */
    @RequestMapping(value = "/joinTeam", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Object> joinTeam(@RequestBody PreApplyDetail preapplygoupdetail, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        Member wxuser = memberService.selectByOpenIdAndAreaCode(openId, areaCode);
        if (wxuser == null || StringUtils.isBlank(wxuser.getName()) || StringUtils.isBlank(wxuser.getPhone())) {
            res.setCode(ApiResult.NOPERSONINFO);
            res.setMsg("请先完善个人信息");
        } else {
            PreApplyGroup preapplygoup = preApplyGroupService.selectBySyncId(preapplygoupdetail.getSyncid(), areaCode);
            if (preapplygoup.getStatus() == 0) {
                PreApplyDetail tempDetail = preApplyDetailService
                        .selectByOpenIdAndSyncIdAndAreaCode(openId, preapplygoupdetail.getSyncid(), areaCode);
                if (tempDetail == null) {
                    String applyId = UUID.randomUUID().toString().toUpperCase();
                    preapplygoupdetail.setApplyid(applyId);
                    preapplygoupdetail.setOpenId(openId);
                    preapplygoupdetail.setUname(wxuser.getName());
                    preapplygoupdetail.setUtel(wxuser.getPhone());
                    preapplygoupdetail.setJointime(new Date());
                    preapplygoupdetail.setType(1);//线上
                    preapplygoupdetail.setAreacode(areaCode);
                    preApplyDetailService.insert(preapplygoupdetail);
                    wxyyStepService.insert(preapplygoup.getSyncid(), applyId, wxuser.getName() + " 加入了团队预约", WxStepEnum.APPLY.getValue(), WxStepEnum.TEAM, areaCode);
                } else {
                    res.setCode(ApiResult.OPERATION_ERROR);
                    res.setMsg("您已经加入此团队申请，请勿重复操作");
                }
            } else {
                res.setCode(ApiResult.OPERATION_ERROR);
                res.setMsg("申请已经被发起人提交审批，不能加入");
            }
        }
        return res;
    }

    /**
     * 预约详情
     */
    @RequestMapping(value = "applyDetail/{areaCode}/{syncid}", method = RequestMethod.GET, produces = {"application/json"})
    public ApiResult<PreApplyGroup> applyDetail(@PathVariable String areaCode, @PathVariable String syncid, HttpServletRequest request) {
        ApiResult<PreApplyGroup> result = new ApiResult<>();
        String openId = request.getHeader("openId");
        PreApplyGroup preApplyGroup = preApplyGroupService.selectBySyncId(syncid, areaCode);
        if (preApplyGroup != null) {
            PreApplyDetail detail = preApplyDetailService.selectByOpenIdAndSyncIdAndAreaCode(openId, preApplyGroup.getSyncid(), areaCode);
            preApplyGroup.setDetail(detail);
            List list =preApplyDetailService.selectBySyncId(syncid,areaCode);
            preApplyGroup.setPreapplygoupdetail(list);

            if(preApplyGroup.getLaunchType()==2){

                PreApplyDetail appplyDetail = preApplyDetailService.selectByOpenIdAndSyncIdAndAreaCode(preApplyGroup.getApplyopenid(), preApplyGroup.getSyncid(), areaCode);
                if (appplyDetail != null) {
                    preApplyGroup.setFromname(appplyDetail.getUname());
                }
            }else {
                if (detail != null) {
                    preApplyGroup.setCardNum(detail.getCardnum());
                    preApplyGroup.setApplyId(detail.getApplyid());
                    preApplyGroup.setFromname(detail.getUname());
                }else {
                    Member member = memberService.selectByOpenIdAndAreaCode(preApplyGroup.getApplyopenid(), areaCode);
                    if (member != null) {
                        preApplyGroup.setFromname(member.getName());
                    }
                }
            }

            if (preApplyGroup.getUid() != null) {
                Member member =new Member();
                member.setUid(preApplyGroup.getUid());
                member = memberService.findOne(Example.of(member));
                if (member != null) {
                    preApplyGroup.setToname(member.getName());
                }
            }
            List<Member> members = memberService.selectWxUserByApplyDetail(preApplyGroup.getSyncid(), preApplyGroup.getAreacode());
            preApplyGroup.setMembers(members);
            preApplyGroup.setApplyCount(members.size());
            if (StringUtils.isNotBlank(preApplyGroup.getApplyopenid())) {
                Member member = memberService.selectByOpenIdAndAreaCode(preApplyGroup.getApplyopenid(), areaCode);
                if (member != null) {
                    preApplyGroup.setApplyname(member.getName());
                }
            }
        }
        result.setContent(preApplyGroup);
        return result;
    }

    /**
     * 预约同意
     */
    @RequestMapping(value = "agree/{syncid}", method = RequestMethod.GET, produces = {"application/json"})
    public ApiResult<Object> agree(@PathVariable String syncid, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String areaCode = request.getHeader("areaCode");
        PreApplyGroup preapplygoup = preApplyGroupService.selectBySyncId(syncid, areaCode);

        preapplygoup.setStatus(2);//通过

        preApplyGroupService.updateSkipNull(preapplygoup);

        String auditTime = dateFormat.format(new Date());
        String url = configService.getWeChatRedirectUrl();
        String redirect_uri = configService.getPlatformDomain() + "/wxmanager/authorize?redirect_uri="
                + url + "&areaCode=" + areaCode + "!2!" + syncid + "&end=results";

        Member member =new Member();
        member.setUid(preapplygoup.getUid());
        member =memberService.findOne(Example.of(member));

        String content ="预约信息:您预约" + member.getName() + ",时间" + preapplygoup.getStarttime() + "--"
        + preapplygoup.getEndtime();

        JSONObject map = PushTemplateUtil.auditingResult(configService.getApplyResultTemplateId(),preapplygoup.getApplyopenid(), redirect_uri,preapplygoup.getActivityTitle(), content,"预约同意", auditTime);

        String accesstoken = merchantService.getAccessToken(areaCode);
        Integer code = WxUtil.sendMsg(accesstoken, map);

        if (code == 40001 || code == 40002) {
            accesstoken = merchantService.updateAccessToken(areaCode);
            WxUtil.sendMsg(accesstoken, map);
        }

        return res;
    }

    /**
     * 访客同意
     */
    @RequestMapping(value = "vistorAgree/{syncid}", method = RequestMethod.GET, produces = {"application/json"})
    public ApiResult<Object> vistorAgree(@PathVariable String syncid, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        PreApplyDetail detail = preApplyDetailService.selectByOpenIdAndSyncIdAndAreaCode(openId,
                syncid, areaCode);
        PreApplyGroup group = preApplyGroupService.selectBySyncId(syncid, areaCode);
        if (detail != null) {
            if (detail.getIsConfirm() == 0) {
                wxyyStepService.insert(syncid, detail.getApplyid(), "访客同意了邀约", WxStepEnum.APPROVAL.getValue(),
                        WxStepEnum.PERSINAL, areaCode);

                detail.setIsConfirm(1);//驳回
                preApplyDetailService.updateSkipNull(detail);
                Merchant merchant = merchantService.getByAreaCode(areaCode);
                if (StringUtils.isNotBlank(group.getApplyopenid())) {
                    //logger.info("================访客反馈确认信息=======================");
                    WeChatUtil weChatUtil = new WeChatUtil(merchant.getAppId(), merchant.getAppScrect());
                    WeChatTemplteMsgModel msgModel = new WeChatTemplteMsgModel();
                    msgModel.setFirst("邀请 " + detail.getUname() + " 来访的反馈");
                    msgModel.setKeyword1(DateUtil.dateToStringWithTime());
                    msgModel.setKeyword2("您好！感谢您的邀约，我会准时赴约。");
                    msgModel.setRmk("邀约信息:您邀请" + detail.getUname() + " 来访,时间" + group.getStarttime() + "--"
                            + group.getEndtime());
                    String url = configService.getWeChatRedirectUrl();
                    String redirect_uri = configService.getPlatformDomain() + "/wxmanager/authorize?redirect_uri="
                            + url + "&areaCode=" + areaCode + "!2!" + syncid + "&end=results";


                    String accesstoken = merchantService.getAccessToken(areaCode);

                    Map map = weChatUtil.sendTemplateMsg(accesstoken, group.getApplyopenid(),
                            redirect_uri, msgModel, configService.getVistorResponseMsgTemplateId());

                    if ("40001".equals(map.get("errcode")) || "40002".equals(map.get("errcode"))) {
                        accesstoken = merchantService.updateAccessToken(areaCode);

                        map = weChatUtil.sendTemplateMsg(accesstoken, group.getApplyopenid(),
                                redirect_uri, msgModel, configService.getVistorResponseMsgTemplateId());
                    }
                }

            } else {
                res.setCode(ApiResult.OPERATION_ERROR);
                res.setMsg("请勿重复操作");
            }

        } else {
            res.setCode(ApiResult.OPERATION_ERROR);
            res.setMsg("没有找到预约记录");
        }
        return res;
    }

    /**
     * 访客拒绝
     */
    @RequestMapping(value = "/vistorRefused", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Object> vistorRefused(@RequestBody VistorRespModel vistorRespModel, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        PreApplyDetail detail = preApplyDetailService.selectByOpenIdAndSyncIdAndAreaCode(openId,
                vistorRespModel.getSyncId(), areaCode);
        PreApplyGroup group = preApplyGroupService.selectBySyncId(vistorRespModel.getSyncId(), areaCode);
        if (detail != null) {
            if (detail.getIsConfirm() == 0) {
                wxyyStepService.insert(vistorRespModel.getSyncId(), detail.getApplyid(), "访客拒绝了邀约",
                        WxStepEnum.APPROVAL.getValue(), WxStepEnum.PERSINAL, areaCode);

                detail.setIsConfirm(2);//拒绝
                detail.setRespMsg(vistorRespModel.getRespMsg());
                preApplyDetailService.updateSkipNull(detail);
                Merchant merchant = merchantService.getByAreaCode(areaCode);
                if (StringUtils.isNotBlank(group.getApplyopenid())) {
                    //logger.info("================访客反馈驳回信息=======================");
                    // 穿过来的参数没有来访原因，没有开始和技术访问时间，素以需要再查一次

                    WeChatUtil weChatUtil = new WeChatUtil(merchant.getAppId(), merchant.getAppScrect());
                    WeChatTemplteMsgModel msgModel = new WeChatTemplteMsgModel();
                    msgModel.setFirst("邀请 " + detail.getUname() + " 来访的反馈");
                    msgModel.setKeyword1(DateUtil.dateToStringWithTime());
                    msgModel.setKeyword2(vistorRespModel.getRespMsg());
                    msgModel.setRmk("邀约信息:您邀请" + detail.getUname() + " 来访,时间" + group.getStarttime() + "--"
                            + group.getEndtime());

                    String url = configService.getWeChatRedirectUrl();
                    String redirect_uri = configService.getPlatformDomain() + "/wxmanager/authorize?redirect_uri="
                            + url + "&areaCode=" + areaCode + "!2!" + vistorRespModel.getSyncId() + "&end=results";


                    String accesstoken = merchantService.getAccessToken(areaCode);

                    Map map =weChatUtil.sendTemplateMsg(accesstoken, group.getApplyopenid(),
                            redirect_uri, msgModel, configService.getVistorResponseMsgTemplateId());

                    if ("40001".equals(map.get("errcode")) || "40002".equals(map.get("errcode"))) {
                        accesstoken = merchantService.updateAccessToken(areaCode);

                        map = weChatUtil.sendTemplateMsg(accesstoken, group.getApplyopenid(),
                                redirect_uri, msgModel, configService.getVistorResponseMsgTemplateId());;
                    }

                }
            } else {
                res.setCode(ApiResult.OPERATION_ERROR);
                res.setMsg("请勿重复操作");
            }

        } else {
            res.setCode(ApiResult.OPERATION_ERROR);
            res.setMsg("没有找到预约记录");
        }
        return res;
    }

    /**
     * 预约驳回
     */
    @RequestMapping(value = "/refused", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Object> refused(@RequestBody VistorRespModel respModel, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();

        String areaCode = request.getHeader("areaCode");
        PreApplyGroup preapplygoup = preApplyGroupService.selectBySyncId(respModel.getSyncId(), areaCode);
        preapplygoup.setStatus(3);//驳回
        preapplygoup.setResponsemsg(respModel.getRespMsg());
        preApplyGroupService.updateSkipNull(preapplygoup);


        Merchant merchant =merchantService.getByAreaCode(areaCode);

        Member member =new Member();
        member.setUid(preapplygoup.getUid());
        member =memberService.findOne(Example.of(member));

        List<PreApplyDetail> detailList = preApplyDetailService.selectBySyncId(preapplygoup.getSyncid(), preapplygoup.getAreacode());
        for (PreApplyDetail detail : detailList) {
            wxyyStepService.insert(preapplygoup.getSyncid(), detail.getApplyid(), "审批被驳回", WxStepEnum.APPROVAL.getValue(), (preapplygoup.getIsteam().intValue() == 1) ? WxStepEnum.TEAM : WxStepEnum.PERSINAL, preapplygoup.getAreacode());

            WeChatTemplteMsgModel model =new WeChatTemplteMsgModel();
            model.setFirst("邀请 " + detail.getUname() + " 来访的反馈");

            if(StrKit.notBlank(respModel.getRespMsg())){
                model.setKeyword1("您的预约已被拒绝:"+respModel.getRespMsg());
            }else {
                model.setKeyword1("您的预约已被拒绝");
            }
            model.setKeyword2(DateUtil.dateToStringWithTime());

            model.setRmk("预约信息:您预约" + member.getName() + ",时间" + preapplygoup.getStarttime() + "--"
                    + preapplygoup.getEndtime());
            sendresponsetempmsg(merchant,areaCode,respModel.getSyncId(),preapplygoup.getApplyopenid(),model);
        }

        return res;
    }

    /**
     * 预约生命周期
     */
    @RequestMapping(value = "/step", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<List<WxyyStep>> step(@RequestBody WxStepParam wxStepParam, HttpServletRequest request) {
        String areaCode = request.getHeader("areaCode");
        ApiResult<List<WxyyStep>> result = new ApiResult<>();
        try {
            List<WxyyStep> list = wxyyStepService.findAll((Specification<WxyyStep>) (root, query, builder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(wxStepParam.getSyncId())) {
                    predicates.add(builder.equal(root.get("syncid").as(String.class), wxStepParam.getSyncId()));
                }
                if (StringUtils.isNotBlank(wxStepParam.getApplyId())) {
                    Predicate builderEqual = builder.equal(root.get("xxyyitemid").as(String.class), wxStepParam.getApplyId());
                    Predicate builderNotNull = builder.isNotNull(root.get("xxyyitemid").as(String.class));
                    Predicate builderOr = builder.or(builderEqual, builderNotNull);
                    predicates.add(builderOr);
                }
                if (StringUtils.isNotBlank(areaCode)) {
                    predicates.add(builder.equal(root.get("areacode").as(String.class), areaCode));
                }
                return builder.and(predicates.toArray(new Predicate[0]));
            }, new Sort(Sort.Direction.DESC, "tabtime"));
            result.setContent(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.getErrorResult(e);
        }
        return result;
    }

    /**
     * 我申请的预约记录
     */
    @RequestMapping(value = "/myReceiveInviteList", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Map> myReceiveInviteList(@RequestBody PageModel pageModel, HttpServletRequest request) {
        ApiResult<Map> result = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        Pageable pageable = PageRequest.of(pageModel.getPageNo() - 1, pageModel.getPageSize(), new Sort(Sort.Direction.DESC, "createtime"));
        Page<PreApplyGroup> groups = preApplyGroupService.findAll((Specification<PreApplyGroup>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("launchType").as(Integer.class), 1));
            predicates.add(builder.equal(root.get("isteam").as(Integer.class), 0));
            if (StringUtils.isNotBlank(areaCode)) {
                predicates.add(builder.equal(root.get("areacode").as(String.class), areaCode));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        for (PreApplyGroup group : groups.getContent()) {
            PreApplyDetail detail = preApplyDetailService.selectByOpenIdAndSyncIdAndAreaCode(openId, group.getSyncid(), group.getAreacode());
            if (detail != null) {
                group.setCardNum(detail.getCardnum());
                group.setApplyId(detail.getApplyid());
                group.setDetail(detail);
            }
            Member member =new Member();
            member.setUid(group.getUid());
            member =memberService.findOne(Example.of(member));
            group.setToname(member.getName());
        }
        Map resultMap = new HashMap();
        resultMap.put("list", groups.getContent());
        resultMap.put("totalCount", groups.getTotalElements());
        result.setContent(resultMap);
        return result;
    }


    //================================================================================================================//

    /**
     * 重复提交
     */
    public Boolean submitVilate(String openId, String type, HttpServletRequest request) {
        String key = openId + "-" + type;
        Date lastDate = (Date) request.getSession().getServletContext().getAttribute(key);
        if (lastDate == null) {
            request.getSession().getServletContext().setAttribute(key, new Date());
            return true;
        } else if ((new Date().getTime() - lastDate.getTime()) / 1000 < 60) {
            return false;
        } else {
            request.getSession().getServletContext().setAttribute(key, new Date());
            return true;
        }
    }

    /**
     * 同意或者驳回发送模板消息
     */
    public void sendresponsetempmsg(Merchant merchant, String areaCode, String syncId, String
            openId, WeChatTemplteMsgModel msgModel) {
        try {

            WeChatUtil weChatUtil = new WeChatUtil(merchant.getAppId(), merchant.getAppScrect());

            String url = configService.getWeChatRedirectUrl();
            String redirect_uri = configService.getPlatformDomain()
                    + "/wxmanager/authorize?redirect_uri=" + url + "&areaCode=" + areaCode + "!2!" + syncId + "&end=results";

            String accesstoken = merchantService.getAccessToken(areaCode);

            Map map =weChatUtil.sendTemplateMsg(accesstoken, openId, redirect_uri, msgModel, configService.getApplyResultTemplateId());

            if ("40001".equals(map.get("errcode")) || "40002".equals(map.get("errcode"))) {
                accesstoken = merchantService.updateAccessToken(areaCode);
                 weChatUtil.sendTemplateMsg(accesstoken, openId, redirect_uri, msgModel, configService.getApplyResultTemplateId());
            }

        } catch (Exception e) {
            // e.printStackTrace();
            //logger.error("微信模板消息发送错误", e);
        }
    }


}
