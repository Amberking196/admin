package com.server.module.app.home;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.app.login.AppLoginInfoService;
import com.server.module.app.login.LoginInfoBean;
import com.server.module.app.login.LoginInfoEnum;
import com.server.module.app.payRecord.AppPayRecordService;
import com.server.module.app.payRecord.PayRecordForm;
import com.server.module.app.payRecord.SumPayRecordDto;
import com.server.module.app.replenish.ReplenishForm;
import com.server.module.app.vminfo.AppVminfoService;
import com.server.module.app.vminfo.VminfoDto;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.VmInfoStateEnum;

@RestController
@RequestMapping("/home")
public class HomePageController {

	@Autowired
	private AppPayRecordService payRecordService;
	@Autowired
	private AppLoginInfoService loginInfoService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AppVminfoService vminfoService;
	
	/**
	 * 首页展示
	 * @author hebiting
	 * @date 2018年6月4日下午4:23:32
	 * @return
	 */
	@PostMapping("/initInfo")
	public ReturnDataUtil initInfo(HttpServletRequest request){
		//查询当前登录者的信息
		Long id = request.getAttribute(AdminConstant.LOGIN_USER_ID)==null?null:Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		LoginInfoBean loginUser = loginInfoService.queryById(id);
		if(loginUser == null){
			return ResultUtil.error();
		}
		Integer companyId = loginUser.getCompanyId();

		PayRecordForm payRecordForm = new PayRecordForm();
		//检测是否为补货人员
		if(companyService.checkIsReplenishCompany(companyId)) {
			payRecordForm.setType(1);
		}
		List<Integer> companyIdList = companyService.findAllSonCompanyId(companyId);
		String companyIds = StringUtils.join(companyIdList,",");
		payRecordForm.setCompanyIds(companyIds);
		
		//设置查询时间
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
		Date startDate = calendar.getTime();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 59, 59);
		Date endDate = calendar.getTime();
		payRecordForm.setStartDate(startDate);
		payRecordForm.setEndDate(endDate);
		
		
		Long dutyId = null;
		List<VminfoDto> queryVMList = null;
		//查询故障机器总数
		if(LoginInfoEnum.PRINCIPAL.getMessage().equals(loginUser.getIsPrincipal())){
			if(payRecordForm.getType()==null) {
				queryVMList = vminfoService.queryVMList(null, companyIds, null, VmInfoStateEnum.MACHINES_BREAKDOWN.getCode());
			}else {
				queryVMList = vminfoService.queryVMListForReplenishMan(null,companyIds,VmInfoStateEnum.MACHINES_BREAKDOWN.getCode());
			}
		}else{
			dutyId = loginUser.getId();
			queryVMList = vminfoService.queryOwnVMList(null, dutyId, null, VmInfoStateEnum.MACHINES_BREAKDOWN.getCode());
		}
		payRecordForm.setDutyId(dutyId);
		//销售统计数
		SumPayRecordDto sumPayRecord = payRecordService.findPayRecordNum(payRecordForm);
		
		//该公司下所有正常运行的机器总数
		Integer queryAllMachinesNum=0;
		if(payRecordForm.getType()==null) {
			 queryAllMachinesNum = vminfoService.queryAllMachinesNum(null, companyIds,VmInfoStateEnum.MACHINES_NORMAL.getCode());
		}else {
			 queryAllMachinesNum = vminfoService.queryAllMachinesNumForReplenishMan(null, companyIds,VmInfoStateEnum.MACHINES_NORMAL.getCode());
		}

		//机器现有商品数/满载商品数,低于rate时的补货机器查询
		//查询待补货机器
		ReplenishForm replenishForm = new ReplenishForm();
		replenishForm.setCompanyIds(companyIds);
		replenishForm.setDutyId(dutyId);
		replenishForm.setVersion(1);
		if(payRecordForm.getType()!=null) {
			replenishForm.setCompanyIds(null);
			replenishForm.setOtherCompanyId(companyId);
		}
		
		//缺货设备数=版本一+版本二
		List<VminfoDto> queryReplenishVMList = vminfoService.queryReplenishVMList(replenishForm);
		int replenishNum=queryReplenishVMList.size();
		replenishForm.setVersion(2);
		queryReplenishVMList = vminfoService.queryReplenishVMList(replenishForm);
		replenishNum=replenishNum+queryReplenishVMList.size();
		
		VmStatisticsDto statisticsDto = new VmStatisticsDto();
		statisticsDto.setMachinesNum(queryAllMachinesNum);
		//statisticsDto.setReplenishList(queryReplenishVMList);
		statisticsDto.setReplenishNum(replenishNum);
		statisticsDto.setTroubleList(queryVMList);
		statisticsDto.setTroubleNum(queryVMList.size());
		statisticsDto.setSumPayRecordDto(sumPayRecord);

		ReturnDataUtil returnData = new ReturnDataUtil();
		if(loginUser.getLevel()==1 || loginUser.getLevel()==2) {//公司区域级别
			returnData.setStatus(1);
			returnData.setMessage("成功");
			returnData.setReturnObject(statisticsDto);
		}
		else {
			returnData.setStatus(0);
			returnData.setMessage("没有权限");//只能查看补货机器数
			statisticsDto = new VmStatisticsDto();
			statisticsDto.setReplenishList(queryReplenishVMList);
			statisticsDto.setReplenishNum(queryReplenishVMList.size());
			returnData.setReturnObject(statisticsDto);
		}
		return returnData;
	}
	
	/**
	 * 初始化用户公司及子公司
	 */
	@PostMapping("/company")
	public ReturnDataUtil initCompany(HttpServletRequest request){
		Long id = request.getAttribute(AdminConstant.LOGIN_USER_ID)==null?null:Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		LoginInfoBean loginUser = loginInfoService.queryById(id);
		if(loginUser == null){
			return ResultUtil.error();
		}
		Integer companyId = loginUser.getCompanyId();
		List<CompanyBean> findAllSonCompany = companyService.findAllSonCompany(companyId);
		return ResultUtil.success(findAllSonCompany);
	}
}
