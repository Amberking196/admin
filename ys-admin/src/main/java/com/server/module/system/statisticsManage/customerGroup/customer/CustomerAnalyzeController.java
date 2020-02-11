package com.server.module.system.statisticsManage.customerGroup.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-10-18 09:08:12
 */
@Api(value = "CustomerAnalyzeController", description = "用户群体分析")
@RestController
@RequestMapping("/customerAnalyze")
public class CustomerAnalyzeController {

	@Autowired
	private CustomerAnalyzeService customerAnalyzeServiceImpl;

	@ApiOperation(value = "用户群体分析列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(CustomerAnalyzeCondition condition) {
		return customerAnalyzeServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "用户群体分析添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody CustomerAnalyzeBean entity) {
		return new ReturnDataUtil(customerAnalyzeServiceImpl.add(entity));
	}

	@ApiOperation(value = "用户群体分析修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody CustomerAnalyzeBean entity) {
		return new ReturnDataUtil(customerAnalyzeServiceImpl.update(entity));
	}

	@ApiOperation(value = "用户群体分析删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(customerAnalyzeServiceImpl.del(id));
	}
}
