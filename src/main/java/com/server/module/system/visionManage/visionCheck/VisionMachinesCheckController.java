package com.server.module.system.visionManage.visionCheck;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.UserVoForSelect;
import com.server.module.system.machineManage.machineList.MachinesInfoAndBaseDto;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.machineManage.machinesWayItem.MachinesClient;
import com.server.module.system.statisticsManage.payRecordPerWeek.PayRecordPerWeekForm;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * author name: yjr
 * create time: 2019-10-10 10:54:34
 */ 
@Api(value ="VisionMachinesCheckController",description="视觉检测")
@RestController
@RequestMapping("/visionMachinesCheck")
public class  VisionMachinesCheckController{


	@Autowired
	private VisionMachinesCheckService visionMachinesCheckServiceImpl;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoServiceImpl;
	@Autowired
	private MachinesClient machinesClient;
	
	@ApiOperation(value = "视觉检测列表",notes = "listPage",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) VisionMachinesCheckCondition condition){
		if(condition==null){
			condition = new VisionMachinesCheckCondition();
		}
		return visionMachinesCheckServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "视觉检测列表明细",notes = "detail",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil detail(@RequestBody VisionMachinesCheckCondition condition){
		return visionMachinesCheckServiceImpl.detail(condition);
	}
	
	@ApiOperation(value = "视觉检测添加",notes = "add",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  add(@RequestBody VisionMachinesCheckBean entity){
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmss");
		String d = sf.format(new Date());
		entity.setCheckId(d);
		MachinesInfoAndBaseDto dto=vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(entity.getVmCode());
		String command=genCheckCommandStr(dto.getFactoryNumber(),1,d);
		machinesClient.sendVisionCommand(dto.getFactoryNumber(),command,UserUtils.getUser().getId().toString());
		return new ReturnDataUtil(visionMachinesCheckServiceImpl.add(entity));
	}
	
	@ApiOperation(value = "视觉重启",notes = "reboot",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/reboot", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil reboot(@RequestBody VisionMachinesCheckBean entity){
		MachinesInfoAndBaseDto dto=vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(entity.getVmCode());
		String command=genRebootCommandStr(dto.getFactoryNumber(),1);
		String result=machinesClient.sendVisionCommand(dto.getFactoryNumber(),command,UserUtils.getUser().getId().toString());
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(result.equals("0")) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("重启成功");

		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("重启失败");

		}
		return returnDataUtil;
	}
	
	@ApiOperation(value = "视觉更新",notes = "update",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil  update(@RequestBody VisionMachinesCheckBean entity){
		MachinesInfoAndBaseDto dto=vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(entity.getVmCode());
		String command="update";
		String result=machinesClient.sendVisionCommand(dto.getFactoryNumber(),command,UserUtils.getUser().getId().toString());
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(result.equals("0")) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("更新成功");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("更新失败");

		}
		return returnDataUtil;
	}
	
	@ApiOperation(value = "视觉检测删除",notes = "del",  httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object  id){
		return new ReturnDataUtil(visionMachinesCheckServiceImpl.del(id));
		
	}

	//获取检测的校验码
	public static String genCheckCommandStr(String factoryNumber, int way,String orderId) {
		//   n:111380310016;d:0,1,0,0;v:99&44$
		//   n:000000000000;d:8;v:7;l:10&21$
		//   fn:open;n:****;order:******;door:*******
	    //   fn:check;n:****;order:******;door:*******
		StringBuilder sb = new StringBuilder();
		sb.append("fn:"+"check");
		sb.append(";n:"+factoryNumber+";order:"+orderId);
		sb.append(";door:"+way);
		sb.append("\n");
		//String doorstr="0,0,0,0";
		//int len = sumString(factoryNumber) + way + 18 + 9;
		//sb.append(";v:9;l:99;&" + len + "$");
		//sb.append("o:"+id);
	
		return sb.toString();
	}
	
	//获取重启的校验码
	public static String genRebootCommandStr(String factoryNumber, int way) {
		//   n:111380310016;d:0,1,0,0;v:99&44$
		//   n:000000000000;d:8;v:7;l:10&21$
		//   fn:open;n:****;order:******;door:*******
	    //   fn:check;n:****;order:******;door:*******
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmss");
		String orderId = sf.format(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append("fn:"+"reboot");
		sb.append(";n:"+factoryNumber+";order:"+orderId);
		sb.append(";door:"+way);
		sb.append("\n");
		//String doorstr="0,0,0,0";
		//int len = sumString(factoryNumber) + way + 18 + 9;
		//sb.append(";v:9;l:99;&" + len + "$");
		//sb.append("o:"+id);
	
		return sb.toString();
	}
}
