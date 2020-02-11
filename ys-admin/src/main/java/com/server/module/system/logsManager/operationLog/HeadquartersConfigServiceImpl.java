package com.server.module.system.logsManager.operationLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author name: why create time: 2018-05-11 10:10:15
 */
@Service
public class HeadquartersConfigServiceImpl implements HeadquartersConfigService {

	@Autowired
	private HeadquartersConfigDao headquartersConfigDaoImpl;
	
	@Override
	public HeadquartersConfigLogBean insert(HeadquartersConfigLogBean entity) {
		// TODO Auto-generated method stub
		return headquartersConfigDaoImpl.insert(entity);
	}

}
