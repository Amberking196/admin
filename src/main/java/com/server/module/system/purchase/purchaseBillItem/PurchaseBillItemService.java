package com.server.module.system.purchase.purchaseBillItem;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 16:27:30
 */
public interface PurchaseBillItemService {

	public ReturnDataUtil listPage(PurchaseBillItemForm condition);

	/**
	 * 根据采购单id 查询采购单的商品
	 * @author why
	 * @date  2018年9月4日下午17:03:25
	 * @param billId
	 * @return
	 */
	public List<PurchaseBillItemBean> list(Long billId);

	
	/**
	 * 入库成功后 修改商品数量
	 * @author why
	 * @date 2018年9月4日 下午17:41:15
	 * @param entity
	 * @return
	 */
	public boolean update(PurchaseBillItemBean entity);

	/**
	 * 删除采购单商品
	 *@author why
	 *@date 2018年9月8日-上午10:41:34
	 *@param id
	 *@return
	 */
	public boolean del(Object id);

	public PurchaseBillItemBean get(Object id);

	public PurchaseBillItemBean add(PurchaseBillItemBean entity);
	
	/**
	 * 查询采购成功的商品
	 * @param purchaseBillItemForm
	 * @return list
	 */
	public ReturnDataUtil successListPage(PurchaseBillItemForm purchaseBillItemForm);
}
