package com.server.module.system.purchase.purchaseBillItem;

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
 * author name: yjr create time: 2018-09-03 16:27:30
 */
@Service
public class PurchaseBillItemServiceImpl implements PurchaseBillItemService {

	private static Logger log = LogManager.getLogger(PurchaseBillItemServiceImpl.class);
	@Autowired
	private PurchaseBillItemDao purchaseBillItemDaoImpl;

	public ReturnDataUtil listPage(PurchaseBillItemForm condition) {
		return purchaseBillItemDaoImpl.listPage(condition);
	}

	public PurchaseBillItemBean add(PurchaseBillItemBean entity) {
		return purchaseBillItemDaoImpl.insert(entity);
	}

	/**
	 * 入库成功后 修改商品数量
	 */
	public boolean update(PurchaseBillItemBean entity) {
		log.info("<PurchaseBillItemServiceImpl>----<update>-----------------start");
		boolean update = purchaseBillItemDaoImpl.update(entity);
		log.info("<PurchaseBillItemServiceImpl>----<update>-----------------end");
		return update;
	}

	/**
	 * 删除采购单商品
	 */
	public boolean del(Object id) {
		log.info("<PurchaseBillItemServiceImpl>-----<del>------start");
		boolean delete = purchaseBillItemDaoImpl.delete(id);
		log.info("<PurchaseBillItemServiceImpl>-----<del>------end");
		return delete;
	}
	
	/**
	 * 根据采购单id 查询采购单的商品
	 */
	public List<PurchaseBillItemBean> list(Long billId) {
		log.info("<PurchaseBillItemServiceImpl>----<list>-------start");
		List<PurchaseBillItemBean> list = purchaseBillItemDaoImpl.list(billId);
		log.info("<PurchaseBillItemServiceImpl>----<list>-------end");
		return list;
	}

	public PurchaseBillItemBean get(Object id) {
		log.info("<PurchaseBillItemServiceImpl>----<get>-----------------start");
		PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemDaoImpl.get(id);
		log.info("<PurchaseBillItemServiceImpl>----<get>-----------------end");
		return purchaseBillItemBean;
	}
	
	/**
	 * 查询采购成功的商品
	 */
	public ReturnDataUtil successListPage(PurchaseBillItemForm purchaseBillItemForm) {
		log.info("<PurchaseBillItemServiceImpl>----<successListPage>-------start");
		ReturnDataUtil returnDataUtil=purchaseBillItemDaoImpl.successListPage(purchaseBillItemForm);
		log.info("<PurchaseBillItemServiceImpl>----<successListPage>-------end");
		return returnDataUtil;
	}
}
