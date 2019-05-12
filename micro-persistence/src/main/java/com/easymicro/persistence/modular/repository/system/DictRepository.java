package com.easymicro.persistence.modular.repository.system;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.system.Dict;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**************************************
 * 字典DAO接口
 * @author LinYingQiang
 * @date 2018-08-09 20:09
 * @qq 961410800
 *
************************************/
public interface DictRepository extends CustomRepository<Dict,Long> {

    @Query("SELECT d FROM Dict d WHERE d.code = :dictCode AND d.pid = :pId")
    List<Dict> selectList(String dictCode, Long pId);
}
