package com.server.module.system.warehouseManage.stock;

import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;

@Data
public class ExportWarehouseVo {
    @ExcelField(title = "仓库名")
    private String warehouseName;//仓库名
    @ExcelField(title = "商品名")
    private String itemName;//商品名
    @ExcelField(title = "商品条形码")
    private String barCode;
//    @ExcelField(title = "数量")
//    private Long quantity;//库存数量
//    @ExcelField(title = "成本价")
//    private Double costPrice;//成本价
//    @ExcelField(title = "总金额")
//    private Double zongJiner;//总金额
    @ExcelField(title = "单位")
    private String unitName;//商品单位
    @ExcelField(title = "商品数量")
    private Long quantity;//库存数量
    @ExcelField(title = "建议采购量")
    private Integer purchaseNumber;
    @ExcelField(title = "告警库存下限")
    private Integer lowerLimit;
    @ExcelField(title = "库存上限")
    private Integer higherLimit;
    
}
