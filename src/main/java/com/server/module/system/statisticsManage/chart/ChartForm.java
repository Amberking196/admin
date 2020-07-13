package com.server.module.system.statisticsManage.chart;

import lombok.Data;

@Data
public class ChartForm {
    private Integer companyId;
    private Integer areaId;
    String vmCode;
    private int type;// 0 日 1 旬 2 月 3 季度
    private String start;//日期
    private String end;
    private boolean superposition;//是否叠加

    String[] functions;// 功能集合


}
