package com.server.module.system.purchase.purchaseApply;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  purchase_apply_bill
 * author name: yjr
 * create time: 2018-08-31 17:27:57
 */ 
@Data
@Entity(tableName="purchase_apply_bill",id="",idGenerate="auto")
public class PurchaseApplyBillBean{


@JsonIgnore	public String tableName="purchase_apply_bill";
@JsonIgnore	public String selectSql="select * from purchase_apply_bill where 1=1 ";
@JsonIgnore	public String selectSql1="select id,warehouseId,operator,operatorName,auditor,auditorName,number,createTime,state,remark,auditOpinion,warehouseName,deleteFlag from purchase_apply_bill where 1=1 ";
	private Long id;
	private Long warehouseId;
	private Long operator;
	private String operatorName;
	private Long auditor;
	private String auditorName;
	private String number;
	private Date createTime;
	private Long state;
	private String remark;
	private String auditOpinion;
	private String warehouseName;
	private Integer deleteFlag;
	@NotField
	private String stateName;//状态信息
	public String getStateName() {
		if (state == 0) {
			return "未提交";
		} else if (state == 1) {
			return "已提交";
		} else if (state == 2) {
			return "审核不通过";
		} else if (state == 3) {
			return "审核通过";
		}else {
			return "";
		}
	}
}

