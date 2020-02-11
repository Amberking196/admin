package com.server.module.system.warehouseManage.checkLog;

import java.util.Date;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr 
 * create time: 2018-06-02 15:10:23
 */
public interface WarehouseCheckLogService {

	public ReturnDataUtil listPage(WarehouseCheckLogForm condition);

	public ReturnDataUtil listDetail(Long lineId,Integer time);


	public List<WarehouseCheckLogBean> list(WarehouseCheckLogForm condition);

	public boolean update(WarehouseCheckLogBean entity);

	public boolean del(Object id);

	public WarehouseCheckLogBean get(Object id);

	public WarehouseCheckLogBean add(WarehouseCheckLogBean entity);
	public void checkStock(Long lineId, Long areaId, Long companyId);

}
