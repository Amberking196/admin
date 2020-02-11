package com.server.module.system.carryWaterVouchersManage.carryRecord;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.common.paramconfig.AlipayConfig;
import com.server.module.customer.car.ShoppingCarService;
import com.server.module.customer.product.ShoppingGoodsService;

import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: hjc create time: 2018-11-09 09:02:25
 */
@Api(value = "CarryRecordController", description = "提水券管理")
@RestController
@RequestMapping("/carryRecordController")
public class CarryRecordController {

	private static Logger log = LogManager.getLogger(CarryRecordController.class);
	@Autowired
	private CarryRecordService carryRecordServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "订单提水券管理列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) CarryRecordForm carryRecordForm) {
		log.info("<CarryRecordController>-------<listPage>-------start");
		if (carryRecordForm == null) {
			carryRecordForm = new CarryRecordForm();
		}
		returnDataUtil = carryRecordServiceImpl.listPage(carryRecordForm);
		log.info("<CarryRecordController>-------<listPage>-------end");
		return returnDataUtil;
	}

}
