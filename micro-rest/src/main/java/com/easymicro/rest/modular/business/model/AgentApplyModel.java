package com.easymicro.rest.modular.business.model;


public class AgentApplyModel {

    /**
     * 被访人
     */
	private Long uid;

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

    /**
     * 来访单位
     */
	private String unit;

    /**
     * 代预约人姓名
     */
	private String name;

    /**
     * 电话
     */
	private String tel;

    /**
     * 性别
     */
	private String sex;

    /**
     * 民族
     */
	private String nation;

    /**
     * 身份证号
     */
	private String idnum;

    /**
     * 地址
     */
	private String address;

    /**
     * 车牌号
     */
	private String carNum;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	
	

}
