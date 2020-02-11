package com.server.module.system.statisticsManage.userState;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.statisticsManage.chart.ChartForm;
import com.server.module.system.statisticsManage.chart.ChartUtil;
import com.server.module.system.statisticsManage.chart.DateCountVo;
import com.server.module.system.statisticsManage.chart.Grouping;
import com.server.module.system.statisticsManage.chart.ResponseData;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayDto;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayService;
import com.server.module.system.statisticsManage.userAction.SumUserAction;
import com.server.module.system.statisticsManage.userActiveDegree.UserActiveDegreeDaoImpl;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

import net.bytebuddy.asm.Advice.Return;


@Controller
@RequestMapping("/userState")
public class UserStateController {


	@Autowired
	CompanyService companyService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	UserStateService userStateService;
	
	private final static Logger log = LogManager.getLogger(UserActiveDegreeDaoImpl.class);
	/**
	 * 用户状态对比
	 */
	@RequestMapping(value="/userStateNum",method=RequestMethod.POST)
	@ResponseBody
	public ReturnDataUtil userStateNum(@RequestBody(required=false) PayRecordPerDayForm payDayForm,HttpServletRequest request){
		if(payDayForm==null){
			payDayForm = new PayRecordPerDayForm();
		}
//		if(payDayForm.getCompanyId()==null){
//			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
//			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
//			Integer companyId = user==null?null:user.getCompanyId();
//			payDayForm.setCompanyId(companyId);
//		}
		if(payDayForm.getCompanyId()!=null){
			List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			payDayForm.setCompanyIds(companyIds);
		}
//		List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
//		String companyIds =  StringUtils.join(companyList, ",");
//		payDayForm.setCompanyIds(null);
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		returnDataUtil.setReturnObject(userStateService.userStateNum2(payDayForm));
		return returnDataUtil;
		
	}
	
	/**
	 * 用户状态日统计
	 */
	@RequestMapping(value="/userStateNumChart",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> userStateNumChart(@RequestBody(required=false) PayRecordPerDayForm payDayForm,HttpServletRequest request){
		if(payDayForm==null){
			payDayForm = new PayRecordPerDayForm();
		}
		if(payDayForm.getCompanyId()!=null){
			List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			payDayForm.setCompanyIds(companyIds);
		}

		return userStateService.userStateNumChart(payDayForm);
	}
	
	/**
	 * 用户状态对比导出
	 */
	@GetMapping(value = "/userStateNumExport", produces = "application/json;charset=UTF-8")
	public void userStateNumExport(PayRecordPerDayForm payDayForm,HttpServletRequest request,HttpServletResponse response){
		if(payDayForm==null) {
			payDayForm=new PayRecordPerDayForm();
			
		}
		if(payDayForm.getCompanyId()!=null){
			List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			payDayForm.setCompanyIds(companyIds);
		}
		List<UserStateVo> list=userStateService.userStateNum2(payDayForm);
		
		String title = "用户状态对比导出";
		//设置导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		bean.setContent("用户: "+bean.getOperatorName()+" 导出用户状态对比的数据");
		String[] headers = new String[] { "date","sumNum","registerNum","one","two","three",
				"four","five", "six"};
		String[] column = new String[] { "日期","总用户数","新用户数","一次","活跃","忠实","低频","流失",
				"回流"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, list, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 用户状态日统计
	 */
	@GetMapping(value="/userStateNumChartExport",produces = "application/json;charset=UTF-8")
	public void userStateNumChartExport(PayRecordPerDayForm payDayForm,HttpServletRequest request,HttpServletResponse response){
		if(payDayForm==null) {
			payDayForm=new PayRecordPerDayForm();
		}
		if(payDayForm.getCompanyId()!=null){
			List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			payDayForm.setCompanyIds(companyIds);
		}
		StringBuffer date = new StringBuffer("起始--至今");
		if (payDayForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDD(payDayForm.getStartDate()));
		}
		if (payDayForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDD(payDayForm.getEndDate()));
		}
		Map<String,Object> map=userStateService.userStateNumChart(payDayForm);
		List<UserStateBean> list=(List<UserStateBean>) map.get("list");
		String title = "用户状态日统计导出";
		//设置导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(payDayForm.getStartDate()!=null&&payDayForm.getEndDate()!=null) {
			//导出日志内容按时间格式导出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(payDayForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(payDayForm.getEndDate())+"的用户状态日统计的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出用户状态日统计的数据");
		}
		String[] headers = new String[] { "date","sumNum","registerNum","one","two","three",
				"four","five", "six"};
		String[] column = new String[] { "日期","总用户数","新用户数","一次","活跃","忠实","低频","流失",
				"回流"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, list, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
}
