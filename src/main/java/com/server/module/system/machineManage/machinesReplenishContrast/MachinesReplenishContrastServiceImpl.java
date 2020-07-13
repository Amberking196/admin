package com.server.module.system.machineManage.machinesReplenishContrast;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;


/**
 * 
 * @author why
 * @date: 2019年5月14日 下午5:48:45
 */
@Service
public class MachinesReplenishContrastServiceImpl implements MachinesReplenishContrastService {

	private static Logger log = LogManager.getLogger(MachinesReplenishContrastServiceImpl.class);
	@Autowired
	private MachinesReplenishContrastDao machinesReplenishContrastDaoImpl;

	public ReturnDataUtil listPage(MachinesReplenishContrastForm machinesReplenishContrastForm) {
		log.info("<MachinesReplenishContrastServiceImpl>----------<listPage>--------start");
		ReturnDataUtil listPage = machinesReplenishContrastDaoImpl.listPage(machinesReplenishContrastForm);
		log.info("<MachinesReplenishContrastServiceImpl>----------<listPage>--------end");
		return listPage;
	}

	public MachinesReplenishContrastBean add(MachinesReplenishContrastBean entity) {
		return machinesReplenishContrastDaoImpl.insert(entity);
	}

	public boolean update(MachinesReplenishContrastBean entity) {
		return machinesReplenishContrastDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return machinesReplenishContrastDaoImpl.delete(id);
	}

	public List<MachinesReplenishContrastBean> list(MachinesReplenishContrastForm machinesReplenishContrastForm) {
		return null;
	}

	public MachinesReplenishContrastBean get(Object id) {
		return machinesReplenishContrastDaoImpl.get(id);
	}
}
