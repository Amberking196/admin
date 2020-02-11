package com.server.module.system.bargainManage;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  tbl_customer_bargain
 * author name: hjc
 * create time: 2018-12-21 16:20:26
 */ 
@Data
public class TblCustomerBargainDetailDto{
	private Integer id;
	private Integer goodsBargainId;//活动id
	private BigDecimal currPrice;//当前价格
	//private BigDecimal cutPrice;//砍掉价格

	private Integer state;//是否砍价成功 0失败 1成功 2砍价中
	@JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
	private Date endTime;//砍价活动结束时间
	@JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
	private Date createTime;//砍价活动创建时间
	@JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;//砍价活动更新时间
	private Integer sendMessage;//是否已经发送短信
	private String ptCode;//订单号
	private String shoppingGoodsName;//商品名称
	private String stateLabel;//状态
	private BigDecimal salesPrice;//销售价
	private BigDecimal lowestPrice;//最低价格

	private String phone;//电话
	private String receiver;//收货人
	private String receiverPhone;//收货人电话
	private String address;//地址
	private String pic;//图片路径
	private Integer hourLimit;//时间限制
	private List<TblCustomerBargainDetailBean> tblCustomerBargainDetailBeanList;
	public String getStateLabel() {
		if (state == null) {
			stateLabel = "";
		}
		else if (state == 0) {
			stateLabel = "失败";
		}
		else if (state == 1) {
			stateLabel = "成功";
		}else if (state == 2) {
			stateLabel = "砍价中";
		}
		return stateLabel;
	}
}

