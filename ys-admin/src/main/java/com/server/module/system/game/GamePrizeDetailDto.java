package com.server.module.system.game;

import lombok.Data;

@Data
public class GamePrizeDetailDto {
	private Long id;
	private String name;
	private Long companyId;
	private String companyName;
	
	private Integer target;// 机器优惠劵的适用范围 1 公司 2 区域 3 机器
	private String vmCode;// 机器编号
	private Long areaId;// 区域id
	private String areaName;// 区域名称


}
