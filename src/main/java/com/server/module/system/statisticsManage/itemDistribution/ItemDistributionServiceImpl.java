package com.server.module.system.statisticsManage.itemDistribution;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDistributionServiceImpl implements ItemDistributionService{

	private final static Logger log = LogManager.getLogger(ItemDistributionServiceImpl.class);
	
	@Autowired
	private ItemDistributionDao itemDistributionDao;

	@Override
	public List<ItemDistributionDto> queryDistribution(ItemDistributionForm form) {
		log.info("<ItemDistributionServiceImpl--queryDistribution--start>");
		List<ItemDistributionDto> queryDistribution = null;
		if(form.getVersion() == null || form.getVersion() == 1){
			queryDistribution = itemDistributionDao.queryDistribution(form);
		}else{
			queryDistribution = itemDistributionDao.queryDisTributionVer2(form);
		}
		log.info("<ItemDistributionServiceImpl--queryDistribution--end>");
		return queryDistribution;
	}

	@Override
	public Long queryDistributionNum(ItemDistributionForm form) {
		log.info("<ItemDistributionServiceImpl--queryDistributionNum--start>");
		Long total = 0L;
		if(form.getVersion() == null || form.getVersion() == 1){
			total = itemDistributionDao.queryDistributionNum(form);
		}else{
			total = itemDistributionDao.queryDistributionNumVer2(form);
		}
		log.info("<ItemDistributionServiceImpl--queryDistributionNum--end>");
		return total;
	}
	
	
}
