package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 来访事由
 * @author LinYingQiang
 * @date 2018-09-19 14:41
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_visit_reasons")
public class VisitReasons extends BaseApiModel {

    /** 来访事由选项表id */
    @Column(name = "id")
    private Integer id;

    /** 来访事由 */
    @Column(name = "value")
    private String value;

    /** 是否使用（0：使用 1：不使用） */
    @Column(name = "isues")
    private Integer isuse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIsuse() {
        return isuse;
    }

    public void setIsuse(Integer isuse) {
        this.isuse = isuse;
    }
}
