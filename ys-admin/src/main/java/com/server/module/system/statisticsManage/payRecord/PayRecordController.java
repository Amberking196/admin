
package com.server.module.system.statisticsManage.payRecord;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.common.utils.excel.ExportExcel;
import com.server.module.app.login.AppLoginInfoService;
import com.server.module.app.login.LoginInfoBean;
import com.server.module.customer.order.OrderService;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.baseManager.stateInfo.StateInfoDto;
import com.server.module.system.baseManager.stateInfo.StateInfoService;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.userManage.CustomerService;
import com.server.util.DateUtil;
import com.server.util.DateUtils;
import com.server.util.ExcelUtil;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.ItemTypeEnum;
import com.server.util.stateEnum.PayStateEnum;
import com.server.util.stateEnum.PayTypeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "PayRecordController", description = "PayRecordController")
@Controller
@RequestMapping("/payRecord")
public class PayRecordController {

	@Autowired
	PayRecordService payRecordService;
	@Autowired
	CompanyService companyManageService;
	@Autowired
	VendingMachinesInfoService machinesService;
	@Autowired
	StateInfoService stateInfoService;
	@Autowired
	CompanyService companyService;
	@Autowired
	CustomerService customerService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	private AppLoginInfoService loginInfoService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PayRecordDao payRecordDao;
	
	/**
	 * 查询销售记录
	 * 
	 * @param payRecordForm
	 * @return 返回查询状态，销售记录List<PayRecordDto>，及分页信息
	 */
	@RequestMapping(value = "/findPayRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil findPayRecord(@RequestBody(required = false) PayRecordForm payRecordForm,
			HttpServletRequest request) {
		if (payRecordForm == null) {
			payRecordForm = new PayRecordForm();
		}
		
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		payRecordForm.setAreaId(user.getAreaId());
		if (payRecordForm.getCompanyId() == null) {
			Integer companyId = user == null ? null : user.getCompanyId();
			payRecordForm.setCompanyId(companyId);
		}
	
		List<Integer> companyList = companyService.findAllSonCompanyId(payRecordForm.getCompanyId());
		String companyIds = StringUtils.join(companyList, ",");
		payRecordForm.setCompanyIds(companyIds);
		return payRecordService.findPayRecord(payRecordForm);
	}

