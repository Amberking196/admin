package com.server.util.stateEnum;

public enum ItemUnitEnum {

	TING(60500,"听"),
	PING(60501,"瓶"),
	JIAN(60502,"件"),
	BAO(60503,"包"),
	XIANG(60504,"箱"),
	HE(60505,"盒"),
	GUAN(60506,"罐"),
	TONG(60507,"桶"),
	ZHI(60508,"支"),
	TIAO(60509,"条"),
	TAO(60510,"套"),
	SHUANG(60511,"双"),
	GE(60512,"个"),
	SHENG(60513,"升");
	
	public Integer state;
	public String name;
	
	ItemUnitEnum(Integer state,String name){
		this.state = state;
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public String getName() {
		return name;
	}
	
	public static String findUnit(Integer state){
		if(state != null){
			ItemUnitEnum[] values = ItemUnitEnum.values();
			for (ItemUnitEnum itemUnitEnum : values) {
				if(itemUnitEnum.getState().equals(state)){
					return itemUnitEnum.getName();
				}
			}
		}
		return null;
	}
	
}
