package com.server.module.customer.order;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.server.common.persistence.BaseDao;
import com.server.module.customer.product.ShoppingGoodsService;
import com.server.module.customer.product.ShoppingGoodsServiceImpl;
import com.server.module.customer.userInfo.TblCustomerBean;
import com.server.module.customer.userInfo.TblCustomerDao;
import com.server.module.customer.userInfo.TblCustomerService;
import com.server.util.HttpUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.server.module.config.aliPay.AlipayAPIClientFactory;
import com.server.module.config.aliPay.AliPayConfig;
import com.server.module.config.aliPay.AlipayConfigFactory;
import com.server.module.customer.CustomerUtil;
import com.server.redis.RedisClient;

/**
 * 支付宝App支付控制层
 * @author why
 * @date: 2019年3月16日 上午10:25:00
 */
@RestController
@RequestMapping("/aliOrder")
public class AliPayController {
	private static final Logger log = LogManager.getLogger(OrderController.class);
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AlipayAPIClientFactory alipayAPIClientFactory;
	@Autowired
	private AlipayConfigFactory alipayConfigFactory;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private ShoppingGoodsService shoppingGoodsService;

	//支付宝app支付
	//https://blog.csdn.net/qq_36004521/article/details/79543665
	//@RequestParam
	@GetMapping("/appPay")
	public String wapPay(OrderForm orderForm,HttpServletRequest request,HttpServletResponse response) throws IOException {
		log.info("<AlipayController--appPay--start>");
		Map<String, Object> id = orderService.findSomeMessByOrderId(orderForm);
		Long customerId = CustomerUtil.getCustomerId();
//		if(record.getCustomerId()!=null && !record.getState().equals(PayStateEnum.PAY_SUCCESS.getState().toString())){
//			CustomerBean customer = customerService.queryById(record.getCustomerId());
//			if(customer!=null && StringUtil.isNotBlank(customer.getAlipayUserId())){
//				String agreementNo = alipayService.querySign(customer.getAlipayUserId(),vmCode);
//				if(StringUtil.isNotBlank(agreementNo)){
//					ptCode = alipayService.cutPayment(record.getTotalPrice(), record.getPayCode(), record.getItemName(), agreementNo);
//				}
//			}
//		}
		Integer companyId=(Integer) id.get("companyId");
		String product = (String) id.get("product");
		String payCode = (String) id.get("payCode");
		double nowprice=(double) id.get("nowprice");

		redisClient.set("Price_"+customerId, id.get("nowprice").toString(),60*5);

		AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(companyId);
		AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
		//AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		//AlipayTradeAppPayRequest aliAppPayRequest = new AlipayTradeAppPayRequest();
		AliPayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		//alipayRequest.setReturnUrl(alipayConfig.wap_return_url);这个接口是获取token返回机器首页
		alipayRequest.setNotifyUrl(alipayConfig.wap_notify_url);// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{"
				+ " \"out_trade_no\":\""+payCode+"\","
				+ " \"total_amount\":\""+nowprice+"\","
				+ " \"subject\":\""+product+"\","
				+ " \"product_code\":\"QUICK_MSECURITY_PAY\""
				+ " }");// 填充业务参数
		AlipayTradeAppPayResponse appResponse = null;
		try {
			appResponse = alipayClient.sdkExecute(alipayRequest);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		} // 调用SDK生成表单
		log.info("支付宝下单结果"+appResponse.getBody());
		log.info("<AlipayController--appPay--end>");
		return appResponse.getBody();

	}

	@GetMapping("/huafaAppPay")
	public String huafaAppPay(OrderForm orderForm,HttpServletRequest request,HttpServletResponse response) throws IOException {
		log.info("<AlipayController--appPay--start>");
		Map<String, Object> id = orderService.findSomeMessByOrderId(orderForm);
		Long customerId = CustomerUtil.getCustomerId();

		//Integer companyId=(Integer) id.get("companyId");
		String product = (String) id.get("product");
		String payCode = (String) id.get("payCode");
		Integer companyId = orderService.getCompanyIdByPayCode(payCode);
		double nowprice=(double) id.get("nowprice");

		redisClient.set("Price_"+customerId, id.get("nowprice").toString(),60*5);

		AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(companyId);
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		//AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		//AlipayTradeAppPayRequest aliAppPayRequest = new AlipayTradeAppPayRequest();
		AliPayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		alipayRequest.setReturnUrl("http://webapp.youshuidaojia.com:8081/cMain/myOrder");//这个接口是获取token返回机器首页
		alipayRequest.setNotifyUrl("http://free-tcp.svipss.top:34731/aliInfo/appNotify");// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{"
				+ " \"out_trade_no\":\""+payCode+"\","
				+ " \"total_amount\":\""+nowprice+"\","
				+ " \"subject\":\""+"订单金额"+"\","
				+ " \"product_code\":\"QUICK_WAP_PAY\""
				+ " }");// 填充业务参数
		String form = "";
		log.info(JSON.toJSONString(alipayRequest));
		log.info(JSON.toJSONString(alipayClient));

		try {
			form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		log.info("<AlipayController--balanceAliPay--end>");

		return form;

	}

//	 public LvQuResult alipay(String body, String subject, String totalAmount, int userId, int member_id, String outTradeNo) {
//	        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//	        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//	        model.setBody(body);//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
//	        model.setSubject(subject);//商品的标题/交易标题/订单标题/订单关键字等
//	        model.setTotalAmount(totalAmount);//订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//	        model.setProductCode("QUICK_MSECURITY_PAY");//销售产品码，商家和支付宝签约的产品码
//	        model.setOutTradeNo(outTradeNo);//商户网站唯一订单号，请保证OutTradeNo值每次保证唯一
//	        model.setTimeoutExpress("30m");//该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
//	        request.setBizModel(model);
//	        request.setNotifyUrl("http://www.lvqutour.com:8081/lvqutour/notify/aliPaynotify_url.jsp");//商户外网可以访问的异步地址
//	        AlipayTradeAppPayResponse response;
//	        try {
//	            response = alipayClient.sdkExecute(request);//这里和普通的接口调用不同，使用的是sdkExecute
//	            return LvQuResult.ok(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
//	        } catch (AlipayApiException e) {
//	            e.printStackTrace();
//	            String massage = "alipay.trade.app.pay接口：订单签名错误";
//	            MdcPut.mdcPut(outTradeNo,new Date(),massage);
//	            logger.error("订单号：" + outTradeNo + massage);
//	            return LvQuResult.msg(false, "订单号：" + outTradeNo + massage);
//	        }
//	    }


}
