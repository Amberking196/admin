package com.server.module.system.warehouseManage.warehouseGiveBack;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: warehouse_give_back author name: why create time: 2018-05-24
 * 11:34:32
 */
@Data
@Entity(tableName = "warehouse_give_back", id = "id", idGenerate = "auto")
public class WarehouseGiveBackBean {

	// 自增id
	private Long id;
	private Integer number;
	// 出库单id
	private Long billId;
	// 商品id
	private long itemId;
	// 商品名称
	private String itemName;
	// 数量
	private Integer quantity;
	// 类型
	private Integer type;
	// 操作人
	private String operatorName;
	// 创建时间
	private Date createTime;
	private String time;
	
	// 类型名称
	@NotField
	private String typeName;
	// 仓库id
	@NotField
	private long warehouseId;

}
