package com.server.module.system.warehouseManage.warehouseAdmin;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 14:23:48
 */
public interface WarehouseAdminService {

	public ReturnDataUtil listPage(WarehouseAdminCondition condition);

	public List<WarehouseAdminBean> list(WarehouseAdminCondition condition);

	public boolean update(WarehouseAdminBean entity);

	public boolean del(Long id);

	public WarehouseAdminBean get(Object id);
	public List<WarehouseAdminBean> listByWarehouseId(Long warehouseInfoId);

	public WarehouseAdminBean add(WarehouseAdminBean entity);
}
