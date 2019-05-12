package com.easymicro.persistence.core.model;


import com.easymicro.persistence.core.generator.FieldComment;
import com.easymicro.persistence.core.generator.FieldType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**************************************
 * 基础实体类
 * @author LinYingQiang
 * @date 2018-08-09 10:51
 * @qq 961410800
 *
************************************/
@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @FieldComment(name = "系统编号",fieType = FieldType.ID)
    protected Long id;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "createtime")
    @CreationTimestamp
    @FieldComment(name = "创建时间",fieType = FieldType.CREATETIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    protected Date createtime;

    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatetime")
    @UpdateTimestamp
    @FieldComment(name = "修改时间",fieType = FieldType.UPDATETIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    protected Date updatetime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

}
