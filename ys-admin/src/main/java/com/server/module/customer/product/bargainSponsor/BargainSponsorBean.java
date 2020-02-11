package com.server.module.customer.product.bargainSponsor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;
import com.server.module.customer.product.bargainDetail.BargainDetailBean;

import lombok.Data;

@Data
public class BargainSponsorBean {

	//主键id
	private Long id;
	//商品id
	private Long goodsId;
	//商品名称
	private String name;
	//商品图片
	private String pic;
	//价格
	private BigDecimal price;
	//购买人数
	private Long purchaseNumber;
	//结束时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	//商品最低价
	private BigDecimal lowestPrice;
	//还差价格
	private BigDecimal surplusPrice;
	//状态  0失败 1成功 2砍价中
	private Integer state;
	@NotField
	private String stateLabel;
	
	//砍价帮集合
	private List<BargainDetailBean> list;
	
	//还差多人人
	private Integer allNumber;
	
	private Long orderId;
	
	
}
