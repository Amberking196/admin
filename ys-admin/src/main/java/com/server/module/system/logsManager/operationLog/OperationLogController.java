package com.server.module.system.logsManager.operationLog;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyService;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-05-07 13:49:31
 */
@Api(value = "OperationLogController", description = "操作日志")
@RestController
@RequestMapping("/operationLog")
public class OperationLogController {

	@Autowired
	private OperationLogService operationLogServiceImpl;
	@Autowired
	private CompanyService companyService;
	

	@ApiOperation(value = "操作日志列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(OperationLogCondition condition) {
		return operationLogServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "价格修改日志列表", notes = "priceLogListPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/priceLogListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil priceLogListPage(PriceLogCondition condition) {
		List<Integer> companyList = companyService.findAllSonCompanyId(UserUtils.getUser().getCompanyId());
		String companyIds = StringUtils.join(companyList, ",");
		condition.setCompanyIds(companyIds);
		return operationLogServiceImpl.priceLogListPage(condition);
	}
	

	
}
