package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersProduct;

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
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-11-03 16:25:36
 */
@Api(value = "CarryWaterVouchersProductController", description = "提水券商品")
@RestController
@RequestMapping("/carryWaterVouchersProduct")
public class CarryWaterVouchersProductController {

	private static Logger log=LogManager.getLogger(CarryWaterVouchersProductController.class);
	@Autowired
	private CarryWaterVouchersProductService carryWaterVouchersProductServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	
 
	@ApiOperation(value = "提水券商品列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(CarryWaterVouchersProductForm condition) {
		return carryWaterVouchersProductServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "提水券商品绑定", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(Long[] itemIds,Long carryId) {
		log.info("<CarryWaterVouchersProductController>-------<add>------start");
		for (int i = 0; i < itemIds.length; i++) {
			CarryWaterVouchersProductBean entity=new CarryWaterVouchersProductBean();
            entity.setCarryId(carryId);
            entity.setCreateUser(1l);
            entity.setItemId(itemIds[i]);
            CarryWaterVouchersProductBean add = carryWaterVouchersProductServiceImpl.add(entity);
    		if(add!=null) {
    			returnDataUtil.setStatus(1);
    			returnDataUtil.setMessage("绑定商品成功!");
    			returnDataUtil.setReturnObject(add);
    		}else {
    			returnDataUtil.setStatus(1);
    			returnDataUtil.setMessage("绑定商品失败!");
    			returnDataUtil.setReturnObject(add);
    		}
        }
		log.info("<CarryWaterVouchersProductController>-------<add>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "提水券商品绑定列表", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list(@RequestBody CarryWaterVouchersProductForm carryWaterVouchersProductForm) {
		log.info("<CarryWaterVouchersProductController>-----<list>----start");
		returnDataUtil=carryWaterVouchersProductServiceImpl.list(carryWaterVouchersProductForm);
		log.info("<CarryWaterVouchersProductController>-----<list>----end");
		return returnDataUtil;
	}

	
}
