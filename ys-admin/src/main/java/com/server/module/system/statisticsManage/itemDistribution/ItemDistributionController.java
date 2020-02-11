package com.server.module.system.statisticsManage.itemDistribution;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoDao;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.ExcelUtil;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/distribution")
public class ItemDistributionController {
	
	private final static Logger log = LogManager.getLogger(ItemDistributionController.class);

	@Autowired
	private ItemDistributionService itemDistributionService;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;
	@Autowired
	AdminUserService adminUserService;
	@PostMapping("/queryItem")
	public ReturnDataUtil queryItemDistribution(@RequestBody(required = false) ItemDistributionForm form,
			HttpServletRequest request){
		log.info("<ItemDistributionController--queryItemDistribution--start>");
		if(form == null){
			form = new ItemDistributionForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		form.setAreaId(user.getAreaId());
		if(form.getCompanyId() == null){
			Integer companyId = (Integer)request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		if(StringUtils.isNotBlank(form.getVmCode())) {
			VendingMachinesInfoBean vmi=vendingMachinesInfoService.findVendingMachinesByCode(form.getVmCode());
			if(vmi!=null) {
				form.setVersion(vmi.getMachineVersion());
			}
		}
		List<ItemDistributionDto> distributionList = itemDistributionService.queryDistribution(form);
		Long total = itemDistributionService.queryDistributionNum(form);
		if(distributionList!=null && distributionList.size()>0){
			return ResultUtil.success(distributionList,form.getCurrentPage(),total);
		}
		return ResultUtil.selectError();
	}
	
	

	@PostMapping("/export")
	public void exportPayRecord(@RequestBody(required = false) ItemDistributionForm form,
			HttpServletResponse response, HttpServletRequest request) {
		if(form == null){
			form = new ItemDistributionForm();
		}
		if(form.getCompanyId() == null){
			Integer companyId = (Integer)request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
			form.setCompanyId(companyId);
		}
		if(StringUtils.isNotBlank(form.getVmCode())) {
			VendingMachinesInfoBean vmi=vendingMachinesInfoService.findVendingMachinesByCode(form.getVmCode());
			if(vmi!=null) {
				form.setVersion(vmi.getMachineVersion());
			}
		}
		form.setIsShowAll(1);
		List<ItemDistributionDto> distributionList = itemDistributionService.queryDistribution(form);
		String title = "商品所在机器库存信息";
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		bean.setContent("用户: "+bean.getOperatorName()+" 导出在售商品库存信息--全部的数据");
		String[] headers = new String[] {"itemName","price","vmCode","address","itemNum","fullNum","monthNum","dayNum","day"};
		String[] column = new String[] {"商品名称","当前价格","机器编码","机器位置","商品余量","最大容量","月销售数量","日均销量","紧迫时间（天）"};
		try {
			ExcelUtil.exportExcel(title.toString(), headers, column, response, distributionList, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
	}
}
