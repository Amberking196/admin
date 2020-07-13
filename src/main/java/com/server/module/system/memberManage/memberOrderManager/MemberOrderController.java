package com.server.module.system.memberManage.memberOrderManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.fasterxml.jackson.core.type.TypeReference;
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
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.statisticsManage.payRecord.PayRecordDto;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.redis.RedisClient;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.HttpUtil;
import com.server.util.IDUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.PayStateEnum;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-09-26 14:36:43
 */
@Api(value = "MemberOrderController", description = "会员订单")
@RestController
@RequestMapping("/memberOrder")
public class MemberOrderController {

	private static Logger log = LogManager.getLogger(MemberOrderController.class);
	@Autowired
	private MemberOrderService memberOrderServiceImpl;
	@Autowired
	private CustomerService customerServiceImpl;
	@Autowired
	private WxPayConfigFactory wxpayConfigFactory;
	@Autowired
	private WxTicketService wxTicketService;
	@Autowired
	private RedisClient redisClient;

	@Value("${wechat.pay.storePayUrl}")
	private String storePayUrl;
	@Value("${wechat.pay.balanceNotify}")
	private String balanceNotify;

	@ApiOperation(value = "会员订单列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) MemberOrderForm memberOrderForm) {
		log.info("<MemberOrderController>-----<listPage>------start");
		if (memberOrderForm == null) {
			memberOrderForm = new MemberOrderForm();
		}
		ReturnDataUtil returnDataUtil = memberOrderServiceImpl.listPage(memberOrderForm);
		log.info("<MemberOrderController>-----<listPage>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "添加会员订单", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody MemberOrderBean entity) {
		log.info("<MemberOrderController>-----<add>------start");
		ReturnDataUtil returnDataUtil = memberOrderServiceImpl.add(entity);
		log.info("<MemberOrderController>-----<add>------end");
		return returnDataUtil;
	}

	@GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil export(MemberOrderForm memberOrderForm,HttpServletResponse response) {
		log.info("<MemberOrderController>-----<listPage>------start");
		if (memberOrderForm == null) {
			memberOrderForm = new MemberOrderForm();
		}
		memberOrderForm.setIsShowAll(1);
		ReturnDataUtil returnDataUtil = memberOrderServiceImpl.listPage(memberOrderForm);
		List<MemberOrderBean> list=(List<MemberOrderBean>) returnDataUtil.getReturnObject();
		
		String title = "会员订单记录";

		String[] headers = new String[] { "phone","price","stateName","payTime","companyName"};
		String[] column = new String[] { "手机号","金额", "状态","支付时间" ,"所属公司"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, list, title.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		log.info("<MemberOrderController>-----<listPage>------end");
		return returnDataUtil;
	}
	
	
	@ApiOperation(value = "充值余额", notes = "充值余额", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/payBalance")
	public Map<String, Object> payBalance(MemberOrderForm memberOrderForm, HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		log.info("<MemberOrderController>-----<payBalance>-------start");
		Map<String, Object> map = new HashMap<String, Object>();
		Long customerId = CustomerUtil.getCustomerId();
		log.info("<MemberOrderController>用户id============" + customerId);
		CustomerBean cus = customerServiceImpl.findCustomerById(customerId);
		if (cus == null) {
			return null;
		}
		if (cus.getPhone() == null) {
			log.info("商城用户手机号未注册，则进行注册");
			try {
				response.sendRedirect("http://webapp.youshuidaojia.com/cLogin?openId=" + cus.getOpenId() + " ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// 得到订单信息
			MemberOrderBean bean = memberOrderServiceImpl.get(memberOrderForm.getOrderId());
			log.info("订单信息========orderId=" + bean.getId() + "-----金额=" + bean.getPrice());
			String product = "余额充值";
			String payCode = bean.getPayCode();
			String spbillCreateIp = req.getRemoteHost();
			String openId = cus.getOpenId();
			String url = memberOrderForm.getUrl();
/*			if(bean.getType()==1){//充值类型

			}*/
			Integer companyId = CompanyEnum.YOUSHUIDAOJIA.getIndex();
			WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			MyWXRequest request = new MyWXRequest();
			request.setOpenid(openId);
			String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
			request.setAppid(wxPayConfig.getAppID());
			request.setMch_id(wxPayConfig.getMchID());
			request.setDevice_info("WEB");
			request.setBody(product);
			request.setTrade_type("JSAPI");
			// 回调地址配置
			request.setNotify_url(balanceNotify);
			request.setNonce_str(nonce_str);
			request.setOut_trade_no(payCode);
			BigDecimal fee = bean.getPrice();
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
		log.info("<MemberOrderController>-----<payMemebr>-------end");
		return map;
	}

	@RequestMapping(value = "/balanceNotify")
	public void payBalanceNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("<MemberOrderController>------<payBalanceNotify>------<start>");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			// 获取真实订单号
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);
			Integer companyId = CompanyEnum.YOUSHUIDAOJIA.getIndex();
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("payBalanceNotify----结果:" + b);
			if (b) {// 订单号 25位0
				// 支付成功 更新状态
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisClient.get(MachinesKey.orderFlag, outTradeNo))) {
					redisClient.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					// 更新订单状态
					log.info("-----------更新订单状态--------------");
					memberOrderServiceImpl.paySuccessMemberOrder(outTradeNo, transactionId);
					log.info("-----------获取订单信息--------------outTradeNo=" + outTradeNo);
					// 获取订单信息
					MemberOrderBean memberOrder = memberOrderServiceImpl.getMemberOrder(outTradeNo);
					// 修改用户余额
					memberOrderServiceImpl.updateCustomerBalance(memberOrder);
				}
				returnResult = ReturnConstant.SUCCESS;
			} else {
				log.info("<MemberOrderController>------<payBalanceNotify>------end");
				returnResult = ReturnConstant.FAIL;
			}
			log.info("<MemberOrderController>------<payBalanceNotify>------end");
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
		log.info("<MemberOrderController>------<payBalanceNotify>------end");
		return;
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
