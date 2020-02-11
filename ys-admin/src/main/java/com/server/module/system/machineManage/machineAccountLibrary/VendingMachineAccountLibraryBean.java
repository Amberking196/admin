package com.server.module.system.machineManage.machineAccountLibrary;

import java.util.Date;

import lombok.Data;

/**
 * 
 * author name: why 售货机账号库 bean
 * create time: 2018-04-02 10:36:45
 *
 */
@Data
public class VendingMachineAccountLibraryBean {

	//商品名称
	private String itemName;  
	//上架货道数
	private Integer numberOfHits;
	//成本价
	private Double costPrice;
	//售价
	private Double price;
	//过期时间
	private  Date endTime;
	//商品图片
	private String pic;
	//条形码
	private String barCode;
	//规格
	private String standard;
	//商品单位
	private String unit;
	//包装规格
	private String pack;
	//所属公司
	private String subordinateCompanies;
	
}
