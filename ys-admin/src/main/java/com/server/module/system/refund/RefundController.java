package com.server.module.system.refund;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.app.home.ResultEnum;
import com.server.module.commonBean.TotalResultBean;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.refund.application.RefundApplicationDto;
import com.server.module.system.refund.application.RefundApplicationForm;
import com.server.module.system.refund.principal.PrincipalForm;
import com.server.module.system.refund.principal.RefundPrincipalBean;
import com.server.module.system.refund.principal.RefundPrincipalForm;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.redis.RedisClient;
import com.server.util.ArithmeticUtil;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.client.SmsClient;

@RestController
@RequestMapping("/refund")
public class RefundController {

	private static Logger log = LogManager.getLogger(RefundController.class);
	@Autowired
	private RefundService refundService;
	@Autowired
	private RefundClient refundClient;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private CustomerService customerService;

	/**
	 * 新增退款负责人
	 * 
	 * @author hebiting
	 * @date 2018年10月23日下午2:51:35
	 * @param principal
	 * @return
	 */
	@PostMapping("/addPrincipal")
	public ReturnDataUtil insertRefundPrincipal(@RequestBody PrincipalForm principalForm, HttpServletRequest request) {
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		String phoneCode = principalForm.getPhoneCode();
		String phone = refundService.getPrincipalInfoById(userId);
		String redisCode = redisClient.get("SmsKey:mobile" + phone);
		if (StringUtil.isBlank(phoneCode) || !phoneCode.equals(redisCode)) {
			return ResultUtil.error();
		}
		Date now = new Date();
		RefundPrincipalBean principal = principalForm.getPrincipal();
		principal.setCreateUser(userId);
		principal.setUpdateUser(userId);
		principal.setUpdateTime(now);
		principal.setCreateTime(now);
		boolean addFlag = refundService.insertRefundPrincipal(principal);
		if (addFlag) {
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}

	/**
	 * 更新退款负责人信息
	 * 
	 * @author hebiting
	 * @date 2018年10月23日下午2:52:54
	 * @param principal
	 * @return
	 */
	@PostMapping("/updatePrincipal")
	public ReturnDataUtil updateRefundPrincipal(@RequestBody PrincipalForm principalForm, HttpServletRequest request) {
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		String phoneCode = principalForm.getPhoneCode();
		String phone = refundService.getPrincipalInfoById(userId);
		String redisCode = redisClient.get("SmsKey:mobile" + phone);
		if (StringUtil.isBlank(phoneCode) || !phoneCode.equals(redisCode)) {
			return ResultUtil.error();
		}
		Date now = new Date();
		RefundPrincipalBean principal = principalForm.getPrincipal();
		principal.setUpdateUser(userId);
		principal.setUpdateTime(now);
		boolean updateFlag = refundService.updateRefundPrincipal(principal);
		if (updateFlag) {
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}

	/**
	 * 获取退款记录信息
	 * 
	 * @author hebiting
	 * @date 2018年10月23日下午3:35:15
	 * @param form
	 * @return
	 */
	@PostMapping("/getRefundInfo")
	public ReturnDataUtil getRefundInfo(@RequestBody(required = false) RefundRecordForm form,
			HttpServletRequest request) {
		if (form == null)
			form = new RefundRecordForm();
		if (form.getCompanyId() == null) {
			Integer companyId = (Integer) request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		List<RefundRecordBean> refundList = refundService.findRefundInfo(form);
		if (refundList != null && refundList.size() > 0) {
			Long total = refundService.findRefundInfoNum(form);
			return ResultUtil.success(refundList, form.getCurrentPage(), total);
		}
		return ResultUtil.selectError();
	}

	@PostMapping("/export")
	public void exportRefundInfo(@RequestBody(required = false) RefundRecordForm form, HttpServletRequest request,
			HttpServletResponse response) {
		if (form == null)
			form = new RefundRecordForm();
		if (form.getCompanyId() == null) {
			Integer companyId = (Integer) request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		String title = "退款记录";
		StringBuffer date = new StringBuffer("起始--至今");
		if (form.getStartTime() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime()));
		}
		if (form.getEndTime() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime()));
		}
		form.setIsShowAll(1);
		List<RefundRecordBean> refundList = refundService.findRefundInfo(form);
		String[] headers = new String[] { "id", "stateName", "typeName", "refundPlatformName", "outRefundNo",
				"platformNo", "payCode", "ptCode", "itemName", "price", "refundPrice", "reason", "createUserName","payTime",
				"createTime","remark" };
		String[] column = new String[] { "记录id", "退款状态", "订单产生类型", "退款平台", "系统内部退款号", "平台退款号", "系统订单号", "平台支付订单号",
				"退款商品名称", "订单总金额", "退款金额", "退款原因", "退款操作人", "订单支付时间","退款记录时间","备注" };
		// 设置导出日志的内容
		ExportLogBean bean = (ExportLogBean) request.getAttribute("exportBean");
		if (form.getStartTime() != null && form.getEndTime() != null) {
			// 导出日志内容按时间格式导出
			bean.setContent("用户: " + bean.getOperatorName() + " 导出" + DateUtil.formatYYYYMMDD(form.getStartTime())
					+ "--" + DateUtil.formatYYYYMMDD(form.getStartTime()) + "的退款记录的数据");
		} else {
			bean.setContent("用户: " + bean.getOperatorName() + " 导出退款记录--当前页的数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers, column, response, refundList, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取负责人信息
	 * 
	 * @author hebiting
	 * @date 2018年10月23日下午3:39:14
	 * @param form
	 * @return
	 */
	@PostMapping("/getPrincipalInfo")
	public ReturnDataUtil findRefundPrincipalInfo(@RequestBody(required = false) RefundPrincipalForm form,
			HttpServletRequest request) {
		if (form == null) {
			form = new RefundPrincipalForm();
		}
		if (form.getCompanyId() == null) {
			Integer companyId = (Integer) request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		List<RefundPrincipalBean> principalList = refundService.getRefundPrincipalInfo(form);
		if (principalList != null && principalList.size() > 0) {
			Long total = refundService.getRefundPrincipalNum(form);
			return ResultUtil.success(principalList, form.getCurrentPage(), total);
		}
		return ResultUtil.selectError();
	}

	/**
	 * 执行退款
	 * 
	 * @author hebiting
	 * @date 2018年10月25日下午3:16:52
	 * @param refundDto
	 * @param request
	 * @return
	 */
	@PostMapping("/do")
	public ReturnDataUtil refund(@RequestBody RefundDto refundDto, HttpServletRequest request) {
		log.info("<RefundController>-------<refund>-----start");
		if (StringUtil.isBlank(refundDto.getPayCode()) || refundDto.getOrderType() == null
				|| refundDto.getRefundPrice() == null || StringUtil.isBlank(refundDto.getPhoneCode())) {
			return ResultUtil.error(0, "参数有误", null);
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		RefundOrderInfo findOrder = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
		if (findOrder == null) {
			refundDto.setOrderType(6);
			findOrder = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
		}
		if (findOrder == null) {
			return ResultUtil.error(0, "订单不存在", null);
		}
		if (refundDto.getRefundPrice().compareTo(findOrder.getPrice()) > 0) {
			return ResultUtil.error(0, "输入金额大于本次订单金额，请重新输入！", null);
		}
		if (refundDto.getOrderType() == 1 || refundDto.getOrderType() == 6) {
			if(refundDto.getRefundNum()!=null) {
				if (refundDto.getRefundNum()>findOrder.getNum()) {
					return ResultUtil.error(0, "输入退款数量不能超过购买数量，请重新输入！", null);
				}
			}
		}
		if (refundDto.getOrderType() == 4) {
			CustomerBean bean = new CustomerBean();
			if (findOrder.getFriendCustomerId() != null && findOrder.getFriendCustomerId() > 0) {
				bean = customerService.findCustomerById(findOrder.getFriendCustomerId());
			} else {
				bean = customerService.findCustomerById(findOrder.getCustomerId());
			}
			// 得到本次充值金额 和退款金额 计算最终余额--退款金额
			BigDecimal price = findOrder.getPrice().subtract(refundDto.getRefundPrice());
			BigDecimal calculate = ArithmeticUtil.calculate(findOrder.getPrice())
					.subtract(ArithmeticUtil.calculate(price));
			// 得到本次实际应扣除用户余额
			BigDecimal sumPrice = refundDto.getRefundPrice().add(calculate);
			if (sumPrice.compareTo(bean.getUserBalance()) > 0) {
				BigDecimal result = ArithmeticUtil.result(findOrder.getPrice(), bean.getUserBalance());
				if (result.compareTo(bean.getUserBalance()) > 0) {
					result = findOrder.getPrice();
				}
				return ResultUtil.error(0, "用户余额不足，不支持本次退款!本次最大退款额度为" + result, null);
			}
		}
		Integer platform = findOrder.getPayType();
		refundDto.setPtCode(findOrder.getPtCode());
		refundDto.setPrice(findOrder.getPrice());
		String token = ((HttpServletRequest) request).getHeader("token");
		if (StringUtil.isBlank(token)) {
			token = request.getParameter("token");
		}
		if (StringUtil.isBlank(token)) {
			return ResultUtil.error();
		}
		refundDto.setAdminToken(token);
		ReturnDataUtil refund = null;
		if (platform == 1) {
			refund = refundClient.wxRefund(refundDto);
		} else if (platform == 2) {
			refund = refundClient.aliRefund(refundDto);
		} else {
			return ResultUtil.error();
		}
		if (ResultEnum.REFUND_SUCCESS.getCode().equals(Integer.valueOf(refund.getStatus()))) {
			refundService.updateRefundApplication(refundDto.getPayCode(), 2, "退款成功", userId);
		}
		log.info("<RefundController>-------<refund>-----end");
		return refund;
	}

	@PostMapping("/sendCode")
	public void refundSendCode(HttpServletRequest request) {
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		String phone = refundService.getPrincipalInfoById(userId);
		if (StringUtil.isNotBlank(phone)) {
			SmsClient.sendCode(phone);
		}
	}

	/**
	 * 获取所有退款申请信息
	 * 
	 * @author hebiting
	 * @date 2018年11月26日下午3:31:06
	 * @return
	 */
	@PostMapping("/getApplication")
	public ReturnDataUtil getRefundApplication(@RequestBody(required = false) RefundApplicationForm form,
			HttpServletRequest request) {
		if (form == null) {
			form = new RefundApplicationForm();
		}
		if (form.getCompanyId() == null) {
			Integer companyId = (Integer) request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		TotalResultBean<List<RefundApplicationDto>> result = refundService.findRefundApplication(form);
		List<RefundApplicationDto> result2 = result.getResult();
		Long total = result.getTotal();
		return ResultUtil.success(result2, form.getCurrentPage(), total);
	}

	/**
	 * 导出退款申请信息
	 * 
	 * @author hebiting
	 * @date 2018年11月28日上午8:57:54
	 * @param form
	 * @param request
	 * @param response
	 */
	@PostMapping("/exportApplication")
	public void exportApplication(@RequestBody(required = false) RefundApplicationForm form, HttpServletRequest request,
			HttpServletResponse response) {
		if (form == null) {
			form = new RefundApplicationForm();
		}
		if (form.getCompanyId() == null) {
			Integer companyId = (Integer) request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		TotalResultBean<List<RefundApplicationDto>> result = refundService.findRefundApplication(form);
		List<RefundApplicationDto> exportData = result.getResult();
		String title = "退款记录";
		StringBuffer date = new StringBuffer("起始--至今");
		if (form.getStartTime() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime()));
		}
		if (form.getEndTime() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime()));
		}
		String[] headers = new String[] { "id", "orderTypeName", "payCode", "ptCode", "phone", "reason", "stateName",
				"refundPrice", "createTime", "updateTime", "updateUserName" };
		String[] column = new String[] { "记录id", "订单类型", "内部订单号", "平台订单号", "手机号", "申请退款原因", "申请状态", "申请退款金额", "申请时间",
				"处理时间", "处理人" };
		// 设置导出日志的内容
		ExportLogBean bean = (ExportLogBean) request.getAttribute("exportBean");
		if (form.getStartTime() != null && form.getEndTime() != null) {
			// 导出日志内容按时间格式导出
			bean.setContent("用户: " + bean.getOperatorName() + " 导出" + DateUtil.formatYYYYMMDD(form.getStartTime())
					+ "--" + DateUtil.formatYYYYMMDD(form.getStartTime()) + "的退款记录的数据");
		} else {
			bean.setContent("用户: " + bean.getOperatorName() + " 导出退款记录--当前页的数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers, column, response, exportData, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 审判者的裁决之镰(审核退款申请)
	 * 
	 * @author hebiting
	 * @date 2018年11月27日下午2:33:21
	 * @param id
	 * @return
	 */
	@PostMapping("/justicalsDecide")
	public ReturnDataUtil refundJusticarsDecide(String payCode, Integer state, String backReason,
			HttpServletRequest request) {
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		boolean result = refundService.updateRefundApplication(payCode, state, backReason, userId);
		if (result) {
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}

	/**
	 * 用户自己发起 拼团退款
	 * 
	 * @author why
	 * @date 2019年1月17日 上午11:48:07
	 * @param refundDto
	 * @param request
	 * @return
	 */
	@PostMapping("/customerRefund")
	public ReturnDataUtil customerRefund(@RequestBody RefundDto refundDto, HttpServletRequest request) {
		log.info("<RefundController>----<customerRefund>----start");
		if (StringUtil.isBlank(refundDto.getPayCode()) || refundDto.getOrderType() == null
				|| refundDto.getRefundPrice() == null) {
			return ResultUtil.error(0, "参数有误", null);
		}
		RefundOrderInfo findOrder = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
		if (findOrder == null) {
			return ResultUtil.error(0, "订单不存在", null);
		}
		Integer platform = findOrder.getPayType();
		refundDto.setPtCode(findOrder.getPtCode());
		refundDto.setPrice(findOrder.getPrice());
		String token = ((HttpServletRequest) request).getHeader("token");
		if (StringUtil.isBlank(token)) {
			token = request.getParameter("token");
		}
		if (StringUtil.isBlank(token)) {
			return ResultUtil.error();
		}
		refundDto.setAdminToken(token);
		ReturnDataUtil refund = null;
		if (platform == 1) {// 微信支付 进行退款
			refund = refundClient.wxCustomerRefund(refundDto);
		} /*
			 * else if(platform == 2 ){ refund = refundClient.aliRefund(refundDto); }
			 */else {
			return ResultUtil.error();
		}
		log.info("<RefundController>----<customerRefund>----end");
		return refund;
	}

	/**
	 * 退款修改商品明细表
	 * @author hjc
	 * @date 2019年5月31日 上午11:48:07
	 * @param refundDto
	 * @param request
	 * @return
	 */
	@PostMapping("/adjustRefundGoods")
	public ReturnDataUtil adjustRefundGoods(@RequestBody RefundDto refundDto, HttpServletRequest request) {
		List<RefundItemDto> refundItemDtos=refundDto.getItemList();
		boolean result=refundService.updatePayRecordItem(refundDto);
		if(result) {
			return ResultUtil.success();
		}else {
			return ResultUtil.error();
		}
	}
}
