package com.server.module.system.promotionManage.promotionActivity;

import java.util.Date;
import lombok.Data;

/**
 * table name: promotion_activity author name: why create time: 2018-08-22
 * 16:51:38
 */
@Data
public class PromotionActivityVo {

	// 主键id
	private Long id;
	// 促销活动名称
	private String name;
	// 促销活动类型 1.固定活动 2.时间段活动
	private Integer type;
	//时间段id
	private String timeQuantumId;
	// 活动开始时间
	private Date startTime;
	// 活动结束时间
	private Date endTime;
	// 活动折扣类型 1.固定 2.满减 3.折扣
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
	// 商品id
	private String itemId;
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

	
}
