package com.server.module.system.warehouseManage.stockLog;

import com.server.module.commonBean.PageAssist;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class WarehouseStockLogForm extends PageAssist {

    // 开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date startTime;
    // 结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endTime;

    Integer type;// 1旧版 2新版
}
