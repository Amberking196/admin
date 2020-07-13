package com.server.module.system.machineManage.machinesWayItem;

import lombok.Data;

@Data
public class WayItemForUpdateVo {
	private Long id;
	private Double price;
	private Double promotionPrice;
	private Long fullNum; 
	private Long picId;
	private Long num;
}
