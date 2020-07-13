package com.server.module.customer.order.storeGroupOrderManage;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupOrderServiceImpl implements GroupOrderService {
	
	private static Logger log = LogManager.getLogger(GroupOrderServiceImpl.class);
	
	@Autowired
	private GroupOrderDao groupOrderDaoImpl;
	
	@Override
	public GroupOrderBean getStoreOrderbyOutTradeNo(String outTradeNo) {
		log.info("<GroupOrderServiceImpl>----<getStoreOrderbyOutTradeNo>----start");
		GroupOrderBean bean = groupOrderDaoImpl.getStoreOrderbyOutTradeNo(outTradeNo);
		log.info("<GroupOrderServiceImpl>----<getStoreOrderbyOutTradeNo>----end");
		return bean;
	}

	@Override
	public List<GroupOrderBean> getStoreOrderbyCustomerGroupId(Long customerGroupId) {
		log.info("<GroupOrderServiceImpl>----<getStoreOrderbyCustomerGroupId>----start");
		List<GroupOrderBean> list = groupOrderDaoImpl.getStoreOrderbyCustomerGroupId(customerGroupId);
		log.info("<GroupOrderServiceImpl>----<getStoreOrderbyCustomerGroupId>----end");
		return list;
	}

	@Override
	public int paySpellgroupStroeOrder(String outTradeNo, Integer type) {
		log.info("<GroupOrderServiceImpl>----<paySpellgroupStroeOrder>----start");
		int result = groupOrderDaoImpl.paySpellgroupStroeOrder(outTradeNo, type);
		log.info("<GroupOrderServiceImpl>----<paySpellgroupStroeOrder>----end");
		return result;
	}
}
