package com.server.module.customer.product.bargainSponsor;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.customer.product.customerBargain.CustomerBargainBean;
import com.server.module.customer.product.customerBargain.CustomerBargainService;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author why
 * @date: 2018年12月24日 上午9:22:09
 */
@Api(value = "BargainSponsorController", description = "砍价发起")
@RestController
@RequestMapping("/bargainSponsor")
public class BargainSponsorController {

	private Logger log=LogManager.getLogger(BargainSponsorController.class);
	
	@Autowired
	private BargainSponsorService BargainSponsorServiceImpl;
	@Autowired
	private CustomerBargainService customerBargainServiceImpl;
	
	@ApiOperation(value = "砍价列表", notes = "bargainList", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/bargainList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil bargainList() {
		log.info("<BargainSponsorController>----<bargainList>----start");
		 ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		returnDataUtil.setReturnObject(BargainSponsorServiceImpl.bargainList());
		log.info("<BargainSponsorController>----<bargainList>----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "砍价详情", notes = "bargainDetails", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/bargainDetails", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil bargainDetails(Long id) {
		log.info("<BargainSponsorController>----<bargainDetails>----start");
		 ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		returnDataUtil.setReturnObject(BargainSponsorServiceImpl.bargainDetails(id));
		log.info("<BargainSponsorController>----<bargainDetails>----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "砍价活动商品列表", notes = "bargainGoodsList", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/bargainGoodsList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil bargainGoodsList() {
		log.info("<BargainSponsorController>----<bargainGoodsList>----start");
		 ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		returnDataUtil.setReturnObject(BargainSponsorServiceImpl.bargainGoodsList());
		log.info("<BargainSponsorController>----<bargainGoodsList>----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "砍价增加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody CustomerBargainBean customerBargainBean) {
		log.info("<BargainSponsorController>----<bargainGoodsList>----start");
		 ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		 Map<String, Object> insert = customerBargainServiceImpl.insert(customerBargainBean);
		if(insert!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("砍价成功！");
			returnDataUtil.setReturnObject(insert);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("砍价失败！");
			returnDataUtil.setReturnObject(insert);
		}
		log.info("<BargainSponsorController>----<bargainGoodsList>----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "判断用户是否可以继续发起砍价", notes = "isCanBargain", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/isCanBargain", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil isCanBargain(Long id) {
		log.info("<BargainSponsorController>----<isCanBargain>----start");
		 ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag = BargainSponsorServiceImpl.isCanBargain(id);
		if(flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("可以点击免费拿！");
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("亲，您已有正在砍价的商品（或者已经砍价完成了），不可以贪心哟！");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<BargainSponsorController>----<isCanBargain>----end");
		return returnDataUtil;
	}
}
