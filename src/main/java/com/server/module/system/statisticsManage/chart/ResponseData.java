package com.server.module.system.statisticsManage.chart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ResponseData {

    private int type;
    @JsonIgnore
    private int[] x;

    String name;
    private String unit;//单位

    private Double[] y;
}
