package com.server.module.system.warehouseManage.warehouseWarrantDetail;

import java.sql.Connection;
import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-17 03:15:57
 */
public interface WarehouseBillItemDao {

	public boolean update(WarehouseBillItemBean entity);

	public List<WarehouseBillItemBean> get(int billId);

	public WarehouseBillItemBean insert(WarehouseBillItemBean entity);
	/**
	 * 事务控制
	 *@author why
	 *@date 2018年9月11日-上午9:50:11
	 *@param conn
	 *@param entity
	 *@return
	 */
	public WarehouseBillItemBean insertTransaction(Connection conn,WarehouseBillItemBean entity);
	
	public boolean delete(Object id);
	public boolean update(Connection conn, WarehouseBillItemBean entity);
	public List<WarehouseBillItemBean> getByBillId(int billId);
	
	public ReturnDataUtil listPage(WarehouseBillItemForm condition);
	
	/**
	 * 事务控制 查询单据下的商品信息
	 *@author why
	 *@date 2018年9月11日-下午1:51:31
	 *@param conn
	 *@param billId
	 *@return
	 */
	public List<WarehouseBillItemBean> getBillItemTransaction(Connection conn,int billId);


	}
