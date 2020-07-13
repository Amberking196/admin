package com.server.module.system.statisticsManage.machinesOnSaleStatistics;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

@Controller
@RequestMapping("/machinesOnSaleStatistics")
public class MachinesOnSaleStatisticsController {

	@Autowired
	private MachinesOnSaleStatisticsService machinesOnSaleStatisticsService;
	@Autowired
	private CompanyService companyService;

	/**
	 * 查询在售商品记录
	 * @param MachinesOnSaleStatisticsForm
	 * @return returnDataUtil
	 */
	@PostMapping(value="/machinesOnSaleList",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil machinesOnSaleList(@RequestBody(required=false) MachinesOnSaleStatisticsForm machinesOnSaleStatisticsForm
			,HttpServletRequest request) {
		if(machinesOnSaleStatisticsForm==null){
			machinesOnSaleStatisticsForm = new MachinesOnSaleStatisticsForm();
		}
		if(machinesOnSaleStatisticsForm.getCompanyId()!=null) {
			List<Integer> companyList = companyService.findAllSonCompanyId(machinesOnSaleStatisticsForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			machinesOnSaleStatisticsForm.setCompanyIds(companyIds);
		}

		ReturnDataUtil returnDataUtil=machinesOnSaleStatisticsService.machinesOnSaleList(machinesOnSaleStatisticsForm);
		return returnDataUtil;
	}
	
	/**
	 * 查询在售商品记录详情记录
	 * @param MachinesOnSaleStatisticsForm
	 * @return returnDataUtil
	 */
	@PostMapping(value="/machinesOnSaleListDetail",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil machinesOnSaleListDetail(@RequestBody MachinesOnSaleStatisticsForm machinesOnSaleStatisticsForm,HttpServletRequest request) {
		if(machinesOnSaleStatisticsForm.getCompanyId()!=null) {
			List<Integer> companyList = companyService.findAllSonCompanyId(machinesOnSaleStatisticsForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			machinesOnSaleStatisticsForm.setCompanyIds(companyIds);
		}
		ReturnDataUtil returnDataUtil=machinesOnSaleStatisticsService.machinesOnSaleListDetail(machinesOnSaleStatisticsForm);
		return returnDataUtil;
	}
	
	
	@GetMapping(value="/export")
	public ReturnDataUtil export(MachinesOnSaleStatisticsForm form,HttpServletRequest request,HttpServletResponse res) {
		if(form==null){
			form = new MachinesOnSaleStatisticsForm();
		}
		form.setIsShowAll(1);
		if(form.getCompanyId()!=null) {
			List<Integer> companyList = companyService.findAllSonCompanyId(form.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			form.setCompanyIds(companyIds);
		}
		List<MachinesOnSaleDto> data=(List<MachinesOnSaleDto>) machinesOnSaleStatisticsService.machinesOnSaleList(form).getReturnObject();
		
		String title ="在售商品统计报表";
		StringBuffer date = new StringBuffer("");
		String[] headers = new String[]{"goodsName","num"};
		String[] column = new String[]{"商品名称","数量"};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		bean.setContent("用户: "+bean.getOperatorName()+" 导出在售商品统计报表--全部数据");
		try {
			ExcelUtil.exportExcel(title, headers,column, res, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
		return machinesOnSaleStatisticsService.machinesOnSaleList(form);
	}
	
}
