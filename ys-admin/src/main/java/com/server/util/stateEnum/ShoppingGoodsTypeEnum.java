package com.server.util.stateEnum;

public enum ShoppingGoodsTypeEnum {

	COUPON(25,"优惠券"),
	CARRYWATERVOUCHERS(26,"提水券"),
	CARRYGOODSVOUCHERS(27,"提货券"),
	MEAL(28,"套餐类型");
	
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
	ShoppingGoodsTypeEnum(int index,String type){
		this.index = index;
		this.type = type;
	}
	
	public static String gettypeInfo(int index){
		for (ShoppingGoodsTypeEnum type : ShoppingGoodsTypeEnum.values()) {
			if(type.getIndex() == index){
				return type.getType();
			}
		}
		return null;
	}
}
