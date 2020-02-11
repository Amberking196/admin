package com.server.module.system.promotionManage.activityProduct;

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
 * author name: why create time: 2018-08-23 18:07:34
 */
@Service
public class ActivityProductServiceImpl implements ActivityProductService {

	private static Logger log = LogManager.getLogger(ActivityProductServiceImpl.class);
	@Autowired
	private ActivityProductDao activityProductDaoImpl;

	/**
	 * 活动商品列表查询
	 */
	public ReturnDataUtil listPage(ActivityProductForm condition) {
		log.info("<ActivityProductServiceImpl>----<listPage>------start");
		ReturnDataUtil listPage = activityProductDaoImpl.listPage(condition);
		log.info("<ActivityProductServiceImpl>----<listPage>------end");
		return listPage;
	}

	/**
	 * 绑定商品
	 */
	public ActivityProductBean add(ActivityProductBean entity) {
		log.info("<ActivityProductServiceImpl>----<listPage>------start");
		ActivityProductBean insert = activityProductDaoImpl.insert(entity);
		log.info("<ActivityProductServiceImpl>----<listPage>------end");
		return insert;
	}

	public boolean update(ActivityProductBean entity) {
		return activityProductDaoImpl.update(entity);
	}

	/**
	 * 解绑商品
	 */
	public boolean del(Object id) {
		log.info("<ActivityProductServiceImpl>----<del>------start");
		boolean delete = activityProductDaoImpl.delete(id);
		log.info("<ActivityProductServiceImpl>----<del>------end");
		return delete;
	}

	/**
	 * 商品列表
	 */
	public ReturnDataUtil list(ActivityProductForm activityProductForm) {
		log.info("<ActivityProductServiceImpl>----<list>------start");
		ReturnDataUtil list = activityProductDaoImpl.list(activityProductForm);
		log.info("<ActivityProductServiceImpl>----<list>------end");
		return list;
	}

	public ActivityProductBean get(Object id) {
		return activityProductDaoImpl.get(id);
	}
}
