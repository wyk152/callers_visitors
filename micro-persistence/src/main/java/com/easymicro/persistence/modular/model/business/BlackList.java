package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 黑名单
 * @author LinYingQiang
 * @date 2018-09-19 9:53
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_black_list")
public class BlackList extends BaseApiModel {

    /** 黑名单记录ID */
    @Column(name = "id")
    private Integer id;

    /** 访客ID */
    @Column(name = "v_id")
    private Integer vid;

    /** 访客黑名单原因 */
    @Column(name = "reason")
    private String reason;

    /** 访客姓名 */
    @Column(name = "v_name")
    private String vname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }
}
