package com.server.module.system.statisticsManage.machinesSaleStatistics;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.VmInfoStateEnum;

@Controller
@RequestMapping("/machinesSaleStatistics")
public class MachinesSaleStatisticsController {

	@Autowired
	private MachinesSaleStatisticsService machinesStatisticsService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private VendingMachinesInfoService vmInfoService;

	/**
	 * 查询售卖机报表
	 * @param machinesStatisticsForm
	 * @return 结果状态，售卖机销售信息List<PerMachinesSaleDto>，及分页信息
	 */
	@PostMapping(value="/findPerMachinesSale")
	@ResponseBody
	public ReturnDataUtil findPerMachinesSale(@RequestBody(required=false) MachinesSaleStatisticsForm machinesStatisticsForm
			,HttpServletRequest request) {
		if(machinesStatisticsForm==null){
			machinesStatisticsForm = new MachinesSaleStatisticsForm();
		}		
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		machinesStatisticsForm.setAreaId(user.getAreaId());
		if(user.getLevel()==2) {
			machinesStatisticsForm.setLevel(String.valueOf(user.getLevel()));
		}
		
		
		if(machinesStatisticsForm.getCompanyId()==null){

			Integer companyId = user==null?null:user.getCompanyId();
			machinesStatisticsForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(machinesStatisticsForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		machinesStatisticsForm.setCompanyIds(companyIds);
		return machinesStatisticsService.findPerMachinesSale(machinesStatisticsForm);
	}
	
	/**
	 * 查询售卖机报表
	 * @param machinesStatisticsForm
	 * @return 结果状态，售卖机总销售信息List<SumMachinesSaleDto>，及分页信息
	 */
	@PostMapping(value="/findSumMachinesSale")
	@ResponseBody
	public ReturnDataUtil findSumMachinesSale(@RequestBody(required=false) MachinesSaleStatisticsForm machinesStatisticsForm
			,HttpServletRequest request) {
		if(machinesStatisticsForm==null){
			machinesStatisticsForm = new MachinesSaleStatisticsForm();
		}
		if(machinesStatisticsForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			machinesStatisticsForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(machinesStatisticsForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		machinesStatisticsForm.setCompanyIds(companyIds);
		return machinesStatisticsService.findSumMachinesSale(machinesStatisticsForm);
	}
	
	
	/**
	 * 查询每台售卖机用户交易记录
	 * @param machinesStatisticsForm
	 * @return 结果状态，售卖机用户消费记录   List<PerMachinesCustomerConsumeDto>，及分页信息
	 */
	@PostMapping(value="/findPerMachinesCustomerConsume")
	@ResponseBody
	public ReturnDataUtil findPerMachinesCustomerConsume(@RequestBody(required=false) MachinesSaleStatisticsForm machinesStatisticsForm
			,HttpServletRequest request) {
		if(machinesStatisticsForm==null){
			machinesStatisticsForm = new MachinesSaleStatisticsForm();
		}
		if(machinesStatisticsForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			machinesStatisticsForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(machinesStatisticsForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		machinesStatisticsForm.setCompanyIds(companyIds);
		return machinesStatisticsService.findPerMachinesCustomerConsume(machinesStatisticsForm);
	}
	
	
	/**
	 * 将售货机用户消费记录导出excel
	 * @param machinesStatisticsForm
	 * @param res 
	 */
	@PostMapping("/exportPerMachinesCustomerConsume")
	public void exportPerMachinesCustomerConsume(@RequestBody(required=false) MachinesSaleStatisticsForm machinesStatisticsForm,HttpServletResponse res,HttpServletRequest request){
		if(machinesStatisticsForm==null){
			machinesStatisticsForm = new MachinesSaleStatisticsForm();
		}
		if(machinesStatisticsForm.getCompanyId()==null){
			Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user==null?null:user.getCompanyId();
			machinesStatisticsForm.setCompanyId(companyId);
		}
		List<Integer> companyList = companyService.findAllSonCompanyId(machinesStatisticsForm.getCompanyId());
		String companyIds =  StringUtils.join(companyList, ",");
		machinesStatisticsForm.setCompanyIds(companyIds);
		ReturnDataUtil returnData = machinesStatisticsService.findPerMachinesCustomerConsume(machinesStatisticsForm);
		List<PerMachinesCustomerConsumeDto> data = (List<PerMachinesCustomerConsumeDto>)returnData.getReturnObject();
		String title ="售货机消费者消费记录";
		StringBuffer date = new StringBuffer("起始--至今");
		if(machinesStatisticsForm.getStartDate()!=null){
			date.replace(0,2,DateUtil.formatYYYYMMDD(machinesStatisticsForm.getStartDate()));
		}
		if(machinesStatisticsForm.getEndDate()!=null){
			date.replace(date.length()-2,date.length(),DateUtil.formatYYYYMMDD(machinesStatisticsForm.getEndDate()));
		}
		if(machinesStatisticsForm.getIsShowAll()==0){
			date.append("--第"+machinesStatisticsForm.getCurrentPage()+"页");
			
		}
		String[] headers = new String[]{"UserName","itemName","price","num","money","payTime","vendingMachinesCode","companyName"};
		String[] column = new String[]{"消费者姓名","商品名称","商品价格","购买数量","消费金额","支付时间","售货机编号","公司名称"};
		try {
			ExcelUtil.exportExcel(title, headers,column, res, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 将售卖机报表导成excel
	 * @param machinesStatisticsForm
	 * @param res 
	 */
	@PostMapping("/export")
	public void exportPerMachinesSale(@RequestBody(required=false) MachinesSaleStatisticsForm machinesStatisticsForm,HttpServletResponse res,HttpServletRequest request){
		if(machinesStatisticsForm==null){
			machinesStatisticsForm = new MachinesSaleStatisticsForm();
		}
		
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		machinesStatisticsForm.setAreaId(user.getAreaId());
		if(machinesStatisticsForm.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			machinesStatisticsForm.setCompanyIds(companyIds);
		}
		ReturnDataUtil returnData = machinesStatisticsService.findPerMachinesSale(machinesStatisticsForm);
		List<PerMachinesSaleDto> data = (List<PerMachinesSaleDto>)returnData.getReturnObject();
		List<PerMachinesSaleDto> removeList = new ArrayList<PerMachinesSaleDto>();
		if(machinesStatisticsForm.getIsShowAll()==1 && StringUtils.isBlank(machinesStatisticsForm.getVendingMachinesAddress()) && StringUtils.isBlank(machinesStatisticsForm.getVendingMachinesCode())){
			//拿到正常售货机的ID
			List<String> vmCodeList = vmInfoService.findCode(VmInfoStateEnum.MACHINES_NORMAL.getCode(),machinesStatisticsForm.getCompanyId(),machinesStatisticsForm.getAreaId());

			data.stream().forEach(perMachinesSaleDto -> {
				if(vmCodeList.contains(perMachinesSaleDto.getVmCode())){
					vmCodeList.remove(perMachinesSaleDto.getVmCode());
				}else{
					//未正常运行的机器不在报表中显示
					removeList.add(perMachinesSaleDto);
				}
			});
			//System.out.println("==========================================遍历正常 vmCodeList");
			// 在这里调用刚才写的service 方法
			List<VendingMachinesInfoBean> vmBeanList = vmInfoService.findVendingMachinesByCodes(vmCodeList);

			Map<String,VendingMachinesInfoBean> vmBeanMap = new HashMap<>();
			for (VendingMachinesInfoBean vmBean : vmBeanList){
				vmBeanMap.put(vmBean.getCode(),vmBean);
			}

			//以下是正常运行但没有销售记录
			for (String code : vmCodeList) {
				PerMachinesSaleDto perMachinesSale = new PerMachinesSaleDto();
				perMachinesSale.setVmCode(code);
				perMachinesSale.setVendingMachinesAddress(vmBeanMap.get(code).getLocatoinName());
				data.add(perMachinesSale);
				//System.out.println(JSON.toJSONString(perMachinesSale));
			}

			//System.out.println("==========================================遍历正常 vmCodeList end");
			//删除不显示的机器(非正常运行的机器不显示)
			data.removeAll(removeList);
			Collections.sort(data, (machines1,machines2) -> {
				BigInteger b1 = new BigInteger(machines1.getVmCode());
				BigInteger b2 = new BigInteger(machines2.getVmCode());
				return  b1.compareTo(b2);
			});
			//System.out.println("===========所有data");
			//System.out.println(JSON.toJSONString(data));
		}

		String title ="售货机报表";
		StringBuffer date = new StringBuffer("起始--至今");
		if(machinesStatisticsForm.getStartDate()!=null){
			date.replace(0,2,DateUtil.formatYYYYMMDD(machinesStatisticsForm.getStartDate()));
		}
		if(machinesStatisticsForm.getEndDate()!=null){
			date.replace(date.length()-2,date.length(),DateUtil.formatYYYYMMDD(machinesStatisticsForm.getEndDate()));
		}
		if(machinesStatisticsForm.getIsShowAll()==0){
			date.append("--第"+machinesStatisticsForm.getCurrentPage()+"页");
		}
		String[] headers = new String[]{"vmCode","vendingMachinesAddress","sumFinishedOrderNum","sumFinishedMoney","basicPrice","freePrice","sumFinishedItemNum",
				"cusUnitPrice","saleUnitPrice","sumRefundOrderNum","sumRefundMoney"};
		String[] column = new String[]{"机器编码","地址","总订单数","总售价","商品原价","优惠金额","总销售商品数","客单价","销售单价","退款单次","退款金额"};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(machinesStatisticsForm.getStartDate()!=null&&machinesStatisticsForm.getEndDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(machinesStatisticsForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(machinesStatisticsForm.getEndDate())+"的售货机报表的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机报表--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,column, res, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将售卖机报表详情导成excel
	 * @param machinesStatisticsForm
	 * @param res 
	 */
	@PostMapping("/exportDetail")
	public void exportPerMachinesSaleDetail(@RequestBody(required=false) MachinesSaleStatisticsForm machinesStatisticsForm,HttpServletResponse res,HttpServletRequest request){
		if(machinesStatisticsForm==null){
			machinesStatisticsForm = new MachinesSaleStatisticsForm();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		machinesStatisticsForm.setAreaId(user.getAreaId());
		if(machinesStatisticsForm.getCompanyId()==null){
			Integer companyId = user==null?null:user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds =  StringUtils.join(companyList, ",");
			machinesStatisticsForm.setCompanyIds(companyIds);
		}
		ReturnDataUtil returnData = machinesStatisticsService.findPerMachinesSaleDetail(machinesStatisticsForm);
		List<PerMachinesSaleDto> data = (List<PerMachinesSaleDto>)returnData.getReturnObject();
		List<PerMachinesSaleDto> removeList = new ArrayList<PerMachinesSaleDto>();
	
		String title ="售货机详情报表";
		StringBuffer date = new StringBuffer("起始--至今");
		if(machinesStatisticsForm.getStartDate()!=null){
			date.replace(0,2,DateUtil.formatYYYYMMDD(machinesStatisticsForm.getStartDate()));
		}
		if(machinesStatisticsForm.getEndDate()!=null){
			date.replace(date.length()-2,date.length(),DateUtil.formatYYYYMMDD(machinesStatisticsForm.getEndDate()));
		}
		if(machinesStatisticsForm.getIsShowAll()==0){
			date.append("--第"+machinesStatisticsForm.getCurrentPage()+"页");
		}
		String[] headers = new String[]{"vmCode","name","sumFinishedItemNum","sumFinishedMoney","saleUnitPrice"};
		String[] column = new String[]{"机器编码","商品名称","销售商品数","总售价","平均售价"};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(machinesStatisticsForm.getStartDate()!=null&&machinesStatisticsForm.getEndDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(machinesStatisticsForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(machinesStatisticsForm.getEndDate())+"的售货机报表的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机详情报表--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,column, res, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将售卖机报表导成excel
	 * @param machinesStatisticsForm
	 * @param res
	 */
	@GetMapping("/exportAll")
	public void exportAllPerMachinesSale(HttpServletResponse res,HttpServletRequest request){
		MachinesSaleStatisticsForm	machinesStatisticsForm = new MachinesSaleStatisticsForm();
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean)adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user==null?null:user.getCompanyId();
		List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
		String companyIds =  StringUtils.join(companyList, ",");
		machinesStatisticsForm.setCompanyIds(companyIds);
		machinesStatisticsForm.setIsShowAll(1);
		ReturnDataUtil returnData = machinesStatisticsService.findPerMachinesSale(machinesStatisticsForm);
		List<PerMachinesSaleDto> data = (List<PerMachinesSaleDto>)returnData.getReturnObject();
		String title ="售货机报表";
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(machinesStatisticsForm.getStartDate()!=null&&machinesStatisticsForm.getEndDate()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(machinesStatisticsForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(machinesStatisticsForm.getEndDate())+"的售货机报表的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机报表--全部数据");
		}
		String[] headers = new String[]{"vmCode","sumFinishedOrderNum","sumFinishedMoney","sumFinishedItemNum",
				"sumFinishedCost","cusUnitPrice","saleUnitPrice","costUnitPrice","sumRefundOrderNum","sumRefundMoney","profitUnitPrice","sumProfit"};
		String[] column = new String[]{"机器编码","总订单数","总售价","总销售商品数","总成本价","客单价","销售单价","成本单价","退款单次","退款金额","毛利单价","总毛利"};
		try {
			ExcelUtil.exportExcel(title, headers,column, res, data, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化查询信息所需内容
	 * @return 公司信息List<CompanyBean>，初始化状态
	 */
	@PostMapping(value="/initInfo")
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
}
