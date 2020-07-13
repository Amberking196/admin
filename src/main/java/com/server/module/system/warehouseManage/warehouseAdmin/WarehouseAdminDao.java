package com.server.module.system.warehouseManage.warehouseAdmin;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 14:23:48
 */
public interface WarehouseAdminDao {

	public ReturnDataUtil listPage(WarehouseAdminCondition condition);

	public List<WarehouseAdminBean> list(WarehouseAdminCondition condition);

	public boolean update(WarehouseAdminBean entity);

	public boolean delete(Long id);

	public WarehouseAdminBean get(Object id);

	public WarehouseAdminBean insert(WarehouseAdminBean entity);
	
	public List<WarehouseAdminBean> listByWarehouseId(Long warehouseInfoId);

	public List<WarehouseAdminBean> listCurrUserWarehouse();
}
