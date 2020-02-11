package com.server.module.system.logsManager.replenishLog;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.server.common.persistence.Page;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr create time: 2018-03-23 11:24:53
 */
@Api(value = "ReplenishLogController", description = "货道商品变更api")
@RestController
@RequestMapping("/replenishLog")
public class ReplenishLogController {

	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 
 
	@Autowired
	private ReplenishLogService replenishLogServiceImpl;
	
	@ApiOperation(value = "货道变更商品查询", notes = "货道变更商品查询", httpMethod = "POST")//, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil listPage(ReplenishLogCondition condition) {
		
		System.out.println("replenishLogServiceImpl into --======");
		//condition.setCompanyId(companyId);
	   return  replenishLogServiceImpl.listPage(condition);

	} 
}
