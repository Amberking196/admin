package com.server.module.system.statisticsManage.payRecord;

import java.math.BigDecimal;

public class PayRecordItemDto {

	private String itemName;//商品名称
	private Integer num;//商品数量
	private String pic;//商品图片
	private BigDecimal realTotalPrice;//商品实际支付金额
	private BigDecimal sumPrice;//该订单总价
	private BigDecimal originalPrice;//商品原总价
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public BigDecimal getRealTotalPrice() {
		return realTotalPrice;
	}
	public void setRealTotalPrice(BigDecimal realTotalPrice) {
		this.realTotalPrice = realTotalPrice;
	}
	public BigDecimal getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(BigDecimal sumPrice) {
		this.sumPrice = sumPrice;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	
	
}
