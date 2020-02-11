package com.server.module.system.machineManage.machineTemperature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

@Service
public class MachineTemperatureServiceImpl implements MachineTemperatureService {

	public static Logger log = LogManager.getLogger(MachineTemperatureServiceImpl.class);
	@Autowired
	private MachineTemperatureDao machineTemperatureDaoImpl;
	
	/**
	 * 查询所有的售货机温度
	 */
	@Override
	public ReturnDataUtil listPage(MachineTemperatureForm machineTemperatureForm) {
		log.info("<MachineTemperatureServiceImpl>----<listPage>----start");
		ReturnDataUtil listPage = machineTemperatureDaoImpl.listPage(machineTemperatureForm);
		log.info("<MachineTemperatureServiceImpl>----<listPage>----end");
		return listPage;
	}

	@Override
	public boolean updateMachinesProgramVersion(String factoryNum, String versionInfo) {
		log.info("<MachineTemperatureServiceImpl>----<updateMachinesVersion>----start");
		boolean result = machineTemperatureDaoImpl.updateMachinesProgramVersion(factoryNum, versionInfo);
		log.info("<MachineTemperatureServiceImpl>----<updateMachinesVersion>----end");
		return result;
	}

}
