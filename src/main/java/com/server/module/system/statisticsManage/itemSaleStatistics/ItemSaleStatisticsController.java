package com.server.module.system.statisticsManage.itemSaleStatistics;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.module.commonBean.IdNameBean;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
@Controller
@Api(value = "ItemSaleStatisticsController", description = "商品报表")
@RequestMapping("/itemSaleStatistics")
public class ItemSaleStatisticsController {

	@Autowired
	ItemSaleStatisticsService itemStatisticsService;
	@Autowired
	CompanyService companyService;
	@Autowired
	AdminUserService adminUserService;
	
	/**
	 * 查询商品报表
	 * @param itemSaleStatisticsForm
	 * @return 返回商品销售报表List<ItemSaleStatisticsDto>，结果状态，分页信息
	 */
	@PostMapping(value="/findItemSaleStatistics")
	@ResponseBody
	public ReturnDataUtil findItemSaleStatistics(@RequestBody(required=false) ItemSaleStatisticsForm itemSaleStatisticsForm,HttpServletRequest request){
		if (itemSaleStatisticsForm == null) {
			itemSaleStatisticsForm = new ItemSaleStatisticsForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		itemSaleStatisticsForm.setAreaId(user.getAreaId());
		if(itemSaleStatisticsForm.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			itemSaleStatisticsForm.setCompanyIds(companyIds);
		}else {
			List<Integer> companyList = companyService.findAllSonCompanyId(itemSaleStatisticsForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			itemSaleStatisticsForm.setCompanyIds(companyIds);
		}
		return itemStatisticsService.findItemSaleStatistics(itemSaleStatisticsForm);
	}
	
	/**
	 * 导出商品报表信息为excel文件
	 * @param itemSaleStatisticsForm
	 * @param res
	 */
	@GetMapping("/export")
	public void exportItemSaleStatistics(ItemSaleStatisticsForm itemSaleStatisticsForm,HttpServletResponse res,HttpServletRequest request){
		if (itemSaleStatisticsForm == null) {
			itemSaleStatisticsForm = new ItemSaleStatisticsForm();
		}
		if(itemSaleStatisticsForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			itemSaleStatisticsForm.setCompanyIds(companyIds);
		}else {
			List<Integer> companyList = companyService.findAllSonCompanyId(itemSaleStatisticsForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			itemSaleStatisticsForm.setCompanyIds(companyIds);
		}
		ReturnDataUtil returnData = itemStatisticsService.findItemSaleStatistics(itemSaleStatisticsForm);
		List<ItemSaleStatisticsDto> data = (List<ItemSaleStatisticsDto>)returnData.getReturnObject();
		String title = "商品销售统计";
		StringBuffer date = new StringBuffer("起始--至今");
		if(itemSaleStatisticsForm.getStartDate()!=null){
			date.replace(0,2,DateUtil.formatYYYYMMDD(itemSaleStatisticsForm.getStartDate()));
		}
		if(itemSaleStatisticsForm.getEndDate()!=null){
			date.replace(date.length()-2,date.length(),DateUtil.formatYYYYMMDD(itemSaleStatisticsForm.getEndDate()));
		}
		if(itemSaleStatisticsForm.getIsShowAll()==0){
			date.append("--第"+itemSaleStatisticsForm.getCurrentPage()+"页");
		}
		String[] headers = new String[]{"basicItemId","itemName","barCode","standard","unit","machinesNum",
				"itemTypeId","sumFinishedMoney","sumFinishedOrderNum","sumFinishedItemNum",
				"unitPrice"};
		String[] columnName = new String[]{"商品id","商品名称","条形码","规格","单位","上架机器数","商品类型id","总销售额","总订单数",
				"总销售商品数","单价"};
		//添加导出日志内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(itemSaleStatisticsForm.getStartDate()!=null&&itemSaleStatisticsForm.getEndDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(itemSaleStatisticsForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(itemSaleStatisticsForm.getEndDate())+"的商品销售统计的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出商品销售统计--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,columnName, res, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出商品报表信息为excel文件
	 * @param itemSaleStatisticsForm
	 * @param res
	 */
	@GetMapping("/exportAll")
	public void exportAllItemSaleStatistics(HttpServletResponse res,HttpServletRequest request){
		ItemSaleStatisticsForm	itemSaleStatisticsForm = new ItemSaleStatisticsForm();
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
		String companyIds =  StringUtils.join(companyList, ",");
		itemSaleStatisticsForm.setCompanyIds(companyIds);
		itemSaleStatisticsForm.setIsShowAll(1);
		ReturnDataUtil returnData = itemStatisticsService.findItemSaleStatistics(itemSaleStatisticsForm);
		List<ItemSaleStatisticsDto> data = (List<ItemSaleStatisticsDto>)returnData.getReturnObject();
		String title = "商品销售统计";
		String[] headers = new String[]{"basicItemId","itemName","barCode","standard","unit","machinesNum",
				"itemTypeId","sumFinishedMoney","sumFinishedOrderNum","sumFinishedItemNum","sumFinishedCost","sumProfit",
				"unitPrice","costUnitPrice","unitProfit"};
		String[] columnName = new String[]{"商品id","商品名称","条形码","规格","单位","上架机器数","商品类型id","总销售额","总订单数",
				"总销售商品数","总成本","总毛利","单价","成本单价","单品毛利"};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(itemSaleStatisticsForm.getStartDate()!=null&&itemSaleStatisticsForm.getEndDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(itemSaleStatisticsForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(itemSaleStatisticsForm.getEndDate())+"的商品销售统计的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出商品销售统计--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,columnName, res, data, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化商品报表查询所需信息
	 * @return 返回公司信息List<CompanyBean>，商品信息（id，name）集合，初始化状态
	 */
	@PostMapping(value="/initInfo")
	@ResponseBody
	public ReturnDataUtil initInfo(HttpServletRequest request){
		ReturnDataUtil data = new ReturnDataUtil();
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Map<Object,Object> dataInfo = new HashMap<Object,Object>();
		Integer companyId = user==null?null:user.getCompanyId();
		dataInfo.put("companyInfo", companyService.findAllSonCompany(companyId));
		dataInfo.put("itemInfo", itemStatisticsService.findItemInfo());
		if(dataInfo!=null && dataInfo.size()>0){
			data.setStatus(1);
			data.setMessage("初始化成功");
			data.setReturnObject(dataInfo);
		}else{
			data.setStatus(0);
			data.setMessage("初始化失败");
		}
		return data;
	}
	
	@PostMapping(value="/itemInfo")
	@ResponseBody
	public ReturnDataUtil itemInfo(@RequestBody ItemSaleStatisticsForm itemSaleStatisticsForm){
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(itemSaleStatisticsForm.getCompanyId()!=null) {
			List<Integer> companyList = companyService.findAllSonCompanyId(itemSaleStatisticsForm.getCompanyId());
			String companyIds =  StringUtils.join(companyList, ",");
			itemSaleStatisticsForm.setCompanyIds(companyIds);
			List<IdNameBean> list = itemStatisticsService.findItemInfoByCompanyId(itemSaleStatisticsForm);
			if(list!=null && list.size()>0){
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("查询商品信息成功！");
				returnDataUtil.setReturnObject(list);
			}else{
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("查询无数据！");
			}
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据！");
		}
		return returnDataUtil;
	}
}
