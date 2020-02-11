package com.server.module.system.statisticsManage.customerGroup.customerEvent;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-10-18 09:10:39
 */
@Api(value = "CustomerAnalyzeEventController", description = "用户群体分析日志")
@RestController
@RequestMapping("/customerAnalyzeEvent")
public class CustomerAnalyzeEventController {

	@Autowired
	private CustomerAnalyzeEventService customerAnalyzeEventServiceImpl;

	@ApiOperation(value = "用户群体分析日志列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(CustomerAnalyzeEventCondition condition) {
		return customerAnalyzeEventServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "用户群体分析日志添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody CustomerAnalyzeEventBean entity) {
		return new ReturnDataUtil(customerAnalyzeEventServiceImpl.add(entity));
	}

	@ApiOperation(value = "用户群体分析日志修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody CustomerAnalyzeEventBean entity) {
		return new ReturnDataUtil(customerAnalyzeEventServiceImpl.update(entity));
	}

	@ApiOperation(value = "用户群体分析日志删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(customerAnalyzeEventServiceImpl.del(id));
	}
	@ApiOperation(value = "用户群体转化状态列表", notes = "listChangeState", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listChangeState", produces = "application/json;charset=UTF-8")
	public List<CustomerEventStateVo> listChangeState(){
		List<CustomerEventStateVo> list=Lists.newArrayList();	
		CustomerEventStateVo vo1=new CustomerEventStateVo("1,2","一次->活跃");
		CustomerEventStateVo vo2=new CustomerEventStateVo("1,4","一次->低频");
		CustomerEventStateVo vo3=new CustomerEventStateVo("2,3","活跃->忠实");
		CustomerEventStateVo vo4=new CustomerEventStateVo("2,4","活跃->低频");
		CustomerEventStateVo vo5=new CustomerEventStateVo("4,6","低频->回流");
		CustomerEventStateVo vo6=new CustomerEventStateVo("4,5","低频->流失");
		CustomerEventStateVo vo7=new CustomerEventStateVo("5,6","流失->回流");
		CustomerEventStateVo vo8=new CustomerEventStateVo("3,2","忠实->活跃");
		list.add(vo1);list.add(vo2);list.add(vo3);list.add(vo4);
		list.add(vo5);list.add(vo6);list.add(vo7);list.add(vo8);
		 
		return list;
	}
}
