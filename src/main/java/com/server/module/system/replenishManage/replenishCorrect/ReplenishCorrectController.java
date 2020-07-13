package com.server.module.system.replenishManage.replenishCorrect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lly835.bestpay.utils.JsonUtil;
import com.server.module.app.home.ResultEnum;
import com.server.module.system.adminUser.AdminConstant;
import com.server.util.DateUtil;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
@RestController
@RequestMapping("/corect")
public class ReplenishCorrectController {
	
	private final static Logger log = LogManager.getLogger(ReplenishCorrectController.class);

	@Autowired
	private ReplenishCorrectService replenishCorrectService;
	
	@PostMapping("/getReplenishInfo")
	public ReturnDataUtil replenishCorrect(@RequestBody ReplenishForm form,HttpServletRequest request){
		log.debug("ReplenishForm="+JsonUtil.toJson(form));
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		Integer companyId = (Integer)request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
		if(userId == null){
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}
		form.setCompanyId(companyId);
//		if(StringUtils.isBlank(form.getVmCode())){
//			return ResultUtil.error(ResultEnum.MACHINE_NOT_EXIST);
//		}
		if(form.getEndTime() == null && form.getStartTime() == null){
			form.setStartTime(DateUtil.getTodayStart());
			form.setEndTime(DateUtil.getTodayEnd());
		}else if(form.getEndTime() == null || form.getStartTime() == null){
			if(form.getEndTime() == null){
				form.setEndTime(DateUtil.getSomeDayEnd(form.getStartTime()));
			}
			if(form.getStartTime() == null){
				form.setStartTime(DateUtil.getSomeDayStart(form.getEndTime()));
			}
		}
		List<ReplenishCollectDto> replenishInfo = replenishCorrectService.getReplenishInfo(form);
		if(replenishInfo!= null &&  replenishInfo.size()>0){
			return ResultUtil.success(replenishInfo);
		}
		return ResultUtil.error();
	}
	@PostMapping("/updateReplenish")
	public ReturnDataUtil updateReplenish(String ids,HttpServletRequest request){
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		if(userId == null){
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}
		log.debug("参数："+ids);
		boolean result = replenishCorrectService.updateReplenishInfo(ids);
		if(result){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	@PostMapping("/getReplenishProcess")
	public ReturnDataUtil replenishProcess(@RequestBody ReplenishForm form,HttpServletRequest request){
		log.debug("replenishProcess="+JsonUtil.toJson(form));
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		Integer companyId = (Integer)request.getAttribute(AdminConstant.LOGIN_USER_COMPANYID);
		if(userId == null){
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}
		if(StringUtils.isBlank(form.getVmCode())){
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		form.setCompanyId(companyId);
		if(form.getEndTime() == null && form.getStartTime() == null){
			form.setStartTime(DateUtil.getTodayStart());
			form.setEndTime(DateUtil.getTodayEnd());
		}else if(form.getEndTime() == null || form.getStartTime() == null){
			if(form.getEndTime() == null){
				form.setEndTime(DateUtil.getSomeDayEnd(form.getStartTime()));
			}
			if(form.getStartTime() == null){
				form.setStartTime(DateUtil.getSomeDayStart(form.getEndTime()));
			}
		}
		List<ReplenishCollectDto> replenishInfo = replenishCorrectService.getReplenishProcess(form);
		if(replenishInfo!= null &&  replenishInfo.size()>0){
			return ResultUtil.success(replenishInfo);
		}
		return ResultUtil.error();
	}
	
}
