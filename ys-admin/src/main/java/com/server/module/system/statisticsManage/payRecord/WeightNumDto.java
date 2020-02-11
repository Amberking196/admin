package com.server.module.system.statisticsManage.payRecord;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class WeightNumDto {

	private Long id;
	//机器编码
	private String vmCode;
	//开门前实际重量
	private Integer preWeight;
	//关门后实际重量
	private Integer posWeight;
	//商品开门前后重量
	private List<NumDto> numList = new ArrayList<NumDto>();
	//开门时间
	private String openDoorTime;
	//关门时间
	private String closedDoorTime;
	//货道号
	private Integer wayNumber;
	//购买的总数量
	private Integer totalNum;
	//所有商品的重量的理论总值
	private Integer theroticalValue;
	//创建订单时间
	private String createOrderTime;
	//相差重量
	private Integer diffWeight;
}
