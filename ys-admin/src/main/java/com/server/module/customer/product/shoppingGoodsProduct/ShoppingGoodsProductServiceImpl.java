package com.server.module.customer.product.shoppingGoodsProduct;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-09-14 09:53:47
 */
@Service
public class ShoppingGoodsProductServiceImpl implements ShoppingGoodsProductService {

	private static Logger log = LogManager.getLogger(ShoppingGoodsProductServiceImpl.class);
	@Autowired
	private ShoppingGoodsProductDao shoppingGoodsProductDaoImpl;

	/**
	 * 商城商品关联商品列表查询
	 */
	public ReturnDataUtil listPage(ShoppingGoodsProductForm shoppingGoodsProductForm) {
		log.info("<ShoppingGoodsProductServiceImpl>------<listPage>------start");
		ReturnDataUtil listPage = shoppingGoodsProductDaoImpl.listPage(shoppingGoodsProductForm);
		log.info("<ShoppingGoodsProductServiceImpl>------<listPage>------end");
		return listPage;
	}

	/**
	 * 绑定商品
	 */
	public ShoppingGoodsProductBean add(ShoppingGoodsProductBean entity) {
		log.info("<ShoppingGoodsProductServiceImpl>------<add>------start");
		ShoppingGoodsProductBean insert = shoppingGoodsProductDaoImpl.insert(entity);
		log.info("<ShoppingGoodsProductServiceImpl>------<add>------end");
		return insert;
	}

	public boolean update(ShoppingGoodsProductBean entity) {
		return shoppingGoodsProductDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return shoppingGoodsProductDaoImpl.delete(id);
	}

	/**
	 * 绑定商品列表查询
	 */
	public ReturnDataUtil list(ShoppingGoodsProductForm shoppingGoodsProductForm) {
		log.info("<ShoppingGoodsProductServiceImpl>------<list>------start");
		ReturnDataUtil list = shoppingGoodsProductDaoImpl.list(shoppingGoodsProductForm);
		log.info("<ShoppingGoodsProductServiceImpl>------<list>------end");
		return list;
	}

	public ShoppingGoodsProductBean get(Object id) {
		return shoppingGoodsProductDaoImpl.get(id);
	}
	
	@Override
	public List<ShoppingGoodsProductBean> getShoppingGoodsProductBean(Long shoppingGoodsId) {
		log.info("<ShoppingGoodsProductServiceImpl>------<getShoppingGoodsProductBean>-----start");
		List<ShoppingGoodsProductBean> list = shoppingGoodsProductDaoImpl.getShoppingGoodsProductBean(shoppingGoodsId);
		log.info("<ShoppingGoodsProductServiceImpl>------<getShoppingGoodsProductBean>-----end");
		return list;
	}
}
