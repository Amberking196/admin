package com.server.module.customer.order;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.module.customer.userInfo.TblCustomerBean;
import com.server.module.customer.userInfo.TblCustomerService;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.util.HttpUtil;
import com.server.util.ReturnDataUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.server.github.wxpay.sdk.WXPay;
import com.server.github.wxpay.sdk.WXPayConfig;
import com.server.github.wxpay.sdk.WXPayUtil;
import com.server.module.config.MachinesKey;
import com.server.module.config.gzh.WxTicketService;
import com.server.module.config.pay.MyWXRequest;
import com.server.module.config.pay.WxPayConfigFactory;
import com.server.module.constant.ReturnConstant;
import com.server.module.customer.CustomerUtil;
import com.server.module.customer.coupon.CouponBean;
import com.server.module.customer.coupon.CouponCustomerBean;
import com.server.module.customer.coupon.CouponForm;
import com.server.module.customer.coupon.CouponService;
import com.server.module.customer.order.storeGroupOrderManage.GroupOrderBean;
import com.server.module.customer.order.storeGroupOrderManage.GroupOrderService;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsService;
import com.server.module.customer.product.shoppingGoodsProduct.ShoppingGoodsProductBean;
import com.server.module.customer.product.shoppingGoodsProduct.ShoppingGoodsProductService;
import com.server.module.customer.product.tblCustomerSpellGroup.TblCustomerSpellGroupService;
import com.server.module.customer.userInfo.stock.CustomerStockBean;
import com.server.module.customer.userInfo.stock.OrderDetailBean;
import com.server.module.customer.userInfo.stock.StoreOrderService;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.redis.RedisClient;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CouponEnum;

import io.swagger.annotations.ApiOperation;

/**
 * 微信支付控制层
 * @author why
 * @date: 2019年3月16日 上午10:25:00
 */
@RestController
@RequestMapping("/order")
public class WxPayController {

