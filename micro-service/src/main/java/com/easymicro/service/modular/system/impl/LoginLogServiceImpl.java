package com.easymicro.service.modular.system.impl;

import com.easymicro.persistence.modular.model.system.LoginLog;
import com.easymicro.persistence.modular.repository.system.LoginLogRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.LoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**************************************
 *
 *@author LinYingQiang
 *@date 2018-08-10 21:21
 *@qq 961410800
 *
 ************************************/
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogRepository,LoginLog,Long> implements LoginLogService {


    @Override
    public Page<LoginLog> getLoginLogs(Pageable page, String beginTime, String endTime, String logName) {
        return baseRepository.findAll((Specification<LoginLog>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //and (createTime between CONCAT(#{beginTime},' 00:00:00') and CONCAT(#{endTime},' 23:59:59'))
            if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
                String start = beginTime.concat(" 00:00:00");
                String end = endTime.concat(" 23:59:59");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Predicate p1 = builder.between(root.get("createtime").as(Date.class), sdf.parse(start), sdf.parse(end));
                    predicates.add(p1);
                } catch (ParseException e) {

                }
            }
            //and logname like CONCAT('%',#{logName},'%')
            if (StringUtils.isNotBlank(logName)) {
                Predicate p1 = builder.like(root.get("logname").as(String.class), "%" + logName + "%");
                predicates.add(p1);
            }
            return builder.and(predicates.stream().toArray(Predicate[]::new));
        },page);
    }
}
