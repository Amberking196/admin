package com.server.module.system.logsManager.operationLog;

/**
 * author name: why create time: 2018-05-11 10:17:10
 */
public interface JurisdictionLogService {

	/**
	 * 增加 权限操作日志
	 * @param entity
	 * @return
	 */
	public JurisdictionLogBean insert(JurisdictionLogBean entity);
}
