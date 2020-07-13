package com.server.module.system.machineManage.distorymachine;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.itemManage.itemBasic.ItemBasicController;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.statisticsManage.machinesSaleStatistics.MachinesSaleStatisticsForm;
import com.server.module.system.statisticsManage.payRecord.PayRecordDto;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/distorymachine")
@Api(value = "distorymachineController", description = "售货机掉线查询")
public class distorymachineController {

	public static Logger log = LogManager.getLogger(distorymachineController.class);
	@Autowired
	distorymachineService machineService;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private CompanyService companyService;
	
	/**
	 * 查询机器掉线信息
	 * @param distorymachineForm
	 * @return List<distorymachineBean>
	 */
	@ApiOperation(value = "查询机器掉线信息", notes = "查询机器掉线信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findDistorymachine")
	@ResponseBody
	public ReturnDataUtil findDistorymachine(@RequestBody(required=false) distorymachineForm distorymachineForm, HttpServletRequest request){
		if(distorymachineForm==null){
			distorymachineForm = new distorymachineForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		distorymachineForm.setAreaId(user.getAreaId());
		if(distorymachineForm.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			distorymachineForm.setCompanyIds(companyIds);
		}
		
		
		return machineService.findDistorymachine(distorymachineForm);

	}



	@ApiOperation(value = "查询机器掉线信息", notes = "查询机器掉线信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/perMachineCount")
	@ResponseBody
	public ReturnDataUtil perMachineCount(@RequestBody(required=false) PerMachineForm perMachineForm, HttpServletRequest request){
		//进行非空判断
		if(perMachineForm==null) {
			perMachineForm=new PerMachineForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		perMachineForm.setAreaId(user.getAreaId());
		if(perMachineForm.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			perMachineForm.setCompanyIds(companyIds);
		}
		return machineService.perMachineCount(perMachineForm);

	}
	
	@ApiOperation(value = "导出机器掉线信息", notes = "导出机器掉线信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/exportMachineCount")
	public void exportMachineCount(PerMachineForm distorymachineForm,HttpServletRequest request, HttpServletResponse response){
		if(distorymachineForm==null){
			distorymachineForm = new PerMachineForm();
		}
		if(distorymachineForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			distorymachineForm.setCompanyIds(companyIds);
		}
		distorymachineForm.setIsShowAll(1);
		ReturnDataUtil returnDataUtil=machineService.perMachineCount(distorymachineForm);
		List<distorymachineBean> data=(List<distorymachineBean>) returnDataUtil.getReturnObject();
		String title = "机器故障统计";

		String[] headers = new String[] { "name","code","machineCode","locatoinName"};
		String[] column = new String[] { "公司", "故障机器编号","出厂编号","详细地址"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, data,"");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@ApiOperation(value = "导出机器掉线信息", notes = "导出机器掉线信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/export")
	public void export(distorymachineForm distorymachineForm,HttpServletRequest request, HttpServletResponse response){
		if(distorymachineForm==null){
			distorymachineForm = new distorymachineForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		distorymachineForm.setAreaId(user.getAreaId());
		if(distorymachineForm.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			distorymachineForm.setCompanyIds(companyIds);
		}
		distorymachineForm.setIsShowAll(1);
		ReturnDataUtil returnDataUtil=machineService.findDistorymachine(distorymachineForm);
		List<distorymachineBean> data=(List<distorymachineBean>) returnDataUtil.getReturnObject();
		String title = "机器故障统计";

		//设置导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(distorymachineForm.getDate()!=null&&distorymachineForm.getDate()!=null) {
			//导出日志内容按时间格式导出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出机器故障统计的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出机器故障统计的数据");
		}

		String[] headers = new String[] { "id","name","code","machineCode","locatoinName","info","createTime"};
		String[] column = new String[] { "序号","公司", "故障机器编号","出厂编号","详细地址","故障原因","时间"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, data,"");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
