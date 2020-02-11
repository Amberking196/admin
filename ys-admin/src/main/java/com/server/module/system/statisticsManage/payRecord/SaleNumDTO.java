package com.server.module.system.statisticsManage.payRecord;

import com.google.common.collect.Lists;
import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.util.List;

@Data
public class SaleNumDTO {
    @ExcelField(title="机器编号",align=2)
    String vmCode;
    @ExcelField(title="",single=false)
    List<SaleNumItemDTO> list = Lists.newArrayList();
}
