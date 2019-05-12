package com.easymicro.rest.modular.business.controller;


import com.easymicro.persistence.modular.constant.business.WxStepEnum;
import com.easymicro.persistence.modular.model.business.*;
import com.easymicro.rest.core.ApiResult;
import com.easymicro.rest.modular.business.model.WxMyActiveList;
import com.easymicro.service.modular.business.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**************************************
 * 活动接口
 * @author LinYingQiang
 * @date 2018-07-05 12:18
 * @qq 961410800
 *
 ************************************/
@CrossOrigin(origins = "*", maxAge = 3600) //* 可以改成ip地址
@RestController
@RequestMapping("/api/wxactive")
public class WxActivityController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PreApplyGroupService preApplyGroupService;

    @Autowired
    private PreApplyDetailService preApplyDetailService;

    @Autowired
    private WxyyStepService wxyyStepService;

    @Autowired
    private UserPreapplyGoupService userPreapplyGoupService;

    /**
     * 活动列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Map> list(@RequestBody WxMyActiveList activeListModel, HttpServletRequest request) {
        ApiResult<Map> result = new ApiResult<>();
        String areaCode = request.getHeader("areaCode");
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        PageRequest pageRequest = PageRequest.of(activeListModel.getPageNo() - 1, activeListModel.getPageSize(), sort);
        Page<PreApplyGroup> page = preApplyGroupService.findAll((Specification<PreApplyGroup>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isActive").as(Integer.class), 1));
            if (StringUtils.isNotBlank(areaCode)) {
                predicates.add(builder.equal(root.get("areacode").as(String.class), areaCode));
            }
            if (StringUtils.isNotBlank(activeListModel.getTitle())) {
                predicates.add(builder.like(root.get("activityTitle").as(String.class), "%" + activeListModel.getTitle() + "%"));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        }, pageRequest);

        List<PreApplyGroup> groupList = page.getContent();
        for (PreApplyGroup group : groupList) {
            List<Member> members = memberService.selectWxUserByApplyDetail(group.getSyncid(), group.getAreacode());
            group.setMembers(members);
        }

        Map resultMap = new HashMap();
        resultMap.put("list", groupList);
        resultMap.put("totalCount", page.getTotalElements());
        result.setContent(resultMap);
        return result;
    }

    /**
     * 活动详情
     */
    @RequestMapping(value = "detail/{syncId}", method = RequestMethod.GET, produces = {"application/json"})
    public ApiResult<Object> detail(@PathVariable String syncId, HttpServletRequest request) {
        ApiResult<Object> result = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");

        PreApplyGroup group = new PreApplyGroup();
        group.setSyncid(syncId);
        group.setAreacode(areaCode);
        group = preApplyGroupService.findOne(Example.of(group));
        //查询申请人数
        PreApplyDetail detail = new PreApplyDetail();
        detail.setAreacode(areaCode);
        detail.setSyncid(syncId);
        Long count = preApplyDetailService.count(Example.of(detail));
        group.setApplyCount(count.intValue());
        //查询申请人
        QMember _Q_Member = QMember.member;
        QPreApplyDetail _Q_detail = QPreApplyDetail.preApplyDetail;

        List<PreApplyDetail> details = preApplyDetailService.qslQuery()
                .select(_Q_detail, _Q_Member)
                .from(_Q_detail)
                .leftJoin(_Q_Member)
                .on(_Q_detail.openId.eq(_Q_Member.openId))
                .where(_Q_detail.areacode.eq(areaCode).and(_Q_detail.syncid.eq(syncId)))
                .fetch()
                .stream().map(tuple -> {
                    PreApplyDetail preApplyDetail = tuple.get(_Q_detail);
                    preApplyDetail.setWxuser(tuple.get(_Q_Member));
                    return preApplyDetail;
                }).collect(Collectors.toList());

        if (details != null && details.size() > 0) {
            group.setFromname(details.get(0).getUname());
            List<Member> members = details.stream().map(PreApplyDetail::getWxuser).collect(Collectors.toList());
            group.setMembers(members);
        }

        //查询被来访
        Member member = new Member();
        member.setUid(group.getUid());
        member.setAreaCode(group.getAreacode());
        member = memberService.findOne(Example.of(member));
        if (member != null) {
            group.setToname(member.getNickName());
        }
        //查询是否参与
        detail.setOpenId(openId);
        detail = preApplyDetailService.findOne(Example.of(detail));
        if (detail != null) {
            group.setIsJoin(1);
            group.setApplyname(detail.getUname());
        }
        result.setContent(group);
        return result;
    }


    /**
     * 加入活动
     */
    @RequestMapping(value = "join/{syncId}/{inviterOpenid}", method = RequestMethod.GET, produces = {
            "application/json"})
    public @ResponseBody
    ApiResult<Object> join(@PathVariable String syncId, @PathVariable String inviterOpenid, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        Member wxuser = memberService.selectByOpenIdAndAreaCode(openId, areaCode);

        if (wxuser == null || StringUtils.isBlank(wxuser.getName()) || StringUtils.isBlank(wxuser.getPhone())) {
            res.setCode(ApiResult.NOPERSONINFO);
            res.setMsg("请先完善个人信息");
        } else {
            PreApplyGroup preapplygoup = new PreApplyGroup();
            preapplygoup.setSyncid(syncId);
            preapplygoup.setAreacode(areaCode);
            preapplygoup = preApplyGroupService.findOne(Example.of(preapplygoup));
            if (preapplygoup == null) {
                res.setCode(ApiResult.OPERATION_ERROR);
                res.setMsg("活动信息不存在");
            }
            if (preapplygoup.getMaxpersonnum() == 0 || preapplygoup.getJionpersonnum() < preapplygoup.getMaxpersonnum()) {
                //查询是否参与
                PreApplyDetail detail = new PreApplyDetail();
                detail.setAreacode(areaCode);
                detail.setSyncid(syncId);
                detail.setOpenId(openId);
                PreApplyDetail tempDetail = preApplyDetailService.findOne(Example.of(detail));
                if (tempDetail == null) {
                    tempDetail = new PreApplyDetail();
                    String sId = UUID.randomUUID().toString().toUpperCase();
                    tempDetail.setApplyid(sId);
                    tempDetail.setSyncid(syncId);
                    tempDetail.setOpenId(openId);
                    tempDetail.setInviterOpenid(inviterOpenid);
                    tempDetail.setUname(wxuser.getName());
                    tempDetail.setUtel(wxuser.getPhone());
                    tempDetail.setJointime(new Date());
                    tempDetail.setType(1);//线上
                    tempDetail.setIsActive(1);//活动
                    tempDetail.setAreacode(areaCode);
                    //修改参与人数
                    if (preapplygoup.getJionpersonnum() == null) {
                        preapplygoup.setJionpersonnum(1);
                    } else {
                        preapplygoup.setJionpersonnum(preapplygoup.getJionpersonnum() + 1);
                    }
                    preApplyGroupService.updateSkipNull(preapplygoup);
                    preApplyDetailService.insert(tempDetail);
                    wxyyStepService.insert(syncId, sId, "您加入了活动", WxStepEnum.APPLY.getValue(), WxStepEnum.ACTIVE, preapplygoup.getAreacode());
                } else {
                    res.setCode(ApiResult.OPERATION_ERROR);
                    res.setMsg("您已经报名了，请勿重复操作");
                }
            } else {
                res.setCode(ApiResult.OPERATION_ERROR);
                res.setMsg("活动人数已满");
            }
        }
        return res;
    }


    /**
     * 我参加的活动
     */
    @RequestMapping(value = "/myActiveList", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Map> myActiveList(@RequestBody WxMyActiveList appleyModel, HttpServletRequest request) {
        ApiResult<Map> result = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        PageRequest pageRequest = PageRequest.of(appleyModel.getPageNo() - 1, appleyModel.getPageSize());
        PreApplyDetail query = new PreApplyDetail();
        query.setIsActive(1);
        query.setAreacode(areaCode);
        query.setOpenId(openId);
        Page<PreApplyDetail> page = preApplyDetailService.findAll(Example.of(query), pageRequest);
        List<PreApplyDetail> content = page.getContent();
        List<PreApplyGroup> list = new ArrayList<>(content.size());
        content.forEach(detail -> {
            PreApplyGroup group = new PreApplyGroup();
            group.setAreacode(areaCode);
            group.setSyncid(detail.getSyncid());
            group.setIsActive(1);
            group = preApplyGroupService.findOne(Example.of(group));
            group.setCardNum(detail.getCardnum());
            list.add(group);
        });
        Map resultMap = new HashMap();
        resultMap.put("list", list);
        resultMap.put("totalCount", page.getTotalElements());
        result.setContent(resultMap);
        return result;
    }

    /**
     * 记录分享的活动
     */
    @RequestMapping(value = "/saveMyShareActive/{syncId}", method = RequestMethod.GET, produces = {"application/json"})
    public ApiResult<String> saveMyShareActive(@PathVariable String syncId, HttpServletRequest request) {
        ApiResult<String> result = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");

        UserPreapplyGoup query = new UserPreapplyGoup();
        query.setAreacode(areaCode);
        query.setOpenid(openId);
        query.setSyncid(syncId);

        query = userPreapplyGoupService.findOne(Example.of(query));

        if (query == null) {
            query.setCreatedate(new Date());
            userPreapplyGoupService.insert(query);
        }

        return result;
    }


    /**
     * 我分享的活动列表
     */
    @RequestMapping(value = "/myShareActiveList", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public ApiResult<Map> myShareActiveList(@RequestBody WxMyActiveList appleyModel, HttpServletRequest request) {
        ApiResult<Map> result = new ApiResult<Map>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        PageRequest pageRequest = PageRequest.of(appleyModel.getPageNo() - 1, appleyModel.getPageSize());
        Page<UserPreapplyGoup> page = userPreapplyGoupService.findAll((Specification<UserPreapplyGoup>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("openid").as(String.class), openId));
            predicates.add(builder.equal(root.get("areaCode").as(String.class), areaCode));
            return builder.and(predicates.toArray(new Predicate[0]));
        }, pageRequest);

        Map resultMap = new HashMap();
        resultMap.put("list", page.getContent());
        resultMap.put("totalCount", page.getTotalElements());
        result.setContent(resultMap);
        return result;
    }

    //=========================================================================================================//

    /**
     * @param openId
     * @param type    操作
     * @param request
     * @return
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
}
