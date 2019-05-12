package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**************************************
 * 终端
 * @author LinYingQiang
 * @date 2018-09-19 12:13
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_terminal")
public class Terminal extends BaseApiModel {

    private Long id;

    private String terminalname;

    private String mac;

    private String ip;

    private String bak;

    private String lastonlinedate;

    private String areacode;

    private String companycode;

    private String terminalcode;

    private String model;

    private Integer status;

    private Integer onlineStatus;//在线状态  1在线  0离线

    private String companyName;

    private String areaName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalname() {
        return terminalname;
    }

    public void setTerminalname(String terminalname) {
        this.terminalname = terminalname;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBak() {
        return bak;
    }

    public void setBak(String bak) {
        this.bak = bak;
    }

    public String getLastonlinedate() {
        return lastonlinedate;
    }

    public void setLastonlinedate(String lastonlinedate) {
        this.lastonlinedate = lastonlinedate;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public String getTerminalcode() {
        return terminalcode;
    }

    public void setTerminalcode(String terminalcode) {
        this.terminalcode = terminalcode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String getAreaName() {
        return areaName;
    }

    @Override
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
