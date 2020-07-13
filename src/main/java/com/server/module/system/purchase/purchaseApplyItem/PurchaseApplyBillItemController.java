package com.server.module.system.purchase.purchaseApplyItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.system.purchase.purchaseApply.PurchaseApplyBillController;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-08-31 17:41:12
 */
@Api(value = "PurchaseApplyBillItemController", description = "申请单下的商品")
@RestController
@RequestMapping("/purchaseApplyBillItem")
public class PurchaseApplyBillItemController {
	public static Logger log = LogManager.getLogger(PurchaseApplyBillItemController.class);
	@Autowired
	private PurchaseApplyBillItemService purchaseApplyBillItemServiceImpl;

	@ApiOperation(value = "申请单下的商品列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(PurchaseApplyBillItemCondition condition) {
		return purchaseApplyBillItemServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "申请单下的商品添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody PurchaseApplyBillItemBean entity) {
		return new ReturnDataUtil(purchaseApplyBillItemServiceImpl.add(entity));
	}

	@ApiOperation(value = "申请单下的商品修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody PurchaseApplyBillItemBean entity) {
		return new ReturnDataUtil(purchaseApplyBillItemServiceImpl.update(entity));
	}

	@ApiOperation(value = "申请单下的商品删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(purchaseApplyBillItemServiceImpl.del(id));
	}
	@ApiOperation(value = "查询申请单下的商品", notes = "findItemsById", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findItemsById", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findItemsById(Integer id) {
		log.info("<PurchaseApplyBillItemController>------<findItemsById>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		if(id==null) {//数据校验
			data.setMessage("参数异常");
			data.setStatus(0);
			return data;
		}
		data=purchaseApplyBillItemServiceImpl.findItemsById(id);
		if(data.getStatus()==0) {
			//操作成功
			data.setStatus(1);
			data.setMessage("查询成功");
		}else {
			data.setStatus(0);
			data.setMessage("查询失败");
		}
		log.info("<PurchaseApplyBillItemController>------<findItemsById>----start");
		return data;
	}
}
