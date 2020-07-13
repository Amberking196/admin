package com.server.module.customer.userInfo.record;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.customer.userInfo.stock.CustomerStockBean;
import com.server.module.system.adminUser.AdminConstant;

@RestController
@RequestMapping("/storeCustomer")
public class PickRecordController {

	private final static Logger log = LogManager.getLogger(PickRecordController.class);
	
	@Autowired
	private PickRecordService pickRecordService;
	
	@PostMapping("/getPickRecord")
	public List<PickRecordBean> getByCustomer(@RequestBody Map<String,Long> param,HttpServletRequest request){
		log.info("<PickRecordController--getByCustomer--start>");
		Long itemId = param.get("itemId");
		Long customerId = Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		List<PickRecordBean> pickRecord = pickRecordService.getPickRecord(customerId,itemId);
		log.info("<PickRecordController--getByCustomer--end>");
		return pickRecord;
	}
	@PostMapping("/getStock")
	public List<CustomerStockBean> getCustomerStock(HttpServletRequest request){
		log.info("<PickRecordController--getCustomerStock--start>");
		Long customerId = Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		List<CustomerStockBean> customerStock = pickRecordService.getCustomerStock(customerId);
		log.info("<PickRecordController--getCustomerStock--end>");
		return customerStock;
	}
}
