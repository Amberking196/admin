package com.server.module.system.warehouseManage.stock;

import com.server.common.persistence.NotField;
import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

@Data
public class ExportCompanyVo {


    @ExcelField(title = "公司")
    private String companyName;//公司名

    @ExcelField(title = "商品条形码")
    private String barCode;

    @ExcelField(title = "商品名称")
    private String itemName;//商品名

    @ExcelField(title = "商品单位")
    private String unitName;//商品单位

    @ExcelField(title = "商品数量")
    private Long quantity;//库存数量

  /*  @ExcelField(title = "商品单价")
    private Double costPrice;//商品单价

    @ExcelField(title = "商品金额")
    private Double zongJiner;//商品金额
*/


}
