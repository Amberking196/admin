package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
@Api(value = "CarryWaterVouchersCustomerController", description = "提水券用户")
@RestController
@RequestMapping("/carryWaterVouchersCustomer")
public class CarryWaterVouchersCustomerController {

	private static  Logger log=LogManager.getLogger(CarryWaterVouchersCustomerController.class);
	@Autowired
	private CarryWaterVouchersCustomerService carryWaterVouchersCustomerServiceImpl;
	
	
	@ApiOperation(value = "提水券用户列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerController>------<listPage>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(carryWaterVouchersCustomerForm==null) {
			carryWaterVouchersCustomerForm=new CarryWaterVouchersCustomerForm();
		}
		returnDataUtil=carryWaterVouchersCustomerServiceImpl.listPage(carryWaterVouchersCustomerForm);
		log.info("<CarryWaterVouchersCustomerController>------<listPage>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "提水券绑定用户", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(Long carryId,Long[] customerIds,Long quantity) {
		log.info("<CarryWaterVouchersCustomerController>------<add>------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		CarryWaterVouchersCustomerBean bean= carryWaterVouchersCustomerServiceImpl.add(carryId,customerIds,quantity);
		if(bean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("发送用户成功！");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("发送用户失败！");
		}
		log.info("<CarryWaterVouchersCustomerController>------<add>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "我的提水券", notes = "myCarryWaterVouchers", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/myCarryWaterVouchers", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil myCarryWaterVouchers(@RequestBody CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerController>------<myCarryWaterVouchers>------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		List<CarryWaterVouchersCustomerDto> list = carryWaterVouchersCustomerServiceImpl.findCustomerIdByCarryWaterVouchersCustomerDto(carryWaterVouchersCustomerForm);
		if( list!=null && list.size()>0 ) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查看成功！");
			returnDataUtil.setReturnObject(list);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查看失败！");
			returnDataUtil.setReturnObject(list);
		}
		log.info("<CarryWaterVouchersCustomerController>------<myCarryWaterVouchers>------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "提水券范围内用户列表", notes = "listPageForCustomer", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/listPageForCustomer", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPageForCustomer(@RequestBody CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
    	log.info("<CarryWaterVouchersCustomerController>----<listPageForCustomer>------start");
    	ReturnDataUtil returnDataUtil= carryWaterVouchersCustomerServiceImpl.listPageForCustomer(carryWaterVouchersCustomerForm);
    	log.info("<CarryWaterVouchersCustomerController>----<listPageForCustomer>------end");
    	return returnDataUtil;
    }
	
	@ApiOperation(value = "更新用户提水券数量", notes = "updateQuantity", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/updateQuantity", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil updateQuantity(@RequestBody CarryWaterVouchersCustomerBean entity) {
    	log.info("<CarryWaterVouchersCustomerController>----<updateQuantity>------start");
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	entity.setUpdateUser(UserUtils.getUser().getId());
    	 boolean update = carryWaterVouchersCustomerServiceImpl.updateQuantity(entity);
    	 if(update) {
    		 returnDataUtil.setStatus(1);
 			returnDataUtil.setMessage("修改用户提水券/提货券成功！");
 			returnDataUtil.setReturnObject(update);
    	 }else {
    		 returnDataUtil.setStatus(0);
 			returnDataUtil.setMessage("修改用户提水券/提货券成功！");
 			returnDataUtil.setReturnObject(update);
    	 }
    	log.info("<CarryWaterVouchersCustomerController>----<updateQuantity>------end");
    	return returnDataUtil;
    }
} 
