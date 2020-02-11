package com.server.util.stateEnum;

public enum PayStateEnum {

	PAY_SUCCESS(10001,"已支付"),
	NOT_PAY(10002,"未支付"),
	PAY_FINISHED(10003,"订单结束"),
	REFUND_LAUNCH(10004,"退款发起"),
	REFUND_FAIL(10005,"退款失败"),
	REFUND_SUCCESS(10006,"退款成功"),
	ORDER_CANCEL(10007,"订单取消"),
	BLANK_ORDER(10008,"空白订单"),
	OPEN_FAIL(10009,"开门失败"),
	PHOTO_FAIL(10010,"拍照失败"),
	ORDER_FAIL(10011,"订单无效"),
	Delivering(200004,"配送中"),
	Delivery_completed(200005,"配送完成")
	;
	private Integer state;
	private String name;
	
	PayStateEnum(Integer state,String name){
		this.state = state;
		this.name= name;
	}
	
	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public static String findStateName(Integer state){
		for (PayStateEnum payStateEnum : PayStateEnum.values()) {
			if(state.equals(payStateEnum.getState()) ){
				return payStateEnum.getName();
			}
		}
		return null;
	}
	
}
