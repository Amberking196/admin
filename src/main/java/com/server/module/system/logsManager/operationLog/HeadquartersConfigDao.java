package com.server.module.system.logsManager.operationLog;

/**
 * author name: why create time: 2018-05-11 09:53:31
 */
public interface HeadquartersConfigDao {

	/**
	 * 增加 总部配置 操作日志
	 * @param entity
	 * @return
	 */
	public HeadquartersConfigLogBean insert(HeadquartersConfigLogBean entity);
}
