package com.server.module.customer.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.server.module.customer.userInfo.TblCustomerBean;
import com.server.module.customer.userInfo.TblCustomerService;
import com.server.module.system.machineManage.machinesWayItem.HuaFaResult;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.server.jwt.JwtTokenUtil;
import com.server.module.customer.car.ShoppingCarBean;
import com.server.module.customer.car.ShoppingCarForm;
import com.server.module.customer.car.ShoppingCarService;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsService;
import com.server.module.customer.userInfo.userWxInfo.TblCustomerWxService;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerDto;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.system.machineManage.machinesWayItem.MachinesClient;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.redis.RedisClient;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order")
public class OrderController {

	private static final Logger log = LogManager.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private ShoppingCarService shoppingCarServiceImpl;
	@Autowired
	private ShoppingGoodsService shoppingGoodsServiceImpl;
	@Autowired
	private CarryWaterVouchersCustomerService carryWaterVouchersCustomerService;
	@Autowired
	private TblCustomerWxService tblCustomerWxService;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private MachinesClient machinesClient;
	/**
	 * 根据openId获取当前用户的所有订单
	 * 
	 * @author hebiting
	 * @date 2018年6月20日上午9:43:04
	 * @param openId
	 * @return
	 */
	@GetMapping("/findOrderByOpenId")
	public List<OrderDto> findOrderByOpenId(String openId) {
		log.info("<OrderController--findOrderByOpenId--start>");
		List<OrderDto> findOrderByOpenId = orderService.findOrderByOpenId(openId);
		log.info("<OrderController--findOrderByOpenId--end>");
		return findOrderByOpenId;
	}

	/**
	 * 根据用户查询用户拥有的优惠券
	 * 
	 * @param openId
	 * @return
	 */
	@ApiOperation(value = "根据用户查询用户拥有的优惠券", notes = "根据用户查询用户拥有的优惠券", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/customerCoupon")
	@ResponseBody
	public ReturnDataUtil customerCoupon(@RequestBody List<Integer> list) {
		log.info("<OrderController>----<customerCoupon>----start");
		String payIds = StringUtils.join(list, ",");
		OrderForm orderform = new OrderForm();
		orderform.setProduct(payIds);
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId().toString()) && orderform.getProduct() != null) {
			orderform.setCustomerId(user.getId());
			returnDataUtil = orderService.customerCoupon(orderform);
			return returnDataUtil;
		}

