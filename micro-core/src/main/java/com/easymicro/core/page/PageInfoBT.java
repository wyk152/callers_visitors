package com.easymicro.core.page;


import com.easymicro.core.base.warpper.BaseControllerWarpper;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页结果的封装(for Bootstrap Table)
 *
 * @author fengshuonan
 * @Date 2017年1月22日 下午11:06:41
 */
public class PageInfoBT {

    // 结果集
    private List<?> rows;

    // 总数
    private long total;

    public PageInfoBT(Page<?> page) {
        this.rows = page.getContent();
        this.total = page.getTotalElements();
    }

    public PageInfoBT(Page<?> page,Class<? extends BaseControllerWarpper> warpper) {
                try {
            BaseControllerWarpper w1 = warpper.newInstance();
            w1.setObj(page.getContent());
            this.rows = (List<?>) w1.warp();
        } catch (Exception e) {

        }
        this.total = page.getTotalElements();
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
