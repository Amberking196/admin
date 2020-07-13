package com.server.module.system.machineManage.machinesPic;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  vending_pic_machines
 * author name: yjr
 * create time: 2018-12-24 15:45:31
 */ 
@Data
@Entity(tableName="vending_pic_machines",id="id",idGenerate="auto")
public class VendingPicMachinesBean{


//@JsonIgnore	public String tableName="vending_advertising_machines";
//@JsonIgnore	public String selectSql="select * from vending_advertising_machines where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,advertisingId,vmCode from vending_advertising_machines where 1=1 ";
	private Long id;
	private Long picId;
	private String vmCode;
	@NotField
	private String address;

}

