package com.easymicro.service.modular.system.impl;

import com.easymicro.core.util.ObjectUtils;
import com.easymicro.persistence.modular.model.system.OperationLog;
import com.easymicro.persistence.modular.repository.system.OperationLogRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.OperationLogService;
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
import java.util.Map;

/**************************************
 * 操作日志查询Service实现
 *@author LinYingQiang
 *@date 2018-08-10 23:23
 *@qq 961410800
 *
 ************************************/
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogRepository,OperationLog,Long> implements OperationLogService {


    @Override
    public Page<OperationLog> getOperationLogs(Pageable page, String beginTime, String endTime, String logName, String logType) {
        return baseRepository.findAll((Specification<OperationLog>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //and (createtime between CONCAT(#{beginTime},' 00:00:00') and CONCAT(#{endTime},' 23:59:59'))
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
            //and logtype like CONCAT('%',#{logType},'%')
            if (StringUtils.isNotBlank(logType)) {
                Predicate p1 = builder.like(root.get("logtype").as(String.class), "%" + logType + "%");
                predicates.add(p1);
            }
            return builder.and(predicates.stream().toArray(Predicate[]::new));
        },page);
    }
}
