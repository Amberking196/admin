package com.server.module.system.onlineUpdate;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.app.home.ResultEnum;
import com.server.module.system.machineManage.machineBase.VendingMachinesBaseService;
import com.server.module.system.machineManage.machineCode.VendingMachinesCodeService;
import com.server.module.system.machineManage.machineTemperature.MachineTemperatureService;
import com.server.module.system.machineManage.machinesWayItem.MachinesClient;
import com.server.redis.RedisClient;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/onlineUpdate")
public class OnlineUpdateController {
	
	@Autowired
	private MachinesClient machinesClient;
	@Autowired
	private VendingMachinesCodeService vmCodeService;
	@Autowired
	private MachineTemperatureService machineTemperatureService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private VendingMachinesBaseService vendingMachinesBaseService;
	
	private static final String ONLINE_UPDATE = "updateMachine";
	private final static Logger log = LogManager.getLogger(OnlineUpdateController.class);

	/**
	 * 机器在线升级
	 * @author hebiting
	 * @date 2019年1月7日下午6:23:28
	 * @param form
	 * @return
	 */
	@PostMapping("/version")
	public ReturnDataUtil onlineUpdateVersion(@RequestBody OnlineUpdateForm form){
		if(StringUtils.isBlank(form.getVmCode()) || StringUtils.isBlank(form.getVersionInfo())){
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		String factNum = vmCodeService.getFactoryNumByVmCode(form.getVmCode());
		String reString = redisClient.get("HM-"+factNum.trim());
		if(!reString.contains("s:0")) {
			log.info("机器非空闲");
			return ResultUtil.error();//失败
		}
		String result = null;
		if(!StringUtils.isBlank(factNum)){
			if(factNum.contains("860020050")) {
				result = machinesClient.sendVisionCommand(factNum, form.getVersionInfo(), ONLINE_UPDATE);
				if(("0").equals(result)){
					redisClient.set("onlineOK"+factNum, form.getVersionInfo(), 60*10);
					return ResultUtil.success();
				}
			}else {
				result = machinesClient.sendCommand(factNum, form.getVersionInfo(), ONLINE_UPDATE);
				if(("0").equals(result)){
					redisClient.set("onlineOK"+factNum, form.getVersionInfo(), 60*10);
					return ResultUtil.success();
				}
			}
		}
		return ResultUtil.error();
	}
	
	/**
	 * 判断机器是否升级成功
	 * @author hebiting
	 * @date 2019年1月16日上午9:41:13
	 * @param vmCode
	 * @return
	 */
	@PostMapping("/isSuccess")
	public ReturnDataUtil isSuccess(String vmCode){
		if(StringUtils.isBlank(vmCode)){
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		String factNum = vmCodeService.getFactoryNumByVmCode(vmCode);
		String updateValue = redisClient.get("updateFlag_"+factNum);
		if(updateValue != null){
			redisClient.del("updateFlag_"+factNum);
			if((updateValue).contains("Updata_OK")){
				String versionInfo = redisClient.get("onlineOK"+factNum);
				redisClient.del("onlineOK"+factNum);
				machineTemperatureService.updateMachinesProgramVersion(factNum, versionInfo);
				return ResultUtil.success();
			}else if(updateValue.contains("Updata_Fail")){
				return ResultUtil.error();
			}
		}
		return ResultUtil.error(80,"升级结果未知:"+updateValue,null);
	}
	/**
	 * 重启机器
	 * @author hebiting
	 * @date 2019年1月16日上午9:41:25
	 * @param vmCode
	 * @return
	 */
	@PostMapping("/restart")
	public ReturnDataUtil restart(String vmCode){
		if(StringUtils.isBlank(vmCode)){
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		String restartMachines="";
		if(vmCode.contains("1991000")) {
			 restartMachines = machinesClient.restartVisionMachines(vmCode);
			
		}else {
			 restartMachines = machinesClient.restartMachines(vmCode);
		}
		if(("0").equals(restartMachines)){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	/**
	 * 判断是否重启成功
	 * @author hebiting
	 * @date 2019年1月30日上午9:20:32
	 * @param vmCode
	 * @return
	 */
	@PostMapping("/restartIsSuccess")
	public ReturnDataUtil restartIsSuccess(String vmCode){
		if(StringUtils.isBlank(vmCode)){
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		String factNum = vmCodeService.getFactoryNumByVmCode(vmCode);
		if(redisClient.exists("restartOK"+factNum)){
			redisClient.del("restartOK"+factNum);
			return ResultUtil.success();
		}
		return ResultUtil.error(0,"重启结果未知",null);
	}
	
	@PostMapping("/freshdb")
	public ReturnDataUtil canOnlineUpdate(@RequestBody String factoryNum){
		boolean result = vendingMachinesBaseService.updateCanOnlineUpdate(factoryNum);
		if(result){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
}
