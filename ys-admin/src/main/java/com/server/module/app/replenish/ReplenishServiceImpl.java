package com.server.module.app.replenish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplenishServiceImpl implements ReplenishService{

	private static Logger log = LogManager.getLogger(ReplenishDaoImpl.class);
	
	@Autowired
	private ReplenishDao replenishDao;

//	@Override
//	public List<ReplenishDto> queryReplenish(ReplenishForm form) {
//		log.info("<ReplenishServiceImpl--queryReplenish--start>");
//		List<ReplenishDto> queryReplenish = replenishDao.queryReplenish(form);
//		log.info("<ReplenishServiceImpl--queryReplenish--end>");
//		return queryReplenish;
//	}


	@Override
	public List<ReplenishDto> queryReplenish(String inCodes,int version) {
		log.info("<ReplenishServiceImpl--queryReplenish--start>");
		List<ReplenishDto> queryReplenish = replenishDao.queryReplenish(inCodes,version);
		log.info("<ReplenishServiceImpl--queryReplenish--end>");
		return queryReplenish;
	}
}
