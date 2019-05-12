package com.easymicro.service.modular.system.impl;

import com.easymicro.core.exception.GunsException;
import com.easymicro.core.exception.impl.BizExceptionEnum;
import com.easymicro.core.support.StrKit;
import com.easymicro.persistence.modular.model.system.Dict;
import com.easymicro.persistence.modular.repository.system.DictRepository;
import com.easymicro.service.core.ServiceImpl;
import com.easymicro.service.modular.system.DictService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**************************************
 * 字典Serivce实现
 *@author LinYingQiang
 *@date 2018-08-10 20:20
 *@qq 961410800
 *
 ************************************/
@Service
public class DictServiceImpl extends ServiceImpl<DictRepository,Dict,Long> implements DictService {

    /**
     * 拼接字符串的CODE
     */
    public static final String MUTI_STR_CODE = "CODE";

    /**
     * 拼接字符串的NAME
     */
    public static final String MUTI_STR_NAME = "NAME";

    /**
     * 拼接字符串的NUM
     */
    public static final String MUTI_STR_NUM = "NUM";

    /**
     * 每个条目之间的分隔符
     */
    public static final String ITEM_SPLIT = ";";

    /**
     * 属性之间的分隔符
     */
    public static final String ATTR_SPLIT = ":";

    /**
     * 拼接字符串的id
     */
    public static final String MUTI_STR_ID = "ID";

    @Override
    public void addDict(String dictCode, String dictName, String dictTips, String dictValues) {
        List<Dict> dicts =  baseRepository.selectList(dictCode, 0L);
        if (dicts != null && dicts.size() > 0) {
            throw new GunsException(BizExceptionEnum.DICT_EXISTED);
        }

        //解析dictValues
        ArrayList<Map<String,String>> results = new ArrayList<>();
        String[] items = StrKit.split(StrKit.removeSuffix(dictValues, ITEM_SPLIT), ITEM_SPLIT);
        for (String item : items) {
            String[] attrs = item.split(ATTR_SPLIT);
            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(MUTI_STR_CODE,attrs[0]);
            itemMap.put(MUTI_STR_NAME,attrs[1]);
            itemMap.put(MUTI_STR_NUM,attrs[2]);
            results.add(itemMap);
        }
        //添加字典
        Dict dict = new Dict();
        dict.setName(dictName);
        dict.setCode(dictCode);
        dict.setTips(dictTips);
        dict.setNum(0);
        dict.setPid(0L);
        dict = baseRepository.save(dict);


        //添加字典条目
        for (Map<String, String> item : results) {
            String code = item.get(MUTI_STR_CODE);
            String name = item.get(MUTI_STR_NAME);
            String num = item.get(MUTI_STR_NUM);
            Dict itemDict = new Dict();
            itemDict.setPid(dict.getId());
            itemDict.setCode(code);
            itemDict.setName(name);
            try {
                itemDict.setNum(Integer.valueOf(num));
            } catch (NumberFormatException e) {
                throw new GunsException(BizExceptionEnum.DICT_MUST_BE_NUMBER);
            }
            baseRepository.save(itemDict);
        }
    }

    @Override
    public void editDict(Integer dictId, String dictCode, String dictName, String dictTips, String dicts) {
        baseRepository.deleteById(dictId.longValue());
        this.addDict(dictCode,dictName,dictTips, dicts);
    }

    @Override
    public void delteDict(Long dictId) {
        Dict tpl = new Dict();
        tpl.setPid(dictId.longValue());
        Example<Dict> example = Example.of(tpl);
        List<Dict> dicts = baseRepository.findAll(example);
        if (dicts != null && dicts.size() > 0) {
            baseRepository.deleteAll(dicts);
        }
        baseRepository.deleteById(dictId.longValue());
    }

    @Override
    public List<Dict> selectByCode(String code) {
        Dict tpl = new Dict();
        tpl.setCode(code);
        return baseRepository.findAll(Example.of(tpl));
    }

    @Override
    public List<Dict> selectByParentCode(String code) {
        List<Dict> dicts = selectByCode(code);
        List<Long> pId = dicts.stream().map(Dict::getId).collect(Collectors.toList());
        if (pId != null && pId.size() > 0) {
            return baseRepository.findAll((Specification<Dict>) (root, query, builder) -> {
                Predicate p1 = root.get("pid").as(Long.class).in(pId);
                return builder.in(p1);
            });
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> list(String conditiion) {
        Dict tpl = new Dict();
        tpl.setName(conditiion);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<Dict> example = Example.of(tpl, matcher);
        List<Dict> all = baseRepository.findAll(example);
        List<Map<String, Object>> rstList = new ArrayList<>(all.size());
        return all.stream().map( m ->{
            Map<String, Object> maps = new HashMap<>();
            maps.put("id",m.getId());
            maps.put("num",m.getNum());
            maps.put("pid",m.getPid());
            maps.put("name",m.getName());
            maps.put("code",m.getCode());
            maps.put("tips",m.getTips());
            return maps;
        }).collect(Collectors.toList());
    }
}
