package com.server.module.system.purchase.purchaseApplyItem;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  purchase_apply_bill_item
 * author name: yjr
 * create time: 2018-09-04 09:05:00
 */ 
@Data
@Entity(tableName="purchase_apply_bill_item",id="",idGenerate="auto")
public class PurchaseApplyBillItemBean{


@JsonIgnore	public String tableName="purchase_apply_bill_item";
@JsonIgnore	public String selectSql="select * from purchase_apply_bill_item where 1=1 ";
@JsonIgnore	public String selectSql1="select id,billId,itemId,itemName,barCode,unitName,quantity,supplierId,createTime,remark from purchase_apply_bill_item where 1=1 ";
	private Long id;
	private Long billId;
	private Long itemId;
	private String itemName;
	private String barCode;
	private String unitName;
	private Integer quantity;
	private Long supplierId;
	private Date createTime;
	private String remark;
	private Double price;//单价
	private Integer realQuantity;//实际采购数量

}

