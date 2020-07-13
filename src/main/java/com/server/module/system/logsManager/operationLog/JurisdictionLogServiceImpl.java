package com.server.module.system.logsManager.operationLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author name: why create time: 2018-05-11 10:20:20
 */
@Service
public class JurisdictionLogServiceImpl implements JurisdictionLogService {

	@Autowired
	private JurisdictionLogDao  jurisdictionLogDaoImpl;

	/**
	 * 增加 权限操作日志
	 */
	public JurisdictionLogBean insert(JurisdictionLogBean entity) {
		return jurisdictionLogDaoImpl.insert(entity);
	}
}
