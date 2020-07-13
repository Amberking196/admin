package com.server.module.system.replenishManage.machinesReplenishStatement;

import java.util.Date;

import com.server.common.persistence.NotField;
import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;

/**
 * 
 * author name: why 补货报表 bean create time: 2018-04-24 11:53:01
 */
@Data
public class ReplenishmentReportBean {

	// 序号
	private Integer id;
	
	// 公司名称
	private String companyName;
	
	// 线路名称
	private String lineName;
	
	// 售货机编号
	private String code;
	
	// 门号
	private Integer wayNumber;
	
	// 商品名称
	private String itemName;
	
	// 补货前数量
	private Integer quantityBeforeReplenishment;
	
	// 补货后数量
	private Integer quantityAfterReplenishment;
	
	// 本次补货数量
	private Integer quantityReplenishment;

	//校正数量
	private Integer adjustNum;
	
	// 补货时间
	private String replenishmentTime;
	
	//补货操作人
	private String replenisher;

	@NotField
	private Integer replenisherId;
	
	private Long operateHistoryId;
}
