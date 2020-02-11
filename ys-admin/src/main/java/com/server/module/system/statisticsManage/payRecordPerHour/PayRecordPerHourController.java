package com.server.module.system.statisticsManage.payRecordPerHour;

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
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayDto;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayService;
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
 * create time: 2018-07-13 09:23:01
 */ 
@Api(value ="PayRecordPerHourController",description="商品销售统计")
@RestController
@RequestMapping("/payRecordPerHour")
public class  PayRecordPerHourController{

	public static Logger log = LogManager.getLogger(PayRecordPerHourController.class);
	@Autowired
	private PayRecordPerHourService payRecordPerHourServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	CompanyService companyService;
	
	@ApiOperation(value = "每小时商品销售统计列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false)PayRecordPerHourForm payHourForm,HttpServletRequest request){
		log.info("<PayRecordPerHourController>--<listPage>--start");
		if(payHourForm==null){
			payHourForm = new PayRecordPerHourForm();
		}
		if(payHourForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payHourForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payHourForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payHourForm.setCompanyIds(companyIds);
		returnDataUtil=payRecordPerHourServiceImpl.listPage(payHourForm);
		log.info("<PayRecordPerHourController>--<listPage>--end");
		return returnDataUtil;
	}

	/**
	 * 初始时销售查询所需信息
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
	
	@SuppressWarnings("unchecked")
	@GetMapping(value="/export")
	public void export(PayRecordPerHourForm payHourForm,HttpServletResponse response,HttpServletRequest request){
		log.info("<PayRecordPerHourController>--<export>--start");

		if(payHourForm==null){
			payHourForm = new PayRecordPerHourForm();
		}
		String title = "销售时报";
		StringBuffer date = new StringBuffer("起始--至今");
		if (payHourForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDD(payHourForm.getStartDate()));
		}
		if (payHourForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDD(payHourForm.getEndDate()));
		}
		if(payHourForm.getIsShowAll()==0){
			date.append("--第"+payHourForm.getCurrentPage()+"页");
		}
		if(payHourForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			payHourForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(payHourForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		payHourForm.setCompanyIds(companyIds);
		ReturnDataUtil returnDataUtil = payRecordPerHourServiceImpl.listPage(payHourForm);
		List<PayRecordPerHourBean> payHourList=(List<PayRecordPerHourBean>) returnDataUtil.getReturnObject();
		String[]  headers = new String[]{"reportDate","companyName","zero","one",
				"two","three","four","five","six","seven","eight",
				"nine","ten","eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen","twenty","twentyone","twentytwo","twentythree"
		};
		String[] column = new String[]{"日期","公司名称","0点","1点",
				"2点","3点","4点","5点","6点","7点","8点",
				"9点","10点","11点","12点","13点","14点","15点","16点","17点","18点","19点","20点","21点","22点","23点"
		};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(payHourForm.getStartDate()!=null&&payHourForm.getStartDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(payHourForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(payHourForm.getStartDate())+"的销售时报的数据");
		}else if(payHourForm.getIsShowAll()==0) {//导出当前页
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售时报--当前页数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出销售时报--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,column, response, payHourList, date.toString());
			log.info("<PayRecordPerHourController>--<export>--end");
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

