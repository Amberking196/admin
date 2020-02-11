package com.server.module.system.purchase.purchaseBillItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 16:27:30
 */
public interface PurchaseBillItemDao {

	public ReturnDataUtil listPage(PurchaseBillItemForm condition);

	/**
	 * 根据采购单id 查询采购单的商品
	 * @author why
	 * @date  2018年9月4日下午16:42:51
	 * @param billId
	 * @return
	 */
	public List<PurchaseBillItemBean> list(Long billId);

	
	/**
	 * 入库成功后 修改商品数量
	 * @author why
	 * @date 2018年9月4日 下午17:36:20
	 * @param entity
	 * @return
	 */
	public boolean update(PurchaseBillItemBean entity);
	
	/**
	 * 入库成功后 修改商品数量 事务控制
	 *@author why
	 *@date 2018年9月11日-上午9:52:58
	 *@param conn
	 *@param entity
	 *@return
	 */
	public boolean updateTransaction(Connection conn,PurchaseBillItemBean entity);

	/**
	 * 删除采购单商品
	 *@author why
	 *@date 2018年9月8日-上午10:49:23
	 *@param id
	 *@return
	 */
	public boolean delete(Object id);

	
	
	public PurchaseBillItemBean get(Object id);

	/**
	 * 事务控制 查询采购单商品
	 *@author why
	 *@date 2018年9月11日-下午2:14:08
	 *@param conn
	 *@param id
	 *@return
	 */
	public PurchaseBillItemBean getTransaction(Connection conn,Object id);
	
	public PurchaseBillItemBean insert(PurchaseBillItemBean entity);
	/**
	 * 添加商品到采购单下
	 * @param billBean
	 * @param conn
	 * @return
	 */
	public ReturnDataUtil addItem(PurchaseBillItemBean billBean, Connection conn) throws SQLException;
	
	/**
	 * 查询采购成功的商品
	 * @param purchaseBillItemForm
	 * @return list
	 */
	public ReturnDataUtil successListPage(PurchaseBillItemForm purchaseBillItemForm);
}
