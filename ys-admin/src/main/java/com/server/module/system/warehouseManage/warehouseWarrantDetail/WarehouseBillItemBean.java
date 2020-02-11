package com.server.module.system.warehouseManage.warehouseWarrantDetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * table name: warehouse_bill_item author name: why create time: 2018-05-17
 * 03:15:57
 */
@Data
@Entity(tableName = "warehouse_bill_item", id = "id", idGenerate = "auto")
public class WarehouseBillItemBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 自增标识
	private Long id;
	@NotField
	private Integer num;
	// 入库单号
	private Integer billId;
	// 商品Id
	private Long itemId;
	//商品条形码
	private String barCode;
	// 商品数量
	private Integer quantity;
	// 商品进价
	private BigDecimal price;
	//平均价格
	private BigDecimal averagePrice;
	

	// 金额
	private BigDecimal money;
	// 创建时间
	private Date createTime;
	// 备注
	private String remark;
	// 商品名称
	private String itemName;
	// 商品单位
	private String unitName;
	//是否删除  0 未删除  1 已删除
	private Integer deleteFlag;
	
	@NotField
	private String number;//单号
	@NotField
	private Integer output;
	@NotField
	private String outputLabel;
	
	
	//采购单商品表 id
	private Integer purchaseItemId;
	//采购数量
	private Long purchaseQuantity;
	//采购单id
	@NotField
	private Integer purchaseId;
	@NotField
	private Integer inventory;//库存
	
	
}
