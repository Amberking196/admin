package com.server.module.system.shoppingManager.customerOrder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;
@Service
public class CustomerOrderServiceImpl implements CustomerOrderService{
	
	private static Logger log=LogManager.getLogger(CustomerOrderServiceImpl.class);
	@Autowired
	CustomerOrderDao orderDao;

	/**
	 * 商城订单列表查询
	 */
	@Override
	public ReturnDataUtil findCustomerByForm(CustomerOrderForm orderForm) {
		log.info("<CustomerOrderServiceImpl>------<findCustomerByForm>-----start");
		ReturnDataUtil returnDataUtil = orderDao.findCustomerByForm(orderForm);
		log.info("<CustomerOrderServiceImpl>------<findCustomerByForm>-----end");
		return returnDataUtil;
	}

	
    @Override
    public ReturnDataUtil statisticsConsumptionRecord(UsersConsumptionRecordForm form) {

		return orderDao.statisticsConsumptionRecord( form);


    }

}
