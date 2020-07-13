package com.server.module.system.warehouseManage.warehouseRemoval;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: warehouse_warrant_info author name: why create time: 2018-05-16
 * 00:05:25
 */
@Data
public class WarehouseRemovalForm extends PageAssist {

	// 状态
	Integer state;
	// 仓库编号
	Integer warehouseId;
	// 入库编号
	String number;
	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endTime;

	Integer type;

}
