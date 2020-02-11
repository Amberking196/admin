package com.server.module.system.machineManage.machinesWay;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class WayDto1 {

    private Long wayId;
    private Integer wayNumber;
    private Integer state;

    private List<WayItem> itemList;

}
