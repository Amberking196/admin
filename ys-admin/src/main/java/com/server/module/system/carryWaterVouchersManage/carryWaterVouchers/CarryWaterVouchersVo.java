package com.server.module.system.carryWaterVouchersManage.carryWaterVouchers;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: carry_water_vouchers author name: why create time: 2018-11-03
 * 09:02:25
 */
@Data
public class CarryWaterVouchersVo {

	//主键id
	private Long id;
	//提水券名称
	private String  carryWaterName;
	//提水劵的适用范围  1 公司  2 区域 3 机器
	private Integer target;
	//公司id
	private Long companyId;
	//区域id
	private Long areaId;
	//区域名称
	private String areaName;
	//公司名称
	private String companyName;
	//售货机编号
	private String vmCode;
	//是否绑定商品 0 不绑定商品 1 绑定商品
	private Integer bindProduct;
	//有效时间类型：0 绝对时间 1 相对时间
	private Integer periodType;
	//有效天数,periodType=1时有意义
	private Integer periodDay;
	//返还提水券数量
	private Integer sendMax;
	//开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0删除 1已删除
	private Integer deleteFlag;
	//提水券图片
	private String pic;
	//备注
	private String remark;
	//商品id
	private Long shoppingGoodsId;
	// 类型 0提水 1提货
	private Integer type;
}
