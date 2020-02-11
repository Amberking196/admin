package com.server.module.system.purchase.purchaseBillItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-09-03 16:27:30
 */
@Api(value = "PurchaseBillItemController", description = "采购单下的商品")
@RestController
@RequestMapping("/purchaseBillItem")
public class PurchaseBillItemController {

	private static Logger log=LogManager.getLogger(PurchaseBillItemController.class);
	@Autowired
	private PurchaseBillItemService purchaseBillItemServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	/*@ApiOperation(value = "采购单下的商品列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(PurchaseBillItemForm condition) {
		return purchaseBillItemServiceImpl.listPage(condition);
	}*/

	@ApiOperation(value = "判断实际采购数量是否足够", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/judgeQuantity", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil judgeQuantity(Long id,Long quantity) {
		log.info("<PurchaseBillItemController>----<judgeQuantity>------start");
		PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemServiceImpl.get(id);
		//得到采购回来的剩余数量
		Long num=purchaseBillItemBean.getQuantity()-purchaseBillItemBean.getStorageQuantity();
		//判断入库是 数量是否足够
		if(quantity>num) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("商品数量不足 无法入库，请重新输入！您本次最大可以入库"+num+"件商品");
		}else {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("商品数量充足，可以入库！");
		}
		log.info("<PurchaseBillItemController>----<judgeQuantity>------end");
		return returnDataUtil;
	}

	
}
