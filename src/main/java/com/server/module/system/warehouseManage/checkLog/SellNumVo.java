package com.server.module.system.warehouseManage.checkLog;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SellNumVo {//商品销售数量
	  Long basicItemId;
	  BigDecimal num;
}
