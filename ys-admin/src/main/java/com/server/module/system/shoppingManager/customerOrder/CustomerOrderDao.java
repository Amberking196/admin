package com.server.module.system.shoppingManager.customerOrder;

import java.util.List;

import com.server.util.ReturnDataUtil;

public interface CustomerOrderDao {
	
	/**
	 * 商城订单的查询
	 * @param orderForm
	 * @return
	 */

	ReturnDataUtil findCustomerByForm(CustomerOrderForm orderForm);
    
	/**
	 * 订单详情查询
	 * @author why
	 * @date 2018年11月12日 下午3:21:17 
	 * @param orderForm
	 * @return
	 */
	public List<ShoppingBean> findCustomerByProduce(CustomerOrderForm orderForm);


	/**
	 * 统计用户的消费记录
	 * @param form
	 * @return
	 */
    ReturnDataUtil statisticsConsumptionRecord(UsersConsumptionRecordForm form);
}
