package com.server.module.system.promotionManage.activityProduct;

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

import com.server.module.system.couponManager.couponProduct.CouponProductBean;
import com.server.util.JsonUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-08-23 18:07:34
 */
@Api(value = "ActivityProductController", description = "活动商品")
@RestController
@RequestMapping("/activityProduct")
public class ActivityProductController {

	private static Logger log=LogManager.getLogger(ActivityProductController.class);
	@Autowired
	private ActivityProductService activityProductServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "活动商品", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) ActivityProductForm activityProductForm) {
		log.info("<ActivityProductController>----<listPage>------start");
		if(activityProductForm==null) {
			activityProductForm=new ActivityProductForm();
		}
		ReturnDataUtil listPage = activityProductServiceImpl.listPage(activityProductForm);
		log.info("<ActivityProductController>----<listPage>------end");
		return listPage;
	}

	@ApiOperation(value = "绑定商品", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(Long[] productIds,Integer activityId) {
		log.info("<ActivityProductController>----<add>------start");
		for (int i = 0; i < productIds.length; i++) {
			ActivityProductBean bean=new ActivityProductBean();
            bean.setActivityId(activityId.longValue());
            bean.setCreateUser(1l);
            bean.setProductId(productIds[i]);
            ActivityProductBean add = activityProductServiceImpl.add(bean);
            if(add!=null) {
            	returnDataUtil.setStatus(1);
            	returnDataUtil.setMessage("绑定商品成功！");
            	returnDataUtil.setReturnObject(add);
            }else {
            	returnDataUtil.setStatus(0);
            	returnDataUtil.setMessage("绑定商品失败！");
            	returnDataUtil.setReturnObject(add);
            }
        }
		log.info("<ActivityProductController>----<add>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "绑定 活动商品列表", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list(@RequestBody ActivityProductForm activityProductForm) {
		log.info("<ActivityProductController>----<list>------start");
		returnDataUtil= activityProductServiceImpl.list(activityProductForm);
		log.info("<ActivityProductController>----<list>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "解绑商品", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<ActivityProductController>----<del>------start");
		boolean flag = activityProductServiceImpl.del(id);
		if(flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("解绑商品成功");
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("解绑商品失败");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<ActivityProductController>----<del>------end");
		return returnDataUtil;
	}
}
