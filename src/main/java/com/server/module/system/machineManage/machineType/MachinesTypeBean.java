package com.server.module.system.machineManage.machineType;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;
/**
 * table name:  machines_type
 * author name: yjr
 * create time: 2018-03-22 13:31:26
 */ 
@Data
@Entity(tableName="machines_type",id="id",idGenerate="auto")
public class MachinesTypeBean{
   //@JsonIgnore
	//public String tableName="machines_type";
  // @JsonIgnore
	//public String selectSql="select * from machines_type where 1=1 ";
  // @JsonIgnore
//	public String selectSql1="select id,name,wayCount,state,stopDate,buyUpperLimit from machines_type where 1=1 ";
	private Long id;
	private String name;
	private Integer wayCount;
	private Integer state;
	private Date stopDate;
	private Integer buyUpperLimit;
	private Date createTime;//创建时间
	@NotField
	private String stateName;

	
}

