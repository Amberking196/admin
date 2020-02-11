package com.server.util.stateEnum;

public enum MachineStateEnum {

	MACHINES_NORMAL(20001,"售货机正常"),
	MACHINES_NOT_FACTORY(20002,"售货机未出厂"),
	MACHINES_BREAKDOWN(20003,"售货机故障"),
	MACHINES_WAIT_PUT(20004,"售货机待投放"),
	MACHINES_BACK_FACTORY(20005,"售货机返厂")

	;
	private Integer state;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	MachineStateEnum(Integer state,String name){
		this.state = state;
		this.name = name;
	}
	
	public static MachineStateEnum getMachineStateEnum(Integer state){
		for(MachineStateEnum a :MachineStateEnum.values()){
			if(a.getState().equals(state)){
				return a;
			}
		}
		return null;
	}
	
}
