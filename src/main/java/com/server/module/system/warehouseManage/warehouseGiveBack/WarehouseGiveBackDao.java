package com.server.module.system.warehouseManage.warehouseGiveBack;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-24 11:34:32
 */
public interface WarehouseGiveBackDao {

	/**
	 * 归还列表查询
	 * @param warehouseGiveBackForm
	 * @return
	 */
	public ReturnDataUtil listPage(WarehouseGiveBackForm warehouseGiveBackForm);

	public WarehouseGiveBackBean insert(WarehouseGiveBackBean entity);
}
