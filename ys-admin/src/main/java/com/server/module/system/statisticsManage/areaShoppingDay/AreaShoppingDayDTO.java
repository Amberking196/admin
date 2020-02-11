package com.server.module.system.statisticsManage.areaShoppingDay;
import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;

@Data
public class AreaShoppingDayDTO {
    @ExcelField(title="日期",align = 2)
    private String day;//日期
    @ExcelField(title="机器台数",align = 2)
    private Integer machinesNum;//机器数
    @ExcelField(title="订单数",align = 2)
    private int orderNum;//订单数
    @ExcelField(title="商品个数",align = 2)
    private int itemNum;//商品数
    @ExcelField(title="平均商品数",align = 2)
    private double averageItemNum;//平均商品数
    @ExcelField(title="销售额",align = 2)
    private double totalMoney;//金额
    @ExcelField(title="最高单价",align = 2)
    private double maxPrice;
    @ExcelField(title="最低单价",align = 2)
    private double minPrice;
    @ExcelField(title="客单价",align = 2)
    private double averageOrderPrice;//客单价
}