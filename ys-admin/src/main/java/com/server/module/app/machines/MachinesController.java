package com.server.module.app.machines;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.app.home.ResultEnum;
import com.server.module.app.login.AppLoginInfoService;
import com.server.module.app.login.LoginInfoBean;
import com.server.module.app.login.LoginInfoEnum;
import com.server.module.app.payRecord.AppPayRecordService;
import com.server.module.app.payRecord.PayRecordDto;
import com.server.module.app.payRecord.PayRecordForm;
import com.server.module.app.vminfo.AppVminfoService;
import com.server.module.app.vminfo.VminfoDto;
import com.server.module.app.vmitem.AppVmitemService;
import com.server.module.app.vmitem.ItemDto;
import com.server.module.app.vmway.AppVmwayService;
import com.server.module.app.vmway.VendingMachinesWayBean;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.VmInfoStateEnum;

@RestController
@RequestMapping("/aliMachine")
public class MachinesController {

	public static Logger log = LogManager.getLogger(MachinesController.class); 
	@Autowired
	private CompanyService companyService;
	@Autowired
	@Qualifier("aliVminfoService")
	private AppVminfoService vminfoService;
	@Autowired
	private AppVmwayService vmwayService;
	@Autowired
	private AppPayRecordService payRecordService;
	@Autowired
	private AppLoginInfoService loginInfoService;
	@Autowired
	private AppVmitemService vmitemService;
	
	/**
	 * 查询售货机信息
	 * @author hebiting
	 * @date 2018年5月21日下午3:49:42
	 * @param form
	 * @param machineType
	 * @param request
	 * @return
	 */
	@PostMapping("/queryVMList")
	public ReturnDataUtil queryVMList(@RequestParam(required=false) String form,
			@RequestParam(required=false) Integer machineType,
			HttpServletRequest request){
		log.info("<MachinesController--queryVMList--start>");
		Long id = request.getAttribute(AdminConstant.LOGIN_USER_ID)==null?null:Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		LoginInfoBean bindLoginInfo = loginInfoService.queryById(id);
		if(bindLoginInfo!=null){
			//if(LoginInfoEnum.PRINCIPAL.getMessage().equals(bindLoginInfo.getIsPrincipal())){
				List<CompanyBean> companyList = companyService.findAllSonCompany(bindLoginInfo.getCompanyId());
				List<Integer> companyIdList = new ArrayList<Integer>();
				companyList.stream().forEach(company-> {
					companyIdList.add(company.getId());
				});
				String companyIds = StringUtils.join(companyIdList,",");
				List<VminfoDto> queryVMList=new ArrayList<VminfoDto>();
				if(companyService.checkIsReplenishCompany(bindLoginInfo.getCompanyId())) {
					queryVMList=vminfoService.queryVMListForReplenishMan(form,companyIds,VmInfoStateEnum.MACHINES_NORMAL.getCode());
				}else {
					queryVMList = vminfoService.queryVMList(form, companyIds, machineType,VmInfoStateEnum.MACHINES_NORMAL.getCode());
				}
				return ResultUtil.success(queryVMList);
			/*}else{//线路负责人只看旗下的机器
				List<VminfoDto> queryOwnVMList = vminfoService.queryOwnVMList(form, bindLoginInfo.getId(), machineType,VmInfoStateEnum.MACHINES_NORMAL.getCode());
				return ResultUtil.success(queryOwnVMList);
			}*/
		}
		return ResultUtil.error(ResultEnum.NOT_AUTHORIZED.getCode(),ResultEnum.NOT_AUTHORIZED.getMessage(),null);
	}
	/**
	 * 查询售货机货道详情信息
	 * @author hebiting
	 * @date 2018年5月21日下午3:49:55
	 * @param vmCode
	 * @param wayNo
	 * @return
	 */
	@PostMapping("/queryDetail")
	public ReturnDataUtil queryDetail(@RequestParam String vmCode,
			@RequestParam(required=false) Integer wayNo){
		log.info("<MachinesController--queryDetail--start>");
		List<VendingMachinesWayBean> vmwList = vmwayService.queryByWayAndVmcode(vmCode);
		log.info("<MachinesController--queryDetail--end>");
		if(vmwList!=null&&vmwList.size()>0){
			return ResultUtil.success(vmwList);
		}
		return ResultUtil.error();
	}
	//废弃
	/*@PostMapping("/queryTradeDetail")
	public ReturnDataUtil queryTradeDetail(@RequestBody PayRecordForm payRecordForm){
		log.info("<MachinesController--queryPayDetail--start>");
		List<PayRecordDto> payRecordList = payRecordService.findPayRecord(payRecordForm);
		log.info("<MachinesController--queryPayDetail--end>");
		if(payRecordList!=null && payRecordList.size()>0){
			return ResultUtil.success(payRecordList);
		}
		return ResultUtil.error();
	}*/
	@GetMapping("/queryTradeDetail")
	public List<PayRecordDto> queryTradeDetail(String vmCode,int day){
		List<PayRecordDto> payRecordList = payRecordService.findPayRecord(vmCode,day);
		return payRecordList;
	}
	
	/**
	 * 查询售卖机商品信息
	 * 
	 * @author hebiting
	 * @date 2018年5月5日下午4:10:52
	 * @param vmCode
	 * @return
	 */
	@GetMapping("/queryItem")
	@ResponseBody
	public ReturnDataUtil queryItem(String vmCode,Integer machineVersion) {
		log.info("<UserAuthorizationController--queryItem--start>");
		Assert.hasText(vmCode, "智能设备编号不能为空");
		List<ItemDto> queryVmitem = vmitemService.queryVmitem(vmCode,machineVersion);
		log.info("<UserAuthorizationController--queryItem--end>");
		return ResultUtil.success(queryVmitem);
	}
}
