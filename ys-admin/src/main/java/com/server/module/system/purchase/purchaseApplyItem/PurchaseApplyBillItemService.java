package com.server.module.system.purchase.purchaseApplyItem;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 17:41:12
 */
public interface PurchaseApplyBillItemService {

	public ReturnDataUtil listPage(PurchaseApplyBillItemCondition condition);

	public List<PurchaseApplyBillItemBean> list(PurchaseApplyBillItemCondition condition);

	public boolean update(PurchaseApplyBillItemBean entity);

	public boolean del(Object id);

	public PurchaseApplyBillItemBean get(Object id);

	public PurchaseApplyBillItemBean add(PurchaseApplyBillItemBean entity);
	/**
	 * 根据申请单id查询商品列表
	 * @param id
	 * @return
	 */
	public ReturnDataUtil findItemsById(Integer id);
}
