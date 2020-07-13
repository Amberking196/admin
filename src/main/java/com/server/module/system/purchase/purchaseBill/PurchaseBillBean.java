package com.server.module.system.purchase.purchaseBill;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;

import lombok.Data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: purchase_bill author name: yjr create time: 2018-09-03 16:23:53
 */
@Data
@Entity(tableName = "purchase_bill", id = "id", idGenerate = "auto")
public class PurchaseBillBean {

	// 自增标识
	private Long id;
	// 申请单id
	private Long applyId;
	// 申请单单号
	private String applyNumber;
	// 所属仓库
	private Long warehouseId;
	// 采购审核人
	private Long auditor;
	// 审核人姓名
	private String auditorName;
	// 采购单单号
	private String number;
	// 创建时间
	private Date createTime;
	// 状态 0 采购中 1已取消 2 已采购
	private Long state;
	// 入库状态 0 未入库 1 部分入库 2 全部入库
	private Integer storageState;
	// 备注
	private String remark;
	// 处理意见
	private String auditOpinion;
	// 仓库名
	private String warehouseName;	
	//申请人
	private Long operator;
	//申请人姓名
	private String operatorName;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;
	
	// 采购单商品集合
	@NotField
	private List<PurchaseBillItemBean> list;
	// 供应商名称
	@NotField
	private String supplierName;
	//供应商Id
	@NotField
	private Integer supplierId;
	// 序号
	@NotField
	private Integer num;

	@NotField
	private String stateName; // 0 采购中 1 已取消 2 已采购

	@NotField
	private String storageStateName;// 0 未入库 1 部分入库 2 全部入库

	public String getStateName() {
		if (state == 0) {
			return "采购中";
		}
		if (state == 1) {
			return "已取消";
		}
		if (state == 2) {
			return "已采购";
		}
		return "";
	}

	public String getStorageStateName() {
		if (storageState == 0) {
			return "未入库";
		}
		if (storageState == 1) {
			return "部分入库";
		}
		if (storageState == 2) {
			return "全部入库";
		}
		return "";
	}

}
