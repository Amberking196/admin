package com.server.module.customer.order;

import java.math.BigDecimal;
import java.util.Date;

import com.server.module.customer.product.bargainDetail.BargainGoodsDto;
import com.server.module.customer.product.customerBargain.CustomerBargainBean;
import com.server.util.IDUtil;
import com.server.util.stateEnum.PayStateEnum;
import com.server.util.stateEnum.PayTypeEnum;


public class OrderCreator {
	
	private static BigDecimal limit = new BigDecimal("0.01");

	/**
	 * 创建砍价新订单
	 * @author hebiting
	 * @date 2018年12月24日上午11:06:28
	 * @return
	 */
	public static OrderBean createBargainNewOrder(CustomerBargainBean cusBargain,BargainGoodsDto goods,String openId){
		OrderBean order = new OrderBean();
		Date now = new Date();
		order.setAddressId(cusBargain.getAddressId());
		order.setCompanyId(Long.valueOf(goods.getCompanyId()));
		order.setCreateTime(now);
		order.setCustomerId(cusBargain.getCustomerId());
		order.setDistributionModel(1);
		order.setNowprice(cusBargain.getCurrPrice());
		order.setOpenid(openId);
		order.setPayCode(IDUtil.getPayCode());
		order.setPayType(PayTypeEnum.WEIXIN.getIndex());
		order.setPrice(goods.getSalesPrice());
		order.setProduct(goods.getId().toString());
		if(cusBargain.getCurrPrice().compareTo(limit)<0){
			order.setState(PayStateEnum.PAY_SUCCESS.getState());
			order.setPayTime(now);
		}else{
			order.setState(PayStateEnum.NOT_PAY.getState());
		}
		order.setType(1);
		order.setCustomerBargainId(cusBargain.getId());
		return order;
	}
	
	public static OrderDetile createBargainOrderDetile(CustomerBargainBean cusBargain,BargainGoodsDto goods,Long orderId){
		OrderDetile orderDetile = new OrderDetile();
		orderDetile.setCreateTime(new Date());
		orderDetile.setCustomerId(cusBargain.getCustomerId());
		orderDetile.setItemId(goods.getId().intValue());
		orderDetile.setItenName(goods.getItemName());
		orderDetile.setNum(1);
		orderDetile.setOrderId(orderId);
		orderDetile.setPrice(cusBargain.getCurrPrice().doubleValue());
		return orderDetile;
	}
}
