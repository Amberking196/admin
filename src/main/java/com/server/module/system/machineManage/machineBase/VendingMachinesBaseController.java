package com.server.module.system.machineManage.machineBase;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.logsManager.operationLog.HeadquartersConfigLogBean;
import com.server.module.system.logsManager.operationLog.HeadquartersConfigService;
import com.server.module.system.machineManage.machineType.MachinesTypeService;
import com.server.module.system.machineManage.machinesWayItem.MachinesClient;
import com.server.redis.RedisClient;
import com.server.util.DateUtil;
import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr create time: 2018-03-29 17:44:47
 */
@Api(value = "VendingMachinesBaseController", description = " 售货机基础信息配置")
@RestController
@RequestMapping("/vendingMachinesBase")
public class VendingMachinesBaseController {
	public static Logger log = LogManager.getLogger(VendingMachinesBaseController.class);
	@Autowired
	private VendingMachinesBaseService vendingMachinesBaseServiceImpl;
	@Autowired
	private HeadquartersConfigService headquartersConfigServiceImpl;
	@Autowired
	private AdminUserService adminUserServiceImpl;
	@Autowired
	private MachinesTypeService machinesTypeServiceImpl;//注入售货机类型Service
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private MachinesClient machinesClient;
	
	@ApiOperation(value = " 售货机基础信息配置列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(VendingMachinesBaseCondition condition) {
		return vendingMachinesBaseServiceImpl.listPage(condition);
	}

	@ApiOperation(value = " 售货机基础信息配置添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody VendingMachinesBaseBean entity, HttpServletRequest request) {
		ReturnDataUtil dataUtil = new ReturnDataUtil();
		boolean flag = vendingMachinesBaseServiceImpl.checkFactoryNumber(entity.getFactoryNumber());
		if (flag) {
			dataUtil.setStatus(0);
			dataUtil.setMessage("该出厂编号，以及存在请重新输入");
			return dataUtil;
		} else {
			if (entity.getFactoryNumber() != null) {
				String trim = entity.getFactoryNumber().trim();
				entity.setFactoryNumber(trim);
				// 增加售货机基础信息
				entity.setMainProgramVersion("1");
				entity.setIpcNumber("1");
				entity.setLiftingGearNumber("1");
				entity.setElectricCabinetNumber("1");
				entity.setCaseNumber("1");
				entity.setDoorNumber("1");
				entity.setAirCompressorNumber("1");
				entity.setKeyStr("1");
				if(StringUtils.isNotBlank(entity.getRemark()) && entity.getRemark().equals("视觉机器")) {
					entity.setMainProgramVersion("Ver.5.001-20191127");//后续改为非固定版本
				}
				VendingMachinesBaseBean baseAdd = vendingMachinesBaseServiceImpl.add(entity);
				if (baseAdd != null) {
					// 得到当前用户Id 查询出当前用户信息
					Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
					ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
					AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
					HeadquartersConfigLogBean bean = new HeadquartersConfigLogBean();
					bean.setCompanyId(userBean.getCompanyId());
					bean.setUserName(userBean.getName());
					bean.setContent("增加售货机基础信息");
					bean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
					// 增加总部配置操作日志
					headquartersConfigServiceImpl.insert(bean);
					dataUtil.setReturnObject(baseAdd);
					return dataUtil;
				} else {
					dataUtil.setStatus(0);
					dataUtil.setMessage("售货机基础信息增加失败！");
					return dataUtil;
				}
			}else {
				dataUtil.setStatus(0);
				dataUtil.setMessage("售货机基础信息增加失败！");
				return dataUtil;
			}
		}

	}

	@ApiOperation(value = " 售货机基础信息配置修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody VendingMachinesBaseBean entity, HttpServletRequest request) {
		 boolean falg = vendingMachinesBaseServiceImpl.update(entity);
		   if (falg) {
		      // 得到当前用户Id 查询出当前用户信息
		      Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		      ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
		      AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
		      HeadquartersConfigLogBean bean = new HeadquartersConfigLogBean();
		      bean.setCompanyId(userBean.getCompanyId());
		      bean.setUserName(userBean.getName());
		      bean.setContent("修改售货机基础信息");
		      bean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		      // 增加总部配置操作日志
		      headquartersConfigServiceImpl.insert(bean);
		      return new ReturnDataUtil(falg);
		   }
		   return null;
	}
	@ApiOperation(value = " 售货机基础信息配置删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(vendingMachinesBaseServiceImpl.del(id));
	}

