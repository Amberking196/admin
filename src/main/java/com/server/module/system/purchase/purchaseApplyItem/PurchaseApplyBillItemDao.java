package com.server.module.system.purchase.purchaseApplyItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.server.module.system.purchase.purchaseApply.PurchaseApplyAndItemBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 17:41:12
 */
public interface PurchaseApplyBillItemDao {

	public ReturnDataUtil listPage(PurchaseApplyBillItemCondition condition);

	public List<PurchaseApplyBillItemBean> list(PurchaseApplyBillItemCondition condition);

	public boolean update(PurchaseApplyBillItemBean entity);

	public boolean delete(Object id);

	public PurchaseApplyBillItemBean get(Object id);

	public PurchaseApplyBillItemBean insert(PurchaseApplyBillItemBean entity);

	/**
	 * 保存申请单下的商品
	 * 
	 * @param conn
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil addItem(Connection conn, PurchaseApplyBillItemBean entity)throws SQLException;
	/**
	 * 根据采购当id查询商品列表
	 * @param id
	 * @return
	 */
	public ReturnDataUtil findItemsById(Integer id);
	/**
	 * 为申请单下的商品添加供应商
	 * @param purchaseApplyBillItemBean
	 * @param conn
	 */
	public void update(PurchaseApplyBillItemBean purchaseApplyBillItemBean, Connection conn)throws SQLException;
	
}
