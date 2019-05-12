package com.easymicro.service.modular.business.impl;

import com.easymicro.persistence.modular.model.business.Member;
import com.easymicro.persistence.modular.repository.business.MemberRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.business.MemberService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**************************************
 * 用户Service实现类
 *@author LinYingQiang
 *@date 2018-08-12 17:17
 *@qq 961410800
 *
 ************************************/
@Service
public class MemberServiceImpl extends ServiceImpl<MemberRepository,Member,Long> implements MemberService {

    @Override
    public Member selectByOpenIdAndAreaCode(String openId, String areaCode) {
        return baseRepository.selectByOpenIdAndAreaCode(openId,areaCode);
    }

    @Override
    public List<Member> selectWxUserByApplyDetail(String syncId, String areaCode) {
        String  hql = "SELECT m FROM Member m ,PreApplyDetail d WHERE m.openId = d.openId AND m.areaCode = d.areacode AND" +
                " d.syncid = :syncid AND d.areacode = :areaCode";
        TypedQuery<Member> query = baseRepository.getEntityManager().createQuery(hql,Member.class).setParameter("syncid", syncId).setParameter("areaCode", areaCode);
        return query.getResultList();
    }

    @Override
    public Member selectWxUserByUid(Long uid, String areaCode) {
        String hql = "SELECT m FROM Member m WHERE m.uid = :uid AND m.areaCode = :areaCode";
        TypedQuery<Member> query = baseRepository.getEntityManager().createQuery(hql, Member.class).setParameter("uid", uid).setParameter("areaCode", areaCode);
        List<Member> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            return resultList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Member selectWxUserByTel(String tel, String areaCode) {
        Member query = new Member();
        query.setPhone(tel);
        query.setAreaCode(areaCode);
        return findOne(Example.of(query));
    }
}