package com.server.module.system.repairManage.repairRecord;

import com.server.common.persistence.Entity;
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
@Entity(tableName="repair_record",id="",idGenerate="auto")
public class RepairRecordBean{
	private Long id;
	private String remark;
	private BigDecimal carPrice;
	private Date createTime;
	private Date updateTime;
	private String address;
	private Long createUser;
	private BigDecimal itemPrice;
	private BigDecimal price;
	
	private List<RepairRecordVmCodeBean> vmCodeList;

}

