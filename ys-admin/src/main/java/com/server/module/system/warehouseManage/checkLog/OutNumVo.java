package com.server.module.system.warehouseManage.checkLog;
import java.math.BigDecimal;

import lombok.Data;
@Data
public class OutNumVo {
    //商品出货数量
    private Long basicItemId;
    private String itemName;
    private BigDecimal num;
}
