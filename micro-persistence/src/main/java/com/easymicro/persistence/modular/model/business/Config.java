package com.easymicro.persistence.modular.model.business;


import com.easymicro.persistence.core.generator.ClassComment;
import com.easymicro.persistence.core.generator.FieldComment;
import com.easymicro.persistence.core.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 平台配置
 * @author LinYingQiang
 * @date 2018-08-30 10:44
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_config")
@ClassComment(name = "配置")
public class Config extends BaseEntity {

    /**
     * 配置名称
     */
    @Column(name = "config_name")
    @FieldComment(name = "配置名称")
    private String configName;

    /**
     * 关键字
     */
    @Column(name = "config_key")
    @FieldComment(name = "关键字")
    private String configKey;

    /**
     * 内容
     */
    @Column(name = "config_value")
    @FieldComment(name = "配置值")
    private String configValue;

    /**
     * 备注
     */
    @Column(name = "tips")
    @FieldComment(name = "备注")
    private String tips;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
