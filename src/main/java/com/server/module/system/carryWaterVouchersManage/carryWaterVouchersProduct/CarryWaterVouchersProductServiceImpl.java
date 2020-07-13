package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersProduct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 16:25:36
 */
@Service
public class CarryWaterVouchersProductServiceImpl implements CarryWaterVouchersProductService {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersProductServiceImpl.class);
	@Autowired
	private CarryWaterVouchersProductDao carryWaterVouchersProductDaoImpl;

	public ReturnDataUtil listPage(CarryWaterVouchersProductForm condition) {
		return carryWaterVouchersProductDaoImpl.listPage(condition);
	}

	/**
	 * 提水券绑定商品
	 */
	public CarryWaterVouchersProductBean add(CarryWaterVouchersProductBean entity) {
		log.info("<CarryWaterVouchersProductServiceImpl>-----<add>------start");
		CarryWaterVouchersProductBean bean = carryWaterVouchersProductDaoImpl.insert(entity);
		log.info("<CarryWaterVouchersProductServiceImpl>-----<add>------end");
		return bean;
	}

	public boolean update(CarryWaterVouchersProductBean entity) {
		return carryWaterVouchersProductDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return carryWaterVouchersProductDaoImpl.delete(id);
	}

	/**
	 * 提水券商品列表
	 */
	public ReturnDataUtil list(CarryWaterVouchersProductForm carryWaterVouchersProductForm) {
		log.info("<CarryWaterVouchersProductServiceImpl>-----<list>------start");
		ReturnDataUtil list = carryWaterVouchersProductDaoImpl.list(carryWaterVouchersProductForm);
		log.info("<CarryWaterVouchersProductServiceImpl>-----<list>------end");
		return list;
	}

	public CarryWaterVouchersProductBean get(Object id) {
		return carryWaterVouchersProductDaoImpl.get(id);
	}
}
