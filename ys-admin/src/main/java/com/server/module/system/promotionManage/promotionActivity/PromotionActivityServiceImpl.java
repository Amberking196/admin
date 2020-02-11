package com.server.module.system.promotionManage.promotionActivity;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-22 16:51:38
 */
@Service
public class PromotionActivityServiceImpl implements PromotionActivityService {

	private static Logger log = LogManager.getLogger(PromotionActivityServiceImpl.class);
	@Autowired
	private PromotionActivityDao promotionActivityDaoImpl;

	/**
	 * 促销活动列表查询
	 */
	public ReturnDataUtil listPage(PromotionActivityForm promotionActivityForm) {
		log.info("<PromotionActivityServiceImpl>----<listPage>-------start");
		ReturnDataUtil listPage = promotionActivityDaoImpl.listPage(promotionActivityForm);
		log.info("<PromotionActivityServiceImpl>----<listPage>-------end");
		return listPage;
	}

	/**
	 * 促销活动 增加
	 */
	public PromotionActivityBean add(PromotionActivityBean entity) {
		log.info("<PromotionActivityServiceImpl>----<add>------start");
		PromotionActivityBean promotionActivityBean = promotionActivityDaoImpl.insert(entity);
		log.info("<PromotionActivityServiceImpl>----<add>------end");
		return promotionActivityBean;
	}

	/**
	 * 促销活动修改
	 */
	public boolean update(PromotionActivityBean entity) {
		log.info("<PromotionActivityServiceImpl>----<update>------start");
		boolean update = promotionActivityDaoImpl.update(entity);
		log.info("<PromotionActivityServiceImpl>----<update>------end");
		return update;
	}

	/**
	 * 促销活动删除
	 */
	public boolean del(Object id) {
		log.info("<PromotionActivityServiceImpl>----<del>------start");
		boolean delete = promotionActivityDaoImpl.delete(id);
		log.info("<PromotionActivityServiceImpl>----<del>------end");
		return delete;
	}

	public List<PromotionActivityBean> list(PromotionActivityForm condition) {
		return null;
	}

	public PromotionActivityBean get(Object id) {
		return promotionActivityDaoImpl.get(id);
	}
}
