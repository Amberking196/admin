package com.server.util.stateEnum;

public enum WarehouseTypeEnum {

	WAREHOUSE_PURCHASE(60204,"采购入库"),
	WAREHOUSE_RETURNWAREHOUSE(60205,"归还入库"),
	WAREHOUSE_STOCKREMOVE(60206,"库存转移"),
	
	WAREHOUSE_NORMAL(60405,"常规"),
	WAREHOUSE_SEND(60406,"赠送"),
	WAREHOUSE_BROKEN(60407,"报损"),
	
	WAREHOUSE_RETURN(60408,"正常归还"),
	WAREHOUSE_SENDRETURN(60409,"赠送归还"),
	WAREHOUSE_OTHER(60410,"其他"),
	WAREHOUSE_REMOVEOTHER(60411,"转移他库");
	
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
	WarehouseTypeEnum(int index,String type){
		this.index = index;
		this.type = type;
	}
	
	public static String gettypeInfo(int index){
		for (WarehouseTypeEnum type : WarehouseTypeEnum.values()) {
			if(type.getIndex() == index){
				return type.getType();
			}
		}
		return null;
	}
}
