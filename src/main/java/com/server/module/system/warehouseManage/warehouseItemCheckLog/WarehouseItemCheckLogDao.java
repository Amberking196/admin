package com.server.module.system.warehouseManage.warehouseItemCheckLog;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-06-15 14:00:02
 */
public interface WarehouseItemCheckLogDao {

	/**
	 * 商品盘点列表
	 * @param warehouseItemCheckLogForm
	 * @return
	 */
	public ReturnDataUtil listPage(WarehouseItemCheckLogForm warehouseItemCheckLogForm);

	/**
	 * 商品盘点列表
	 * @param warehouseItemCheckLogForm
	 * @return
	 */
	public ReturnDataUtil newListPage(WarehouseItemCheckLogForm warehouseItemCheckLogForm);
	
}
