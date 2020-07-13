package com.server.module.system.warehouseManage.warehouseReplenish;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-05-28 17:58:52
 */ 
public interface  WarehouseReplenishService{
	/**
	 * 补水量列表查询
	 * @param warehouseReplenishForm
	 * @return
	 */
	public ReturnDataUtil listPage(WarehouseReplenishForm warehouseReplenishForm);

}

