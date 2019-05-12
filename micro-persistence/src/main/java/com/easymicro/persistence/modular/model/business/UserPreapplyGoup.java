package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**************************************
 * 用户记录的活动
 * @author LinYingQiang
 * @date 2018-09-19 20:07
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_user_preapply_group")
public class UserPreapplyGoup extends BaseEntity {

    private String syncid;

    private String areacode;

    private String openid;

    private Date createdate;

    public String getSyncid() {
        return syncid;
    }

    public void setSyncid(String syncid) {
        this.syncid = syncid;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}
