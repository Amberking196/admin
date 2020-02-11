package com.server.module.system.statisticsManage.payRecordPerWeek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.io.IOException;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.statisticsManage.payRecordPerHour.PayRecordPerHourForm;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
/**
 * author name: hjc
 * create time: 2018-07-14 14:38:10
 */ 
@Api(value ="PayRecordPerWeekController",description="每周商品销售统计")
@RestController
@RequestMapping("/payRecordPerWeek")
public class  PayRecordPerWeekController{

	public static Logger log = LogManager.getLogger(PayRecordPerWeekController.class);
	@Autowired
	private PayRecordPerWeekService payRecordPerWeekServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	CompanyService companyService;
	
	@ApiOperation(value = "每周商品销售统计列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false)PayRecordPerWeekForm payWeekForm,HttpServletRequest request){
		log.info("<PayRecordPerWeekController>--<listPage>--start");
		if(payWeekForm==null){
			payWeekForm = new PayRecordPerWeekForm();
		}
		if(payWeekForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payWeekForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payWeekForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payWeekForm.setCompanyIds(companyIds);
		returnDataUtil=payRecordPerWeekServiceImpl.listPage(payWeekForm);
		log.info("<PayRecordPerWeekController>--<listPage>--end");
		return returnDataUtil;
	}

	/**
	 * 初始化周销售查询所需信息
	 * @return 返回公司信息List<CmpanyBean>，初始化状态
	 */
	@ApiOperation(value = "公司初始化",notes = "initInfo",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/initInfo", produces = "application/json;charset=UTF-8")
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
	public void export(HttpServletResponse response,HttpServletRequest request, PayRecordPerWeekForm payWeekForm) {
		log.info("<PayRecordPerWeekController>------<export>-----start");
		if(payWeekForm==null){
			payWeekForm = new PayRecordPerWeekForm();
		}
		StringBuffer date = new StringBuffer("");
//		if (payWeekForm.getStartDate() != null) {
//			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(payWeekForm.getStartDate()));
//		}
//		if (payWeekForm.getEndDate() != null) {
//			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(payHourForm.getEndDate()));
//		}
		if(payWeekForm.getIsShowAll()==0){
			date.append("第"+payWeekForm.getCurrentPage()+"页");
		}
		if(payWeekForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payWeekForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payWeekForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payWeekForm.setCompanyIds(companyIds);
		ReturnDataUtil returnData = payRecordPerWeekServiceImpl.listPage(payWeekForm);
		List<PayRecordPerWeekBean> data = (List<PayRecordPerWeekBean>) returnData.getReturnObject();
		String title = "销售周报";
		String[] headers = new String[] {"reportDate","companyName","num"};
		String[] column = new String[] {"日期","公司名称","销售数量"};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(payWeekForm.getStartDate()!=null) {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(payWeekForm.getStartDate())+"的销售周报的数据");
		}else if(payWeekForm.getIsShowAll()==0) {//导出当期页
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售周报--当前页数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售周报--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,column, response, data, date.toString());
			log.info("<PayRecordPerWeekController>------<export>-----end");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException 
				| IOException e) {
			e.printStackTrace();
		}
	}
}

