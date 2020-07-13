package com.server.module.system.warehouseManage.warehouseGiveBack;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-05-24 11:34:32
 */
public interface WarehouseGiveBackService {

	public ReturnDataUtil listPage(WarehouseGiveBackForm warehouseGiveBackForm);

	public WarehouseGiveBackBean add(WarehouseGiveBackBean entity);
	
	//改变库存以及增加日志
	public void changeStock(WarehouseGiveBackBean entity);
}
