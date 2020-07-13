package com.server.module.system.statisticsManage.payRecord;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

@Data
public class ListSaleNumCondition extends PageAssist {

    private Integer companyId;
    private String vmCode;
    private String startTime;
    private String endTime;
    private Integer basicItemId;
    Integer areaId;
}
