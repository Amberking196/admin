package com.server.module.system.shoppingManager.customerOrder;

import com.server.util.ReturnDataUtil;

public interface CustomerOrderService {
    
	
	/**
	 * 商城订单的查询
	 * @param orderForm
	 * @return
	 */
	ReturnDataUtil findCustomerByForm(CustomerOrderForm orderForm);
    
	

	/**
	 * 统计用户的消费记录
	 * @param form
	 * @return
	 */
    ReturnDataUtil statisticsConsumptionRecord(UsersConsumptionRecordForm form);
}