	/**
	 * 查询视觉机器销售记录 orderType 2
	 * 
	 * @param payRecordForm
	 * @return 返回查询状态，销售记录List<PayRecordDto>，及分页信息
	 */
	@RequestMapping(value = "/findVisionPayRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil findVisionPayRecord(@RequestBody(required = false) PayRecordForm payRecordForm,
			HttpServletRequest request) {
		if (payRecordForm == null) {
			payRecordForm = new PayRecordForm();
		}
		if (payRecordForm.getCompanyId() == null) {
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user == null ? null : user.getCompanyId();
			payRecordForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payRecordForm.getCompanyId());
		String companyIds = StringUtils.join(companyList, ",");
		payRecordForm.setCompanyIds(companyIds);
		//payRecordForm.setOrderType(2);
		return payRecordService.findVisionPayRecord(payRecordForm);
	}
	
	
	@PostMapping("/exportVision")
	public void exportVisionPayRecord(@RequestBody(required = false) PayRecordForm payRecordForm,
			HttpServletResponse response, HttpServletRequest request) {
		if (payRecordForm == null) {
			payRecordForm = new PayRecordForm();
		}
		//payRecordForm.setOrderType(2);
		if (payRecordForm.getCompanyId() == null) {
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user == null ? null : user.getCompanyId();
			payRecordForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payRecordForm.getCompanyId());
		String companyIds = StringUtils.join(companyList, ",");
		payRecordForm.setCompanyIds(companyIds);
		payRecordForm.setIsShowAll(1);
		List<PayRecordDto> data  = payRecordDao.findVisionPayRecord(payRecordForm);
		for (PayRecordDto payRecordDto : data) {
			if(StringUtils.isNotBlank(payRecordDto.getItemName())) {
				//农夫山泉天然水5L*2,怡宝纯净水4.5L*2,怡宝纯净水4.5L*3
				//农夫山泉天然水5L*2,怡宝纯净水4.5L*2
				//农夫山泉天然水5L*2
				String[] strings=payRecordDto.getItemName().split("\\*");
				String a=strings[0];
				for(int i=2;i<strings.length;i++) {
					String[] b=strings[i-1].split(",");
					a=a+","+b[1];
				}
				payRecordDto.setItemName(a);
			}
			payRecordDto.setItemType(ItemTypeEnum.getItemTypeInfo(payRecordDto.getItemTypeId()));
			payRecordDto.setPayType(PayTypeEnum.getPayTypeInfo(payRecordDto.getPayTypeId()));
			payRecordDto.setState(PayStateEnum.findStateName(payRecordDto.getStateId()));
		}
		
		String title = "销售记录";
		StringBuffer date = new StringBuffer("起始--至今");
		if (payRecordForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()));
		}
		if (payRecordForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()));
		}
		if (payRecordForm.getIsShowAll() == 0) {
			date.append("--第" + payRecordForm.getCurrentPage() + "页");
		}
		// 设置导出日志的内容
		ExportLogBean bean = (ExportLogBean) request.getAttribute("exportBean");
		if (payRecordForm.getStartDate() != null && payRecordForm.getEndDate() != null) {
			// 导出日志内容按时间格式导出
			bean.setContent(
					"用户: " + bean.getOperatorName() + " 导出" + DateUtil.formatYYYYMMDD(payRecordForm.getStartDate())
							+ "--" + DateUtil.formatYYYYMMDD(payRecordForm.getStartDate()) + "的销售记录的数据");
		} else {
			bean.setContent("用户: " + bean.getOperatorName() + " 导出销售记录--当前页的数据");
		}
		// 修改导出手机号内容
		/*
		 * for (PayRecordDto payRecordDto : data) {
		 * payRecordDto.setPhone("***********"); }
		 */
		String[] headers = new String[] { "id", "companyName", "areaName", "locatoinName", "ptCode", "payCode", "phone",
				"loginUser","vendingMachinesCode", "wayNumber", "payType", "itemName", "price", "num", "totalPrice", "memberPay","realNum","realPrice",
				"refundNum", "refundPrice", "state", "createTime", "payTime", "refundTime", "remark" };
		String[] column = new String[] { "记录id", "公司名称", "区域名称", "机器地址", "内部订单号", "交易平台订单号", "手机号","备注","售卖机编码", "货道",
				"支付类型", "商品名称", "商品单价", "商品数量", "商品总价", "余额支付","实际数量","实际金额","退款商品数量", "退款金额", "状态", "订单创建时间", "订单支付时间", "订单退款时间",
				"订单描述"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询售货机开关门的记录
	 * 
	 * @param customerMachineForm
	 * @return 返回查询状态，开门记录
	 */
	@RequestMapping(value = "/findMachineHistory", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil findMachineHistory(@RequestBody(required = false) CustomerMachineForm customerMachineForm,
			HttpServletRequest request) {
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (customerMachineForm.getPayCode() != null) {
			return payRecordService.findMachineHistory(customerMachineForm);
		} else {
			returnDataUtil.setMessage("内部平台订单号不存在");
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(null);
			return returnDataUtil;
		}

	}

	/**
	 * 查询售货机开关门的记录
	 * 
	 * @param customerMachineForm
	 * @return 返回查询状态，开门记录
	 */
	@RequestMapping(value = "/findMachineHistoryVision", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil findMachineHistoryVision(@RequestBody(required = false) CustomerMachineForm customerMachineForm,
			HttpServletRequest request) {
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (customerMachineForm.getPayCode() != null) {
			return payRecordService.findMachineHistoryVision(customerMachineForm);
		} else {
			returnDataUtil.setMessage("内部平台订单号不存在");
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(null);
			return returnDataUtil;
		}

	}
	
	/**
	 * 查询支付状态
	 * 
	 * @return 支付状态List<StateInfoDto>
	 */
	@PostMapping("/findPayState")
	@ResponseBody
	public ReturnDataUtil findPayState() {
		ReturnDataUtil data = new ReturnDataUtil();
		List<StateInfoDto> stateList = stateInfoService.findStateInfoByKeyName("pay");
		if (stateList != null && stateList.size() > 0) {
			data.setStatus(1);
			data.setMessage("成功");
			data.setReturnObject(stateList);
		} else {
			data.setStatus(0);
			data.setMessage("失败");
		}
		return data;
	}

	/**
	 * 初始化查询所需信息
	 * 
	 * @return 返回结果状态，支付的订单状态List<StateInfoDto>，支付类型Map<Object,Object>
	 */
	@PostMapping("/initInfo")
	@ResponseBody
	public ReturnDataUtil initInfo() {
		ReturnDataUtil data = new ReturnDataUtil();
		List<StateInfoDto> stateList = stateInfoService.findStateInfoByKeyName("pay");
		Map<String, Object> returnData = new HashMap<String, Object>();
		Map<Object, Object> payType = new HashMap<Object, Object>();
		for (PayTypeEnum payEnum : PayTypeEnum.values()) {
			payType.put(payEnum.getIndex(), payEnum.getPayType());
		}
		returnData.put("payTypeList", payType);
		returnData.put("stateList", stateList);
		if (returnData != null && returnData.size() > 0) {
			data.setStatus(1);
			data.setMessage("初始化成功");
			data.setReturnObject(returnData);
		} else {
			data.setStatus(0);
			data.setMessage("初始化失败");
		}
		return data;
	}

	@PostMapping("/export")
	public void exportPayRecord(@RequestBody(required = false) PayRecordForm payRecordForm,
			HttpServletResponse response, HttpServletRequest request) throws ParseException {
		if (payRecordForm == null) {
			payRecordForm = new PayRecordForm();
		}		
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		payRecordForm.setAreaId(user.getAreaId());
		if (payRecordForm.getCompanyId() == null) {
			Integer companyId = user == null ? null : user.getCompanyId();
			payRecordForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payRecordForm.getCompanyId());
		String companyIds = StringUtils.join(companyList, ",");
		payRecordForm.setCompanyIds(companyIds);
		payRecordForm.setIsShowAll(1);
		List<PayRecordDto> data  = payRecordDao.findPayRecord(payRecordForm);
		for (PayRecordDto payRecordDto : data) {
			if(StringUtils.isNotBlank(payRecordDto.getItemName())) {
				String[] strings=payRecordDto.getItemName().split("\\*");
				payRecordDto.setItemName(strings[0]);
			}
			payRecordDto.setItemType(ItemTypeEnum.getItemTypeInfo(payRecordDto.getItemTypeId()));
			payRecordDto.setPayType(PayTypeEnum.getPayTypeInfo(payRecordDto.getPayTypeId()));
			payRecordDto.setState(PayStateEnum.findStateName(payRecordDto.getStateId()));
		}
		
		String title = "销售记录";
		StringBuffer date = new StringBuffer("起始--至今");
		if (payRecordForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()));
		}
		if (payRecordForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()));
		}
		if (payRecordForm.getIsShowAll() == 0) {
			date.append("--第" + payRecordForm.getCurrentPage() + "页");
		}
		// 设置导出日志的内容
		ExportLogBean bean = (ExportLogBean) request.getAttribute("exportBean");
		if (payRecordForm.getStartDate() != null && payRecordForm.getEndDate() != null) {
			// 导出日志内容按时间格式导出
			bean.setContent(
					"用户: " + bean.getOperatorName() + " 导出" + DateUtil.formatYYYYMMDD(payRecordForm.getStartDate())
							+ "--" + DateUtil.formatYYYYMMDD(payRecordForm.getStartDate()) + "的销售记录的数据");
		} else {
			bean.setContent("用户: " + bean.getOperatorName() + " 导出销售记录--当前页的数据");
		}
		// 修改导出手机号内容
		/*
		 * for (PayRecordDto payRecordDto : data) {
		 * payRecordDto.setPhone("***********"); }
		 */
		String[] headers = new String[] { "id", "companyName", "areaName", "locatoinName", "payCode", "ptCode", "phone",
				"loginUser","vendingMachinesCode", "wayNumber", "payType", "itemName", "price", "num", "totalPrice", "memberPay","realNum","realPrice",
				"refundNum", "refundPrice", "state", "createTime", "payTime", "refundTime", "remark" };
		String[] column = new String[] { "记录id", "公司名称", "区域名称", "机器地址", "内部订单号", "交易平台订单号", "手机号","备注","售卖机编码", "货道",
				"支付类型", "商品名称", "商品单价", "商品数量", "商品总价", "余额支付","实际数量","实际金额","退款商品数量", "退款金额", "状态", "订单创建时间", "订单支付时间", "订单退款时间",
				"订单描述"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GetMapping("/exportAll")
	public void exportAllPayRecord(HttpServletResponse response, HttpServletRequest request) {
		PayRecordForm payRecordForm = new PayRecordForm();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();

		Integer companyId = user == null ? null : user.getCompanyId();
		List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
		String companyIds = StringUtils.join(companyList, ",");
		payRecordForm.setCompanyIds(companyIds);
		payRecordForm.setIsShowAll(1);
		ReturnDataUtil returnData = payRecordService.findPayRecordForExport(payRecordForm);
		List<PayRecordDto> data = (List<PayRecordDto>) returnData.getReturnObject();
		// 设置导出日志的内容
		ExportLogBean bean = (ExportLogBean) request.getAttribute("exportBean");
		if (payRecordForm.getStartDate() != null && payRecordForm.getEndDate() != null) {
			// 导出日志内容按时间格式导出
			bean.setContent(
					"用户: " + bean.getOperatorName() + " 导出" + DateUtil.formatYYYYMMDD(payRecordForm.getStartDate())
							+ "--" + DateUtil.formatYYYYMMDD(payRecordForm.getStartDate()) + "的销售记录的数据");
		} else {
			bean.setContent("用户: " + bean.getOperatorName() + " 导出销售记录--全部的数据");
		}
		// 修改导出手机号内容
		/*
		 * for (PayRecordDto payRecordDto : data) {
		 * payRecordDto.setPhone("***********"); }
		 */
		String title = "销售记录";
		String[] headers = new String[] { "id", "companyName", "areaName", "locatoinName", "ptCode", "payCode", "phone",
				"vendingMachinesCode", "wayNumber", "payType", "itemName", "price", "num", "totalPrice", "memberPay",
				"refundNum", "refundPrice", "state", "createTime", "payTime", "refundTime", "remark" };
		String[] column = new String[] { "记录id", "公司名称", "区域名称", "机器地址", "内部订单号", "交易平台订单号", "手机号", "售卖机编码", "货道",
				"支付类型", "商品名称", "商品单价", "商品数量", "商品总价", "余额支付", "退款商品数量", "退款金额", "状态", "订单创建时间", "订单支付时间", "订单退款时间",
				"订单描述" };
		try {
			ExcelUtil.exportExcel(title, headers, column, response, data, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/deletePayRecord")
	@ResponseBody
	public ReturnDataUtil deletePayRecord(@RequestBody List<Integer> payRecordIds) {
		return payRecordService.deletePayRecord(payRecordIds);
	}

	@PostMapping("/updateOrderState")
	@ResponseBody
	public ReturnDataUtil updateOrderState(@RequestBody Map<String, Integer> param) {
		return payRecordService.updateOrderState(param.get("state"), param.get("id"),param.get("orderType"));
	}

	@GetMapping("/listSellNumStatisticsPage")
	@ResponseBody
	public ReturnDataUtil listSellNumStatisticsPage(PayRecordForm1 condition) {

		return payRecordService.listSellNumStatisticsPage(condition);
	}

	@GetMapping("/payBefore7Day")
	@ResponseBody
	public ReturnDataUtil payBefore7Day(HttpServletRequest request, String companyId) {
		Long id = request.getAttribute(AdminConstant.LOGIN_USER_ID) == null ? null
				: Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		LoginInfoBean loginUser = loginInfoService.queryById(id);
		Map<String, Object> map = payRecordService.payBefore7Day(companyId);
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (loginUser.getLevel() == 1 || loginUser.getLevel() == 2) {
			returnData.setStatus(1);
			returnData.setMessage("成功");
			returnData.setReturnObject(map);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("没有权限");
			returnData.setReturnObject(0);
		}
		return returnData;
	}

	@ApiOperation(value = "销售数量统计导出", notes = "销售数量统计导出")
	@PostMapping("/listSellNumStatisticsPageExport")
	public void listSellNumStatisticsPageExport(@RequestBody(required = false) PayRecordForm1 condition,
			HttpServletRequest request, HttpServletResponse response) {
		if (condition == null) {
			condition = new PayRecordForm1();
		}
		ReturnDataUtil returnData = payRecordService.listSellNumStatisticsPage(condition);
		List<PayRecordBean> data = (List<PayRecordBean>) returnData.getReturnObject();
		StringBuffer date = new StringBuffer("起始--至今");
		if (condition.getStartTime() != null) {
			date.replace(0, 2, condition.getStartTime());
		}
		if (condition.getEndTime() != null) {
			date.replace(date.length() - 2, date.length(), condition.getEndTime());
		}
		if (condition.getIsShowAll() == 0) {
			date.append("--第" + condition.getCurrentPage() + "页");
		}
		String title = "销售数量统计列表";
		String[] headers = new String[] { "countNum", "itemName", "vendingMachinesCode", "count", "num" };
		String[] column = new String[] { "序号", "商品名称", "机器编号", "销售次数", "销售数量" };
		try {
			// 添加导出日志的内容
			ExportLogBean bean = (ExportLogBean) request.getAttribute("exportBean");
			bean.setContent("用户: " + bean.getOperatorName() + " 导出" + condition.getStartTime() + "--"
					+ condition.getEndTime() + "销售数量统计列表的数据");
			ExcelUtil.exportExcel(title, headers, column, response, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/payRecordDetail")
	@ResponseBody
	public ReturnDataUtil payRecordDetail(@RequestBody PayRecordForm payRecordForm) {
		ReturnDataUtil returnData = payRecordService.payRecordDetail(payRecordForm);
		return returnData;
	}

	@ApiOperation(value = "销售数量统计", notes = "listSaleNumPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/listSaleNumPage")
	@ResponseBody
	public ReturnDataUtil listSaleNumPage(ListSaleNumCondition condition,HttpServletRequest request) {
		condition.setPageSize(16);
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		condition.setAreaId(user.getAreaId());
		ReturnDataUtil returnData = payRecordService.listSaleNum(condition);
		return returnData;
	}

	@ApiOperation(value = "货道销售数量统计导出", notes = "exportSaleNum", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/exportSaleNum")
	@ResponseBody
	public void exportSaleNum(ListSaleNumCondition condition, HttpServletResponse response,
			HttpServletRequest request) {
		condition.setPageSize(6000);
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		condition.setAreaId(user.getAreaId());
		ReturnDataUtil returnData = payRecordService.listSaleNum(condition);
		List<SaleNumDTO> list = (List<SaleNumDTO>) returnData.getReturnObject();

		try {
			String fileName = "货道销售数量统计" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			ExportLogBean bean = (ExportLogBean) request.getAttribute("exportBean");
			bean.setContent("用户: " + bean.getOperatorName() + " 导出" + condition.getStartTime() + "--"
					+ condition.getEndTime() + "货道销售统计的数据");
			new ExportExcel("货道销售数量统计", SaleNumDTO.class).setDataList1(list).write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/getPayRecordItemList")
	@ResponseBody
	public ReturnDataUtil getPayRecordItemList(String payCode, Integer orderType) {
		List<PayRecordItemDto> payRecordItemList = null;
		if (orderType == 1) {
			payRecordItemList = payRecordService.getPayRecordItemList(payCode);
		} else {
			payRecordItemList = orderService.getPayRecordItemList(payCode);
		}
		return ResultUtil.success(payRecordItemList);
	}
}
