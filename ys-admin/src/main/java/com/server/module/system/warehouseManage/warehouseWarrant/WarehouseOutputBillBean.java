package com.server.module.system.warehouseManage.warehouseWarrant;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: warehouse_output_bill author name: why create time: 2018-05-16
 * 00:05:25
 */
@Data
@Entity(tableName = "warehouse_output_bill", id = "id", idGenerate = "auto")
public class WarehouseOutputBillBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int BILL_OUT = 1;// 出库
	public final static int BILL_PUT = 0;// 入库

	// 序号
	private Long id;
	private Integer num;
	// 给前端判断用的
	@NotField
	private Integer isShow;
	// 仓库编号
	private Long warehouseId;
	// 出入库操作人(loginInfoId)
	private Integer operator;
	// 出入库审核人(loginInfoId)
	private Integer auditor;
	// 出入库单号
	private String number;
	// 供货单位Id
	private Integer supplierId;
	// 状态 60201 待审核 60202 已审核 60203 已入库
	private Integer state;
	private Integer output;// 0 入库 1 出库
	// 类型 60204 采购入库 60205 归还入库 60206 库存转移
	private Integer type;
	// 提货人id
	private Integer consigneeId;
	// 提货人姓名
	private String consigneeName;
	// 备注
	private String remark;
	// 审核意见
	private String auditOpinion;
	// 公司id
	private Integer companyId;
	// 仓库名
	private String warehouseName;
	// 公司名称
	private String companyName;
	// 线路Id
	private Integer lineId;
	// 区域Id
	private Integer areaId;
	// 是否删除 0 未删除 1 已删除
	private Integer deleteFlag;

	private Long targetWarehouseId;// 目标库 转移库时需要
	private Long sourceWarehouseId;// 来源库 转移库时需要
	private String purchaseNumber;// 采购单号
	//发生时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date happenDate;
	
	// 创建时间
	@NotField
	private Date createTime;


	// 时间
	@NotField
	private String time;

	// 操作人名
	@NotField
	private String operatorName;
	// 审核人名
	@NotField
	private String auditorName;
	// 商品名
	@NotField
	private String itemName;
	// 单位
	@NotField
	private String unitName;
	// 状态名称
	@NotField
	private String stateName;
	// 类型名称
	@NotField
	private String typeName;
	// 提货单位
	@NotField
	private String supplierName;
	// 出入库名称
	@NotField
	private String outputName;

	// 总数量
	@NotField
	private Integer allNum;
	// 总金额
	@NotField
	private double allMoney;

	// 入库商品集合
	@NotField
	private List<WarehouseBillItemBean> list;

	// 采购单id或者出库单id
	private Integer purchaseId;

	// 入库类型
	@NotField
	private String typeLabel;

	public String getTypeLabel() {
		if(type==null) {
			return "";
		}
		//入库
		if (type == 60204) {
			return "采购入库";
		}
		if (type == 60205) {
			return "归还入库";
		}
		if (type == 60206) {
			return "库存转移";
		}
		//出库
		if (type == 60405) {
			return "常规";
		}
		if (type == 60406) {
			return "赠送";
		}
		if (type == 60407) {
			return "报损";
		}
		if (type == 60410) {
			return "其他";
		}
		if (type == 60411) {
			return "转移他库";
		}
		return "";
	}

}
