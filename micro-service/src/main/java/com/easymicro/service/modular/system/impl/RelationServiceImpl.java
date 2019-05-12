package com.easymicro.service.modular.system.impl;


import com.easymicro.persistence.modular.model.system.Relation;
import com.easymicro.persistence.modular.repository.system.RelationRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.RelationService;
import org.springframework.stereotype.Service;

/**************************************
 * 角色和菜单关联表Service实现
 *@author LinYingQiang
 *@date 2018-08-11 00:00
 *@qq 961410800
 *
 ************************************/
@Service
public class RelationServiceImpl extends ServiceImpl<RelationRepository,Relation,Long> implements RelationService {
}
