package com.easymicro.service.modular.system;

import com.easymicro.persistence.modular.model.system.Dict;
import com.easymicro.service.core.IService;

import java.util.List;
import java.util.Map;

/**************************************
 * 字典Service接口
 *@author LinYingQiang
 *@date 2018-08-10 20:57
 *@qq 961410800
 *
************************************/
public interface DictService extends IService<Dict,Long> {

    /**
     * 添加字典
     */
    void addDict(String dictCode,String dictName,String dictTips, String dictValues);

    /**
     * 编辑字典
     */
    void editDict(Integer dictId,String dictCode, String dictName,String dictTips, String dicts);

    /**
     * 删除字典
     */
    void delteDict(Long dictId);

    /**
     * 根据编码获取词典列表
     */
    List<Dict> selectByCode(String code);

    /**
     * 根据父类编码获取词典列表
     */
    List<Dict> selectByParentCode(String code);

    /**
     * 查询字典列表
     */
    List<Map<String, Object>> list(String conditiion);
}
