package com.easymicro.rest.modular.business.model;

public class VistorRespModel {

    /**
     * 预约syncId
     */
	private String syncId;

    /**
     * 反馈的信息
     */
	private String respMsg;


    public String getSyncId() {
        return syncId;
    }

    public void setSyncId(String syncId) {
        this.syncId = syncId;
    }

    public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	


}
