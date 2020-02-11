package com.server.module.system.statisticsManage.chart;

import com.server.module.system.statisticsManage.customerGroup.customer.CustomerAnalyzeService;
import com.server.module.system.statisticsManage.customerGroup.customer.StateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jersey.repackaged.com.google.common.collect.Maps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.statisticsManage.customerGroup.customerEvent.CustomerAnalyzeEventService;
import com.server.module.system.statisticsManage.customerGroup.customerEvent.CustomerEventStateVo;

import java.util.List;
import java.util.Map;

@Api(value = "ChartController", description = "统计图表")

@RestController
@RequestMapping("/chart")
public class ChartController {
	private final static Logger log = LogManager.getLogger(ChartController.class);

	@Autowired
	ChartService chartService;
	
	@Autowired
	CustomerAnalyzeEventService customerAnalyzeEventService;
	@Autowired
	CustomerAnalyzeService customerAnalyzeService;

	@ApiOperation(value = "基础功能集合", notes = "baseFunctions", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/baseFunctions")
	public Map<String,String> baseFunctions(){//2018-06-07 00:00:00
		return ChartUtil.baseFunctionMap;
	}

	@ApiOperation(value = "基础数据统计", notes = "statisticsMachines", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/baseStatistics")
	public MultipleResponseData baseStatistics(ChartForm form){
		return chartService.baseStatistics(form);
	}
	@ApiOperation(value = "24小时订单分布", notes = "orderByHour", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/orderByHour")
	public MultipleResponseData orderByHour(ChartForm form){
		return chartService.orderByHour(form);
	}


	@ApiOperation(value = "商品销售统计", notes = "goodsStatistics", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/goodsStatistics")
	public MultipleResponseData goodsStatistics(ChartForm form){
		return chartService.goodsStatistics(form);
	}

	@ApiOperation(value = "所有在售商品", notes = "listGoods", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/listGoods")
	public Map<Integer,String> listGoods(){
		return chartService.listGoods();
	}
	
	
	@ApiOperation(value = "用户群组功能集合", notes = "listChangeState", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/listChangeState")
	public Map<String,String> listChangeState(){//2018-06-07 00:00:00
		List<CustomerEventStateVo> list= customerAnalyzeEventService.listStateChange();
		Map<String,String> map=Maps.newHashMap();
		
	    for(CustomerEventStateVo vo : list){
	    	map.put(vo.getFromTo(), vo.getName());
	    }
	    return map;
	}
	
	@ApiOperation(value = "用户状态转化统计", notes = "customerStateChange", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/customerStateChange")
	public MultipleResponseData customerStateChange(ChartForm form){
		return customerAnalyzeEventService.customerStateChangeStatistics(form);
	}
	@ApiOperation(value = "用户状态列表", notes = "listCustomerState", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/listCustomerState")
	public Map<Integer,String> listCustomerState(){
		List<StateVO> list= customerAnalyzeService.listState();
		Map<Integer,String> map=Maps.newHashMap();
		for(StateVO vo : list){
			map.put(vo.getState(), vo.getLabel());
		}
		return map;
	}

	@ApiOperation(value = "用户状态统计", notes = "customerState", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping("/customerState")
	public MultipleResponseData customerState(ChartForm form){
		form.setType(0);
		return customerAnalyzeEventService.customerStateStatistics(form);
	}

}
