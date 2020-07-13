package com.server.module.system.machineManage.machineCode;

import com.server.common.persistence.Entity;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  vending_machines_code
 * author name: why
 * create time: 2018-04-10 15:07:28
 */ 
@Data
@Entity(tableName="vending_machines_code",id="id",idGenerate="auto")
public class VendingMachinesCodeBean{


	/*@JsonIgnore	
	public String tableName="vending_machines_code";
	@JsonIgnore	
	public String selectSql="select * from vending_machines_code where 1=1 ";
	@JsonIgnore	
	public String selectSql1="select areaNumber,machinesTypeId,code,id from vending_machines_code where 1=1 ";
	*/
	private String areaNumber;
	private Long machinesTypeId;
	private Long code;
	private Long id;

}

