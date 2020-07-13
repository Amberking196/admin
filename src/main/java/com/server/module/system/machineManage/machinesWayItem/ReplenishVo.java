package com.server.module.system.machineManage.machinesWayItem;

import java.util.List;

import com.google.common.collect.Lists;
import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;

@Data
public class ReplenishVo {
	@ExcelField(title="机器编号")
	 private String vmCode;
	@ExcelField(title="机器地址")
	 private String locatoinName;
	
	@ExcelField(title="机器状态")
	 private String state;
	
	 
	 private Integer companyId;
	 private Integer areaId;
	 private Integer lineId;
	 
	 List<ReplenishItemVo> itemList;
	 public ReplenishVo(){
		 itemList=Lists.newArrayList();
	 }
	 @ExcelField(title="详情（商品名  货道号∼当前货道容量/货道容量  可补数  可补货率  下架数）")
	 public String detail;
	 
	
	 
}
