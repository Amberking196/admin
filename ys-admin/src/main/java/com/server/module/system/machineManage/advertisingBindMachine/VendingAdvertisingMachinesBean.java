package com.server.module.system.machineManage.advertisingBindMachine;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  vending_advertising_machines
 * author name: yjr
 * create time: 2018-12-24 15:45:31
 */ 
@Data
@Entity(tableName="vending_advertising_machines",id="id",idGenerate="auto")
public class VendingAdvertisingMachinesBean{


//@JsonIgnore	public String tableName="vending_advertising_machines";
//@JsonIgnore	public String selectSql="select * from vending_advertising_machines where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,advertisingId,vmCode from vending_advertising_machines where 1=1 ";
	private Long id;
	private Long advertisingId;
	private String vmCode;
	@NotField
	private String address;

}

