package com.server.module.system.statisticsManage.purchaseItemStatistics;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  purchase_item
 * author name: hjc
 * create time: 2018-08-24 11:02:40
 */ 
@Data
public class PurchaseItemStatisticsCondition extends PageAssist{

    //private  String itemName;
    //private String supplierName;
    private Long itemId;
	private String barCode;
	private String itemName;
    //private Long supplierId;
}

