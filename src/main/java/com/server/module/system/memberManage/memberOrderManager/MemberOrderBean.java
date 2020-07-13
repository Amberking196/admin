package com.server.module.system.memberManage.memberOrderManager;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: member_order author name: why create time: 2018-09-26 14:36:43
 */
@Data
@Entity(tableName = "member_order", id = "id", idGenerate = "auto")
public class MemberOrderBean {

	// 主键id
	private Long id;
	// 会员id
	private Long customerId;

	// 会员类型
	//private Integer memberTypeId;
	// 购买金额
	private BigDecimal price;
	// 状态
	private Long state;
	// 平台订单号
	private String ptCode;
	// 支付订单号
	private String payCode;
	// 创建时间
	private Date createTime;
	// 订单支付时间
	private Date payTime;
	// 更新时间
	private Date updateTime;
	//好友用户id
	private  Long friendCustomerId;
	//好友手机号
	private String friendPhone;
	//退款时间
	private Date refundTime;
	//货道
	private String wayNumber;

	//公司名称
	@NotField
	private String companyName;


	private Integer type;//0 充值 1捐献

	private String vmCode;//机器编码

	private Integer payType;//支付方式

	// 会员电话
	@NotField
	private String phone;
	// 创建时间 时分秒
	@NotField
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTimeLabel;
	// 支付时间 时分秒
	@NotField
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTimeLabel;

	// 状态 10001：已支付      10002：未支付
	@NotField
	private String stateName;
/*	// 会员类型
	@NotField
	private String type;*/

	public String getStateName() {
		if (state == 10001) {
			return "已支付";
		}
		if (state == 10002) {
			return "未支付";
		}
		if(state == 10006){
			return "退款成功";
		}
		return "";
	}
	public String setStateName() {
		if (state == 10001) {
			return "已支付";
		}
		if (state == 10002) {
			return "未支付";
		}
		if(state == 10006){
			return "退款成功";
		}
		return "";
	}

	@NotField
	public String receiver;
	@NotField
	public String address;

}