	@ApiOperation(value = "查询售货机出厂信息", notes = "getBase", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getBase")
	public ReturnDataUtil getBase(String factoryNumber, HttpServletRequest request) {
		// 得到公司编号
		Integer companyId = (Integer) request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
		return new ReturnDataUtil(vendingMachinesBaseServiceImpl.getBase(factoryNumber, companyId));
	}

	@ApiOperation(value = "售货机导出", notes = "售货机导出")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String exportFile(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, VendingMachinesBaseCondition condition) {
		try {
			String fileName = "售货机基础信息列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			ReturnDataUtil data = vendingMachinesBaseServiceImpl.listPage(condition);
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机基础信息列表--当前页数据");
			new ExportExcel("售货机基础信息列表", VendingMachinesBaseDto.class).setDataList((List) data.getReturnObject())
					.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@ApiOperation(value = "售货机导出", notes = "售货机导出")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportAll", method = RequestMethod.GET)
	public void exportAllFile(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, VendingMachinesBaseCondition condition) {
		try {
			condition.setPageSize(1000000);
			String fileName = "全部售货机基础信息列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			ReturnDataUtil data = vendingMachinesBaseServiceImpl.listPage(condition);
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机基础信息列表--全部数据");
			new ExportExcel("全部售货机基础信息列表", VendingMachinesBaseDto.class).setDataList((List) data.getReturnObject())
					.write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 * 
	 * @param factoryNumber
	 * @return
	 */
	@ApiOperation(value = "重启发送规格指令", notes = "findItemStandard", httpMethod = "POST")
	@PostMapping(value = "/findItemStandard")
	public String findItemStandard(@RequestBody Map<String, String> factoryNumber) {
//		String findItemStandard = null;
//		String factNum = factoryNumber.get("factnum").trim();
//		Integer version=vendingMachinesBaseServiceImpl.getVersionByFactoryNumber(factoryNumber.get("factnum"));
//		if(CommandVersionEnum.VER1.getState().equals(version)){
//			findItemStandard = vendingMachinesBaseServiceImpl.findItemStandard(factNum);
//		}else if(CommandVersionEnum.VER2.getState().equals(version)){
//			findItemStandard = vendingMachinesBaseServiceImpl.getMultilayerCommand(factNum);
//		}
	    String programVersion = factoryNumber.get("version");
		String factnum = factoryNumber.get("factnum");
		redisClient.set("restartOK"+factnum, programVersion, 600);
		//更新数据库的版本信息记录
		if(StringUtils.isNotBlank(programVersion) && StringUtils.isNotBlank(factnum)){
			vendingMachinesBaseServiceImpl.updateProgramVersion(programVersion, factnum);
		}
		String findItemStandard = null;
		Integer version=vendingMachinesBaseServiceImpl.getVersionByFactoryNumber(factnum);
		if(version == 1) {
			 findItemStandard = vendingMachinesBaseServiceImpl.findItemStandard(factnum);
		}else if(version == 2) {
			 findItemStandard = vendingMachinesBaseServiceImpl.findItemWeight(factnum);
		}
		log.info(factnum+"重启返回指令:"+findItemStandard);
		return findItemStandard;
	}
	
	@ApiOperation(value = "发送机器自检指令", notes = "sendCheckOrder", httpMethod = "POST")
	@PostMapping(value = "/sendCheckOrder")
	public ReturnDataUtil sendCheckOrder(@RequestBody Map<String, String> factoryNumber) {
//		String findItemStandard = null;
//		String factNum = factoryNumber.get("factnum").trim();
//		Integer version=vendingMachinesBaseServiceImpl.getVersionByFactoryNumber(factoryNumber.get("factnum"));
//		if(CommandVersionEnum.VER1.getState().equals(version)){
//			findItemStandard = vendingMachinesBaseServiceImpl.findItemStandard(factNum);
//		}else if(CommandVersionEnum.VER2.getState().equals(version)){
//			findItemStandard = vendingMachinesBaseServiceImpl.getMultilayerCommand(factNum);
//		}
		//h:199400000888;d:1&12$
		String factnum = factoryNumber.get("factnum");
	    String door = factoryNumber.get("door");
	    StringBuffer sBuffer=new StringBuffer();
	    sBuffer.append("h:"+factnum+";d:"+door);
	    String order=sBuffer.toString();
		char[] ss = order.toCharArray();
		int sum=0;
		for (char c : ss) {
			if(Character.isDigit(c)){
				Integer unitChar = Integer.valueOf(String.valueOf(c));
				sum+=unitChar;
			}
		}
		order=order.concat("&"+sum+"$");
		
		machinesClient.sendCommand(factnum,order,"adminSystem");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order=redisClient.get("Check-"+factnum);
		
		ReturnDataUtil returnDataUtil= new ReturnDataUtil();
		returnDataUtil.setReturnObject(order);
		if(StringUtil.isNotBlank(order)) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查询成功");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该机器无返回信息");
		}
		log.info("order"+order);
		return returnDataUtil;
	}
	
	@ApiOperation(value = "发送机器自检指令", notes = "sendUpdateOrder", httpMethod = "POST")
	@PostMapping(value = "/sendUpdateOrder")
	public ReturnDataUtil sendUpdateOrder(@RequestBody Map<String, String> factoryNumber) {
//		String findItemStandard = null;
//		String factNum = factoryNumber.get("factnum").trim();
//		Integer version=vendingMachinesBaseServiceImpl.getVersionByFactoryNumber(factoryNumber.get("factnum"));
//		if(CommandVersionEnum.VER1.getState().equals(version)){
//			findItemStandard = vendingMachinesBaseServiceImpl.findItemStandard(factNum);
//		}else if(CommandVersionEnum.VER2.getState().equals(version)){
//			findItemStandard = vendingMachinesBaseServiceImpl.getMultilayerCommand(factNum);
//		}
		//h:199400000888;d:1&12$
		String factnum = factoryNumber.get("factnum");
	    String door = factoryNumber.get("door");
	    StringBuffer sBuffer=new StringBuffer();
	    sBuffer.append("n:"+factnum+";d:"+door+";update");
	    String order=sBuffer.toString();
		char[] ss = order.toCharArray();
		int sum=0;
		for (char c : ss) {
			if(Character.isDigit(c)){
				Integer unitChar = Integer.valueOf(String.valueOf(c));
				sum+=unitChar;
			}
		}
		order=order.concat("&"+sum+"$");
		
		machinesClient.sendCommand(factnum,order,"adminSystem");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order=redisClient.get("Check-"+factnum);
		
		ReturnDataUtil returnDataUtil= new ReturnDataUtil();
		returnDataUtil.setReturnObject(order);
		if(StringUtil.isNotBlank(order)) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查询成功");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该机器无返回信息");
		}
		log.info("order"+order);
		return returnDataUtil;
	}
}
