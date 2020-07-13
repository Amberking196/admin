package com.server.module.customer.product.spellGroupShare;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author name: why create time: 2018-11-15 15:05:14
 */
@Service
public class SpellGroupShareServiceImpl implements SpellGroupShareService {

	private static Logger log = LogManager.getLogger(SpellGroupShareServiceImpl.class);
	
	@Autowired
	private SpellGroupShareDao spellGroupShareDaoImpl;
	
	@Override
	public SpellGroupShareBean list(Long customerSpellGroupId) {
		log.info("<SpellGroupShareServiceImpl>-----<list>-----start");
		SpellGroupShareBean bean = spellGroupShareDaoImpl.list(customerSpellGroupId);
		log.info("<SpellGroupShareServiceImpl>-----<list>-----end");
		return bean;
	}

	@Override
	public SpellGroupShareBean payFinishList(Long orderId) {
		log.info("<SpellGroupShareServiceImpl>-----<payFinishList>-----start");
		SpellGroupShareBean bean = spellGroupShareDaoImpl.payFinishList(orderId);
		log.info("<SpellGroupShareServiceImpl>-----<payFinishList>-----end");
		return bean;
	}
}
