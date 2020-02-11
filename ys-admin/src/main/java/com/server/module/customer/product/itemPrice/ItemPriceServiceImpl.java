package com.server.module.customer.product.itemPrice;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemPriceServiceImpl implements ItemPriceService{

	private final static Logger log = LogManager.getLogger(ItemPriceServiceImpl.class);
	
	@Autowired
	private ItemPriceDao itemPriceDao;

	@Override
	public ItemPriceBean insert(ItemPriceBean itemPrice) {
		log.info("ItemPriceServiceImpl--insert--start");
		ItemPriceBean insert = itemPriceDao.insert(itemPrice);
		log.info("ItemPriceServiceImpl--insert--end");
		return insert;
	}

	@Override
	public boolean update(ItemPriceBean itemPrice) {
		log.info("ItemPriceServiceImpl--update--start");
		boolean update = itemPriceDao.update(itemPrice);
		log.info("ItemPriceServiceImpl--update--end");
		return update;
	}

	@Override
	public List<ItemPriceDto> getPriceByBasicItemId(Long basicItemId) {
		log.info("ItemPriceServiceImpl--getPriceByBasicItemId--start");
		List<ItemPriceDto> priceList = itemPriceDao.getPriceByBasicItemId(basicItemId);
		log.info("ItemPriceServiceImpl--getPriceByBasicItemId--end");
		return priceList;
	}
}
