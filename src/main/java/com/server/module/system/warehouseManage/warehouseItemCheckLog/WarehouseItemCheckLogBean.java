package com.server.module.system.warehouseManage.warehouseItemCheckLog;

import java.math.BigDecimal;
import java.util.Date;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * 
 * author name: why create time: 2018-06-15 14:00:02
 */
@Data
@Entity(tableName="warehouse_item_check_log",id="id",idGenerate="auto")
public class WarehouseItemCheckLogBean {

	//序号
	private Long id;
	@NotField
	private Integer num;
	//商品id
	private Long itemId;
	//商品名称
	private String itemName;
	//入库数
	private Integer inQuantity;
	//出库数
	private Integer outQuantity;
	//其他出库数
	private Integer otherQuantity;
	//初期数
	private Integer quantity;
	//时间
	private Date createTime;
	//公司id
	private Integer companyId;
	//期末数
	@NotField
	private Integer endQuantity;
	
	private BigDecimal startMoney;
	private BigDecimal inMoney;
	private BigDecimal outMoney;
	private BigDecimal otherMoney;
	private BigDecimal money;
	@NotField
	private BigDecimal endMoney;
	
}
