package com.server.module.system.warehouseManage.stockLog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-05-22 11:00:53
 */
@Service
public class WarehouseStockLogServiceImpl implements WarehouseStockLogService {

    private static Log log = LogFactory.getLog(WarehouseStockLogServiceImpl.class);
    @Autowired
    private WarehouseStockLogDao warehouseStockLogDaoImpl;

    public ReturnDataUtil listPage(WarehouseStockLogCondition condition) {
        return warehouseStockLogDaoImpl.listPage(condition);
    }

    public WarehouseStockLogBean add(WarehouseStockLogBean entity) {
        return warehouseStockLogDaoImpl.insert(entity);
    }

    public boolean update(WarehouseStockLogBean entity) {
        return warehouseStockLogDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return warehouseStockLogDaoImpl.delete(id);
    }

    public List<WarehouseStockLogBean> list(WarehouseStockLogCondition condition) {
        return null;
    }

    public WarehouseStockLogBean get(Object id) {
        return warehouseStockLogDaoImpl.get(id);
    }
}

