package com.server.module.system.statisticsManage.payRecordPerDay;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;


@Controller
@RequestMapping("/payRecordPerDay")
public class PayRecordPerDayController {

	@Autowired
	PayRecordPerDayService payRecordPerDayService;
	@Autowired
	CompanyService companyService;
	@Autowired
	AdminUserService adminUserService;
	/**
	 * 查询日销售记录
	 * @param payDayForm
	 * @return 返回日销售记录List<PayRecordPerDayDto> ，分页信息，查询状态
	 */
	@RequestMapping(value="/findPayRecordPerDay",method=RequestMethod.POST)
	@ResponseBody
	public ReturnDataUtil findPayRecordPerDay(@RequestBody(required=false) PayRecordPerDayForm payDayForm,HttpServletRequest request){
		if(payDayForm==null){
			payDayForm = new PayRecordPerDayForm();
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.DATE,-1);
//			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
//					calendar.get(Calendar.DATE), 0, 0, 0);
//			Date startDate = calendar.getTime();
//			payDayForm.setStartDate(startDate);
//			payDayForm.setEndDate(startDate);
		}
		
		
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		payDayForm.setAreaId(user.getAreaId());
		if(user.getLevel()==2) {
			payDayForm.setLevel(String.valueOf(user.getLevel()));
		}
		if(payDayForm.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			payDayForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payDayForm.setCompanyIds(companyIds);
		return payRecordPerDayService.findPayRecordPerDay(payDayForm);
	}
	
	/**
	 * 初始化日销售查询所需信息
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
	
	/**
	 * 将日销售记录导出excel文件
	 * @param payDayForm
	 * @param response
	 */
	@GetMapping(value="/export")
	public void exportPayRecordPerDay(PayRecordPerDayForm payDayForm,HttpServletResponse response,HttpServletRequest request){
		if(payDayForm==null){
			payDayForm = new PayRecordPerDayForm();
		}
		String title = "销售日报";

		StringBuffer date = new StringBuffer("起始--至今");
		if (payDayForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(payDayForm.getStartDate()));
		}
		if (payDayForm.getEndDate() != null) {
		    DateTime dt = new DateTime(payDayForm.getEndDate().getTime());
		    dt=dt.withHourOfDay(23);
		    dt=dt.withMinuteOfHour(59);
		    dt=dt.withSecondOfMinute(59);
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(dt.toDate()));
		}
		if(payDayForm.getIsShowAll()==0){
			date.append("--第"+payDayForm.getCurrentPage()+"页");
		}
		if(payDayForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payDayForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payDayForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payDayForm.setCompanyIds(companyIds);
		List<PayRecordPerDayDto> payDayList = payRecordPerDayService.exportPayRecordPerDay(payDayForm);
		//设置导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(payDayForm.getStartDate()!=null&&payDayForm.getEndDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(payDayForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(payDayForm.getEndDate())+"的销售日报的数据");
		}else if(payDayForm.getIsShowAll()==0){
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售日报--当前页数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售日报--全部数据");
		}
		String[]  headers = new String[]{"createTime","companyName","vmCode","finishedMoney",
				"finishedItemNum","finishedOrderNum","refundMoney","refundOrderNum","normalNum","averageAll","machinesNum","average"};
		String[] column = new String[]{"日期","公司名称","售卖机编码","销售金额",
				"销售商品数","销售订单数","退款金额","退款订单数","机器总数","单台机平均销售数","销售机器数","销售机平均销售数"};
		try {
			ExcelUtil.exportExcel(title, headers,column, response, payDayList, date.toString());
//			new ExportExcel("销售日报", PayRecordPerDayDto.class).setDataList(payDayList).write(response, "销售日报").dispose();
		}catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将日销售记录导出excel文件
	 * @param payDayForm
	 * @param response
	 */
	@GetMapping(value="/exportAll")
	public void exportAllPayRecordPerDay(HttpServletResponse response,HttpServletRequest request){
		String title="销售日报";
		PayRecordPerDayForm payDayForm = new PayRecordPerDayForm();
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
		String companyIds =  StringUtils.join(companyList, ",");
		payDayForm.setCompanyIds(companyIds);
		payDayForm.setIsShowAll(1);
		List<PayRecordPerDayDto> payDayList = payRecordPerDayService.exportPayRecordPerDay(payDayForm);
		String[]  headers = new String[]{"createTime","companyName","vmCode","companyId","finishedMoney",
				"finishedItemNum","finishedOrderNum","refundMoney","refundItemNum","refundOrderNum","normalNum","machinesNum","average"};
		String[] column = new String[]{"日期","公司名称","售卖机编码","公司id","销售金额",
				"销售商品数","销售订单数","退款金额","退款商品数","退款订单数","机器总数","销售机器数","单台机平均销售数"};
		//设置导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(payDayForm.getStartDate()!=null&&payDayForm.getEndDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(payDayForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(payDayForm.getEndDate())+"的销售日报的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售日报--全部的数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,column, response, payDayList, null);
//			new ExportExcel("销售日报", PayRecordPerDayDto.class).setDataList(payDayList).write(response, "销售日报").dispose();
		}catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
