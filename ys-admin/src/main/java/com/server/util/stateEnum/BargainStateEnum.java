package com.server.util.stateEnum;

public enum BargainStateEnum {

	BARGAIN_FAIL(0,"砍价失败"),
	BARGAIN_SUCCESS(1,"砍价成功"),
	BARGAINING(2,"砍价中")
	;
	private Integer state;
	private String stateName;
	
	BargainStateEnum(Integer state,String stateName){
		this.state = state;
		this.stateName = stateName;
	}

	public Integer getState() {
		return state;
	}

	public String getStateName() {
		return stateName;
	}
	
	
}
