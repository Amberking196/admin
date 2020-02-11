package com.server.module.system.logsManager.operationLog;

import org.springframework.stereotype.Repository;

import com.server.common.persistence.BaseDao;

/**
 * author name: why create time: 2018-05-11 10:00:30
 */
@Repository
public class HeadquartersConfigDaoImpl extends BaseDao<HeadquartersConfigLogBean>implements HeadquartersConfigDao {

	/**
	 * 增加 总部配置 操作日志
	 */
	@Override
	public HeadquartersConfigLogBean insert(HeadquartersConfigLogBean entity) {
		// TODO Auto-generated method stub
		return super.insert(entity);
	}

}
