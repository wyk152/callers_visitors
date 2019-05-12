package com.easymicro.rest.modular.business.model;

public class WeChatApplyModel {
	
	private int pageNo;

	private int pageSize;
	
	private Integer status;
	
	private Integer isteam;
	
	private Integer launchType;//0 个人主动发起 1 邀约  2 代预约

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsteam() {
		return isteam;
	}

	public void setIsteam(Integer isteam) {
		this.isteam = isteam;
	}

	public Integer getLaunchType() {
		return launchType;
	}

	public void setLaunchType(Integer launchType) {
		this.launchType = launchType;
	}
	
	

}
