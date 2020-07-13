package com.server.module.system.statisticsManage.userActiveDegree;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.commonBean.TotalResultBean;

@Service
public class UserActiveDegreeServiceImpl implements UserActiveDegreeService{
	
	private final static Logger log = LogManager.getLogger(UserActiveDegreeServiceImpl.class);

	@Autowired
	private UserActiveDegreeDao userActiveDegreeDao;

	@Override
	public TotalResultBean<List<UserActiveDegreeBean>> calculateUserActiveGegree(UserActiveDegreeForm form) {
		log.info("UserActiveDegreeServiceImpl--calculateUserActiveGegree--start");
		TotalResultBean<List<UserActiveDegreeBean>> activeTotalResult = userActiveDegreeDao.
				calculateUserActiveGegree(form);
		log.info("UserActiveDegreeServiceImpl--calculateUserActiveGegree--end");
		return activeTotalResult;
	}
	
	
}
