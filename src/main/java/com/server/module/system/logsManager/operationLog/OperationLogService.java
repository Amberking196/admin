package com.server.module.system.logsManager.operationLog;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-07 13:49:31
 */
public interface OperationLogService {
	
	/**
	 * 查询 操作日志
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(OperationLogCondition condition);

	
	/**
	 * 增加 运营管理操作日志
	 * @param entity
	 * @return
	 */
	public OperationsManagementLogBean add(OperationsManagementLogBean entity);
	
	
	/**
	 * 价格修改日志 查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil priceLogListPage(PriceLogCondition condition);
}
