package com.server.module.system.logsManager.replenishLog;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;

import lombok.Data;
/**
 * table name:  replenish_log
 * author name: yjr
 * create time: 2018-03-23 11:24:53
 */ 
@Data
@Entity(tableName="replenish_log",id="",idGenerate="auto")
public class ReplenishLogBean{

	@JsonIgnore
	public String tableName="replenish_log";
	@JsonIgnore 
	String selectSql="select * from replenish_log where 1=1 ";
	@JsonIgnore
	public String selectSql1="select vmCode,wayNumber,newItemName,newBarCode,newStandard,newState,newNum,oldItemName,oldBarCode,oldStandard,oldState,oldNum,operator,createTime,id from replenish_log where 1=1 ";
	
	private String vmCode;
	private Integer wayNumber;
	private String newItemName;
	private String newBarCode;
	private String newStandard;
	private Integer newState;
	private Integer newNum;
	private String oldItemName;
	private String oldBarCode;
	private String oldStandard;
	private Integer oldState;
	private Integer oldNum;
	private String operator;
	private Date createTime;
	private Long id;


}

