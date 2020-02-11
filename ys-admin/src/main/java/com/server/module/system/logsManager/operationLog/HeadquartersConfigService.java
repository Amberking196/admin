package com.server.module.system.logsManager.operationLog;


/**
 * author name: why create time: 2018-05-11 10:02:30
 */
public interface HeadquartersConfigService {

	/**
	 * 增加 总部配置 操作日志
	 * @param entity
	 * @return
	 */
	public HeadquartersConfigLogBean insert(HeadquartersConfigLogBean entity);
}
