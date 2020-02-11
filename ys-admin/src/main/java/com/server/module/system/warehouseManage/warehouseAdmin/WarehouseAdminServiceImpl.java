package com.server.module.system.warehouseManage.warehouseAdmin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 14:23:48
 */
@Service
public class WarehouseAdminServiceImpl implements WarehouseAdminService {

	private static Log log = LogFactory.getLog(WarehouseAdminServiceImpl.class);
	@Autowired
	private WarehouseAdminDao warehouseAdminDaoImpl;

	public ReturnDataUtil listPage(WarehouseAdminCondition condition) {
		return warehouseAdminDaoImpl.listPage(condition);
	}

	public WarehouseAdminBean add(WarehouseAdminBean entity) {
		entity.setCreateTime(new Date());
		return warehouseAdminDaoImpl.insert(entity);
	}

	public boolean update(WarehouseAdminBean entity) {
		return warehouseAdminDaoImpl.update(entity);
	}

	public boolean del(Long id) {
		return warehouseAdminDaoImpl.delete(id);
	}

	public List<WarehouseAdminBean> list(WarehouseAdminCondition condition) {
		return null;
	}

	public WarehouseAdminBean get(Object id) {
		return warehouseAdminDaoImpl.get(id);
	}
	
	public List<WarehouseAdminBean> listByWarehouseId(Long warehouseInfoId){
		return warehouseAdminDaoImpl.listByWarehouseId(warehouseInfoId);
	}

}
