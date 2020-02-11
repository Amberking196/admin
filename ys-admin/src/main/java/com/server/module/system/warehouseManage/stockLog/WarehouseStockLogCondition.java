package com.server.module.system.warehouseManage.stockLog;

import com.server.module.commonBean.PageAssist;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * table name:  warehouse_stock_log
 * author name: yjr
 * create time: 2018-05-22 11:00:53
 */ 
@Data
public class WarehouseStockLogCondition extends PageAssist{
    private String warehouseId;
    private String billId;
    private String itemId;

    private String companyId;
    // 开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date startTime;
    // 结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endTime;
}

