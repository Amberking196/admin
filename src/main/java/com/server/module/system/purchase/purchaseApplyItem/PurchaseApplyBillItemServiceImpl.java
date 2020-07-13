package com.server.module.system.purchase.purchaseApplyItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 17:41:12
 */
@Service
public class PurchaseApplyBillItemServiceImpl implements PurchaseApplyBillItemService {

	private static Log log = LogFactory.getLog(PurchaseApplyBillItemServiceImpl.class);
	@Autowired
	private PurchaseApplyBillItemDao purchaseApplyBillItemDaoImpl;

	public ReturnDataUtil listPage(PurchaseApplyBillItemCondition condition) {
		return purchaseApplyBillItemDaoImpl.listPage(condition);
	}

	public PurchaseApplyBillItemBean add(PurchaseApplyBillItemBean entity) {
		return purchaseApplyBillItemDaoImpl.insert(entity);
	}

	public boolean update(PurchaseApplyBillItemBean entity) {
		return purchaseApplyBillItemDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return purchaseApplyBillItemDaoImpl.delete(id);
	}

	public List<PurchaseApplyBillItemBean> list(PurchaseApplyBillItemCondition condition) {
		return null;
	}

	public PurchaseApplyBillItemBean get(Object id) {
		return purchaseApplyBillItemDaoImpl.get(id);
	}

	@Override
	public ReturnDataUtil findItemsById(Integer id) {
		log.info("<PurchaseApplyBillItemServiceImpl>------<findItemsById>----start");
		ReturnDataUtil data=purchaseApplyBillItemDaoImpl.findItemsById(id);
		log.info("<PurchaseApplyBillItemServiceImpl>------<findItemsById>----end");
		return data;
	}
}
