/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easymicro.admin.modular.system.controller;
import com.easymicro.admin.core.common.annotion.BussinessLog;
import com.easymicro.admin.core.common.annotion.Permission;
import com.easymicro.admin.core.common.constant.Const;
import com.easymicro.admin.core.common.constant.dictmap.DictMap;
import com.easymicro.admin.core.common.constant.factory.ConstantFactory;
import com.easymicro.admin.core.log.LogObjectHolder;
import com.easymicro.admin.modular.system.warpper.DeptWarpper;
import com.easymicro.admin.modular.system.warpper.DictWarpper;
import com.easymicro.core.base.controller.BaseController;
import com.easymicro.persistence.modular.model.system.Dict;
import com.easymicro.service.modular.system.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 字典控制器
 *
 * @author fengshuonan
 * @Date 2017年4月26日 12:55:31
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/system/dict/";

    @Autowired
    private DictService dictService;

    /**
     * 跳转到字典管理首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:21 PM
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dict.html";
    }

    /**
     * 跳转到添加字典类型
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:21 PM
     */
    @RequestMapping("/dict_add_type")
    public String deptAddType() {
        return PREFIX + "dict_add_type.html";
    }

    /**
     * 跳转到添加字典条目
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:22 PM
     */
    @RequestMapping("/dict_add_item")
    public String deptAddItem(@RequestParam("dictId") Long dictId, Model model) {
        model.addAttribute("dictTypeId", dictId);
        model.addAttribute("dictTypeName", ConstantFactory.me().getDictName(dictId));
        return PREFIX + "dict_add_item.html";
    }

    /**
     * 新增字典
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:22 PM
     */
    @RequestMapping(value = "/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object add(Dict dictDto) {
        this.dictService.addDict(dictDto.getCode(),dictDto.getName(),dictDto.getTips(),dictDto.getNum()+"");
        return SUCCESS_TIP;
    }

    /**
     * 获取所有字典列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:22 PM
     */
    @RequestMapping(value = "/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(String condition) {
   /*     Page<Map<String, Object>> list = this.dictService.list(condition);
        Page<Map<String, Object>> warpper = new DictWrapper(list).wrap();
*/
        List<Map<String, Object>> list = this.dictService.list(condition);
        return super.warpObject(new DictWarpper(list));

    }

    /**
     * 删除字典记录
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:22 PM
     */
    @BussinessLog(value = "删除字典记录", key = "dictId", dict = DictMap.class)
    @RequestMapping(value = "/delete")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delete(@RequestParam Long dictId) {

        //缓存被删除的名称
        LogObjectHolder.me().set(ConstantFactory.me().getDictName(dictId));

        this.dictService.delteDict(dictId);

        return SUCCESS_TIP;
    }

}
