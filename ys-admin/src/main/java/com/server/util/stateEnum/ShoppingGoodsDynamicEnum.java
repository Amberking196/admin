package com.server.util.stateEnum;

public enum ShoppingGoodsDynamicEnum {
	
	NEWEST(1,"最新"),
	HOTSELL(2,"热销"),
	SECKILL(3,"秒杀");
	
	Integer index;
	String name;
	
	
	public Integer getIndex() {
		return index;
	}


	public void setIndex(Integer index) {
		this.index = index;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	ShoppingGoodsDynamicEnum(Integer index,String name){
		this.index = index;
		this.name = name;
	}
	
	
	

}