		log.info("<OrderController>----<customerCoupon>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "订单地址校验", notes = "订单地址校验", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/checkIfneedAddress")
	@ResponseBody
	public ReturnDataUtil checkIfneedAddress(@RequestBody(required = false) ShoppingCarForm shoppingCarForm,
			HttpServletRequest request) {
		log.info("<OrderController>----<checkIfneedAddress>----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		String ids = shoppingCarForm.getIds();
		String[] split = ids.split(",");
		for (String s : split) {
			ShoppingCarBean shoppingCarBean = shoppingCarServiceImpl.get(s);
			if (shoppingCarBean != null) {
				ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(shoppingCarBean.getItemId());
				if (shoppingGoodsBean.getIsHelpOneself() == 0) {
					returnDataUtil.setStatus(0);
					break;
				}
			}
		}
		log.info("<OrderController>----<checkIfneedAddress>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "团购订单地址校验", notes = "订单地址校验", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/checkGroupIfneedAddress")
	@ResponseBody
	public ReturnDataUtil checkGroupIfneedAddress(@RequestBody(required = false) ShoppingCarForm shoppingCarForm,
			HttpServletRequest request) {
		log.info("<OrderController>----<checkGroupIfneedAddress>----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		String ids = shoppingCarForm.getIds();
		String[] split = ids.split(",");
		for (String s : split) {
			ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(s);
			if (shoppingGoodsBean.getIsHelpOneself() == 0) {
				returnDataUtil.setStatus(0);
				break;
			}
			if (shoppingGoodsBean.getIsHelpOneself() == 1) {
				if (shoppingCarForm.getSpellgroupId() != null) {
					List<Map<String, Object>> pickUpAddress = (List<Map<String, Object>>) orderService
							.pickUpAddress(shoppingCarForm.getSpellgroupId()).getReturnObject();
					if (pickUpAddress.size() == 0) {
						Map<String, Object> map = new HashMap<String, Object>();
						// 查询用户注册机器地址
						String findCusteomerAddress = tblCustomerWxService.findCusteomerAddress();
						map.put("id", 1);
						map.put("name", findCusteomerAddress);
						pickUpAddress.add(map);
					}
					returnDataUtil.setStatus(1);
					returnDataUtil.setReturnObject(pickUpAddress);
				}

			}

		}
		log.info("<OrderController>----<checkGroupIfneedAddress>----end");
		return returnDataUtil;
	}


	/**
	 * 商城生成订单接口
	 * 
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "商城订单的生成", notes = "商城订单的生成", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/storeOrderAdd")
	@ResponseBody
	public ReturnDataUtil add(@RequestBody OrderForm orderform) {
		log.info("<OrderController>----<add>----start");
		ReturnDataUtil returnDataUtil = null;
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId().toString()) && StringUtils.isNotBlank(orderform.getProduct())) {
			returnDataUtil = orderService.add(orderform);
		} else {
			returnDataUtil = new ReturnDataUtil();
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage("订单创建失败！(找不到用户或未选择商品)");
		}
		log.info("<OrderController>----<add>----end");
		return returnDataUtil;
	}
	
	/**
	 * 根据登陆id查询顾客地址
	 * 
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "根据登陆id查询顾客地址", notes = "根据登陆id查询顾客地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/addressFind")
	@ResponseBody
	public List<String> addressFind(@RequestBody OrderForm orderform) {
		log.info("<OrderController>----<addressFind>----start");
		if (orderform == null) {
			orderform = new OrderForm();
		}
		// UserUtils.getUser().getId().toString();
		orderform.setCustomerId(UserUtils.getUser().getId());

		List<String> list = orderService.addressFind(orderform);

		log.info("<OrderController>----<addressFind>----end");
		return list;
	}

	/**
	 * 商城订单的更新
	 * 
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "商城订单的更新", notes = "商城订单的更新", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/storeOrderUpdate")
	@ResponseBody
	public ReturnDataUtil update(@RequestBody OrderForm orderform) {
		log.info("<OrderController>----<update>----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId().toString()) && StringUtils.isNotBlank(orderform.getProduct())
				&& orderform.getOrderId() != null) {
			orderform.setCustomerId(user.getId());
			boolean bean = orderService.update(orderform);
			if (bean) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("订单修改成功！");
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("订单修改失败！");
			}
		}

		log.info("<OrderController>----<update>----end");
		return returnDataUtil;
	}

	/**
	 * 批量删除商城订单
	 * 
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "批量删除商城订单", notes = "批量删除商城订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/storeOrderDel")
	@ResponseBody
	public ReturnDataUtil del(@RequestBody OrderForm orderform) {
		log.info("<OrderController>----<del>----start");
		if (orderform == null) {
			orderform = new OrderForm();
		}
		returnDataUtil = orderService.del(orderform.getOrderIdList());
		log.info("<OrderController>----<del>----end");
		return returnDataUtil;
	}

	/**
	 * 查询用户的订单
	 * 
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "查询用户的订单", notes = "查询用户的订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/storeOrderFind")
	@ResponseBody
	public ReturnDataUtil storeOrderFind(@RequestBody(required = false) OrderForm orderForm) {
		log.info("<OrderController>----<storeOrderFind>----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (orderForm == null) {
			orderForm = new OrderForm();
		}
		orderForm.setCustomerId(UserUtils.getUser().getId());
		orderForm.setPageSize(30);
		String openId = orderService.findOpenIdByCustomerId(orderForm.getCustomerId());
		if (orderForm.getCustomerId() != null) {
			if (orderForm.getOrderType() != null) {// 订单类型 1.商城订单 2.机器订单
				if (orderForm.getOrderType() == 1) {// 1.商城订单
					if (StringUtil.isNotEmpty(openId)){
						returnDataUtil = orderService.storeOrderFind(orderForm);
					}else {
						returnDataUtil= orderService.storeOrderFindByHuafa(orderForm);
				   }
				}
				if (orderForm.getOrderType() == 2) {// 2.机器订单
					List<OrderDto> machinesOrder = orderService.findOrderById(UserUtils.getUser().getId(),
							orderForm.getFindType());
					returnDataUtil.setReturnObject(machinesOrder);
				}
				if (orderForm.getOrderType() == 3) {// 拼团订单
					returnDataUtil = orderService.mySpellgroupOrder(orderForm);
				}
			}
		}
		return returnDataUtil;
	}

	/**
	 * 支付完成跳转页
	 */
	@ApiOperation(value = "支付完成跳转页", notes = "支付完成跳转页", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/storeOrderFininshPay")
	public Map<String, Object> storeOrderFininshPay() {
		log.info("<OrderController>----<storeOrderFininshPay>----start");
		Map<String, Object> map = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		CustomerBean cus = customerService.findCustomerById(user.getId());
		String openId = cus.getOpenId();
		log.info("map" + map);
		// 暂无优惠券提水券使用情况
		String price = redisClient.get("Price_" + openId);
		//支付宝没有openid
		if(StringUtils.isBlank(price)) {
			price = redisClient.get("Price_" + user.getId());
		}
		
		String couponId = redisClient.get("CouponId_" + openId);
		String couponName = redisClient.get("CouponName_" + openId);
		String sumDeductionMoney = redisClient.get("SumDeductionMoney_" + openId);
		String payType = redisClient.get("PayType_" + openId);
		String memberMoney = redisClient.get("MemberMoney_" + openId);

		map.put("price", price);
		map.put("couponId", couponId);
		map.put("couponName", couponName);
		map.put("sumDeductionMoney", sumDeductionMoney);
		map.put("memberMoney", memberMoney);

		map.put("payType", payType);
		map.put("follow", 1);// 能商城购买即已关注
		log.info("<OrderController>----<storeOrderFininshPay>----end");
		return map;
	}

	@ApiOperation(value = "商城团购订单的生成", notes = "商城团购订单的生成", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/storeOrderSpellGroupAdd")
	@ResponseBody
	public ReturnDataUtil storeOrderSpellGroupAdd(@RequestBody OrderForm orderForm) {
		log.info("<OrderController>----<storeOrderSpellGroupAdd>----start");
		log.info(JsonUtils.toJson(orderForm));
		ReturnDataUtil returnDataUtil = null;
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId().toString()) && StringUtils.isNotBlank(orderForm.getProduct())) {
			returnDataUtil = orderService.addSpellGroup(orderForm);
		} else {
			returnDataUtil = new ReturnDataUtil();
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage("订单创建失败！(找不到用户或未选择商品)");
		}
		log.info("<OrderController>----<storeOrderSpellGroupAdd>----end");
		return returnDataUtil;
	}

	/**
	 * 创建临时二维码
	 */
	@ApiOperation(value = "创建二维码", notes = "创建二维码", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/createStrQr")
	public Map<String, Object> createStrQr() {
		log.info("<OrderController>----<createStrQr>----start");
		Map<String, Object> map = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		String url = "http://yms.youshuidaojia.com/admin/createStrQr";
		String jsonResult = HttpUtil.post(url, JsonUtils.toJson(user.getId().toString()));
		if (StringUtil.isNotBlank(jsonResult)) {
			map = JsonUtils.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
			});
		}
		log.info("调用生成微信临时二维码URL接口返回结果:" + map);
		log.info("<OrderController>----<createStrQr>----end");
		return map;
	}

	/**
	 * 取消订单
	 * 
	 * @author hebiting
	 * @date 2018年12月25日下午5:09:05
	 * @param orderId
	 * @return
	 */
	@PostMapping("/cancel/{orderId}")
	public ReturnDataUtil cancelOrder(@PathVariable Long orderId) {
		log.info("cancelOrder--start");
		boolean result = orderService.updateOrderStateById(orderId, PayStateEnum.ORDER_CANCEL);
		if (result) {
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}

	/*
	 * 拼团订单详情
	 */
	@ApiOperation(value = "拼团订单详情", notes = "拼团订单详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/orderParticulars")
	public Map<String, Object> orderParticulars(Long orderId) {
		log.info("<OrderController>----<orderParticulars>----start");
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询商品信息
		ShoppingDto dto = orderService.orderParticulars(orderId);
		// 根据商品团购活动id查询商品取货地址
		List<Map<String, Object>> pickUpAddress = (List<Map<String, Object>>) orderService
				.pickUpAddress(dto.getSpellGroupId()).getReturnObject();
		if (pickUpAddress.size() == 0) {
			Map<String, Object> map2 = new HashMap<String, Object>();
			// 查询用户注册机器地址
			String findCusteomerAddress = tblCustomerWxService.findCusteomerAddress();
			map2.put("id", 1);
			map2.put("name", findCusteomerAddress);
			pickUpAddress.add(map2);
		}
		// 查询通过订单id提水券信息
		List<CarryWaterVouchersCustomerDto> carryList = carryWaterVouchersCustomerService
				.queryCarryWaterCustomerDto(orderId);
		map.put("shoppingBean", dto);
		map.put("addressList", pickUpAddress);
		map.put("carryCustomer", carryList);
		log.info("<OrderController>----<orderParticulars>----end");
		return map;
	}

	/**
	 * 发起支付判断该团是否拼成功
	 * 
	 * @author why
	 * @date 2019年1月25日 下午6:46:24
	 * @param orderId
	 * @return
	 */
	@ApiOperation(value = "拼团订单详情", notes = "拼团订单详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/isPaymentAllowed")
	public ReturnDataUtil isPaymentAllowed(Long orderId) {
		log.info("<OrderController>----<isPaymentAllowed>----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		boolean flag = orderService.isPaymentAllowed(orderId);
		if (flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("可以发起支付！");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("亲，您好，本次拼团订单已经完成了本次拼团，亲不能继续支付了，您可以取消本次订单！");
		}
		log.info("<OrderController>----<isPaymentAllowed>----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "生成机器token", notes = "生成机器token", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/getToken")
	public ReturnDataUtil test2(Long id,String openId){
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		final String randomKey = jwtTokenUtil.getRandomKey();
		User userVo = new User();
		userVo.setId(id);
		userVo.setOpenId(openId);
		userVo.setType(2);
		userVo.setPayType(1);
		final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
		returnDataUtil.setReturnObject(token);
		return returnDataUtil;
	}

	@ApiOperation(value = "更改配送状态", notes = "更改配送状态", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/deliveryCompleted1")
	public ReturnDataUtil delivering1(@RequestParam Long orderId){
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				// run方法具体重写
				try {
					Thread.sleep(1000000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
		t.start();
		return returnDataUtil;
	}


	@RequestMapping(value = "/returnOrder")
	@ResponseBody
	public HuaFaResult returnOrder(@RequestBody String order){
		//HuaFaResult huaFaResult = JSON.parseObject(order,HuaFaResult.class);
		log.info("returnOrderAdmin"+order);
		HuaFaResult huaFaResult = new HuaFaResult();
		huaFaResult.setSuccess("true");
		huaFaResult.setMessage("GG");
		return huaFaResult;
	}

	@ApiOperation(value = "更改配送状态", notes = "更改配送状态", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/deliveryCompleted")
	public ReturnDataUtil delivering(Long orderId){
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		log.info(orderId);
		String payCode = orderService.findPayCodeByOrderId(orderId);
		OrderBean oBean=orderService.getMessageByPayCode(payCode);
		Long customerId =oBean.getCustomerId();
		log.info(customerId+"====="+customerId);
		TblCustomerBean tblCustomerBean = tblCustomerService.getCustomerById(customerId);
		boolean bean = orderService.delivering(orderId);
		if (bean) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("配送状态修改成功！");
			if (oBean.getType()==1  && tblCustomerBean.getHuafaAppOpenId() != null){
				HashMap<String, Object> map = new HashMap<>();
				map.put("openId",tblCustomerBean.getHuafaAppOpenId());
				map.put("orderId",orderId);
				map.put("state",200005);
				String json = JSON.toJSONString(map);//map转String
				//HttpUtil.post("https://devapp.huafatech.com/app/water/orderInfo/updateWaterOrderInfo", json);
				//HttpUtil.post("https://devapp.huafatech.com/app/water/orderInfo/updateWaterOrderInfo", json);
				Thread t = new Thread(new Runnable(){
					@Override
					public void run(){
						// run方法具体重写
						try {
							machinesClient.sendHuaFaUpdate(5,orderId, json);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}});
				t.start();
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("配送状态修改失败！");
		}
		return  returnDataUtil;
	}

	@PostMapping("/getState")
	public ReturnDataUtil getState(String payCode){
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		OrderBean orderBean = orderService.getMessageByPayCode(payCode);
		if (orderBean.getState()==10001 || orderBean.getState() == 200004){
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("用户已成功支付");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("用户未成功支付，请重新支付！");
		}
		return returnDataUtil;
	}

}
