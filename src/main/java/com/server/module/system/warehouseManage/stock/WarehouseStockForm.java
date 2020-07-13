package com.server.module.system.warehouseManage.stock;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  warehouse_stock
 * author name: yjr
 * create time: 2018-05-22 10:49:58
 */ 
@Data
public class WarehouseStockForm extends PageAssist{

    private int type;//1 仓库 2 公司
    private String itemId;
    private Long warehouseId;
    private String companyId;
    private String itemName;
    private String barCode;
}

