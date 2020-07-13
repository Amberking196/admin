package com.server.util.stateEnum;

public enum WarehouseOutputEnum {

	WAREHOUSE_INPUT(0,"入库"),
	WAREHOUSE_OUTPUT(1,"出库");

	
	private int index;
	private String type;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	WarehouseOutputEnum(int index,String type){
		this.index = index;
		this.type = type;
	}
	
	public static String gettypeInfo(int index){
		for (WarehouseOutputEnum type : WarehouseOutputEnum.values()) {
			if(type.getIndex() == index){
				return type.getType();
			}
		}
		return null;
	}
}
