package com.server.module.system.machineManage.machinesWay;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.itemManage.itemBasic.ItemBasicBean;
import com.server.module.system.itemManage.itemBasic.ItemBasicService;
import com.server.module.system.logsManager.operationLog.OperationLogService;
import com.server.module.system.logsManager.operationLog.OperationsManagementLogBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemBean;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemService;
import com.server.module.system.replenishManage.machineHistory.VendingMachineHistoryService;
import com.server.util.DateUtil;
import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author name: yjr create time: 2018-04-12 14:07:22
 */
@Api(value = "VendingMachinesWayController", description = "机器货道")
@RestController
@RequestMapping("/vendingMachinesWay")
public class VendingMachinesWayController {

	@Autowired
	private VendingMachinesWayService vendingMachinesWayServiceImpl;
	@Autowired
	private OperationLogService operationLogServiceImpl;
	@Autowired
	private AdminUserService adminUserServiceImpl;
	@Autowired
	private ItemBasicService itemBasicServiceImpl;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoServiceImpl;
	@Autowired
	AdminUserService adminUserService;
    @Autowired
    private VendingMachinesWayService vendingMachinesWayService;
    @Autowired
	private VendingMachineHistoryService vendingMachineHistoryService;
	@Autowired
	private VendingMachinesWayItemService vendingMachinesWayItemService;

	/*
	 * @ApiOperation(value = "机器货道列表", notes = "listPage", httpMethod = "GET",
	 * produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	 * 
	 * @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	 * public ReturnDataUtil listPage(VendingMachinesWayCondition condition) {
	 * return vendingMachinesWayServiceImpl.listPage(condition); }
	 */

	@ApiOperation(value = "机器货道添加", notes = "传递的参数   vendingMachinesCode,wayNumber", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody VendingMachinesWayBean entity) {
		ReturnDataUtil re = vendingMachinesWayServiceImpl.checkAdd(entity);
		if (re.getStatus() == 0) {
			return re;
		}
		return new ReturnDataUtil(vendingMachinesWayServiceImpl.add(entity));
	}

	@ApiOperation(value = "机器货道修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody VendingMachinesWayBean entity) {
		return new ReturnDataUtil(vendingMachinesWayServiceImpl.update(entity));
	}

