package com.server.module.system.promotionManage.promotionActivity;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.system.promotionManage.timeQuantum.TimeQuantumBean;

import lombok.Data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: promotion_activity author name: why create time: 2018-08-22
 * 16:51:38
 */
@Data
@Entity(tableName = "promotion_activity", id = "id", idGenerate = "auto")
public class PromotionActivityBean {

	// 主键id
	private Long id;
	// 促销活动名称
	private String name;
	// 促销活动类型 1.固定活动 2.时间段活动
	private Integer type;
	//时间段id
	private String timeQuantumId;
	// 活动开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	// 活动结束时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	// 活动折扣类型 1.固定 2.满减 3.折扣 4.买就送5.多买折扣
	private Integer discountType;
	// 订单金额
	private Double money;
	// 扣减金额
	private Double deductionMoney;
	// 促销活动适用范围 1.公司 2.区域 3.机器
	private Integer target;
	// 公司id
	private Long companyId;
	// 区域id
	private Long areaId;
	// 区域名称
	private String areaName;
	// 公司名称
	private String companyName;
	// 售货机编号
	private String vmCode;
	// 是否绑定商品   0 不绑定商品  1 绑定商品
	private Integer bindProduct;
	// 活动应用范围 1 .机器活动 2 .商城活动
	private Integer useWhere;
	// 活动图片
	private String pic;
	// 活动备注
	private String remark;
	// 创建时间
	private Date createTime;
	// 创建用户
	private Long createUser;
	// 更新时间
	private Date updateTime;
	// 更新时间
	private Long updateUser;
	// 是否删除 0 未删除 1 已删除
	private Integer deleteFlag;
	//时间段 
	private String timeFrame;
	
	// 活动状态 已开始 或 已结束 未开始
	@NotField
	private String stateLabel;

	// 促销活动类型 1.固定活动 2.时间段活动
	@NotField
	private String typeLabel;

	// 活动折扣类型 1.固定 2.满减 3.折扣 4.买就送
	@NotField
	private String discountTypeLabel;

	// 活动应用范围 1 .机器活动 2 .商城活动
	@NotField
	private String useWhereLabel;

	public String getStateLabel() {
		return getStateByDate(getStartTime(), getEndTime());
	}

	private String getStateByDate(Date startTime, Date endTime) {
		Long now = System.currentTimeMillis();
		if (now < startTime.getTime()) {
			return "未开始";
		}
		if (now > endTime.getTime()) {
			return "已结束";
		}
		if (now > startTime.getTime() && now < endTime.getTime()) {
			return "已开始";
		}
		return "";
	}

	public String getTypeLabel() {
		if (type == 1) {
			typeLabel = "固定活动";
		}
		if (type == 2) {
			typeLabel = "时间段活动";
		}
		return typeLabel;
	}

	public String getDiscountTypeLabel() {
		if (discountType == 1) {
			discountTypeLabel = "固定减";
		}
		if (discountType == 2) {
			discountTypeLabel = "满减";
		}
		if (discountType == 3) { 
			discountTypeLabel = "折扣减";
		}
		if(discountType == 4) {
			discountTypeLabel = "买就送";
		}
		if(discountType == 5) {
			discountTypeLabel = "多买折扣";
		}
		return discountTypeLabel;
	}

	public String getUseWhereLabel() {
		if (useWhere == 1) {
			useWhereLabel="机器活动";
		}
		if (useWhere == 2) {
			useWhereLabel="商城活动";
		}
		return useWhereLabel;
	}
}
