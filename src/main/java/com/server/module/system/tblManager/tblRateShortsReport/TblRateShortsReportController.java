package com.server.module.system.tblManager.tblRateShortsReport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr 
 * create time: 2018-03-28 08:58:56
 */
@Api(value = "tblRateShortsReportController", description = "缺货报表")
@RestController
@RequestMapping("/tblRateShortsReport")
public class TblRateShortsReportController {

	@Autowired
	private TblRateShortsReportService tblRateShortsReportServiceImpl;

	@ApiOperation(value = "list", notes = "缺货报表查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object list(TblRateShortsReportCondition condition) {
		ReturnDataUtil redata=new ReturnDataUtil();
		if(condition.getStartTime()==null){
			redata.setStatus(0);
			redata.setMessage("开始时间不能为空");
			return redata;
		}
		if(condition.getEndTime()==null){
			redata.setStatus(0);
			redata.setMessage("结束时间不能为空");
			return redata;
		}
		
		return tblRateShortsReportServiceImpl.list(condition);
	}
}
