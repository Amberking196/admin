package com.server.module.system.logsManager.replenishLog;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name:  replenish_log
 * author name: yjr
 * create time: 2018-03-27 17:19:51
 */ 
@Data
public class ReplenishLogCondition extends PageAssist{
	
	String barCode;
	String vmCode;
	String itemName;
	String operator;
	Integer wayNumber;
	Integer companyId;
	@DateTimeFormat(iso=ISO.DATE)  Date startTime;
	@DateTimeFormat(iso=ISO.DATE) Date endTime;


}

