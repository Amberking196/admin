package com.server.module.system.warehouseManage.warehouseWarrant;

import java.sql.Connection;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
public interface WarehouseOutputBillService {

	/**
	 * 入库单列表查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(WarehouseOutputBillForm warehouseOutputBillForm);
	
	/**
	 * 增加入库单
	 * @param entity
	 * @return
	 */
	public WarehouseOutputBillBean add(WarehouseOutputBillBean entity);

	/**
	 * 根据单号 查询详情
	 * @param changeId
	 * @return
	 */
	public WarehouseOutputBillBean get(String  changeId);


	public boolean update(WarehouseOutputBillBean entity);


	
	/**
	 * 生成入库单号
	 * @return
	 */
	public String findChangeId();
	
	public WarehouseOutputBillBean getById(Object id);
	
	/**
	 * 事务控制
	 *@author why
	 *@date 2018年9月11日-上午11:26:50
	 *@param conn
	 *@param id
	 *@return
	 */
	public WarehouseOutputBillBean getById(Connection conn,Object id);
	/**
	 * 库存转移时 查询已经入库数量
	 * @param removalStockId 出库单id
	 * @param targetWarehouseId 目标仓库id
	 * @param sourceWarehouseId 来源仓库id
	 * @param itemId  商品id
	 * @return
	 */
	public Long getQuantity(Long removalStockId ,Long targetWarehouseId,Long sourceWarehouseId,Long itemId);
	
	
	/**
	 * 入库审核
	 *@author why
	 *@date 2018年9月8日-下午3:39:24
	 *@param entity
	 *@return
	 */
	public ReturnDataUtil putStock(WarehouseOutputBillBean entity);

	
}
