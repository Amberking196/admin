package com.server.module.system.machineManage.machineInit;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * table  name: vending_machine_init
 * author name: why 售货机初始化 bean
 * create time: 2018-04-03 09:23:44
 */
@Entity(tableName="vending_machine_init",id="id",idGenerate="assign")
@Data
public class VendingMachineInitBean {

	@JsonIgnore
	public String tableName="vending_machine_init";
	@JsonIgnore
	public String selectSql="select * from vending_machine_init where 1=1 ";
	@JsonIgnore
	public String selectSql1="select id,vendingMachinesCode,startDate,endDate,isInit  from vending_machine_init where 1=1 ";
	
	//ID
	private Integer id;
	//售货机编号
	private String vendingMachinesCode;
	//开始时间
	private Date startDate;
	//结束时间
	private Date endDate;
	//是否初始化 0 未完成  1  已完成
	private Integer isInit;
	//耗时
	private String consumeTime;
}
