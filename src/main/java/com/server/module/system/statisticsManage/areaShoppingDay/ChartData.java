package com.server.module.system.statisticsManage.areaShoppingDay;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ChartData {
    @JsonIgnore
    private int[] x;
    String name;
    private String unit;//单位
    private Double[] y;
}
