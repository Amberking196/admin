package com.server.module.system.warehouseManage.stock;

import java.sql.Connection;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-05-22 10:49:58
 */
public interface WarehouseStockDao {

	public ReturnDataUtil listPage(WarehouseStockForm condition);

	public List<WarehouseStockBean> list(WarehouseStockForm condition);

	public boolean update(WarehouseStockBean entity);
	public boolean update(Connection conn,WarehouseStockBean entity);


	public boolean delete(Object id);

	public WarehouseStockBean get(Object id);

	public WarehouseStockBean insert(WarehouseStockBean entity);
	public WarehouseStockBean insert(Connection conn,WarehouseStockBean entity);

	public WarehouseStockBean getStock(Long warehouseId,Long itemId);

	public double getAveragePrice(Long warehouseId, Long itemId,int quantity,double price);
	
	/**
	 * 查询仓库现有的商品 
	 * @return
	 */
	public List<WarehouseStockBean> getItemInfo(Long warehouseId,Integer type);
	/**
	 * 判断该仓库中该商品是否唯一,有该商品返回getState=-1;
	 * @param bean
	 * @return
	 */
	public ReturnDataUtil checkOnlyOne(WarehouseStockBean bean);
	/**
	 * 把商品添加到仓库中
	 * @param bean
	 * @return
	 */
	public ReturnDataUtil addItemToWarehouse(WarehouseStockBean bean);
	
	/**
	 * 根据商品id查询该商品的库存记录
	 *@author why
	 *@date 2018年9月11日-下午1:55:16
	 *@param conn
	 *@param warehouseId
	 *@param itemId
	 *@return
	 */
	public WarehouseStockBean getStockTransaction(Connection conn,Long warehouseId,Long itemId);
    /**
     * 根据条形码查询该仓库的商品
     * @param warehouseId
     * @param barCode
     * @return
     */
	public List<WarehouseStockBean> getWarehouseItems(Long warehouseId, String barCode);
	
	public WarehouseStockBean getWarehouseItem(Long warehouseId, Long itemId);
}
