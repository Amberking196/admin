package com.server.module.system.purchase.purchaseBill;

import java.util.List;

import com.server.module.system.purchase.purchaseApply.PurchaseApplyAndItemBean;
import com.server.module.system.purchase.purchaseApply.PurchaseApplyBillBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 16:23:53
 */
public interface PurchaseBillService {

	/**
	 * 采购单列表查询
	 * @author why
	 * @date  2018年9月4日下午16:26:31
	 * @param purchaseBillForm
	 * @return
	 */
	public ReturnDataUtil listPage(PurchaseBillForm purchaseBillForm);
	
	/**
	 * 根据采购单号 查询 采购单的商品
	 * @author why
	 * @date  2018年9月4日下午16:34:55
	 * @param number
	 * @return
	 */
	public PurchaseBillBean getItemBean(String number);

	public List<PurchaseBillBean> list(PurchaseBillForm condition);

	public boolean update(PurchaseBillBean entity);

	/**
	 * 删除采购单
	 *@author why
	 *@date 2018年9月8日-上午10:19:48
	 *@param id
	 *@return
	 */
	public boolean del(Object id);

	public PurchaseBillBean get(Object id);

	public PurchaseBillBean add(PurchaseBillBean entity);

	/**
	 * 采购申请单状态的修改为不通过
	 * 
	 * @param id
	 * @return
	 */
	public ReturnDataUtil checkFalse(PurchaseApplyBillBean bean);

	/**
	 * 申请单审核通过，并生成新的采购单
	 * 
	 * @param bean
	 * @return
	 */
	public ReturnDataUtil auditing(PurchaseApplyAndItemBean bean);
}
