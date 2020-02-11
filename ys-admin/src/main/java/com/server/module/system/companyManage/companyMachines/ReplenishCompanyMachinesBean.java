package com.server.module.system.companyManage.companyMachines;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  replenish_company_machines
 * author name: yjr
 * create time: 2018-09-14 15:10:24
 */ 
@Data
@Entity(tableName="replenish_company_machines",id="id",idGenerate="auto")
public class ReplenishCompanyMachinesBean{


//@JsonIgnore	public String tableName="replenish_company_machines";
//@JsonIgnore	public String selectSql="select * from replenish_company_machines where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,companyId,code,create_time from replenish_company_machines where 1=1 ";
	private Long id;
	private Long companyId;
	private String code;
	private Date createTime;
	@NotField
	private String companyName;

}

