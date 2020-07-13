package com.server.module.system.warehouseManage.stockLog;

import java.sql.Connection;
import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2018-05-22 11:00:53
 */ 
public interface  WarehouseStockLogDao{

public ReturnDataUtil listPage(WarehouseStockLogCondition condition);
public List<WarehouseStockLogBean> list(WarehouseStockLogCondition condition);
public boolean update(WarehouseStockLogBean entity);
public boolean delete(Object id);
public WarehouseStockLogBean get(Object id);
public WarehouseStockLogBean insert(Connection conn,WarehouseStockLogBean entity);

public WarehouseStockLogBean insert(WarehouseStockLogBean entity);
}

