package com.server.module.system.purchase.purchaseApply;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 17:27:57
 */
public interface PurchaseApplyBillDao {

	public ReturnDataUtil listPage(PurchaseApplyBillCondition condition);

	public List<PurchaseApplyBillBean> list(PurchaseApplyBillCondition condition);

	public boolean update(PurchaseApplyBillBean entity);

	public boolean delete(Object id);

	public PurchaseApplyBillBean get(Object id);

	public PurchaseApplyBillBean insert(PurchaseApplyBillBean entity);

	/**
	 * 采购申请单的生成
	 * 
	 * @param bean
	 * @param conn
	 * @return
	 */
	public ReturnDataUtil addPurchaseApply(PurchaseApplyBillBean bean, Connection conn)throws SQLException;
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
	public ReturnDataUtil delete(Integer id);
	/**
	 * 采购申请单审核不通过
	 * @param id
	 * @return
	 */
	public ReturnDataUtil checkFalse(PurchaseApplyBillBean bean);
	/**
	 * 更新采购申请单的信息
	 * @param bean
	 * @param conn
	 */
	public void update(PurchaseApplyAndItemBean bean, Connection conn) throws SQLException;
	/**
	 * 根据id查询部分信息
	 * @param id
	 * @return
	 */
	public PurchaseApplyBillBean getBeanById(Integer id);
}
