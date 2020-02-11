package com.server.module.system.statisticsManage.areaShoppingDay;

import lombok.Data;

@Data
public class AreaShoppingDayForm {

    private String start;
    private String end;
    private String companyId;
    private String district;//区
    private String city;//市
}
