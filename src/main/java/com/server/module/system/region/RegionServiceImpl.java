package com.server.module.system.region;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl implements RegionService{

	private final static Logger log = LogManager.getLogger(RegionServiceImpl.class);
	
	@Autowired
	private RegionDao regionDao;

	@Override
	public List<RegionBean> getByParentId(Integer parentId) {
		log.info("<RegionServiceImpl--getByParentId--start>");
		List<RegionBean> byParentId = regionDao.getByParentId(parentId);
		log.info("<RegionServiceImpl--getByParentId--end>");
		return byParentId;
	}

	@Override
	public String getNameById(Integer id) {
		log.info("<RegionServiceImpl--getNameById--start>");
		String nameById = regionDao.getNameById(id);
		log.info("<RegionServiceImpl--getNameById--end>");
		return nameById;
	}

	@Override
	public String getNameById(Integer province, Integer city, Integer area) {
		String provinceName = regionDao.getNameById(province);
		String cityName = regionDao.getNameById(city);
		String areaName = regionDao.getNameById(area);
		String address = provinceName +"省"+cityName+areaName;
		return address;
	}

	@Override
	public boolean canEnterAli(String vmCode) {
		log.info("<RegionServiceImpl--getNameById--start>");
		boolean canEnterAli = regionDao.canEnterAli(vmCode);
		log.info("<RegionServiceImpl--getNameById--start>");
		return canEnterAli;
	}
}
