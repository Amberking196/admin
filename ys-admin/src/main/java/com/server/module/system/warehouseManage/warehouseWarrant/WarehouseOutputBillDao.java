package com.server.module.system.warehouseManage.warehouseWarrant;

import com.server.util.ReturnDataUtil;

import java.sql.Connection;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
public interface WarehouseOutputBillDao {

	/**
	 * 库单列表查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(WarehouseOutputBillForm warehouseOutputBillForm);

	/**
	 * 增加入库单
	 * @param entity
	 * @return
	 */
	public WarehouseOutputBillBean insert(WarehouseOutputBillBean entity);
	
	/**
	 * 事务控制 增加入库单
	 *@author why
	 *@date 2018年9月11日-上午9:44:25
	 *@param conn
	 *@param entity
	 *@return
	 */
	public WarehouseOutputBillBean insertTransaction(Connection conn,WarehouseOutputBillBean entity);
		
	
	
	/**
	 * 修改入库单
	 * @param entity
	 * @return
	 */
	public boolean update(WarehouseOutputBillBean entity);

	/**
	 * 根据单号 查询详情
	 * @param changeId
	 * @return
	 */
	public WarehouseOutputBillBean get(String  changeId);
	
	/**
	 * 根据单号 查询详情 事务控制
	 *@author why
	 *@date 2018年9月11日-上午10:16:00
	 *@param conn
	 *@param changeId
	 *@return
	 */
	public WarehouseOutputBillBean gettransaction(Connection conn,String  changeId);

	/**
	 * 生成入库单号
	 * @return
	 */
	public String findChangeId();
	
	public WarehouseOutputBillBean getById(Object id);
	/**
	 * 事务实现
	 *@author why
	 *@date 2018年9月11日-上午11:25:18
	 *@param conn
	 *@param id
	 *@return
	 */
	public WarehouseOutputBillBean getById(Connection conn,Object id);
	
	

	public boolean update(Connection conn, WarehouseOutputBillBean entity);
	
	/**
	 * 库存转移时 查询已经入库数量
	 * @param removalStockId 出库单id
	 * @param targetWarehouseId 目标仓库id
	 * @param sourceWarehouseId 来源仓库id
	 * @param itemId  商品id
	 * @return
	 */
	public Long getQuantity(Long removalStockId ,Long targetWarehouseId,Long sourceWarehouseId,Long itemId);


}
