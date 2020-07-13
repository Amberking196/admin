package com.server.module.system.warehouseManage.warehouseReplenish;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
 import java.util.List;

import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoServiceImpl;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-05-28 17:58:52
 */ 
@Service
public class  WarehouseReplenishServiceImpl  implements WarehouseReplenishService{

	public static Logger log = LogManager.getLogger(WarehouseInfoServiceImpl.class); 
	@Autowired
	private WarehouseReplenishDao WarehouseReplenishDaoImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	public ReturnDataUtil listPage(WarehouseReplenishForm warehouseReplenishForm){
		log.info("<WarehouseReplenishServiceImpl>------<listPage>-----start");

		returnDataUtil=WarehouseReplenishDaoImpl.listPage(warehouseReplenishForm);
		log.info("<WarehouseReplenishServiceImpl>------<listPage>-----end");
		return returnDataUtil;

	}	

}