	private static final Logger log = LogManager.getLogger(WxPayController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private ShoppingGoodsService shoppingGoodsService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private WxPayConfigFactory wxpayConfigFactory;
	@Autowired
	private WxTicketService wxTicketService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private ShoppingGoodsProductService shoppingGoodsProductService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private CarryWaterVouchersCustomerService carryWaterVouchersCustomerServiceImpl;
	@Autowired
	private StoreOrderService storeOrderServiceImpl;
	@Autowired
	private GroupOrderService groupOrderServiceImpl;
	@Autowired
	private  TblCustomerSpellGroupService tblCustomerSpellGroupService;

	@Value("${wechat.pay.storeNotify}")
	private String storeNotify;
	@Value("${wechat.pay.storeAppNotify}")
	private String storeAppNotify;
	@Value("${wechat.pay.storePayUrl}")
	private String storePayUrl;
	@Value("${wechat.pay.storeGroupNotify}")
	private String storeGroupNotify;

	/**
	 * 微信支付
	 * @throws Exception
	 */
	@ApiOperation(value = "微信支付", notes = "微信支付", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/storeOrderPay")
	public Map<String, Object> linkSms(OrderForm orderForm,HttpServletRequest req,HttpServletResponse response) throws Exception{
		log.info("<WxPayController>----<storeOrderPay>----start");
		Map<String, Object> map = Maps.newHashMap();
		Long customerId = CustomerUtil.getCustomerId();
		CustomerBean cus = customerService.findCustomerById(customerId);
		if (cus == null) {
			return null;
		}
		if(cus.getPhone()==null) {
			log.info("商城用户手机号未注册，则进行注册");
			try {
				response.sendRedirect("http://webapp.youshuidaojia.com/cLogin?openId="+cus.getOpenId());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			Map<String, Object> id = orderService.findSomeMessByOrderId(orderForm);
			String product = (String) id.get("product");
			String payCode = (String) id.get("payCode");
			double nowprice=(double) id.get("nowprice");
			Integer companyId=(Integer) id.get("companyId");
			String spbillCreateIp = req.getRemoteHost();
			String openId = cus.getOpenId();
			String url = orderForm.getUrl();
			redisClient.set("Price_"+cus.getOpenId(), id.get("nowprice").toString(),60*5);
			//Integer companyId = orderService.getCompanyIdByPayCode(payCode);
			WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			MyWXRequest request = new MyWXRequest();
			request.setOpenid(openId);
			String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
			List<String> list = shoppingGoodsService.findItemName(product);
			String products = "";
			for (int a = 0; a < list.size(); a++) {
				if (a == list.size() - 1) {
					products = products + list.get(a);
				} else {
					products = products + list.get(a) + ",";
				}
			}
			request.setAppid(wxPayConfig.getAppID());
			request.setMch_id(wxPayConfig.getMchID());
			request.setDevice_info("WEB");
			request.setBody(products);//
			request.setTrade_type("JSAPI");
			request.setNotify_url(storeNotify);
			request.setNonce_str(nonce_str);
			request.setOut_trade_no(payCode);
			BigDecimal fee = new BigDecimal(nowprice);
			BigDecimal totalFee = fee.multiply(new BigDecimal(100));
			request.setTotal_fee(totalFee.intValue() + "");
			request.setSpbill_create_ip(spbillCreateIp);
			Map<String, String> mapParam = request.requestToMap(request, false);
			Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
			Map<String, String> wxJsapiSignature = null;
			if (StringUtil.isNotBlank(url)) {
				wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
			} else {
				wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
			}
			map.put("config", wxJsapiSignature);// 微信jssdk config用
			Map<String, String> payInfo = Maps.newHashMap();
			payInfo.put("appId", wxPayConfig.getAppID());
			payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
			payInfo.put("nonceStr", nonce_str);
			payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
			payInfo.put("signType", "MD5");
			try {
				String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
				payInfo.put("sign", sign);
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("payInfo", payInfo);// 微信支付
		}
		log.info("<WxPayController>----<linkSms>----end");
		return map;
	}

	@ApiOperation(value = "微信APP支付", notes = "微信APP支付", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/storeOrderAppPay")
	public String appLinkSms(OrderForm orderForm,HttpServletRequest req,HttpServletResponse response) throws Exception{
		log.info("<WxPayController>----<storeOrderAppPay>----start");
		Map<String, Object> map = Maps.newHashMap();
		Long customerId = CustomerUtil.getCustomerId();
		CustomerBean cus = customerService.findCustomerById(customerId);
		if (cus == null) {
			//return null;
		}
		Map<String, Object> id = orderService.findSomeMessByOrderId(orderForm);
		String product = (String) id.get("product");
		String payCode = (String) id.get("payCode");
		double nowprice = (double) id.get("nowprice");
		Integer companyId = (Integer) id.get("companyId");
		String spbillCreateIp = req.getRemoteHost();
		//String openId = cus.getOpenId();
		//String url = orderForm.getUrl();
		redisClient.set("Price_"+customerId, id.get("nowprice").toString(),60*5);
		//Integer companyId = orderService.getCompanyIdByPayCode(payCode);
		companyId=1;
		WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
		WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
		MyWXRequest request = new MyWXRequest();
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		List<String> list = shoppingGoodsService.findItemName(product);
		String products = "";
		for (int a = 0; a < list.size(); a++) {
			if (a == list.size() - 1) {
				products = products + list.get(a);
			} else {
				products = products + list.get(a) + ",";
			}
		}

		request.setScene_info("{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"webapp.youshuidaojia.com\",\"wap_name\": \"优水到家\"}}");
		request.setAppid(wxPayConfig.getAppID());
		request.setMch_id(wxPayConfig.getMchID());
		request.setBody("优水到家-"+products);//
		request.setTrade_type("MWEB");
		request.setNotify_url(storeAppNotify);
		request.setNonce_str(nonce_str);
		request.setOut_trade_no(payCode);

		BigDecimal fee = new BigDecimal(nowprice);
		BigDecimal totalFee = fee.multiply(new BigDecimal(100));
		request.setTotal_fee(totalFee.intValue() + "");
		request.setSpbill_create_ip(spbillCreateIp);
		Map<String, String> mapParam = request.requestToMap(request, false);
		Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
		String  mweb_url=unifiedOrder.get("mweb_url");
		log.info("prepay_id"+unifiedOrder.get("prepay_id"));
		log.info("mweb_url"+unifiedOrder.get("mweb_url"));

//		Map<String, String> wxJsapiSignature = null;
//		if (StringUtil.isNotBlank(url)) {
//			wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
//		} else {
//			wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
//		}
//		map.put("config", wxJsapiSignature);// 微信jssdk config用
		//appid，partnerid，prepayid，noncestr，timestamp，package
		Map<String, String> payInfo = Maps.newHashMap();
		payInfo.put("appid", wxPayConfig.getAppAppID());
		payInfo.put("partnerid", wxPayConfig.getMchID());
		payInfo.put("prepayid",unifiedOrder.get("prepay_id"));
		payInfo.put("noncestr", nonce_str);
		payInfo.put("timestamp", System.currentTimeMillis() / 1000 + "");
		payInfo.put("package", "Sign=WXPay");
		try {
			String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getAppKey());
			log.info("sign"+sign);
			payInfo.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("payInfo", payInfo);// 微信支付
		mweb_url=mweb_url+"&redirect_url=http://webapp.youshuidaojia.com:8081/cMain/myOrder";
		log.info("<WxPayController>----<appLinkSms>----end");
		return mweb_url;
		//return map;
	}
	/**
	 * 微信完成商城订单后的回调，用以确认订单是否完成支付，并更新状态
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/storeNotify")
	public void payStoreNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("<WxPayController>----<payStoreNotify>----start");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);// 获取真实订单号
			Integer companyId = orderService.getCompanyIdByPayCode(outTradeNo);
			Integer distributionModel = orderService.getDistributionModelByPayCode(outTradeNo);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("notify 结果:" + b);
			if (b) {// 订单号 25位
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisClient.get(MachinesKey.orderFlag, outTradeNo))) {
					redisClient.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					//更改订单的状态
					orderService.paySuccessStroeOrder(distributionModel,outTradeNo, transactionId,0);
					// 给商城顾客增加存水
					List<OrderDetailBean> orderDetail = storeOrderServiceImpl.getOrderDetail(outTradeNo);
					Long customerId = orderDetail.get(0).getCustomerId();
					for (OrderDetailBean orderDetailBean : orderDetail) {
						ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsService.get(orderDetailBean.getItemId().longValue());
						// 购买优惠券不增加存水
						if (shoppingGoodsBean.getTypeId().equals(25l)) {// 购买的商品为 优惠券此时绑定用户
							//根据商品名称获取优惠券信息
							CouponBean couponBean = couponService.getCouponInfoByProduct(shoppingGoodsBean);
							//获取用户的优惠券信息
							CouponCustomerBean couponCustomerBean = couponService.getCouponCustomerBean(customerId,
									couponBean.getId());
							if (couponCustomerBean != null) {//更新绑定用户的优惠券数量(仅限于购买优惠券的用户)
								couponCustomerBean.setQuantity(couponCustomerBean.getQuantity() + orderDetailBean.getNum());
								couponCustomerBean.setSumQuantity(couponCustomerBean.getSumQuantity() + orderDetailBean.getNum());// 券的总数
								couponService.updateCouponCustomerBean(couponCustomerBean);
							} else {//赠券插入
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setCouponId(couponBean.getId());
								couCusBean.setCustomerId(customerId);
								couCusBean.setStartTime(couponBean.getLogicStartTime());
								couCusBean.setEndTime(couponBean.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couCusBean.setQuantity(orderDetailBean.getNum().longValue());
								couCusBean.setSumQuantity(orderDetailBean.getNum().longValue());// 总券数
								couponService.insertCouponCustomer(couCusBean);
							}

						} else if (shoppingGoodsBean.getTypeId().equals(26l) || shoppingGoodsBean.getTypeId().equals(27l)) {
							// 购买的商品为提水券 此时绑定用户
							carryWaterVouchersCustomerServiceImpl.add(shoppingGoodsBean.getVouchersId(), customerId,
									orderDetailBean.getNum(),orderDetailBean.getOrderId());
						}else if (shoppingGoodsBean.getTypeId().equals(28l)) {
							// 商品为套餐类型 绑定多张提水券
							ShoppingGoodsBean sgs=shoppingGoodsService.get(orderDetailBean.getItemId().longValue());
							String[] vouchers=StringUtils.split(sgs.getVouchersIds(),",");
							for(String v:vouchers) {
								carryWaterVouchersCustomerServiceImpl.add(Long.valueOf(v), customerId,
										orderDetailBean.getNum(),orderDetailBean.getOrderId());
							}
						}
						else {
							if (orderDetailBean.getDistributionModel() == 1) {// 自取// 直接去给用户存水
								CustomerStockBean stock = storeOrderServiceImpl.getStock(orderDetailBean.getItemId(),
										orderDetailBean.getCustomerId());
								if (stock == null) {
									stock = new CustomerStockBean();
									stock.setBasicItemId(shoppingGoodsBean.getBasicItemId());
									stock.setItemId(orderDetailBean.getItemId());
									stock.setItemName(orderDetailBean.getItemName());
									stock.setCustomerId(orderDetailBean.getCustomerId());
									stock.setStock(orderDetailBean.getNum());
									stock.setPickNum(0);
									storeOrderServiceImpl.insert(stock);
								} else {
									stock.setStock(stock.getStock() + orderDetailBean.getNum());
									storeOrderServiceImpl.update(stock);
								}
							} else {// 非自取 判断用户购买的商品订单是否有绑定套餐 如果有 加入存水
								// 根据商城商品id 查询绑定商品信息
								List<ShoppingGoodsProductBean> shoppingGoodsProductBeanList = shoppingGoodsProductService
										.getShoppingGoodsProductBean(orderDetailBean.getItemId().longValue());
								if (shoppingGoodsProductBeanList != null && shoppingGoodsProductBeanList.size() > 0) {
									for (ShoppingGoodsProductBean shoppingGoodsProductBean : shoppingGoodsProductBeanList) {
										// 获取用户 拥有当前商品的存量 如果没有就新增 存在就修改
										CustomerStockBean stock = storeOrderServiceImpl.getStock(
												shoppingGoodsProductBean.getGoodsId().intValue(),
												orderDetailBean.getCustomerId());
										if (stock == null) {
											stock = new CustomerStockBean();
											stock.setBasicItemId(shoppingGoodsProductBean.getItemId());
											stock.setItemId(shoppingGoodsProductBean.getGoodsId().intValue());
											stock.setItemName(shoppingGoodsProductBean.getItemName());
											stock.setCustomerId(orderDetailBean.getCustomerId());
											stock.setStock(orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue());
											stock.setPickNum(0);
											storeOrderServiceImpl.insert(stock);
										} else {
											int num = orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue();
											stock.setStock(stock.getStock() + num);
											storeOrderServiceImpl.update(stock);
										}
									}

								}
							}
						}
					}

					// 获取优惠券赠送客户
					CouponForm couponForm = new CouponForm();
					couponForm.setWay(CouponEnum.PURCHASE_COUPON.getState());
					couponForm.setLimitRange(false);
					couponForm.setUseWhere(CouponEnum.USE_STORE.getState());
					List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
					if (presentCoupon != null && presentCoupon.size() > 0) {
						presentCoupon.stream().filter(coupon -> !couponService.isReceive(customerId, coupon.getId()))
								.forEach(coupon -> {
									CouponCustomerBean couCusBean = new CouponCustomerBean();
									couCusBean.setQuantity(coupon.getSendMax().longValue());
									couCusBean.setCouponId(coupon.getId());
									couCusBean.setCustomerId(customerId);
									couCusBean.setStartTime(coupon.getLogicStartTime());
									couCusBean.setEndTime(coupon.getLogicEndTime());
									couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
									couponService.insertCouponCustomer(couCusBean);
								});
					}

					// 邀请人赠券
					CustomerBean customer = customerService.findCustomerById(customerId);
					if (customer.getInviterId() > 0 && customerService.isFirstBuy(customer.getId()).equals(0)
							&& customerService.isStoreFirstBuy(customer.getId()).equals(1)) {
						CouponForm couponForm2 = new CouponForm();
						couponForm2.setLimitRange(false);
						couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
						List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
						if (presentCoupon2 != null && presentCoupon2.size() > 0) {
							for (CouponBean coupon : presentCoupon2) {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax().longValue());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customer.getInviterId());
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							}
						}
					}
				}
				returnResult = ReturnConstant.SUCCESS;
				writeMsgToWx(returnResult, response);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 微信完成华发商城订单后的回调，用以确认订单是否完成支付，并更新状态，通知珠海
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/storeAppNotify")
	public void storeAppNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("<WxPayController>----<payStoreNotify>----start");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);// 获取真实订单号
			Integer companyId = orderService.getCompanyIdByPayCode(outTradeNo);
			Integer distributionModel = orderService.getDistributionModelByPayCode(outTradeNo);
			OrderBean ob = orderService.getMessageByPayCode(outTradeNo);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("notify 结果:" + b);
			if (b) {// 订单号 25位
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisClient.get(MachinesKey.orderFlag, outTradeNo))) {
					redisClient.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					//更改订单的状态couponOrderId
					orderService.paySuccessStroeOrder(distributionModel,outTradeNo, transactionId,0);

					List<OrderDetailBean> orderDetail = storeOrderServiceImpl.getOrderDetail(outTradeNo);
					Long customerId = orderDetail.get(0).getCustomerId();
					TblCustomerBean tbBean = tblCustomerService.getCustomerById(customerId);
					//通知珠海订单成功支付
					if (tbBean.getHuafaAppOpenId() !=null) {
						Map<String, Object> huaAppMap = new HashMap<String, Object>();
						List<ShoppingBean> newlist = orderService.findShoppingBeandByOrderId(ob.getId(),ob.getType());
						huaAppMap.put("orderId", ob.getId());
						huaAppMap.put("openId", tbBean.getHuafaAppOpenId());
						huaAppMap.put("state", ob.getState());
						huaAppMap.put("nowprice", ob.getNowprice());
						huaAppMap.put("payCode", ob.getPayCode());
						huaAppMap.put("createTime", ob.getCreateTime());
						huaAppMap.put("time", new Date());
						huaAppMap.put("type", ob.getType());
						huaAppMap.put("useMoney", "");
						huaAppMap.put("price", ob.getPrice());
						huaAppMap.put("payType", ob.getPayType());
						huaAppMap.put("stateName", ob.getStateName());
						huaAppMap.put("ptCode", transactionId);
						huaAppMap.put("product", ob.getProduct());
						huaAppMap.put("phone", tbBean.getPhone());
						huaAppMap.put("list", newlist);
						String json = JSON.toJSONString(huaAppMap);//map转String
						//JSONObject jsonObject = JSON.parseObject(json);//String转json
						HttpUtil.post("https://devapp.huafatech.com/app/water/orderInfo/createWaterOrderInfo", json);
						log.info("订单传输成功！");
					}
					// 给商城顾客增加存水
					for (OrderDetailBean orderDetailBean : orderDetail) {
						ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsService.get(orderDetailBean.getItemId().longValue());
						// 购买优惠券不增加存水
						if (shoppingGoodsBean.getTypeId().equals(25l)) {// 购买的商品为 优惠券此时绑定用户
							//根据商品名称获取优惠券信息
							CouponBean couponBean = couponService.getCouponInfoByProduct(shoppingGoodsBean);
							//获取用户的优惠券信息
							CouponCustomerBean couponCustomerBean = couponService.getCouponCustomerBean(customerId,
									couponBean.getId());
							if (couponCustomerBean != null) {//更新绑定用户的优惠券数量(仅限于购买优惠券的用户)
								couponCustomerBean.setQuantity(couponCustomerBean.getQuantity() + orderDetailBean.getNum());
								couponCustomerBean.setSumQuantity(couponCustomerBean.getSumQuantity() + orderDetailBean.getNum());// 券的总数
								couponService.updateCouponCustomerBean(couponCustomerBean);
							} else {//赠券插入
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setCouponId(couponBean.getId());
								couCusBean.setCustomerId(customerId);
								couCusBean.setStartTime(couponBean.getLogicStartTime());
								couCusBean.setEndTime(couponBean.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couCusBean.setQuantity(orderDetailBean.getNum().longValue());
								couCusBean.setSumQuantity(orderDetailBean.getNum().longValue());// 总券数
								couponService.insertCouponCustomer(couCusBean);
							}

						} else if (shoppingGoodsBean.getTypeId().equals(26l) || shoppingGoodsBean.getTypeId().equals(27l)) {
							// 购买的商品为提水券 此时绑定用户
							carryWaterVouchersCustomerServiceImpl.add(shoppingGoodsBean.getVouchersId(), customerId,
									orderDetailBean.getNum(),orderDetailBean.getOrderId());
						}else if (shoppingGoodsBean.getTypeId().equals(28l)) {
							// 商品为套餐类型 绑定多张提水券
							ShoppingGoodsBean sgs=shoppingGoodsService.get(orderDetailBean.getItemId().longValue());
							String[] vouchers=StringUtils.split(sgs.getVouchersIds(),",");
							for(String v:vouchers) {
								carryWaterVouchersCustomerServiceImpl.add(Long.valueOf(v), customerId,
										orderDetailBean.getNum(),orderDetailBean.getOrderId());
							}
						}
						else {
							if (orderDetailBean.getDistributionModel() == 1) {// 自取// 直接去给用户存水
								CustomerStockBean stock = storeOrderServiceImpl.getStock(orderDetailBean.getItemId(),
										orderDetailBean.getCustomerId());
								if (stock == null) {
									stock = new CustomerStockBean();
									stock.setBasicItemId(shoppingGoodsBean.getBasicItemId());
									stock.setItemId(orderDetailBean.getItemId());
									stock.setItemName(orderDetailBean.getItemName());
									stock.setCustomerId(orderDetailBean.getCustomerId());
									stock.setStock(orderDetailBean.getNum());
									stock.setPickNum(0);
									storeOrderServiceImpl.insert(stock);
								} else {
									stock.setStock(stock.getStock() + orderDetailBean.getNum());
									storeOrderServiceImpl.update(stock);
								}
							} else {// 非自取 判断用户购买的商品订单是否有绑定套餐 如果有 加入存水
								// 根据商城商品id 查询绑定商品信息
								List<ShoppingGoodsProductBean> shoppingGoodsProductBeanList = shoppingGoodsProductService
										.getShoppingGoodsProductBean(orderDetailBean.getItemId().longValue());
								if (shoppingGoodsProductBeanList != null && shoppingGoodsProductBeanList.size() > 0) {
									for (ShoppingGoodsProductBean shoppingGoodsProductBean : shoppingGoodsProductBeanList) {
										// 获取用户 拥有当前商品的存量 如果没有就新增 存在就修改
										CustomerStockBean stock = storeOrderServiceImpl.getStock(
												shoppingGoodsProductBean.getGoodsId().intValue(),
												orderDetailBean.getCustomerId());
										if (stock == null) {
											stock = new CustomerStockBean();
											stock.setBasicItemId(shoppingGoodsProductBean.getItemId());
											stock.setItemId(shoppingGoodsProductBean.getGoodsId().intValue());
											stock.setItemName(shoppingGoodsProductBean.getItemName());
											stock.setCustomerId(orderDetailBean.getCustomerId());
											stock.setStock(orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue());
											stock.setPickNum(0);
											storeOrderServiceImpl.insert(stock);
										} else {
											int num = orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue();
											stock.setStock(stock.getStock() + num);
											storeOrderServiceImpl.update(stock);
										}
									}

								}
							}
						}
					}

					// 获取优惠券赠送客户
					CouponForm couponForm = new CouponForm();
					couponForm.setWay(CouponEnum.PURCHASE_COUPON.getState());
					couponForm.setLimitRange(false);
					couponForm.setUseWhere(CouponEnum.USE_STORE.getState());
					List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
					if (presentCoupon != null && presentCoupon.size() > 0) {
						presentCoupon.stream().filter(coupon -> !couponService.isReceive(customerId, coupon.getId()))
								.forEach(coupon -> {
									CouponCustomerBean couCusBean = new CouponCustomerBean();
									couCusBean.setQuantity(coupon.getSendMax().longValue());
									couCusBean.setCouponId(coupon.getId());
									couCusBean.setCustomerId(customerId);
									couCusBean.setStartTime(coupon.getLogicStartTime());
									couCusBean.setEndTime(coupon.getLogicEndTime());
									couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
									couponService.insertCouponCustomer(couCusBean);
								});
					}

					// 邀请人赠券
					CustomerBean customer = customerService.findCustomerById(customerId);
					if (customer.getInviterId() > 0 && customerService.isFirstBuy(customer.getId()).equals(0)
							&& customerService.isStoreFirstBuy(customer.getId()).equals(1)) {
						CouponForm couponForm2 = new CouponForm();
						couponForm2.setLimitRange(false);
						couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
						List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
						if (presentCoupon2 != null && presentCoupon2.size() > 0) {
							for (CouponBean coupon : presentCoupon2) {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax().longValue());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customer.getInviterId());
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							}
						}
					}
				}
				returnResult = ReturnConstant.SUCCESS;
				writeMsgToWx(returnResult, response);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}


	/**
	 * 团购微信支付
	 */
	@ApiOperation(value = "团购微信支付", notes = "团购微信支付", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/storeOrderSpellGroupPay")
	public Map<String, Object> linkSpellGroupSms(OrderForm orderForm, HttpServletRequest req,HttpServletResponse response)throws Exception {
		log.info("<WxPayGroupController>----<linkSpellGroupSms>----start");
		Map<String, Object> map = new HashMap<String, Object>();
		//得到用户信息
		Long customerId = CustomerUtil.getCustomerId();
		CustomerBean cus = customerService.findCustomerById(customerId);
		if (cus == null) {
			return null;
		}
		if(cus.getPhone()==null) {
			log.info("商城用户手机号未注册，则进行注册");
			try {
				response.sendRedirect("http://webapp.youshuidaojia.com/cLogin?openId="+cus.getOpenId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {

			Map<String, Object> id = orderService.findSomeMessByOrderId(orderForm);
			String product = (String) id.get("product");
			String payCode = (String) id.get("payCode");
			double nowprice = (double) id.get("nowprice");
			String spbillCreateIp = req.getRemoteHost();
			String openId = cus.getOpenId();
			String url = orderForm.getUrl();

			Integer companyId = orderService.getCompanyIdByPayCode(payCode);
			WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			MyWXRequest request = new MyWXRequest();
			String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
			String findShoppingGoogsName = shoppingGoodsService.findShoppingGoogsName(product);
			if(StringUtils.isNotEmpty(openId)){
				request.setAppid(wxPayConfig.getAppID());
				request.setTrade_type("JSAPI");
				request.setOpenid(openId);
			}else{
				request.setAppid(wxPayConfig.getAppAppID());
				request.setTrade_type("APP");
			}
			request.setMch_id(wxPayConfig.getMchID());
			request.setDevice_info("WEB");
			request.setBody(findShoppingGoogsName);//
			request.setNotify_url(storeGroupNotify);
			request.setNonce_str(nonce_str);
			request.setOut_trade_no(payCode);
			BigDecimal fee = new BigDecimal(nowprice);
			BigDecimal totalFee = fee.multiply(new BigDecimal(100));
			request.setTotal_fee(totalFee.intValue() + "");
			request.setSpbill_create_ip(spbillCreateIp);
			Map<String, String> mapParam = request.requestToMap(request, false);
			Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
			Map<String, String> wxJsapiSignature = null;
			if(StringUtils.isNotEmpty(openId)){
				if (StringUtil.isNotBlank(url)) {
					wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
				} else {
					wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
				}
				map.put("config", wxJsapiSignature);// 微信jssdk config用
			}
			Map<String, String> payInfo = Maps.newHashMap();
			if(StringUtils.isNotEmpty(openId)){
				payInfo.put("appId", wxPayConfig.getAppID());
				payInfo.put("signType", "MD5");
				payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
				payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
				payInfo.put("nonceStr", nonce_str);
			}else{
				payInfo.put("appid", wxPayConfig.getAppAppID());
				payInfo.put("partnerid",wxPayConfig.getMchID());
				payInfo.put("prepayid",unifiedOrder.get("prepay_id").toString());
				payInfo.put("package","Sign=WXPay");
				payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
				payInfo.put("nonceStr", nonce_str);
			}
//				payInfo.put("appId", wxPayConfig.getAppID());
//				payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
//				payInfo.put("signType", "MD5");
			try {
				String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
				payInfo.put("sign", sign);
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("payInfo", payInfo);// 微信支付
			redisClient.set("Price_"+cus.getOpenId(), id.get("nowprice").toString(),60*5);
		}
		log.info("<OrderController>----<linkSpellGroupSms>----end");
		return map;
	}

	@RequestMapping(value = "/storeGroupNotify")
	public void payStoreGroupNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("=====gourpNotify====");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);// 获取真实订单号
			Integer companyId = orderService.getCompanyIdByPayCode(outTradeNo);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			Integer distributionModel = orderService.getDistributionModelByPayCode(outTradeNo);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("notify 结果:" + b);
			if (b) {// 订单号 25位
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisClient.get(MachinesKey.orderFlag, outTradeNo))) {
					redisClient.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					//更改订单的状态
					orderService.paySuccessStroeOrder(distributionModel,outTradeNo, transactionId,1);
					//查询拼团订单
					GroupOrderBean order = groupOrderServiceImpl.getStoreOrderbyOutTradeNo(outTradeNo);
					String message = "恭喜你拼团成功，你参加购买的" + order.getGoodsName() + "提货券已经发到你的账号上了，请关注优水到家的公众号，优水商城-个人中心-我的提货券里面查看";
					if (order.getCustomerId().equals(order.getStartCustomerId())) {// 团长订单 订单->拼团中									
						//更新用户拼团表
						tblCustomerSpellGroupService.updateParCustomerIdAndState(order.getCustomerGroupId(),null,2);
					} else {// 拼团者订单 判断成团人数
						String ids = order.getParticipationCustomerId();
						if (ids == null) {// 第一个拼单人
							if (order.getMinimumGroupSize() == 2) {
								log.info("两人拼团成功");
								String id = order.getCustomerId().toString();
								//更新用户拼团表
								tblCustomerSpellGroupService.updateParCustomerIdAndState(order.getCustomerGroupId(), id, 1);
								//根据用户拼团id 查询订单信息
								List<GroupOrderBean> list = groupOrderServiceImpl.getStoreOrderbyCustomerGroupId(order.getCustomerGroupId());
								//查询参与本次拼团用户订单
								for (GroupOrderBean orderBean : list) {
									//派提水券
									sendVouchers(orderBean,orderBean.getCustomerId());
									//修改拼团状态
									groupOrderServiceImpl.paySpellgroupStroeOrder(orderBean.getPayCode(),2);
									////发送公众号客服信息
									customerService.sendWechatMessage(orderBean.getOpenid(), message);
								}
							} else {
								//更新用户拼团表
								tblCustomerSpellGroupService.updateParCustomerIdAndState(order.getCustomerGroupId(),
										order.getCustomerId().toString(), null);
							}
						} else if (ids.split(",").length < order.getMinimumGroupSize() - 2) {// 未达到成团人数
							ids = ids + "," + order.getCustomerId();
							//更新用户拼团表
							tblCustomerSpellGroupService.updateParCustomerIdAndState(order.getCustomerGroupId(), ids, null);
						} else if (ids.split(",").length == order.getMinimumGroupSize() - 2) {// 已达到成团人数
							log.info("拼团成功");
							ids = ids + "," + order.getCustomerId();
							//更新用户拼团表
							tblCustomerSpellGroupService.updateParCustomerIdAndState(order.getCustomerGroupId(), ids, 1);
							//根据用户拼团id 查询订单信息
							List<GroupOrderBean> list  = groupOrderServiceImpl.getStoreOrderbyCustomerGroupId(order.getCustomerGroupId());
							for (GroupOrderBean orderBean : list) {
								//修改拼团状态
								groupOrderServiceImpl.paySpellgroupStroeOrder(orderBean.getPayCode(),2);
								//派提水券
								sendVouchers(orderBean,orderBean.getCustomerId());
								//发送公众号客服信息
								customerService.sendWechatMessage(orderBean.getOpenid(), message);
							}
						} else if (ids.split(",").length > order.getMinimumGroupSize() - 2) {// 已超过成团人数
							log.info("拼团人数>成团人数");
							ids = ids + "," + order.getCustomerId();
							//更新用户拼团表
							tblCustomerSpellGroupService.updateParCustomerIdAndState(order.getCustomerGroupId(), ids, 1);
							CustomerBean customer = customerService.findCustomerById(order.getCustomerId());
							sendVouchers(order,customer.getId());//派提水券
							//发送公众号客服信息
							customerService.sendWechatMessage(customer.getOpenId(), message);
						}
					}

					// 邀请人赠券
					CustomerBean customer = customerService.findCustomerById(order.getCustomerId());
					if (customer.getInviterId() > 0 && customerService.isFirstBuy(customer.getId()).equals(0)
							&& customerService.isStoreFirstBuy(customer.getId()).equals(1)) {
						CouponForm couponForm2 = new CouponForm();
						couponForm2.setLimitRange(false);
						couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
						List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
						if (presentCoupon2 != null && presentCoupon2.size() > 0) {
							for (CouponBean coupon : presentCoupon2) {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax().longValue());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customer.getInviterId());
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							}
						}
					}
				}
				returnResult = ReturnConstant.SUCCESS;
			} else {
				returnResult = ReturnConstant.FAIL;
			}
			writeMsgToWx(returnResult, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}



	public void sendVouchers(GroupOrderBean order,Long customerId) {
		if(order.getVouchersId() > 0 || StringUtils.isNotBlank(order.getVouchersIds())){
			if(order.getTypeId().equals(28)) {//套餐类型
				List<String>  vouchersIds=Arrays.asList(StringUtils.split(order.getVouchersIds(),","));
				for(String v:vouchersIds) {
					carryWaterVouchersCustomerServiceImpl.add(Long.valueOf(v), customerId, order.getNum(), order.getId());
				}
			}else {
				carryWaterVouchersCustomerServiceImpl.add(order.getVouchersId(), customerId, order.getNum(), order.getId());
			}
		}
	}



	public  void writeMsgToWx(String msg, HttpServletResponse response) {
		try {
			response.reset();
			PrintWriter printWriter = response.getWriter();
			printWriter.write(msg);
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
