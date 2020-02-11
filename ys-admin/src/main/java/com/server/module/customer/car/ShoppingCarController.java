package com.server.module.customer.car;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * author name: hjc
 * create time: 2018-06-29 11:03:25
 */ 
@Api(value ="ShoppingCarController",description="购物车")
@RestController
@RequestMapping("/shoppingCar")
public class  ShoppingCarController{

	public static Logger log = LogManager.getLogger(ShoppingCarController.class);
	@Autowired
	private ShoppingCarService shoppingCarServiceImpl;
	@Autowired
	private CustomerService customerServiceImpl;
	
	@ApiOperation(value = "购物车列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false)ShoppingCarForm shoppingCarForm){
		log.info("<ShoppingCarController>--<listPage>--start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(shoppingCarForm==null) {
			shoppingCarForm=new ShoppingCarForm();
		}
		returnDataUtil=shoppingCarServiceImpl.listPage(shoppingCarForm);
		log.info("<ShoppingCarController>--<listPage>--end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "app购物车列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/appListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil appListPage(@RequestBody(required=false)ShoppingCarForm shoppingCarForm,HttpServletRequest request){
		log.info("<ShoppingCarController>--<appListPage>--start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		Long userId=UserUtils.getUser().getId();
		if(shoppingCarForm==null) {
			shoppingCarForm=new ShoppingCarForm();
			shoppingCarForm.setCustomerId(userId);
			shoppingCarForm.setIsShowAll(1);
		}
		returnDataUtil=shoppingCarServiceImpl.listPage(shoppingCarForm);
		log.info("<ShoppingCarController>--<appListPage>--end");
		return returnDataUtil;
	}
	

	@ApiOperation(value = "加入购物车",notes = "add",  httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  add(ShoppingCarBean newShoppingCarBean,HttpServletRequest request,HttpServletResponse response){
		log.info("<ShoppingCarController>--<add>--start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		Long userId=UserUtils.getUser().getId();
		CustomerBean customerBean=customerServiceImpl.findCustomerById(userId);
		if(customerBean.getPhone()==null) {
			log.info("商城用户手机号未注册，则进行注册");
			try {
				response.sendRedirect("http://webapp.youshuidaojia.com/cLogin?openId="+customerBean.getOpenId());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			ShoppingCarForm shoppingCarForm=new ShoppingCarForm();
			shoppingCarForm.setCustomerId(userId);
			shoppingCarForm.setIsShowAll(1);
			newShoppingCarBean.setCustomerId(userId);
			returnDataUtil=shoppingCarServiceImpl.add(shoppingCarForm,newShoppingCarBean);
		}
		log.info("<ShoppingCarController>--<add>--end");
		return returnDataUtil;
		
	}
	
	@ApiOperation(value = "加入购物车购买",notes = "add",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/addAndBuy", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  addAndBuy(@RequestBody ShoppingCarBean newShoppingCarBean,HttpServletRequest request,HttpServletResponse response){
		log.info("<ShoppingCarController>--<add>--start");
		ReturnDataUtil returnDataUtil=shoppingCarServiceImpl.addAndBuy(newShoppingCarBean);
		log.info("<ShoppingCarController>--<add>--end");
		return returnDataUtil;
		
	}
	
	
	
	
	@ApiOperation(value = "更新购物车",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  update(@RequestBody  ShoppingCarBean newShoppingCarBean,HttpServletRequest request){
		log.info("<ShoppingCarController>--<update>--start");
		Long userId=UserUtils.getUser().getId();
		ShoppingCarForm shoppingCarForm=new ShoppingCarForm();
		shoppingCarForm.setCustomerId(userId);
		shoppingCarForm.setIsShowAll(1);
		ReturnDataUtil returnDataUtil=shoppingCarServiceImpl.update(shoppingCarForm,newShoppingCarBean);
		log.info("<ShoppingCarController>--<update>--end");
		return returnDataUtil;
		
	}
	
	@ApiOperation(value = "单个删除购物车商品",notes = "delete",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil delete(@RequestBody ShoppingCarBean shoppingCarBean,HttpServletRequest request){
		log.info("<ShoppingCarController>--<delete>--start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag= shoppingCarServiceImpl.updateFlag(shoppingCarBean);
		if(flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("删除成功!");
			returnDataUtil.setReturnObject(flag);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("删除失败!");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<ShoppingCarController>--<delete>--end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "保存购物车",notes = "save",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  save(@RequestBody(required=false) List<ShoppingCarBean> newShoppingCarBeanList,HttpServletRequest request){
		log.info("<ShoppingCarController>--<save>--start");
		Long userId=UserUtils.getUser().getId();
		ShoppingCarForm shoppingCarForm=new ShoppingCarForm();
		shoppingCarForm.setCustomerId(userId);
		shoppingCarForm.setIsShowAll(1);
		ReturnDataUtil 	returnDataUtil=shoppingCarServiceImpl.save(shoppingCarForm,newShoppingCarBeanList);
		log.info("<ShoppingCarController>--<save>--end");
		return returnDataUtil;
		
	}
	
	@ApiOperation(value = "批量删除购物车商品",notes = "del",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody List<Long> shoppingCarIdList,HttpServletRequest request){
		log.info("<ShoppingCarController>--<del>--start");
		ReturnDataUtil	returnDataUtil=shoppingCarServiceImpl.del(shoppingCarIdList);
		log.info("<ShoppingCarController>--<del>--end");
		return returnDataUtil;
	}

}

