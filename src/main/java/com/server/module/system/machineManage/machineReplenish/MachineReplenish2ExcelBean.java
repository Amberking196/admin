package com.server.module.system.machineManage.machineReplenish;


import lombok.Data;

@Data
public class MachineReplenish2ExcelBean {

    private String vmCode;
    private String wayNumber;

    // 商品名字
    private String itemName;

    // 补货时间
    private String replenishTime;

    // 补货数量
    private String replenishCount;

}
