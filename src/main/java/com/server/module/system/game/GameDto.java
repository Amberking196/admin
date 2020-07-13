package com.server.module.system.game;

public class GameDto {

	//剩余次数
	private Integer times;
	//指标停留
	private Integer indexOf;
	//用户抽奖记录 主键id
	private Long id;
	//用户剩余积分
	private Integer integral;
	
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getIndexOf() {
		return indexOf;
	}
	public void setIndexOf(Integer indexOf) {
		this.indexOf = indexOf;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	
	
}
