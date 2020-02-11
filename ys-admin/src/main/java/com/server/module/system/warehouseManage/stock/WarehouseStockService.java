package com.server.module.system.warehouseManage.stock;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-05-22 10:49:58
 */
public interface WarehouseStockService {

	public ReturnDataUtil listPage(WarehouseStockForm condition);

	public List<WarehouseStockBean> list(WarehouseStockForm condition);

	public boolean update(WarehouseStockBean entity);

	public boolean del(Object id);

	public WarehouseStockBean get(Object id);

	public WarehouseStockBean add(WarehouseStockBean entity);
	
	public void putStorage(Long billId);
	
	/**
	 * 查询仓库现有的商品 
	 * @return
	 */
	public List<WarehouseStockBean> getItemInfo(Long warehouseId,Integer type);
	/**
	 * 判断该仓库是否存在该商品，如果存在data.getState==-1
	 * @param bean
	 * @return
	 */
	public ReturnDataUtil checkOnlyOne(WarehouseStockBean bean);
	/**
	 *把商品添加到仓库中
	 * @param bean
	 * @return
	 */
	public ReturnDataUtil addItemToWarehouse(WarehouseStockBean bean);
	/**
	 * 更新仓库商品的部分信息
	 * @return
	 */
	public ReturnDataUtil updateItemToWarehouse(WarehouseStockBean bean);
    /**
     * 根据条形码查询指定仓库的商品
     * @param warehouseId
     * @param barCode
     * @return
     */
	public ReturnDataUtil getWarehouseItem(Long warehouseId, String barCode);
	
	public WarehouseStockBean getWarehouseItem(Long warehouseId, Long itemId);
}
