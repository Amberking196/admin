package com.server.module.system.logsManager.exportLog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.filter.WebFilter;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-09-14 10:31:38
 */
@Api(value = "ExportLogController", description = "采购单下的商品")
@RestController
@RequestMapping("/exportLog")
public class ExportLogController {
	public static Logger log = LogManager.getLogger(ExportLogController.class);
	@Autowired
	private ExportLogService exportLogServiceImpl;

	@ApiOperation(value = "采购单下的商品列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false)ExportLogCondition condition) {
		log.info("---------<ExportLogController>--------<listPage>--------start");
		ReturnDataUtil data=null;
		if(condition==null) {
			condition=new ExportLogCondition();
		}
		data=exportLogServiceImpl.listPage(condition);
		if(data.getStatus()==0) {//查询成功
			data.setStatus(1);
			data.setMessage("查询成功");
		}else {
			data.setStatus(0);
			data.setMessage("查询失败");
		}
		log.info("---------<ExportLogController>--------<listPage>--------end");
		return data;
	}

	@ApiOperation(value = "采购单下的商品添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody ExportLogBean entity) {
		return new ReturnDataUtil(exportLogServiceImpl.add(entity));
	}

	@ApiOperation(value = "采购单下的商品修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody ExportLogBean entity) {
		return new ReturnDataUtil(exportLogServiceImpl.update(entity));
	}

	@ApiOperation(value = "采购单下的商品删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(exportLogServiceImpl.del(id));
	}
}
