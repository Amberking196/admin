package com.server.module.system.replenishManage.machinesReplenishStatement;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-04-24 11:53:01
 */
@Api(value = "ReplenishmentReportController", description = "补货报表")
@RestController
@RequestMapping("/ReplenishmentReport")
public class ReplenishmentReportController {

	private static Logger log=LogManager.getLogger();
	@Autowired
	private ReplenishmentReportService replenishmentReportServiceImpl;
	@Autowired
	AdminUserService adminUserService;
	@ApiOperation(value = "补货报表列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) ReplenishmentReportForm replenishmentReportForm,HttpServletRequest request) {
		log.info("<ReplenishmentReportController>------<listPage>-------start");
		if(replenishmentReportForm==null) {
			replenishmentReportForm=new ReplenishmentReportForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user.getAreaId()!=null && user.getAreaId()>0) {
			replenishmentReportForm.setAreaId(user.getAreaId());
		}
		if(replenishmentReportForm.getStartDate()==null){
			Date date=new Date();//取时间
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.DATE,-3);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
			date=calendar.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			replenishmentReportForm.setStartDate(date);
		}
		if(replenishmentReportForm.getEndDate()==null){
			replenishmentReportForm.setEndDate(new Date());
		}
		ReturnDataUtil returnDataUtil = replenishmentReportServiceImpl.listPage(replenishmentReportForm);
		log.info("<ReplenishmentReportController>------<listPage>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "视觉补货报表列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/visionListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil visionListPage(@RequestBody(required=false) ReplenishmentReportForm replenishmentReportForm) {
		log.info("<ReplenishmentReportController>------<visionListPage>-------start");
		if(replenishmentReportForm==null) {
			replenishmentReportForm=new ReplenishmentReportForm();
		}
		ReturnDataUtil returnDataUtil = replenishmentReportServiceImpl.visionListPage(replenishmentReportForm);
		log.info("<ReplenishmentReportController>------<visionListPage>-------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "换货报表列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/changeListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil changeListPage(@RequestBody(required=false) ReplenishmentReportForm replenishmentReportForm,HttpServletRequest request) {
		log.info("<ReplenishmentReportController>------<changeListPage>-------start");
		if(replenishmentReportForm==null) {
			replenishmentReportForm=new ReplenishmentReportForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user.getAreaId()!=null && user.getAreaId()>0) {
			replenishmentReportForm.setAreaId(user.getAreaId());
		}
		ReturnDataUtil returnDataUtil = replenishmentReportServiceImpl.changeListPage(replenishmentReportForm);
		log.info("<ReplenishmentReportController>------<changeListPage>-------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "补货人员列表", notes = "userListPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/userListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil userListPage(@RequestBody(required=false) ReplenishmentReportForm replenishmentReportForm,HttpServletRequest request) {
		log.info("<ReplenishmentReportController>------<userListPage>-------start");
		if(replenishmentReportForm==null) {
			replenishmentReportForm=new ReplenishmentReportForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user.getAreaId()!=null && user.getAreaId()>0) {
			replenishmentReportForm.setAreaId(user.getAreaId());
		}
		ReturnDataUtil returnDataUtil = replenishmentReportServiceImpl.userListPage(replenishmentReportForm);
		log.info("<ReplenishmentReportController>------<userListPage>-------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "补货人员详情表", notes = "userListPageDetail", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/userListPageDetail", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil userListPageDetail(@RequestBody(required=false) ReplenishmentReportForm replenishmentReportForm) {
		log.info("<ReplenishmentReportController>------<userListPageDetail>-------start");
		if(replenishmentReportForm==null) {
			replenishmentReportForm=new ReplenishmentReportForm();

		}
		ReturnDataUtil returnDataUtil = replenishmentReportServiceImpl.userListPageDetail(replenishmentReportForm);
		log.info("<ReplenishmentReportController>------<userListPageDetail>-------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "补货人员全部导出", notes = "补货人员全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportUserAll")
    public void exportUserAll(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,ReplenishmentReportForm replenishmentReportForm) {
		ReturnDataUtil data1 = replenishmentReportServiceImpl.userListPage(replenishmentReportForm);
		replenishmentReportForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = replenishmentReportServiceImpl.userListPage(replenishmentReportForm);
		List<ReplenishmentReportBean> data = (List<ReplenishmentReportBean>)returnData.getReturnObject();
		String title ="补货人员列表";
		StringBuffer date = new StringBuffer("起始--至今");
		if (replenishmentReportForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()));
		}
		if (replenishmentReportForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate()));
		}
		String[] headers = new String[]{"id","companyName","quantityReplenishment","replenisher"};
		String[] column = new String[]{"序号","公司名称","补货总数量","补货操作人"};
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(replenishmentReportForm.getStartDate()!=null&&replenishmentReportForm.getEndDate()!=null) {
				//导出内容按时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getEndDate())+"的补货人员列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出补货人员列表--全部数据");
			}
			ExcelUtil.exportExcel(title, headers,column, response, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
	
	
	@ApiOperation(value = "全部导出", notes = "全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportAll")
    public void exportFileAll(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,ReplenishmentReportForm replenishmentReportForm) {
		ReturnDataUtil data1 = replenishmentReportServiceImpl.listPage(replenishmentReportForm);
		replenishmentReportForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = replenishmentReportServiceImpl.listPage(replenishmentReportForm);
		List<ReplenishmentReportBean> data = (List<ReplenishmentReportBean>)returnData.getReturnObject();
		String title ="补货报表列表";
		StringBuffer date = new StringBuffer("起始--至今");
		if (replenishmentReportForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()));
		}
		if (replenishmentReportForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate()));
		}
		String[] headers = new String[]{"id","companyName","lineName","code",
				"wayNumber","itemName","quantityBeforeReplenishment","quantityAfterReplenishment","quantityReplenishment","adjustNum","replenishmentTime","replenisher"};
		String[] column = new String[]{"序号","公司名称","线路名称","售货机编号","门号","商品名称","补货前数量","补货后数量","本次补货数量","校正数量","补货时间","补货操作人"};
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(replenishmentReportForm.getStartDate()!=null&&replenishmentReportForm.getEndDate()!=null) {
				//导出日志内容按时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getEndDate())+"的补货报表列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出补货报表列表--全部数据");
			}
			
			ExcelUtil.exportExcel2007(title, headers,column, response, data, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	@ApiOperation(value = "全部导出", notes = "全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportAllVision")
    public void exportVisonFileAll(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,ReplenishmentReportForm replenishmentReportForm) {
		replenishmentReportForm.setOrderType(2);
		ReturnDataUtil data1 = replenishmentReportServiceImpl.visionListPage(replenishmentReportForm);
		replenishmentReportForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = replenishmentReportServiceImpl.visionListPage(replenishmentReportForm);
		List<ReplenishmentReportBean> data = (List<ReplenishmentReportBean>)returnData.getReturnObject();
		String title ="视觉补货报表列表";
		StringBuffer date = new StringBuffer("起始--至今");
		if (replenishmentReportForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()));
		}
		if (replenishmentReportForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate()));
		}
		String[] headers = new String[]{"id","companyName","lineName","code",
				"wayNumber","itemName","quantityBeforeReplenishment","quantityAfterReplenishment","quantityReplenishment","adjustNum","replenishmentTime","replenisher"};
		String[] column = new String[]{"序号","公司名称","线路名称","售货机编号","门号","商品名称","补货前数量","补货后数量","本次补货数量","校正数量","补货时间","补货操作人"};
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(replenishmentReportForm.getStartDate()!=null&&replenishmentReportForm.getEndDate()!=null) {
				//导出日志内容按时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getEndDate())+"的补货报表列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出补货报表列表--全部数据");
			}
			
			ExcelUtil.exportExcel2007(title, headers,column, response, data, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	@ApiOperation(value = "全部导出", notes = "全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportChangeAll")
    public void exportChangeAll(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,ReplenishmentReportForm replenishmentReportForm) {
		ReturnDataUtil data1 = replenishmentReportServiceImpl.changeListPage(replenishmentReportForm);
		replenishmentReportForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = replenishmentReportServiceImpl.changeListPage(replenishmentReportForm);
		List<ReplenishmentReportBean> data = (List<ReplenishmentReportBean>)returnData.getReturnObject();
		String title ="撤货报表列表";
		StringBuffer date = new StringBuffer("起始--至今");
		if (replenishmentReportForm.getStartDate() != null) {
			date.replace(0, 2, DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getStartDate()));
		}
		if (replenishmentReportForm.getEndDate() != null) {
			date.replace(date.length() - 2, date.length(), DateUtil.formatYYYYMMDDHHMMSS(replenishmentReportForm.getEndDate()));
		}
		String[] headers = new String[]{"id","companyName","lineName","code",
				"wayNumber","itemName","quantityBeforeReplenishment","quantityAfterReplenishment","quantityReplenishment","adjustNum","replenishmentTime","replenisher"};
		String[] column = new String[]{"序号","公司名称","线路名称","售货机编号","门号","商品名称","撤货前数量","撤货后数量","本次撤货数量","校正数量","撤货时间","撤货操作人"};
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(replenishmentReportForm.getStartDate()!=null&&replenishmentReportForm.getEndDate()!=null) {
				//导出日志内容按时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(replenishmentReportForm.getEndDate())+"的撤货报表列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出撤货报表列表--全部数据");
			}
			
			ExcelUtil.exportExcel2007(title, headers,column, response, data, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
