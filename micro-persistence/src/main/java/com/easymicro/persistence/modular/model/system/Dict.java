package com.easymicro.persistence.modular.model.system;

import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 字典表
 * @author LinYingQiang
 * @date 2018-08-09 18:18
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "sys_dict")
public class Dict extends BaseEntity {

    /**
     * 排序
     */
    private Integer num;
    /**
     * 父级字典
     */
    private Long pid;
    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 提示
     */
    private String tips;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
