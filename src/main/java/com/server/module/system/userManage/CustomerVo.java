package com.server.module.system.userManage;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.server.common.utils.excel.annotation.ExcelField;
import com.server.module.system.replenishManage.machinesReplenishManage.ItemNumVo;

import lombok.Data;

@Data
public class CustomerVo {

	@ExcelField(title = "电话号码")
	private String phone;
	
	@ExcelField(title = "注册时间")
	private Date createTime;
	
	@ExcelField(title = "机器编号")
	private String vmCode;
	
	@ExcelField(title = "公司")
	private String CompanyName;
	
	@ExcelField(title = "消费次数",align=2)
	private Integer length; 
	
	@ExcelField(title="",single=false)
	List<CustomerPayTimeVo> payTimeList = Lists.newArrayList();
	
	private String payTime;
	

}
