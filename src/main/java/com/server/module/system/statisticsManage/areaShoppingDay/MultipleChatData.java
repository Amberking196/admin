package com.server.module.system.statisticsManage.areaShoppingDay;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class MultipleChatData {
    double totalMoney;
    int orderNum;
    double averageOrderPrice;
    String x[];
    private List<ChartData> list = Lists.newArrayList();
}
