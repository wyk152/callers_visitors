package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.*;
import java.util.Date;

/**************************************
 * 外设状态
 * @author LinYingQiang
 * @date 2018-09-19 14:24
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_parts_status")
public class PartsStatus extends BaseApiModel {

    @Column(name = "id")
    private Long id;


    @Column(name = "part_code")
    private String partcode;

    @Column(name = "status")
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "record_time")
    private Date recordtime;

    @Column(name = "ex_content")
    private String excontent;

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
        this.partcode = partcode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Date recordtime) {
        this.recordtime = recordtime;
    }

    public String getExcontent() {
        return excontent;
    }

    public void setExcontent(String excontent) {
        this.excontent = excontent;
    }
}
