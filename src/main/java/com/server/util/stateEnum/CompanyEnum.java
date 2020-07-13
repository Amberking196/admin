package com.server.util.stateEnum;

public enum CompanyEnum {
	
	YOUSHUIDAOJIA(76,"广州优水到家"),
	WUHANYOUSHUI(113,"武汉优水"),
	YOUSHUI(149,"优水"),
	
	USING(2050,"启用"),
	FORBIDDEN(2051,"禁用");
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


	CompanyEnum(Integer index,String name){
		this.index = index;
		this.name = name;
	}
	
}
