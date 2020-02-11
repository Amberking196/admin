package com.server.module.system.machineManage.machinesBadOpenLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachinesBadOpenServiceImpl implements MachinesBadOpenService{

	@Autowired
	private MachinesBadOpenDao machinesBadOpenDao;

	@Override
	public BadOpenLogBean getByPhone(String phone) {
		
		return machinesBadOpenDao.getByPhone(phone);
	}
}
