package com.server.module.system.statisticsManage.payRecordPerMonth;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.statisticsManage.payRecordPerMonth.PayRecordPerMonthBean;
import com.server.module.system.statisticsManage.payRecordPerMonth.PayRecordPerMonthController;
import com.server.module.system.statisticsManage.payRecordPerMonth.PayRecordPerMonthForm;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/payRecordPerMonth")
public class PayRecordPerMonthController {

	public static Logger log = LogManager.getLogger(PayRecordPerMonthController.class);
	@Autowired
	PayRecordPerMonthService payRecordPerMonthService;
	@Autowired
	CompanyService companyService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	
	/**
	 * 查询月销售记录
	 * @param payMonthForm
	 * @return 返回月销售记录集合List<PayRecordPerMonthDto>，分页信息，查询状态
	 */
	@ApiOperation(value = "每月商品销售统计列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findPayRecordPerMonth(@RequestBody(required=false) PayRecordPerMonthForm payMonthForm,HttpServletRequest request){
		log.info("<PayRecordPerMonthController>--<listPage>--start");
		if(payMonthForm==null){
			payMonthForm = new PayRecordPerMonthForm();
		}
		if(payMonthForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user == null? null:user.getCompanyId();
			payMonthForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payMonthForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payMonthForm.setCompanyIds(companyIds);
		returnDataUtil=payRecordPerMonthService.findPayRecordPerMonth(payMonthForm);
		log.info("<PayRecordPerMonthController>--<listPage>--end");
		return returnDataUtil;
	}
	
	/**
	 * 初始化月销售记录信息
	 * @return 返回公司信息List<CompanyBean>，初始化状态
	 */
	@RequestMapping("/initInfo")
	@ResponseBody
	public ReturnDataUtil initInfo(HttpServletRequest request){
		ReturnDataUtil data = new ReturnDataUtil();
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
		if(companyList!=null && companyList.size()>0){
			data.setStatus(1);
			data.setMessage("初始化成功");
			data.setReturnObject(companyList);
		}else{
			data.setStatus(0);
			data.setMessage("初始化失败");
		}
		return data;
	}
	
	@ApiOperation(value = "导出", notes = "导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/export")
	public void export(HttpServletResponse response,HttpServletRequest request, PayRecordPerMonthForm payMonthForm) {
		log.info("<PayRecordPerMonthController>------<export>-----start");
		if(payMonthForm==null){
			payMonthForm = new PayRecordPerMonthForm();
		}
		StringBuffer date = new StringBuffer("");
		if (payMonthForm.getStartDate() != null) {
			date.append(DateUtil.formatYYYYMMDD(payMonthForm.getStartDate()).substring(0,7));
		}
		if(payMonthForm.getIsShowAll()==0){
			date.append(" 第"+payMonthForm.getCurrentPage()+"页");
		}
		if(payMonthForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user == null? null:user.getCompanyId();
			payMonthForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payMonthForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payMonthForm.setCompanyIds(companyIds);
		ReturnDataUtil returnData = payRecordPerMonthService.findPayRecordPerMonth(payMonthForm);
		List<PayRecordPerMonthBean> data = (List<PayRecordPerMonthBean>) returnData.getReturnObject();
		String title = "销售月报";
		String[] headers = new String[] {"reportDate","companyName","num"};
		String[] column = new String[] {"日期","公司名称","销售数量"};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(payMonthForm.getStartDate()!=null) {
			//导出日志按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(payMonthForm.getStartDate())+"的销售月报的数据");
		}else if(payMonthForm.getIsShowAll()==0) {//导出当期页
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售月报--当前页数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售月报--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,column, response, data, date.toString());
			log.info("<PayRecordPerMonthController>------<export>-----end");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException 
				| IOException e) {
			e.printStackTrace();
		}
	}
}
