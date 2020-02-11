package com.server.module.system.warehouseManage.checkLog;
import java.math.BigDecimal;

import lombok.Data;
@Data
public class ReplenishNumVo {
  //商品补货数
  Long basicItemId;
  BigDecimal num;
}
