package com.server.module.app.vmway;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("aliVmwayService")
public class AppVmwayServiceImpl implements AppVmwayService{

	public static Logger log = LogManager.getLogger(AppVmwayServiceImpl.class); 
	@Autowired
	@Qualifier("aliVmwayDao")
	private AppVmwayDao vmwayDao;
	@Override
	public VendingMachinesWayBean queryByWayAndVmcode(String vmCode, Integer wayNo) {
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--start>");
		VendingMachinesWayBean vmwList = vmwayDao.queryByWayAndVmcode(vmCode, wayNo);
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--end>");
		return vmwList;
	}
	
	@Override
	public List<VendingMachinesWayBean> queryByWayAndVmcode(String vmCode) {
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--start>");
		List<VendingMachinesWayBean> vmwList = vmwayDao.queryByWayAndVmcode(vmCode);
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--start>");
		return vmwList;
	}

}
