package com.server.module.system.synthesizeManage.vendingLineManage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.commonBean.IdNameBean;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.statisticsManage.itemSaleStatistics.ItemSaleStatisticsService;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaDto;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaForm;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaService;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/vendingLine")
@Api(value = "vendingLineController", description = "路线管理操作")
public class VendingLineController {

	public static Logger log = LogManager.getLogger(VendingLineController.class);
	@Autowired
	VendingLineService vendingLineService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	CompanyService companyService;
	@Autowired
	VendingMachinesInfoService vendingMachinesInfoService;
	@Autowired
	VendingAreaService vendingAreaService;
	@Autowired
	ItemSaleStatisticsService itemSaleStatisticsService;
	;
	/**
	 * 添加路线
	 * 
	 * @author hebiting
	 * @date 2018年4月9日下午1:59:57
	 * @param line
	 * @return
	 */
	@ApiOperation(value = "添加路线", notes = "添加路线", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/add")
	public ReturnDataUtil addVendingLine(@RequestBody(required = false) VendingLineBean line) {
		return vendingLineService.addVendingLine(line);
	}

	/**
	 * 修改路线信息
	 * 
	 * @author hebiting
	 * @date 2018年4月9日下午2:00:11
	 * @param line
	 * @return
	 */
	@ApiOperation(value = "修改路线信息", notes = "修改路线信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/update")
	public ReturnDataUtil updateVendingLine(@RequestBody(required = false) VendingLineBean line) {
		return vendingLineService.updateVendingLine(line);
	}

	// /**
	// * 查询所有路线信息
	// * @author hebiting
	// * @date 2018年4月9日下午2:00:23
	// * @return
	// */
	// @ApiOperation(value = "查询所有路线信息", notes = "查询所有路线信息", httpMethod = "POST",
	// produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @PostMapping("/findAllVendingLines")
	// public ReturnDataUtil findAllVendingLine(){
	// return vendingLineService.findAllVendingLine();
	// }
	/**
	 * 改变路线状态(isUsing=1:启用，isUsing=0:禁用)
	 * 
	 * @author hebiting
	 * @date 2018年4月9日下午2:00:39
	 * @param param
	 * @return
	 */
	@ApiOperation(value = "改变路线状态(isUsing=1:启用，isUsing=0:禁用)", notes = "改变路线状态(isUsing=1:启用，isUsing=0:禁用)", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/changeLineStatus")
	public ReturnDataUtil changeLineStatus(@RequestBody(required = false) Map<String, Integer> param) {
		return vendingLineService.changeLineStatus(param.get("id"), param.get("isUsing"));
	}

	/**
	 * 根据条件查询路线信息
	 * 
	 * @author hebiting
	 * @date 2018年4月13日上午9:11:14
	 * @param param
	 * @return
	 */
	@ApiOperation(value = "根据条件查询路线信息", notes = "根据条件查询路线信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findLineByForm")
	public ReturnDataUtil findLineByForm(@RequestBody(required = false) VendingLineForm vendingLineForm,
			HttpServletRequest request) {
		if (vendingLineForm == null) {
			vendingLineForm = new VendingLineForm();
		}
		if (vendingLineForm.getCompanyId() == null) {
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
			Integer companyId = user == null ? null : user.getCompanyId();
			List<Integer> companyList = companyService.findAllSonCompanyId(companyId);
			String companyIds = StringUtils.join(companyList, ",");
			vendingLineForm.setCompanyIds(companyIds);
		}
		return vendingLineService.findLineByForm(vendingLineForm);
	}

	/**
	 * 查询详情
	 * 
	 * @author hebiting
	 * @date 2018年4月13日下午5:41:12
	 * @param lineId
	 * @return
	 */
	@ApiOperation(value = "查询线路详情", notes = "查询线路详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/findLineDetail")
	public ReturnDataUtil findLineDetail(@RequestBody Map<String, Integer> param) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<VendingMachinesInfoBean> machinesInfoList = vendingMachinesInfoService
				.findMachinesByLineId(param.get("lineId"));
		if (machinesInfoList != null && machinesInfoList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(machinesInfoList);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		return returnData;
	}

	/**
	 * 初始化
	 * 
	 * @author hebiting
	 * @date 2018年4月12日上午10:37:04
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "初始化", notes = "初始化", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initInfo")
	public ReturnDataUtil initInfo(HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		Integer companyId = user == null ? null : user.getCompanyId();
		List<CompanyBean> companyList = companyService.findAllSonCompany(companyId);
		VendingAreaForm areaForm = new VendingAreaForm();
		List<CompanyAreaUserDto> infoList = new ArrayList<CompanyAreaUserDto>();
		CompanyAreaUserDto info = null;
		for (CompanyBean company : companyList) {
			info = new CompanyAreaUserDto();
			info.setComapanyValue(company);
			areaForm.setCompanyId(company.getId());
			areaForm.setIsShowAll(1);
			areaForm.setIsUsing(1);
			List<VendingAreaDto> areaList = (List<VendingAreaDto>) vendingAreaService.findAreaByForm(areaForm)
					.getReturnObject();
			List<AdminUserBean> userList = (List<AdminUserBean>) adminUserService.findAllCompanyUser(company.getId())
					.getReturnObject();
			info.setAreaList(areaList);
			info.setUserList(userList);
			infoList.add(info);
		}
		if (companyList != null && companyList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("初始化成功");
			returnData.setReturnObject(infoList);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("初始化失败");
		}
		return returnData;
	}

	/**
	 * 初始化
	 * 
	 * @author why
	 * @date 2018年4月26日下午17:48:20
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "三级初始化", notes = "三级初始化", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initLineInfo")
	public ReturnDataUtil initLineInfo(HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		// 得到当前登录用户的公司及子公司
		List<CompanyBean> companyList = companyService.findAllSonCompany(UserUtils.getUser().getCompanyId());
		VendingAreaForm areaForm = new VendingAreaForm();// 地区
		// 最后返回的结果集
		List<CompanyAreaUserDto> infoList = new ArrayList<CompanyAreaUserDto>();
		CompanyAreaUserDto info = null;
		for (CompanyBean company : companyList) {
			info = new CompanyAreaUserDto();
			info.setComapanyValue(company);
			areaForm.setCompanyId(company.getId());
			areaForm.setIsUsing(1);
			areaForm.setIsShowAll(1);
			// 区域集合
			List<VendingAreaDto> areaList = (List<VendingAreaDto>) vendingAreaService.findAreaByForm(areaForm)
					.getReturnObject();
			if (areaList != null) {
				// 线路集合
				List<VendingLineBean> lineList = null;
				for (VendingAreaDto area : areaList) {
					lineList = vendingLineService.findLine(area.getId());
					// 将线路增加到区域
					area.setLineList(lineList);
				}
				info.setAreaList(areaList);
			}
			infoList.add(info);
		}
		if (companyList != null && companyList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("初始化成功");
			returnData.setReturnObject(infoList);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("初始化失败");
		}
		return returnData;
	}

	/**
	 * 初始化
	 * 
	 * @author why
	 * @date 2018年4月27日下午15:51:20
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "四级初始化", notes = "四级初始化", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initMacnineInfo")
	public ReturnDataUtil initMacnineInfo(HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		// 得到当前登录用户的公司及子公司
		List<CompanyBean> companyList = companyService.findAllSonCompany(UserUtils.getUser().getCompanyId());

		VendingAreaForm areaForm = new VendingAreaForm();// 地区
		// 最后返回的结果集
		List<CompanyAreaUserDto> infoList = new ArrayList<CompanyAreaUserDto>();

		CompanyAreaUserDto info = null;
		for (CompanyBean company : companyList) {
			info = new CompanyAreaUserDto();
			info.setComapanyValue(company);
			areaForm.setCompanyId(company.getId());
			areaForm.setIsUsing(1);
			areaForm.setIsShowAll(1);
			// 区域集合
			List<VendingAreaDto> areaList = (List<VendingAreaDto>) vendingAreaService.findAreaByForm(areaForm)
					.getReturnObject();
			if (areaList != null) {
				// 线路集合
				List<VendingLineBean> lineList = null;
				Iterator i = areaList.iterator();
				while (i.hasNext()) {
					VendingAreaDto area = (VendingAreaDto) i.next();
					lineList = vendingLineService.findLine(area.getId());
					Iterator it = lineList.iterator();
					while (it.hasNext()) {
						VendingLineBean line = (VendingLineBean) it.next();
						// 机器集合
						List<VendingMachinesInfoBean> machineList = vendingMachinesInfoService
								.findMachinesByLineId(line.getId());
						// 将机器增加到线路
						if (machineList == null || machineList.isEmpty()) {
							it.remove();
						} else {
							line.setList(machineList);
						}
					}
					// 将线路增加到区域
					if (lineList == null || lineList.isEmpty()) {
						i.remove();
					} else {
						area.setLineList(lineList);
					}
				}
				info.setAreaList(areaList);
			}
			infoList.add(info);
		}
		if (companyList != null && companyList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("初始化成功");
			returnData.setReturnObject(infoList);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("初始化失败");
		}
		return returnData;
	}

	/**
	 * 初始化
	 * 
	 * @author hjc
	 * @date 2018年8月8日下午15:51:20
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "公司与子公司区域初始化", notes = "初始化", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initAllMacnineInfo")
	public ReturnDataUtil initAllMacnineInfo(HttpServletRequest request) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		// 得到当前登录用户的公司及子公司
		List<CompanyBean> companyList = companyService.findAllSonCompany(UserUtils.getUser().getCompanyId());

		VendingAreaForm areaForm = new VendingAreaForm();// 地区
		// 最后返回的结果集
		List<CompanyAreaUserDto> infoList = new ArrayList<CompanyAreaUserDto>();

		CompanyAreaUserDto info = null;
		for (CompanyBean company : companyList) {
			info = new CompanyAreaUserDto();
			info.setComapanyValue(company);
			String  companyIds= StringUtils.join(companyService.findAllSonCompanyId(company.getId()), ",");
			areaForm.setCompanyIds(companyIds);
			log.info("----!!!------"+companyIds);
			areaForm.setIsUsing(1);areaForm.setIsShowAll(1);
			// 区域集合
			List<VendingAreaDto> areaList = (List<VendingAreaDto>) vendingAreaService.findAreaByForm(areaForm)
					.getReturnObject();
			if (areaList != null) {
				// 线路集合
//				log.info("----!!!------"+areaList.size());
//				List<VendingLineBean> lineList = null;
//				Iterator i = areaList.iterator();
//				while (i.hasNext()) {
//					VendingAreaDto area = (VendingAreaDto) i.next();
//					lineList = vendingLineService.findLine(area.getId());
//					Iterator it = lineList.iterator();
//					while (it.hasNext()) {
//						VendingLineBean line = (VendingLineBean) it.next();
//						// 机器集合
//						List<VendingMachinesInfoBean> machineList = vendingMachinesInfoService
//								.findMachinesByLineId(line.getId());
//						// 将机器增加到线路
//						if (machineList == null || machineList.isEmpty()) {
//							it.remove();
//						} else {
//							line.setList(machineList);
//						}
//					}
//					// 将线路增加到区域
//					if (lineList == null || lineList.isEmpty()) {
//						i.remove();
//					} else {
//						area.setLineList(lineList);
//					}
//				}
				info.setAreaList(areaList);
			}
			infoList.add(info);
		}
		if (companyList != null && companyList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("初始化成功");
			returnData.setReturnObject(infoList);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("初始化失败");
		}
		return returnData;
	}
	/**
	 * 判断线路名是否唯一
	 * 
	 * @author hebiting
	 * @date 2018年5月9日下午4:28:52
	 * @param lineName
	 * @return
	 */
	@PostMapping("/isOnlyOne")
	public ReturnDataUtil isOnlyOne(@RequestBody Map<String, String> param) {
		String lineName = param.get("lineName");
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (vendingLineService.isOnlyOne(lineName)) {
			returnData.setStatus(1);
			returnData.setMessage("线路名未被占用");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("线路名被占用");
		}
		return returnData;
	}

	/**
	 * 初始化
	 * 
	 * @author why
	 * @date 2018年4月27日下午15:51:20
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "线路负责人初始化", notes = "线路负责人初始化", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initDatyInfo")
	public ReturnDataUtil initDatyInfo() {
		// 得到当前登录用户的公司及子公司
		List<Integer> findAllSonCompanyId = companyService.findAllSonCompanyId(UserUtils.getUser().getCompanyId());
		String companyIds = StringUtils.join(findAllSonCompanyId, ",");
		VendingAreaForm areaForm = new VendingAreaForm();
		areaForm.setCompanyIds(companyIds);
		areaForm.setIsShowAll(1);
		areaForm.setIsUsing(1);
		// 区域集合
		List<VendingAreaDto> infoList = (List<VendingAreaDto>) vendingAreaService.findAreaByForm(areaForm)
				.getReturnObject();
		if (infoList != null) {
			for (VendingAreaDto vendingAreaDto : infoList) {
				VendingLineForm vendingLineForm = new VendingLineForm();
				vendingLineForm.setAreaId(vendingAreaDto.getId() + "");
				vendingLineForm.setIsShowAll(1);
				// 得到线路集合
				List<VendingLineDto> list = (List<VendingLineDto>) vendingLineService.findLineByForm(vendingLineForm)
						.getReturnObject();
				if (list != null) {
					for (VendingLineDto dto : list) {
						// 得到线路下的负责人集合
						List<VendingLineBean> findDuty = vendingLineService.findDuty(dto.getId());
						dto.setDutyList(findDuty);
					}
				}
				vendingAreaDto.setList(list);
			}
		}
		return new ReturnDataUtil(infoList);
	}
	
	/**
	 */
	@ApiOperation(value = "删除路线信息", notes = "删除路线信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/delete")
	public ReturnDataUtil deleteVendingLine(@RequestBody List<Integer> id,HttpServletRequest request) {
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		log.info("<VendingLineController>------<deleteVendingLine>-----start");
		String payIds = StringUtils.join(id, ",");
		boolean delete = vendingLineService.delete(payIds);
		if (delete) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("线路删除成功!");
			log.info("<VendingLineController>------<deleteVendingLine>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("线路删除失败!");
			log.info("<VendingLineController>------<deleteVendingLine>-----end");
			return returnDataUtil;
		}
	}
	
	/**
	 * 查询公司与商品 两级初始化
	 * vendingLineController/initItemInfo vendingLineController
	 */
	@ApiOperation(value = "二级初始化(公司与商品)", notes = "二级初始化(公司与商品)", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/initItemInfo")
	public ReturnDataUtil initItemInfo() {
		ReturnDataUtil returnData = new ReturnDataUtil();
//		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
//		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
//		Integer companyId = user == null ? null : user.getCompanyId();
		List<CompanyBean> companyList = companyService.findAllSonCompany(UserUtils.getUser().getCompanyId());
		List<IdNameBean> itemBeans=itemSaleStatisticsService.findItemInfo();
		List<CompanyAreaUserDto> infoList = new ArrayList<CompanyAreaUserDto>();
		CompanyAreaUserDto info = null;

		for(CompanyBean c:companyList) {
			info =new CompanyAreaUserDto();
			info.setComapanyValue(c);
			List<IdNameBean> itemList = new ArrayList<IdNameBean>();
			for(IdNameBean i:itemBeans) {
				if(i.getCompanyId().equals(c.getId())) {
					itemList.add(i);
				}
			}
			info.setItemList(itemList);
			infoList.add(info);
		}
		if (companyList != null && companyList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("初始化成功");
			returnData.setReturnObject(infoList);
		} else {
			returnData.setStatus(0);
			returnData.setMessage("初始化失败");
		}
		return returnData;
	}
}
