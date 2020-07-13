package com.server.module.system.logsManager.itemLog;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

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
import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr 
 * create time: 2018-03-24 09:24:43
 */
@Api(value = "ItemLogController", description = "售货机商品库变更查询")
@RestController
@RequestMapping("/itemLog")
public class ItemLogController {

	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 
	@Autowired
	private ItemLogService itemLogServiceImpl;
	@ApiOperation(value = "售货机商品库变更查询", notes = "售货机商品库变更查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil listPage(ItemLogCondition condition) {
		return itemLogServiceImpl.listPage(condition);
	}


}
