package com.easymicro.admin.core.common.constant.factory;

import com.easymicro.admin.core.common.constant.state.Order;
import com.easymicro.core.support.HttpKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
public class PageFactory {

    public static Pageable defaultPage(){
        HttpServletRequest request = HttpKit.getRequest();
        int limit = Integer.valueOf(request.getParameter("limit"));     //每页多少条数据
        int offset = Integer.valueOf(request.getParameter("offset"));   //每页的偏移量(本页当前有多少条)
        String sort = request.getParameter("sort");         //排序字段名称
        String order = request.getParameter("order");       //asc或desc(升序或降序)
        Pageable pageable;
        if (StringUtils.isNotBlank(sort)) {
            Sort s1;
            if (Order.ASC.getDes().equals(order)) {
                s1 = new Sort(Sort.Direction.ASC, sort);
            }else{
                s1 = new Sort(Sort.Direction.DESC, sort);
            }
            pageable = PageRequest.of((offset/limit),limit,s1);
        }else{
            pageable = PageRequest.of((offset/limit),limit);
        }
        return pageable;
    }

    public static Pageable defaultPage(Sort sort){
        HttpServletRequest request = HttpKit.getRequest();
        int limit = Integer.valueOf(request.getParameter("limit"));     //每页多少条数据
        int offset = Integer.valueOf(request.getParameter("offset"));   //每页的偏移量(本页当前有多少条)
        return PageRequest.of((offset/limit),limit,sort);
    }
}
