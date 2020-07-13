package com.server.module.system.logsManager.messageLog;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.server.common.persistence.Page;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr create time: 2018-03-24 14:45:24
 */
@Api(value = "messageLogController", description = "错误短信查询")
@RestController
@RequestMapping("/messageLog")
public class MessageLogController {

	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 	@Autowired
	private MessageLogService messageLogServiceImpl;
	 @ApiOperation(value = "listPage", notes = "错误信息查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	 @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	 @ResponseBody
	 public ReturnDataUtil listPage(MessageLogCondition condition){
		 return messageLogServiceImpl.listPage(condition);
		 }

	/*
	 * public Page listPage(Integer companyId, String vmCode, String payCode,
	 * Page page) {
	 * 
	 * return messageLogServiceImpl.listPage(companyId, vmCode, payCode, page);
	 * }
	 */

}
