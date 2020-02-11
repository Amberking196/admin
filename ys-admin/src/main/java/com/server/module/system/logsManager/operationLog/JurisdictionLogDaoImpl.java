package com.server.module.system.logsManager.operationLog;

import org.springframework.stereotype.Repository;

import com.server.common.persistence.BaseDao;

/**
 * author name: why create time: 2018-05-11 10:13:05
 */
@Repository
public class JurisdictionLogDaoImpl extends BaseDao<JurisdictionLogBean> implements JurisdictionLogDao{

	/**
	 * 增加 权限 操作日志
	 * @param entity
	 * @return
	 */
	public JurisdictionLogBean insert(JurisdictionLogBean entity) {
		return super.insert(entity);
	}
}
