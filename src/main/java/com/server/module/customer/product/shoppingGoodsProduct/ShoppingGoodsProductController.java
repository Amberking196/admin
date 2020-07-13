package com.server.module.customer.product.shoppingGoodsProduct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-09-14 09:53:47
 */
@Api(value = "ShoppingGoodsProductController", description = "商城商品关联商品")
@RestController
@RequestMapping("/shoppingGoodsProduct")
public class ShoppingGoodsProductController {

	private static Logger log = LogManager.getLogger(ShoppingGoodsProductController.class);
	@Autowired
	private ShoppingGoodsProductService shoppingGoodsProductServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "商城商品关联商品列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) ShoppingGoodsProductForm shoppingGoodsProductForm) {
		log.info("<ShoppingGoodsProductController>----<listPage>-----start");
		if (shoppingGoodsProductForm == null) {
			shoppingGoodsProductForm = new ShoppingGoodsProductForm();
		}
		returnDataUtil = shoppingGoodsProductServiceImpl.listPage(shoppingGoodsProductForm);
		log.info("<ShoppingGoodsProductController>----<listPage>-----start");
		return returnDataUtil;
	}

	@ApiOperation(value = "绑定商品列表", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list(@RequestBody ShoppingGoodsProductForm shoppingGoodsProductForm) {
		log.info("<ShoppingGoodsProductController>----<listPage>-----start");
		returnDataUtil = shoppingGoodsProductServiceImpl.list(shoppingGoodsProductForm);
		log.info("<ShoppingGoodsProductController>----<listPage>-----start");
		return returnDataUtil;
	}

	@ApiOperation(value = "绑定商品", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody ShoppingGoodsProductBean bean) {
		log.info("<ShoppingGoodsProductController>----<add>------start");
		bean.setCreateUser(UserUtils.getUser().getId());
		ShoppingGoodsProductBean add = shoppingGoodsProductServiceImpl.add(bean);
		if (add != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("绑定商品成功！");
			returnDataUtil.setReturnObject(add);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("绑定商品失败！");
			returnDataUtil.setReturnObject(add);
		}
		log.info("<ActivityProductController>----<add>------end");
		return returnDataUtil;
	}

	
}
