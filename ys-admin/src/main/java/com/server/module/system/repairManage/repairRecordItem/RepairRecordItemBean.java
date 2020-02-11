package com.server.module.system.repairManage.repairRecordItem;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.system.repairManage.repairRecordVmCode.RepairRecordVmCodeBean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  repair_record
 * author name: yjr
 * create time: 2019-08-14 10:42:05
 */ 
@Data
@Entity(tableName="repair_record_item",id="id",idGenerate="auto")
public class RepairRecordItemBean{
	private Long id;
	private Long mid;
	private Long vid;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private Integer number;
	private String remark;
	//private List<RepairRecordVmCodeBean> vmCodeList;

	
	@NotField
	private String name;
	@NotField
	private BigDecimal price;
}

