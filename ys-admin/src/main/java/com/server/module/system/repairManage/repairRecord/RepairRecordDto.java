package com.server.module.system.repairManage.repairRecord;

import com.server.common.persistence.Entity;
import com.server.module.system.repairManage.repairRecordVmCode.RepairRecordVmCodeBean;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
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
public class RepairRecordDto{
	private Long id;
	private String remark;
	private Date createTime;
	private Date updateTime;
	private String address;
	private Long createUser;
	private BigDecimal itemPrice;
	private BigDecimal price;
	
	private Long rid;
	private String vmCode;
	private String reason;
	private String plan;
	private Integer state;
	private BigDecimal carPrice;


	
	private Long vid;
	private String name;
	private BigDecimal perPrice;
	private Integer number;



	private List<RepairRecordVmCodeBean> vmCodeList;

}

