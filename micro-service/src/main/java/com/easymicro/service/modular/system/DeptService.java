package com.easymicro.service.modular.system;


import com.easymicro.core.node.ZTreeNode;
import com.easymicro.persistence.modular.model.system.Dept;
import com.easymicro.service.core.IService;

import java.util.List;
import java.util.Map;

/**************************************
 * 部门Service接口
 * @author LinYingQiang
 * @date 2018-08-10 0:37
 * @qq 961410800
 *
************************************/
public interface DeptService extends IService<Dept,Long> {

    /**
     * 删除部门
     */
    void deleteDept(Long deptId);

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(String condition);

    /**
     * 根据id查询
     */
    List<Long> selectByPids(Long id);
}
