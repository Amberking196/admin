package com.server.module.system.purchase.purchaseBill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 16:23:53
 */

public interface PurchaseBillDao {

	/**
	 * 采购单列表查询
	 * @author why
	 * @date  2018年9月4日下午16:24:09
	 * @param purchaseBillForm
	 * @return
	 */
	public ReturnDataUtil listPage(PurchaseBillForm purchaseBillForm);

	/**
	 * 根据采购单号 查询 采购单的商品
	 * @author why
	 * @date  2018年9月4日下午16:30:51
	 * @param number
	 * @return
	 */
	public PurchaseBillBean getItemBean(String number);
	
	public List<PurchaseBillBean> list(PurchaseBillForm condition);

	
	public boolean update(PurchaseBillBean entity);
	
	/**
	 * 事务控制 修改
	 *@author why
	 *@date 2018年9月11日-下午2:10:56
	 *@param conn
	 *@param entity
	 *@return
	 */
	public boolean updateTransaction(Connection conn,PurchaseBillBean entity);

	/**
	 * 删除采购单
	 *@author why
	 *@date 2018年9月8日-上午10:02:43
	 *@param id
	 *@return
	 */
	public boolean delete(Object id);

	
	public PurchaseBillBean get(Object id);

	public PurchaseBillBean insert(PurchaseBillBean entity);

	/**
	 * 生成采购单并返回id
	 * 
	 * @param purchaseBean
	 * @param conn
	 * @return
	 */
	public ReturnDataUtil addPurchaseBill(PurchaseBillBean purchaseBean, Connection conn)throws SQLException;
	/**
	 * 根据申请单id查询采购单
	 * @param id
	 * @return
	 */
	public List<PurchaseBillBean> getItemBean(Integer id);
	/**
	 * 验证采购单单号的唯一性
	 * @param newStr
	 * @return
	 */
	public boolean checkOnlyOne(String newStr);
	
}
