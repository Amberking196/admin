package com.server.module.customer.product.spellGroupShare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
public class SpellGroupShareBean {

	//用户拼团id
	private Long id;
	//商品id
	private Long goodsId;
	//商品名称
	private String goodsName;
	//商品图片
	private String pic;
	//销售价
	private BigDecimal salesPrice;
	//拼团价
	private BigDecimal spellGroupPrice;
	//最低成团人数
	private Integer minimumGroupSize;
	//结束时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	//团购件数限制
	private Integer numberLimit;
	//商品团购设置活动id
	private Long  spellGroupId;
	//用户购买价格
	private BigDecimal price;
	//订单id
	private Long orderId;
	//拼团是否成功状态
	private Integer state;
	//拼团成功件数
	private Integer count;
	
	//用户集合
	private List<SpellGroupCustomerBean> list=new ArrayList<SpellGroupCustomerBean>();

}
