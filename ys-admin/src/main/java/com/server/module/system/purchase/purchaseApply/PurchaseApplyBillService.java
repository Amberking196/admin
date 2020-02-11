package com.server.module.system.purchase.purchaseApply;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 17:27:57
 */
public interface PurchaseApplyBillService {

	public ReturnDataUtil listPage(PurchaseApplyBillCondition condition);

	public List<PurchaseApplyBillBean> list(PurchaseApplyBillCondition condition);

	public boolean update(PurchaseApplyBillBean entity);

	public boolean del(Object id);

	public PurchaseApplyBillBean get(Object id);

	public PurchaseApplyBillBean add(PurchaseApplyBillBean entity);

	/**
	 * 采购申请单的生成
	 * 
	 * @param bean
	 * @return
	 */
	public ReturnDataUtil addPurchaseApply(PurchaseApplyAndItemBean bean);
	/**
	 * 提交采购申请单
	 * @param id
	 * @return
	 */
	public ReturnDataUtil submitPurchaseApply(Integer id);
	/**
	 * 删除采购申请单
	 * @param id
	 * @return
	 */
	public ReturnDataUtil del(Integer id);
	/**
	 * 根据id查询部分信息
	 * @param id
	 * @return
	 */
	public PurchaseApplyBillBean getBeanById(Integer id);
	/**
	 * 根据申请单id查询详情
	 * @param id
	 * @return
	 */
	public ReturnDataUtil getPurchaseBillList(Integer id);
}
