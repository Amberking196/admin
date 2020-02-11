package com.server.module.customer.userInfo.record;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.customer.userInfo.stock.CustomerStockBean;
@Service
public class PickRecordServiceImpl implements PickRecordService{

	private final static Logger log = LogManager.getLogger(PickRecordServiceImpl.class);
	
	@Autowired
	private PickRecordDao pickRecordDao;

	@Override
	public List<PickRecordBean> getPickRecord(Long customerId) {
		log.info("<PickRecordServiceImpl--getPickRecord--start>");
		List<PickRecordBean> pickRecord = pickRecordDao.getPickRecord(customerId);
		log.info("<PickRecordServiceImpl--getPickRecord--end>");
		return pickRecord;
	}

	@Override
	public List<PickRecordBean> getPickRecord(Long customerId, Long itemId) {
		log.info("<PickRecordServiceImpl--getPickRecord--start>");
		List<PickRecordBean> pickRecord = pickRecordDao.getPickRecord(customerId, itemId);
		log.info("<PickRecordServiceImpl--getPickRecord--end>");
		return pickRecord;
	}

	@Override
	public List<CustomerStockBean> getCustomerStock(Long customerId) {
		log.info("<PickRecordServiceImpl--getCustomerStock--start>");
		List<CustomerStockBean> customerStock = pickRecordDao.getCustomerStock(customerId);
		log.info("<PickRecordServiceImpl--getCustomerStock--end>");
		return customerStock;
	}
}
