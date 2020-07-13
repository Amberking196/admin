package com.server.module.system.statisticsManage.payRecord;

import lombok.Data;

@Data
public class ListSaleNumDTO {
    String vmCode;
    int wayNumber;
    Long basicItemId;
    String itemName;
    int num;

}
