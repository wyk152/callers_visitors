package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**************************************
 * 终端外设
 * @author LinYingQiang
 * @date 2018-09-19 14:05
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_terminal_parts")
public class TerminalParts extends BaseApiModel {

    @Column(name = "id")
    private Long id;


    @Column(name = "part_code")
    private String partcode;

    @Column(name = "status")
    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartcode() {
        return partcode;
    }

    public void setPartcode(String partcode) {
        this.partcode = partcode == null ? null : partcode.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}