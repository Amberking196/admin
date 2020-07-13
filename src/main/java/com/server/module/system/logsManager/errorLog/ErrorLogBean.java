package com.server.module.system.logsManager.errorLog;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;
/**
 * table name:  error_log
 * author name: yjr
 * create time: 2018-03-24 09:57:07
 */ 
@Data
@Entity(tableName="error_log",id="id",idGenerate="auto")
public class ErrorLogBean{

   /* @JsonIgnore
	public String tableName="error_log";
    @JsonIgnore
	public String selectSql="select * from error_log where 1=1 ";
    @JsonIgnore
	public String selectSql1="select id,state,orderId,vendingMachinesCode,msg,createTime,solveTime,solveState,solve,remark from error_log where 1=1 ";*/
	private Long id;
	private Integer state;
	private Long orderId;
	private String vendingMachinesCode;
	private String msg;
	private Date createTime;
	private Date solveTime;
	private Integer solveState;
	private String solve;//解决人
	private String remark;
	
	//以下不对应数据库字段
	@NotField
	private String stateLable;//解决状态


}

