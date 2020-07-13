package com.server.module.system.machineManage.machinesWayTemplateDetail;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  vending_machines_way_template_del
 * author name: zfc
 * create time: 2018-08-03 10:35:22
 */ 


@Data
public class VendingMachinesWayTemplateDelCondition extends PageAssist{

    // 模板Id
    private Long templateId;

    // 机器编号
    private String vmCode;
}

