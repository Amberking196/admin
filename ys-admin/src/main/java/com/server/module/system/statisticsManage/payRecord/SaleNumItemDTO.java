package com.server.module.system.statisticsManage.payRecord;

import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;

@Data
public class SaleNumItemDTO {
    @ExcelField(title="货道号",align=2)
    int wayNumber;
    Long basicItemId;
    @ExcelField(title="商品名",align=2)
    String itemName;
    @ExcelField(title="销售量",align=2)
    int num;
}
