package com.easymicro.rest.modular.business.model;


public class InviteApplyModel {


    /**
     * 来访原因
     */
	private String reson;

    /**
     * 开始时间
     */
	private String starttime;

    /**
     * 结束时间
     */
	private String endtime;

	
	//////////////////////////////////////

    /**
     * 邀约人姓名
     */
	private String name;

    /**
     * 邀约人手机号
     */
	private String tel;


	public String getReson() {
		return reson;
	}

	public void setReson(String reson) {
		this.reson = reson;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	
}
