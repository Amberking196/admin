package com.server.module.system.userManage;

import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;

@Data
public class CustomerPayTimeVo {
	@ExcelField(title = "支付时间",align=2)
	String date;
}
