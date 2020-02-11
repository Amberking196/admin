package com.server.module.system.machineManage.machinesWayTemplate;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  vending_machines_way_template
 * author name: zfc
 * create time: 2018-08-03 10:19:25
 */

@Data
public class VendingMachinesWayTemplateCondition extends PageAssist {


    // 公司Id
    private Integer companyId;

    // 区域Id
    private Integer areaId;

    // 用户的Id
    private Long userId;


}

