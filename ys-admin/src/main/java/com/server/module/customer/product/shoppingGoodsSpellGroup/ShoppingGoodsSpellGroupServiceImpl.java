package com.server.module.customer.product.shoppingGoodsSpellGroup;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-10-16 16:41:04
 */
@Service
public class ShoppingGoodsSpellGroupServiceImpl implements ShoppingGoodsSpellGroupService {

	private static Logger log = LogManager.getLogger(ShoppingGoodsSpellGroupServiceImpl.class);
	@Autowired
	private ShoppingGoodsSpellGroupDao shoppingGoodsSpellGroupDaoImpl;

	public ReturnDataUtil listPage(ShoppingGoodsSpellGroupForm condition) {
		return shoppingGoodsSpellGroupDaoImpl.listPage(condition);
	}

	public ShoppingGoodsSpellGroupBean add(ShoppingGoodsSpellGroupBean entity) {
		log.info("<ShoppingGoodsSpellGroupServiceImpl>-----<add>-----start");
		ShoppingGoodsSpellGroupBean bean = shoppingGoodsSpellGroupDaoImpl.insert(entity);
		log.info("<ShoppingGoodsSpellGroupServiceImpl>-----<add>-----end");
		return bean;
	}

	public boolean update(ShoppingGoodsSpellGroupBean entity) {
		return shoppingGoodsSpellGroupDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return shoppingGoodsSpellGroupDaoImpl.delete(id);
	}

	public List<ShoppingGoodsSpellGroupBean> list(ShoppingGoodsSpellGroupForm condition) {
		return null;
	}

	public ShoppingGoodsSpellGroupBean get(Object id) {
		return shoppingGoodsSpellGroupDaoImpl.get(id);
	}

	@Override
	public ShoppingGoodsSpellGroupBean isConglomerateCommodity(Long shoppingGoodsId) {
		log.info("<ShoppingGoodsSpellGroupServiceImpl>------<isConglomerateCommodity>------start");
		ShoppingGoodsSpellGroupBean bean = shoppingGoodsSpellGroupDaoImpl.isConglomerateCommodity(shoppingGoodsId);
		return bean;
	}
}
