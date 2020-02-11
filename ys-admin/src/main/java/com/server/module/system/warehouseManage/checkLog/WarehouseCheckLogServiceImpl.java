package com.server.module.system.warehouseManage.checkLog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr 
 * create time: 2018-06-02 15:10:23
 */
@Service
public class WarehouseCheckLogServiceImpl implements WarehouseCheckLogService {

	private static Log log = LogFactory.getLog(WarehouseCheckLogServiceImpl.class);
	@Autowired
	private WarehouseCheckLogDao warehouseCheckLogDaoImpl;

	public ReturnDataUtil listPage(WarehouseCheckLogForm condition) {
		return warehouseCheckLogDaoImpl.listPage(condition);
	}
	public ReturnDataUtil listDetail(Long lineId,Integer time){
		return warehouseCheckLogDaoImpl.listDetail(lineId,time);
	}
	public WarehouseCheckLogBean add(WarehouseCheckLogBean entity) {
		return warehouseCheckLogDaoImpl.insert(entity);
	}

	public boolean update(WarehouseCheckLogBean entity) {
		return warehouseCheckLogDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return warehouseCheckLogDaoImpl.delete(id);
	}

	public List<WarehouseCheckLogBean> list(WarehouseCheckLogForm condition) {
		return null;
	}

	public WarehouseCheckLogBean get(Object id) {
		return warehouseCheckLogDaoImpl.get(id);
	}
	/**
	 * 盘点,根据线路
	 */
	public void checkStock(Long lineId, Long areaId, Long companyId){
		warehouseCheckLogDaoImpl.checkStock(lineId, areaId, companyId, null);
	}

}
