package com.server.module.system.replenishManage.machinesReplenishManage;

import com.google.common.collect.Lists;
import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;

import java.util.List;

/**
 * 
 * author name: why
 * create time: 2018-04-23 16:19:47
 */ 

@Data
public class MachinesReplenishBean{

	//序号
	private Integer id;
	
	//String rate;
	//缺货比例
	@ExcelField(title = "缺货百分比",align=2)
		private Integer ratio;
	//公司名称
	@ExcelField(title = "公司名",align=2)
	private String companyName;
	//线路名称
	@ExcelField(title = "线路名",align=2)
	private String lineName;
	//售货机编号
	@ExcelField(title = "机器编码",align=2)
	private String code;
	//负责人
	@ExcelField(title = "负责人",align=2)
	private String principal;
	@ExcelField(title = "故障",align=2)
	private String errorInfo;
	//详细地址
	@ExcelField(title = "机器地址",align=2)
	private String locatoinName;

	
	//线路id
	private Integer lineId;
	//@ExcelField(title = "商品缺货详情",align=2)
	//private String itemNums;
	//@ExcelField(title = "商品名称",align=2)
	String itemName;
	//@ExcelField(title = "当前",align=2)
	int num;
	//@ExcelField(title = "最大",align=2)
	int fullNum;

	//@ExcelField(title = "可补",align=2)
	int buNum;
	//@ExcelField(title = "商品",align=2)
	String simpleName1;
	//@ExcelField(title = "可补",align=2)
	int buNum1;
	//@ExcelField(title = "商品",align=2)
	String simpleName2;
	//@ExcelField(title = "可补",align=2)
	int buNum2;
	//@ExcelField(title = "商品",align=2)
	String simpleName3;
	//@ExcelField(title = "可补",align=2)
	int buNum3;
	//@ExcelField(title = "商品",align=2)
	String simpleName4;
	//@ExcelField(title = "可补",align=2)
	int buNum4;
	@ExcelField(title="",single=false)
	List<ItemNumVo> listVo = Lists.newArrayList();
	
	
}

