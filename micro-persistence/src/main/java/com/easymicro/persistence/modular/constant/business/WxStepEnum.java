package com.easymicro.persistence.modular.constant.business;

public enum WxStepEnum {
	CREATED(0){public String getName(){return "创建预约";}},
	APPLY(1){public String getName(){return "访客预约";}},
	APPROVAL(2){public String getName(){return "被访者审批";}},
	VISITED(3){public String getName(){return "访客开始访问";}},
	LEAVE(4){public String getName(){return "访客离开";}};
	
	public static Integer PERSINAL = 1;//个人预约
	
	public static Integer TEAM = 2;//团队预约
	
	public static Integer ACTIVE = 3;//活动预约
	
	private int value;
	
	private WxStepEnum(int value){
		this.value=value;
	}
	
	public int getValue(){
		return this.value;
	}
	public abstract String getName();
}

