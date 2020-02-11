package com.server.module.system.statisticsManage.purchaseItemStatistics;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * table name:  purchase_item_statistics
 * author name: hjc
 * create time: 2018-09-03 11:02:40
 */ 
@Data
@Entity(tableName="purchase_item_statistics",id="id",idGenerate="auto")
public class PurchaseItemStatisticsBean{
	private Long id;
	private Long itemId;//商品id
	//private Long supplierId;//供应商
	private Double avgPrice;
	private Long sumQuantity;
	
	private Date createTime;
	private Date updateTime;
	private Integer deleteFlag;
	
	@NotField
	private String supplierName;
	@NotField
	private String itemName;
	@NotField
	private String unitName;
	@NotField
	private String barCode;
}

