package com.server.module.system.purchase.purchaseBillItem;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  purchase_bill_item
 * author name: yjr
 * create time: 2018-09-03 16:27:30
 */ 
@Data
@Entity(tableName="purchase_bill_item",id="id",idGenerate="auto")
public class PurchaseBillItemBean{

	//自增标识
	private Long id;
	//采购单id
	private Long billId;
	//商品id
	private Long itemId;
	//商品名称
	@ExcelField(title="商品名称")
	private String itemName;
	//商品条形码
	@ExcelField(title="商品条形码")
	private String barCode;
	//商品单位
	@ExcelField(title="单位")
	private String unitName;
	//商品价格
	@ExcelField(title="单价(元)")
	private Double price;
	//供应商
	private Long supplierId;
	//申请数量
	private Long applyQuantity;
	//实际采购数量
	@ExcelField(title="数量")
	private Long quantity;
	@NotField
	@ExcelField(title="总金额(元)")
	private Double totalMoney;
	//已入库数量
	private Long storageQuantity;
	//入库状态 0 未入库 1 部分入库 2 全部入库
	private Integer storageState;
	//创建时间
	private Date createTime;
	//备注
	private String remark;

	@NotField
	private String type;//出入库类别
	
	//供应商名称
	@NotField
	private String supplierName;
	//本次入库数量
	private Long thisQuantity;
	
	
	//采购单号
	@NotField
	private String applyNumber;


}

