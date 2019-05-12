package com.easymicro.service.modular.business.impl;

import com.easymicro.persistence.modular.model.business.Merchant;
import com.easymicro.persistence.modular.model.system.User;
import com.easymicro.persistence.modular.repository.business.MerchantRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.MerchantService;
import com.easymicro.service.modular.system.UserService;
import com.easymicro.service.modular.util.WxUtil;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**************************************
 * 商家Service实现类
 *@author LinYingQiang
 *@date 2018-08-12 17:17
 *@qq 961410800
 *
 ************************************/
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantRepository,Merchant,Long> implements MerchantService {

    @Autowired
    private UserService userService;

    @Override
    public void assign(Long id, String userIds) {
        if (id != null) {
            //将所有相关商家id关联的用户关系解除
            List<User> users = userService.findAll((Specification<User>) (root, query, builder) -> {
                Predicate company = builder.equal(root.get("merchant").as(Merchant.class), id);
                return builder.and(company);
            });
            users.forEach(user -> user.setMerchant(null));//设置为空
            userService.insert(users);//更新
            //重新绑定关系
            if (StringUtils.isNotBlank(userIds)) {
                Merchant merchant = baseRepository.getOne(id);
                List<String> strings = Splitter.on(",").omitEmptyStrings().splitToList(userIds);
                List<User> flushUsers = new ArrayList<>(strings.size());
                for (String tmpId : strings) {
                    if (NumberUtils.isCreatable(tmpId)) {
                        User user = userService.find(Long.valueOf(tmpId));
                        user.setMerchant(merchant);
                        flushUsers.add(user);
                    }
                }
                userService.insert(flushUsers);
            }
        }
    }

    @Override
    public Merchant getByAreaCode(String areaCode) {
        Merchant merchant = new Merchant();
        merchant.setAreaCode(areaCode);
        return this.findOne(Example.of(merchant));
    }


    @Autowired
    MerchantRepository merchantRepository;

    /**
     * 获取微信access_token
     * @param areaCode
     * @return
     */
    public String getAccessToken(String areaCode) {

        Merchant merchant = merchantRepository.getByAreaCode(areaCode);//查询商户信息

        String accesstoken = "";

        Date expiresTime = new Date(merchant.getExpiresTime());//获取access_token过期时间
        if (expiresTime.before(new Date())) {
            //access_token过期
            accesstoken = WxUtil.getWxAccessToken(merchant.getAppId(),merchant.getAppScrect());//更新微信access_token
            if (accesstoken != null) {
                merchant.setAccessToken(accesstoken);//更新商户的access_token
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.SECOND, 5400);//微信过期时间为7200，现在系统内设置为5400秒，提前更新access_token
                merchant.setExpiresTime(calendar.getTimeInMillis());
                merchantRepository.update(merchant);//保存到数据库
            }
        } else {
            //access_token未过期
            accesstoken = merchant.getAccessToken();//直接从商户信息里面获取access_token
        }
        return accesstoken;
    }

    public String  updateAccessToken(String areaCode){
        Merchant merchant = merchantRepository.getByAreaCode(areaCode);//查询商户信息
        String accesstoken = WxUtil.getWxAccessToken(merchant.getAppId(),merchant.getAppScrect());//更新微信access_token
        if (accesstoken != null) {
            merchant.setAccessToken(accesstoken);//更新商户的access_token
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.SECOND, 5400);//微信过期时间为7200，现在系统内设置为5400秒，提前更新access_token
            merchant.setExpiresTime(calendar.getTimeInMillis());
            merchantRepository.update(merchant);//保存到数据库
        }
        return accesstoken;
    }
}