	@ApiOperation(value = "机器货道删除", notes = "机器货道删除", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody VendingMachinesWayBean entity, HttpServletRequest request) {
		// 可以删除价格库vending_machines_item ,暂时不做
		boolean del = vendingMachinesWayServiceImpl.del(entity.getId());
		if (del) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			OperationsManagementLogBean logBean = new OperationsManagementLogBean();
			logBean.setCompanyId(userBean.getCompanyId());
			logBean.setUserName(userBean.getName());
			logBean.setVmCode(entity.getVendingMachinesCode());
			logBean.setContent("删除售货机" + entity.getVendingMachinesCode() + "的" + entity.getWayNumber() + "货道");
			logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加操作日志
			operationLogServiceImpl.add(logBean);
			return new ReturnDataUtil(del);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "机器货道列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listAll", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listAll(String vmCode,HttpServletRequest request) {

		ReturnDataUtil dataUtil = new ReturnDataUtil();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(StringUtil.isNotBlank(vmCode)) {
			// 根据售货机编号，查找售货机的状态
			VendingMachinesInfoBean machine = vendingMachinesInfoServiceImpl.findVendingMachinesByCode(vmCode);
			if(user.getAreaId()>0 && !user.getAreaId().equals(machine.getAreaId())) {
				dataUtil.setStatus(0);
				dataUtil.setMessage("无权限操作售货机货道！");
				return dataUtil;
			}
	        if(machine==null){
	        	dataUtil.setStatus(0);
				dataUtil.setMessage("不存在该售货机或已禁用！");
				return dataUtil;
	        }
			// 如果是禁用状态的话
			if (machine != null && machine.getState() == 20007) {
				dataUtil.setStatus(0);
				dataUtil.setMessage("当前售货机已被禁用/解绑，不能操作售货机货道！");
				return dataUtil;

			} else {
				    if(machine.getMachineVersion()!=1){
				    	dataUtil.setStatus(0);
						dataUtil.setMessage("请到版本二操作！");
						return dataUtil;
				    }
					List<WayDto> list = vendingMachinesWayServiceImpl.listAll(vmCode);
					// 如果没有找到数据，那说明售货机编号输入错误(或者售货机下没有货道[也可以归结于售后机输入错误])
					if (list == null || list.size() <= 0) {
						dataUtil.setStatus(0);
						dataUtil.setMessage("请输入正确的售货机编号！");
						return dataUtil;
					}	
				return new ReturnDataUtil(list);

			}
		}else {
			dataUtil.setStatus(0);
			dataUtil.setMessage("请输入售货机编号！");
			return dataUtil;
		}
		

	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "机器货道列表2", notes = "listAll2", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listAll2", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listAll2(String vmCode,HttpServletRequest request) {
		ReturnDataUtil dataUtil = new ReturnDataUtil();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(StringUtil.isNotBlank(vmCode)) {
			// 根据售货机编号，查找售货机的状态
			VendingMachinesInfoBean machine = vendingMachinesInfoServiceImpl.findVendingMachinesByCode(vmCode);
			if(user.getAreaId()>0 && !user.getAreaId().equals(machine.getAreaId())) {
				dataUtil.setStatus(0);
				dataUtil.setMessage("无权限操作售货机货道！");
				return dataUtil;
			}
			// 如果是禁用状态的话
			if (machine != null && machine.getState() == 20007) {
				dataUtil.setStatus(0);
				dataUtil.setMessage("当前售货机已被禁用/解绑，不能操作售货机货道！");
				return dataUtil;

			} else {
				if(machine.getMachineVersion()!=2){
			    	dataUtil.setStatus(0);
					dataUtil.setMessage("请到版本一操作！");
					return dataUtil;
			    }
				VendingMachinesWayItemBean bean = vendingMachinesWayItemService.findItemBean(vmCode);
				Map map= Maps.newHashMap();
				map.put("machineType",machine.getMachineType());
				if(bean!=null) {
					map.put("maxCapacity",bean.getMaxCapacity());
				}
				map.put("vmCode",vmCode);
				map.put("machineVersion",machine.getMachineVersion());
				map.put("stateName",machine.getStateName());
				map.put("locationName", machine.getLocatoinName());
				map.put("address",machine.getLocatoinName());
			
				List<WayDto1> list=vendingMachinesInfoServiceImpl.findAllWayAndItem(vmCode);
				map.put("wayList",list);
				
				return new ReturnDataUtil(map);

			}
		}else {
			dataUtil.setStatus(0);
			dataUtil.setMessage("请输入售货机编号！");
			return dataUtil;
		}	
	}

	@ApiOperation(value = "机器货道列表地图版本1", notes = "listAllForMap", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listAllForMap", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listAllForMap(String vmCode) {
		ReturnDataUtil dataUtil = new ReturnDataUtil();
		if(StringUtil.isNotBlank(vmCode)) {
			// 根据售货机编号，查找售货机的状态
			VendingMachinesInfoBean machine = vendingMachinesInfoServiceImpl.findVendingMachinesByCodeForMap(vmCode);
	        if(machine==null){
	        	dataUtil.setStatus(0);
				dataUtil.setMessage("不存在该售货机或已禁用！");
				return dataUtil;
	        }
			// 如果是禁用状态的话
			if (machine != null && machine.getState() == 20007) {
				dataUtil.setStatus(0);
				dataUtil.setMessage("当前售货机已被禁用/解绑，不能操作售货机货道！");
				return dataUtil;

			} else {
				    if(machine.getMachineVersion()!=1){
				    	dataUtil.setStatus(0);
						dataUtil.setMessage("请到版本二操作！");
						return dataUtil;
				    }
					List<WayDto> list = vendingMachinesWayServiceImpl.listAllForExport(vmCode);
					// 如果没有找到数据，那说明售货机编号输入错误(或者售货机下没有货道[也可以归结于售后机输入错误])
					if (list == null || list.size() <= 0) {
						dataUtil.setStatus(0);
						dataUtil.setMessage("请输入正确的售货机编号！");
						return dataUtil;
					}	
				return new ReturnDataUtil(list);

			}
		}else {
			dataUtil.setStatus(0);
			dataUtil.setMessage("请输入售货机编号！");
			return dataUtil;
		}
	}
	
	@ApiOperation(value = "机器货道列表地图版本2", notes = "listAllForMap2", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listAllForMap2", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listAllForMap2(String vmCode) {
		ReturnDataUtil dataUtil = new ReturnDataUtil();
		if(StringUtil.isNotBlank(vmCode)) {
			// 根据售货机编号，查找售货机的状态
			VendingMachinesInfoBean machine = vendingMachinesInfoServiceImpl.findVendingMachinesByCodeForMap(vmCode);
			// 如果是禁用状态的话
			if (machine != null && machine.getState() == 20007) {
				dataUtil.setStatus(0);
				dataUtil.setMessage("当前售货机已被禁用/解绑，不能操作售货机货道！");
				return dataUtil;

			} else {
				if(machine.getMachineVersion()!=2){
			    	dataUtil.setStatus(0);
					dataUtil.setMessage("请到版本一操作！");
					return dataUtil;
			    }
				Map map= Maps.newHashMap();
				map.put("machineVersion",machine.getMachineVersion());
				map.put("stateName",machine.getStateName());
				map.put("locationName", machine.getLocatoinName());
				map.put("address",machine.getLocatoinName());
				List<WayDto1> list=vendingMachinesInfoServiceImpl.findAllWayAndItemForExport(vmCode);
				map.put("wayList",list);
				return new ReturnDataUtil(map);

			}
		}else {
			dataUtil.setStatus(0);
			dataUtil.setMessage("请输入售货机编号！");
			return dataUtil;
		}	
	}
	@ApiOperation(value = "货道添加商品", notes = "货道添加商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/bindItem", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil bindItem(@RequestBody BindItemDto dto, HttpServletRequest request) {
		
		List<VendingMachinesWayBean> listWay = vendingMachinesWayService.listWay(dto.getVmCode(),
				dto.getWayNumber().intValue());
		/*if (listWay.size() > 0) {
			VendingMachinesWayBean wayBean = listWay.get(0);
			if (wayBean.getState().intValue() != 30001) {
				ReturnDataUtil data=new ReturnDataUtil();
				data.setMessage("货道处于不正常状态，不可编辑");
				data.setStatus(0);
				return data;
			}
		}*/
		
		// 得到之前商品名称信息
		List<WayDto> listAll = vendingMachinesWayServiceImpl.listAll(dto.getVmCode());
		//判断该售货机是否有给货道，如果有执行添加操作
		ReturnDataUtil returnData=new ReturnDataUtil();
		List<Integer>wayNumbers=new ArrayList<Integer>();
		String itemName = null;
		for (WayDto wayDto : listAll) {
			wayNumbers.add(wayDto.getWayNumber());//添加货道数到集合中
			if (wayDto.getWayNumber() == dto.getWayNumber()) {
				itemName = wayDto.getItemName();
				if(wayDto.getNum()>0){//
					returnData.setStatus(-1);
					returnData.setMessage("该货道商品没清空，不能重新绑定商品");
					return returnData;
				}
			}
		}
		
		if(wayNumbers.size()>0&&wayNumbers.contains(dto.getWayNumber())) {
			// 得到修改后商品名称;
			ItemBasicBean itemBasic = itemBasicServiceImpl.getItemBasic(dto.getBasicItemId());
			//绑定商品前先做日志记录，如，该货道商品的当日销售补货数量统计
			vendingMachineHistoryService.updateBean(dto.getVmCode(),dto.getWayNumber());

			//绑定(主逻辑)
			dto.setNum(0);
			ReturnDataUtil bindItem = vendingMachinesWayServiceImpl.bindItem(dto);
			vendingMachineHistoryService.addBean(dto.getVmCode(),dto.getWayNumber());

			//以下为非必要逻辑
			boolean falg = (boolean) bindItem.getReturnObject();
			if (falg) {
				// 得到当前用户Id 查询出当前用户信息
				Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
				ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
				AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
				// 操作日志
				OperationsManagementLogBean logBean = new OperationsManagementLogBean();
				logBean.setCompanyId(userBean.getCompanyId());
				logBean.setUserName(userBean.getName());
				logBean.setVmCode(dto.getVmCode());
				if (itemName == null) {
					itemName = "无";
				}
				logBean.setContent("更改" + dto.getVmCode() + "的" + dto.getWayNumber() + "货道上的商品" + itemName + "为-->"
						+ itemBasic.getName());
				logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
				// 增加操作日志
				operationLogServiceImpl.add(logBean);
				return bindItem;
			}
			return null;
		}else {//直接返回，该货道不存在
			returnData.setStatus(-1);
			returnData.setMessage("该货道不存在，请选择其他货道");
			return returnData;
		}
	
	}
	/*
	 * @ApiOperation(value = "修改货道及商品", notes = "修改货道及商品", httpMethod = "POST",
	 * produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	 * 
	 * @PostMapping(value = "/editWayAndItem", produces =
	 * "application/json;charset=UTF-8") public ReturnDataUtil
	 * editWayAndItem(@RequestBody BindItemDto dto) { return
	 * vendingMachinesWayServiceImpl.editWayAndItem(dto); }
	 */

	@ApiOperation(value = "机器货道启用", notes = "机器货道启用", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/enabled/{id}", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil enabled(@PathVariable("id") Long id) {
		VendingMachinesWayBean bean = vendingMachinesWayServiceImpl.get(id);
		bean.setState(30001);
		bean.setUpdateTime(new Date());
		return new ReturnDataUtil(vendingMachinesWayServiceImpl.update(bean));
	}


	/**
	 * 售货机货道禁用或者启用
	 * @param id
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "机器货道禁用/启用", notes = "机器货道禁用/启用", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/disableOrEnable/{id}", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil disableOrEnable(@PathVariable("id") Long id, HttpServletRequest request) {
		VendingMachinesWayBean bean = vendingMachinesWayServiceImpl.get(id);

		if (bean.getState() == 30001) {
			bean.setState(30003);
		} else if(bean.getState() == 30003) {
			bean.setState(30001);
		}
		bean.setUpdateTime(new Date());
		boolean update = vendingMachinesWayServiceImpl.update(bean);
		if (update) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			OperationsManagementLogBean logBean = new OperationsManagementLogBean();
			logBean.setCompanyId(userBean.getCompanyId());
			logBean.setUserName(userBean.getName());
			logBean.setVmCode(bean.getVendingMachinesCode());
			if (bean.getState() == 30003) {
				logBean.setContent("禁用收货机" + bean.getVendingMachinesCode() + "的" + bean.getWayNumber() + "货道");
			}
			if (bean.getState() == 30001) {
				logBean.setContent("启用收货机" + bean.getVendingMachinesCode() + "的" + bean.getWayNumber() + "货道");
			}
			logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加操作日志
			operationLogServiceImpl.add(logBean);
			return new ReturnDataUtil(update);
		}


		return null;
	}

	@ApiOperation(value = "货道商品统计", notes = "货道商品统计", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/statisticsWayNum", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil statisticsWayNumPage(StatisticsWayNumCondition condition) {
		return new ReturnDataUtil(vendingMachinesWayServiceImpl.statisticsWayNum(condition));
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "货道商品统计导出", notes = "货道商品统计导出（全部）", httpMethod = "GET")
	@RequestMapping(value = "/statisticsWayNumExport", method = RequestMethod.GET)
	public void statisticsWayNumExport(HttpServletRequest request, HttpServletResponse response,
			StatisticsWayNumCondition condition) {
		try {
			String fileName = "货道商品统计" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			condition.setPageSize(1000000);
			condition.setCurrentPage(1);
			ReturnDataUtil data = vendingMachinesWayServiceImpl.statisticsWayNum(condition);
			new ExportExcel("货道商品统计", StatisticsWayNumVo.class).setDataList((List) data.getReturnObject())
					.write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@ApiOperation(value = "修改货道及商品", notes = "修改货道及商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/editWayAndItem", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil editWayAndItem(@RequestBody BindItemDto dto, HttpServletRequest request) {
		// 得到之前信息
		List<WayDto> listAll = vendingMachinesWayServiceImpl.listAll(dto.getVmCode());
		Integer fullNum = 0;
		BigDecimal price = new BigDecimal(0);
		BigDecimal costPrice = new BigDecimal(0);
		for (WayDto wayDto : listAll) {
			if (wayDto.getWayNumber() == dto.getWayNumber()) {
				fullNum = wayDto.getFullNum();
				price = wayDto.getPrice();
				costPrice = wayDto.getCostPrice();
			}
		}
        //只修改价格跟容量
		ReturnDataUtil data = vendingMachinesWayServiceImpl.editWayAndItem(dto);
		boolean falg = (boolean) data.getReturnObject();
		if (falg) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil userData = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) userData.getReturnObject();
			// 操作日志
			OperationsManagementLogBean logBean = new OperationsManagementLogBean();
			logBean.setCompanyId(userBean.getCompanyId());
			logBean.setUserName(userBean.getName());
			logBean.setVmCode(dto.getVmCode());
			logBean.setContent("修改" + dto.getVmCode() + "的" + dto.getWayNumber() + "货道" + "上的货道最大容量" + fullNum + "为-->"
					+ dto.getFullNum() + ",更改商品销售价格" + price + "为-->" + dto.getPrice() + ",商品成本价格" + costPrice + "为-->"
					+ dto.getCostPrice());
			logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加操作日志
			operationLogServiceImpl.add(logBean);
			return data;
		}
		return null;
	}



}
