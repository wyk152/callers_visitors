package com.easymicro.rest.modular.business.model;

public class WxStepParam {

    /**
     * 预约syncId
     */
	private String syncId;

    /**
     * 子表applyId
     */
	private String applyId;

    public String getSyncId() {
        return syncId;
    }

    public void setSyncId(String syncId) {
        this.syncId = syncId;
    }

    public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	
	

}
