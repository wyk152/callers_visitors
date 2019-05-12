package com.easymicro.service.modular.system.impl;

import com.easymicro.core.datascope.DataScope;
import com.easymicro.core.util.ObjectUtils;
import com.easymicro.persistence.modular.model.system.User;
import com.easymicro.persistence.modular.repository.system.UserRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.DeptService;
import com.easymicro.service.modular.system.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**************************************
 * 用户实现类
 * @author LinYingQiang
 * @date 2018-08-09 21:41
 * @qq 961410800
 *
************************************/
@Service
public class UserServiceImpl extends ServiceImpl<UserRepository,User,Long> implements UserService {

    @Autowired
    private DeptService deptService;

    @Override
    public void setStatus(Long userId, int status) {
        User user = find(userId);
        if (user != null) {
            user.setStatus(status);
            update(user);
        }
    }

    @Override
    public void changePwd(Long userId, String pwd) {
        User user = find(userId);
        if (user != null) {
            user.setPassword(pwd);
            update(user);
        }
    }

    @Override
    public List<Map<String, Object>> selectUsers(DataScope dataScope,String name, String beginTime, String endTime, Long deptid) {
        List<User> users = findAll((Specification<User>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // status != 3
            Predicate predicate = builder.notEqual(root.get("status").as(Integer.class), 3);
            predicates.add(predicate);
            if (StringUtils.isNotBlank(name)) {
                // (phone like CONCAT('%',#{name},'%') or account like CONCAT('%',#{name},'%') or name like CONCAT('%',#{name},'%'))
                Predicate p1 = builder.like(root.get("phone").as(String.class), "%" + name + "%");
                Predicate p2 = builder.like(root.get("account").as(String.class), "%" + name + "%");
                Predicate p3 = builder.like(root.get("name").as(String.class), "%" + name + "%");
                predicate = builder.or(p1, p2, p3);
                predicates.add(predicate);
            }
            if (deptid != null) {
                // (deptid = #{deptid} or deptid in ( select id from sys_dept where pids like CONCAT('%[', #{deptid}, ']%') ))
                Predicate p1 = builder.equal(root.get("deptid").as(Long.class),deptid);
                List<Long> deptIds = deptService.selectByPids(deptid);
                if (deptIds != null && deptIds.size() > 0) {
                    Predicate p2 = root.get("deptid").as(Long.class).in(deptIds);
                    predicate = builder.or(p1,p2);
                    predicates.add(predicate);
                }else{
                    predicates.add(p1);
                }
            }
            if (dataScope != null && CollectionUtils.isNotEmpty(dataScope.getDeptIds())) {
                List<Long> deptIdsLong = new ArrayList<>(dataScope.getDeptIds().size());
                dataScope.getDeptIds().forEach(deptId -> {
                    deptIdsLong.add(deptId.longValue());
                });
                Predicate p2 = root.get("deptid").as(Long.class).in(deptIdsLong);
                predicates.add(p2);
            }
            if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
                // (createtime between CONCAT(#{beginTime},' 00:00:00') and CONCAT(#{endTime},' 23:59:59'))
                String start = beginTime.concat(" 00:00:00");
                String end = endTime.concat(" 23:59:59");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = sdf.parse(start);
                    endDate = sdf.parse(end);
                } catch (Exception e) {
                    Date current = new Date();
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(current);
                    instance.set(Calendar.HOUR,0);
                    instance.set(Calendar.MINUTE,0);
                    instance.set(Calendar.MILLISECOND,0);
                    startDate = instance.getTime();
                    instance = Calendar.getInstance();
                    instance.setTime(current);
                    instance.set(Calendar.HOUR,23);
                    instance.set(Calendar.MINUTE,59);
                    instance.set(Calendar.MILLISECOND,59);
                    endDate = instance.getTime();
                }
                Predicate p1 = builder.between(root.get("createtime").as(Date.class), startDate, endDate);
                predicates.add(p1);
            }
            return builder.and(predicates.stream().toArray(Predicate[]::new));
        });
        return users.stream().map(ObjectUtils::toMaps).collect(Collectors.toList());
    }

    @Override
    public int setRoles(Long userId, String roleIds) {
        User user = baseRepository.getOne(userId);
        if (user != null) {
            user.setRoleid(roleIds);
        }
        return 0;
    }

    @Override
    public User getByAccount(String account) {
        Optional<User> optional = baseRepository.findOne((Specification<User>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate predicate = builder.notEqual(root.get("status").as(Integer.class), 3);
            predicates.add(predicate);
            if (StringUtils.isNotBlank(account)) {
                predicate = builder.equal(root.get("account").as(String.class), account);
                predicates.add(predicate);
            }
            return builder.and(predicates.stream().toArray(Predicate[]::new));
        });
        return optional.orElse(null);
    }
}
