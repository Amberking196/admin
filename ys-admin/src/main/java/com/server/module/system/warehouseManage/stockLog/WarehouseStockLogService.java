package com.server.module.system.warehouseManage.stockLog;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: yjr
 * create time: 2018-05-22 11:00:53
 */ 
public interface  WarehouseStockLogService{


public ReturnDataUtil listPage(WarehouseStockLogCondition condition);
public List<WarehouseStockLogBean> list(WarehouseStockLogCondition condition);
public boolean update(WarehouseStockLogBean entity);
public boolean del(Object id);
public WarehouseStockLogBean get(Object id);
public WarehouseStockLogBean add(WarehouseStockLogBean entity);
}

