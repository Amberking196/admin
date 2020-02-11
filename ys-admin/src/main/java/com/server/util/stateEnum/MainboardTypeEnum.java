package com.server.util.stateEnum;

public enum MainboardTypeEnum {

	OLD_MAINBOARD_MACHINES(1,"旧主板机器"),
	NEW_MAINBOARD_ALARM_LAMP(2,"新主板报警灯"),
	NEW_MAINBOARD_TOP_LAMP(3,"新主板顶灯"),
	EIGHT_DOOR_MACHINES(4,"八门机器")
	;
	private Integer type;
	private String typeName;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	MainboardTypeEnum(Integer type, String typeName){
		this.type = type;
		this.typeName = typeName;
	}
	
	public static String foreach(Integer type){
		for (MainboardTypeEnum mainboardType : MainboardTypeEnum.values()) {
			if(mainboardType.getType().equals(type)){
				return mainboardType.getTypeName();
			}
		}
		return null;
	}
	public static void main(String[] args) {
		Integer type = Integer.valueOf("Ver.2".substring(4,5));
		System.out.println(MainboardTypeEnum.foreach(type));
	}
}
