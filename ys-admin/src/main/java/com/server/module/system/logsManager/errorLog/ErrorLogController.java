package com.server.module.system.logsManager.errorLog;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
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
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr create time: 2018-03-24 09:57:07
 */
@Api(value = "ErrorLogController", description = "售货机错误日志")
@RestController
@RequestMapping("/errorLog")
public class ErrorLogController {

	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 
	@Autowired
	private ErrorLogService errorLogServiceImpl;

	@ApiOperation(value = "售货机错误日志查询", notes = "售货机错误日志查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil listPage(ErrorLogCondition condition) {
		return errorLogServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "售货机错误日志修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody ErrorLogBean entity) {
		entity.setSolveTime(new Date());
		return new ReturnDataUtil(errorLogServiceImpl.update(entity));
	}
}
