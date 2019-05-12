package com.easymicro.persistence.modular.model.business;
import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 外设
 * @author LinYingQiang
 * @date 2018-09-19 14:14
 * @qq 961410800
 *
************************************/

@Entity
@Table(name = "bus_parts")
public class Parts extends BaseApiModel {

    @Column(name = "id")
    private Long id;

    @Column(name = "part_code")
    private String partcode;

    @Column(name = "part_name")
    private String partname;

    @Column(name = "model")
    private String model;

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

    public String getPartname() {
        return partname;
    }

    public void setPartname(String partname) {
        this.partname = partname;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
