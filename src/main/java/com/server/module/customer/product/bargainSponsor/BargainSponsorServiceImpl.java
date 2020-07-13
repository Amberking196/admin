package com.server.module.customer.product.bargainSponsor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author why
 * @date: 2018年12月24日 上午9:21:38
 */
@Service
public class BargainSponsorServiceImpl implements BargainSponsorService{

	private Logger log=LogManager.getLogger(BargainSponsorServiceImpl.class);
	@Autowired
	private BargainSponsorDao BargainSponsorDaoImpl;
	
	
	@Override
	public Map<String, Object> bargainList() {
		log.info("<BargainSponsorServiceImpl>-----<bargainList>------start");
		Map<String, Object> map=new HashMap<String, Object>();
		List<BargainSponsorBean> bargainGoodsList = BargainSponsorDaoImpl.bargainGoodsList();
		List<BargainSponsorBean> bargainGoodsFindCustomerList = BargainSponsorDaoImpl.bargainGoodsFindCustomerList();
		map.put("bargainGoodsList", bargainGoodsList);
		map.put("bargainGoodsFindCustomerList", bargainGoodsFindCustomerList);
		log.info("<BargainSponsorServiceImpl>-----<bargainList>------end");
		return map;
	}

	public BargainSponsorBean bargainDetails(Long id) {
		log.info("<BargainSponsorServiceImpl>-----<bargainDetails>------start");
		BargainSponsorBean bean = BargainSponsorDaoImpl.bargainDetails(id);
		log.info("<BargainSponsorServiceImpl>-----<bargainDetails>------end");
		return bean;
	}

	@Override
	public List<BargainSponsorBean> bargainGoodsList() {
		log.info("<BargainSponsorServiceImpl>-----<bargainGoodsList>------start");
		List<BargainSponsorBean> bargainGoodsList = BargainSponsorDaoImpl.bargainGoodsList();
		log.info("<BargainSponsorServiceImpl>-----<bargainGoodsList>------end");
		return bargainGoodsList;
	}

	@Override
	public boolean isCanBargain(Long id) {
		log.info("<BargainSponsorServiceImpl>-----<isCanBargain>------start");
		boolean flag = BargainSponsorDaoImpl.isCanBargain(id);
		log.info("<BargainSponsorServiceImpl>-----<isCanBargain>------end");
		return flag;
	}
	
}
