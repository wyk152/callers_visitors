package com.easymicro.persistence.modular.repository.system;

import com.easymicro.persistence.core.repository.CustomRepository;
import com.easymicro.persistence.modular.model.system.Menu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**************************************
 * 菜单DAO接口
 * @author LinYingQiang
 * @date 2018-08-09 20:12
 * @qq 961410800
 *
************************************/
public interface MenuRepository extends CustomRepository<Menu,Long> {

}
