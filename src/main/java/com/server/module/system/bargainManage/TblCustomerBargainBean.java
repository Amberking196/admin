package com.server.module.system.bargainManage;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  tbl_customer_bargain
 * author name: hjc
 * create time: 2018-12-21 16:20:26
 */ 
@Data
@Entity(tableName="tbl_customer_bargain",id="",idGenerate="auto")
public class TblCustomerBargainBean{

	private Integer id;
	private Integer goodsBargainId;
	private Long customerId;
	private BigDecimal currPrice;//当前价格
	private Integer state;//是否砍价成功 0失败 1成功 2砍价中
	private Long addressId;
	private Date endTime;//结束时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;//砍价创建时间
	private Date updateTime;
	private Integer sendMessage;//是否已经发送短信
	
	
	@NotField
	private String ptCode;//订单号
	@NotField
	private String shoppingGoodsName;//商品名称
	@NotField
	private String stateLabel;//状态
	@NotField
	private String salesPrice;//价格
	@NotField
	private String phone;//电话
	@NotField
	private String 	receiver;//收货人
	@NotField
	private String 	receiverPhone;//收货人电话
	

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

