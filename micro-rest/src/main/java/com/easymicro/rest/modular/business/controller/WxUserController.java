package com.easymicro.rest.modular.business.controller;


import com.easymicro.persistence.modular.model.business.Employee;
import com.easymicro.persistence.modular.model.business.Member;
import com.easymicro.persistence.modular.model.business.Merchant;
import com.easymicro.rest.core.ApiResult;
import com.easymicro.rest.modular.business.model.SetAutoReplyModel;
import com.easymicro.service.modular.business.EmployeeService;
import com.easymicro.service.modular.business.MemberService;
import com.easymicro.service.modular.business.MerchantService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**************************************
 * 用户相关控制
 *@author LinYingQiang
 *@date 2018-06-29 00:00
 *@qq 961410800
 *
 ************************************/
@CrossOrigin(origins = "*", maxAge = 7200)
@RestController
@RequestMapping("/api/wxuser")
public class WxUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxUserController.class);


    @Autowired
    private MemberService memberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MerchantService merchantService;

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    public ApiResult<Object> login(@RequestBody Member entity, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String areaCode = request.getHeader("areaCode");
        String openId = request.getHeader("openId");
        //从session中获取用户的手机验证码
        String phoneYZM = (String) request.getSession().getServletContext().getAttribute(entity.getPhone());
        if (StringUtils.isBlank(phoneYZM)
                || StringUtils.isBlank(entity.getPhone())
                || !(phoneYZM.equals(entity.getTelYzm()))) {
            res.setCode(ApiResult.PARAMERROR);
            res.setMsg("手机验证码错误!");
            return res;
        }
        Member wrapper = new Member();
        wrapper.setAreaCode(areaCode);
        wrapper.setOpenId(openId);
        Member member = memberService.findOne(Example.of(wrapper));

        if (member == null) {//如果数据库查不出数据
            member = new Member();
            member.setOpenId(openId);
            member.setPhone(entity.getPhone());
            member.setAreaCode(areaCode);
            member.setType(1);//默认是普通用户
            member.setIsLogin(1);//默认是普通用户
            member = memberService.insert(member);//新增操作
            //判断是否员工
            Employee employee = new Employee();
            employee.setPhone(member.getPhone());
            employee.setAreaCode(areaCode);
            employee = employeeService.findOne(Example.of(employee));
            if (employee != null) {
                member.setUid(employee.getId());//员工id
                member.setName(employee.getNickname());//名称
                member.setCardNo(employee.getIdNum());//身份证
                memberService.updateSkipNull(member);//更新
            }
        }else {
            if(entity.getPhone()!=null&&!"".equals(entity.getPhone())){
                member.setPhone(entity.getPhone());
            }
            member.setAreaCode(areaCode);
            member.setIsLogin(1);
            memberService.update(member);

            if(member.getPhone()!=null){
                Employee employee = new Employee();
                employee.setPhone(member.getPhone());
                employee.setAreaCode(areaCode);
                employee = employeeService.findOne(Example.of(employee));
                if (employee != null) {
                    member.setUid(employee.getId());//员工id
                    member.setName(employee.getNickname());//名称
                    member.setCardNo(employee.getIdNum());//身份证
                    memberService.updateSkipNull(member);//更新
                }
            }
        }

        try {
            res.setCode(ApiResult.SUCCESS);
            res.setMsg("登陆成功");
            res.setContent(member);
        } catch (Exception e) {
            LOGGER.error("系统错误", e);
            res.getErrorResult(e);
        }
        return res;
    }



    /**
     * 判断是否已经登录过
     */
    @RequestMapping(value = "/isLogin", method = RequestMethod.GET, produces = "application/json")
    public ApiResult<Object> isLogin(HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        try {
            String openId = request.getHeader("openId");
            String areaCode = request.getHeader("areaCode");
            Member wrapper = new Member();
            wrapper.setOpenId(openId);
            wrapper.setAreaCode(areaCode);
            Member member = memberService.findOne(Example.of(wrapper));
            if (member == null||member.getIsLogin()==0) {
                res.setCode(ApiResult.NOLOGIN);
                res.setMsg("未登录");
            } else {
                if (null == member.getUid()) {
                    Employee employee = new Employee();
                    employee.setPhone(member.getPhone());
                    employee.setAreaCode(areaCode);
                    employee = employeeService.findOne(Example.of(employee));
                    if (employee != null) {
                        member.setUid(employee.getId());
                        memberService.updateSkipNull(member);
                    }
                }
                res.setContent(member);
            }
        } catch (Exception e) {
            res.getErrorResult(e);
        }
        return res;
    }

    /**
     * 完善个人信息
     */
    @RequestMapping(value = "/addOrUpdateWxUser", method = RequestMethod.POST, produces = {
            "application/json"}, consumes = {"application/json"})
    @ResponseBody
    public ApiResult<Object> addOrUpdateWxUser(@RequestBody Member entity, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<Object>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        try {
            Member wrapper = new Member();
            wrapper.setOpenId(openId);
            wrapper.setAreaCode(areaCode);
            Member member = memberService.findOne(Example.of(wrapper));
            if (member == null) {
                res.setCode(ApiResult.NOLOGIN);
                res.setMsg("没有用户信息");
            } else {
                entity.setId(member.getId());
                if (!member.getPhone().equals(entity.getPhone())) {
                    String phoneYZM = (String) request.getSession().getServletContext().getAttribute(entity.getPhone());
                    if (phoneYZM != null && phoneYZM.equals(entity.getTelYzm())) {
                        memberService.updateSkipNull(entity);
                    } else {
                        res.setCode(ApiResult.PARAMERROR);
                        res.setMsg("手机验证码不正确");
                    }
                } else {
                    memberService.updateSkipNull(entity);
                }
            }
            res.setContent(member);
        } catch (Exception e) {
            res.getErrorResult(e);
            LOGGER.error("发生错误:", e);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 发送短信验证码
     */
    @RequestMapping(value = "/phoneValiate/{phone}", method = RequestMethod.GET, produces = {
            "application/json"})
    public @ResponseBody
    ApiResult<Object> phoneValiate(@PathVariable String phone, HttpServletRequest request) {
        String areaCode = request.getHeader("areaCode");
        ApiResult<Object> res = new ApiResult<>();
        try {
            Random random = new Random();
            String result = "";
            for (int i = 0; i < 6; i++) {
                result += random.nextInt(10);
            }
            List<Map<String, String>> list = new ArrayList<>();
            Map<String, String> mp = new HashedMap();
            mp.put("telYzm", result);
            list.add(mp);
            Merchant merchant = merchantService.getByAreaCode(areaCode);
//            Map resutlMap = SmsUtil.SendMsg(phone, "2018418", list,company.getAppKey(),company.getPublicKey());
//            String  reslut = HttpSender.batchSend(merchant.getAppKey(),merchant.getPublicKey(),phone, "2018418", list);
            request.getSession().getServletContext().setAttribute(phone, result);
            //LOGGER.info("发送短信掩码结果:"+ JSON.toJSONString(resutlMap));
            res.setContent(mp);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("发送短信验证出错", e);
        }
        return res;
    }

    /**
     * 根据手机号查询员工
     */
    @RequestMapping(value = "/memberByPhone", method = RequestMethod.POST, produces = {
            "application/json"}, consumes = {"application/json"})
    @ResponseBody
    public ApiResult<Object> memberByPhone(@RequestBody Employee entity, HttpServletRequest request) {
        ApiResult<Object> res = new ApiResult<>();
        String areaCode = request.getHeader("areaCode");
        try {
            Member wrapper = new Member();
            wrapper.setPhone(entity.getPhone());
            wrapper.setAreaCode(areaCode);
            Member member = memberService.findOne(Example.of(wrapper));
//            Employee employee = new Employee();
//            employee.setPhone(entity.getPhone());
//            employee.setAreaCode(areaCode);
//            employee = employeeService.findOne(Example.of(employee));
            if (member == null) {
                res.setCode(ApiResult.NOPERSONINFO);
                res.setMsg("没有找到人员");
            } else {
                res.setContent(member);
            }

        } catch (Exception e) {
            res.getErrorResult(e);
            e.printStackTrace();
            LOGGER.error("发生错误:", e);
        }
        return res;
    }

    /**
     * 用户未登陆
     */
    @RequestMapping(value = "/unAuth", method = RequestMethod.POST, produces = {
            "application/json"}, consumes = {"application/json"})
    public ApiResult<Object> unAuth() {
        ApiResult<Object> res = new ApiResult<>();
        res.setCode(ApiResult.NOLOGIN);
        res.setMsg("未登陆");
        return res;
    }

    /**
     * 查询微信用户
     */
    @RequestMapping(value = "/getWxUserByUid/{uid}", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody
    ApiResult<Member> getWxUserByUid(@PathVariable Long uid, HttpServletRequest request) {
        String areaCode = request.getHeader("areaCode");
        ApiResult<Member> res = new ApiResult<>();
        try {
            Member employee = new Member();
            employee.setUid(uid);
            employee = memberService.findOne(Example.of(employee));
            res.setContent(employee);
        } catch (Exception e) {
            res.getErrorResult(e);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查询微信用户
     */
    @RequestMapping(value = "/getWxUserByOpenId/{openId}", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody
    ApiResult<Member> getWxUserByOpenId(@PathVariable String openId, HttpServletRequest request) {
        String areaCode = request.getHeader("areaCode");
        ApiResult<Member> res = new ApiResult<>();
        try {
            Member member = memberService.selectByOpenIdAndAreaCode(openId, areaCode);
            res.setContent(member);
        } catch (Exception e) {
            res.getErrorResult(e);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 验证邀约对象的手机号
     *
     * @param tel
     * @param request
     * @return
     */
    @RequestMapping(value = "/validateYyTel/{tel}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public ApiResult<Member> validateYyTel(@PathVariable String tel, HttpServletRequest request) {
        String areaCode = request.getHeader("areaCode");
        String openId = request.getHeader("openId");
        ApiResult<Member> res = new ApiResult<>();
        try {
            Member member = memberService.selectByOpenIdAndAreaCode(openId, areaCode);
            if (member != null && member.getPhone().equals(tel)) {
                res.setCode(ApiResult.OPERATION_ERROR);
                res.setMsg("自己不允许邀约自己 !");
            } else {
                Member member1 = memberService.selectWxUserByTel(tel, areaCode);
                if (member1 != null) {
                    if (member1.getUid() != null && member1.getUid() != 0) {
                        res.setCode(ApiResult.OPERATION_ERROR);
                        res.setMsg("不允许邀约所属公司的员工!");
                    } else {
                        res.setContent(member1);
                    }
                }
            }
        } catch (Exception e) {
            res.getErrorResult(e);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 设置自动回复
     */
    @RequestMapping(value = "/setAutoReply", method = RequestMethod.POST, produces = {
            "application/json"}, consumes = {"application/json"})
    public @ResponseBody
    ApiResult<Member> setAutoReply(@RequestBody SetAutoReplyModel setAutoReplyModel, HttpServletRequest request) {
        ApiResult<Member> res = new ApiResult<>();
        String openId = request.getHeader("openId");
        String areaCode = request.getHeader("areaCode");
        try {
            Member member = memberService.selectByOpenIdAndAreaCode(openId, areaCode);
            if (StringUtils.isNotBlank(setAutoReplyModel.getTip())) {
                member.setTip(setAutoReplyModel.getTip());
                memberService.updateSkipNull(member);
                res.setContent(member);
            }
        } catch (Exception e) {
            res.getErrorResult(e);
            e.printStackTrace();
        }
        return res;
    }
}
