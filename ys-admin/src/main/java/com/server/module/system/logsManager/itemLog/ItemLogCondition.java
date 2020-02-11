package com.server.module.system.logsManager.itemLog;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name:  item_log
 * author name: yjr
 * create time: 2018-03-27 16:57:05
 */ 
@Data
public class ItemLogCondition extends PageAssist{

	String vmCode;
	Integer wayNumber;
	String itemName;
	String barCode;
	@DateTimeFormat(iso=ISO.DATE) Date startTime;
	@DateTimeFormat(iso=ISO.DATE) Date endTime;
	String operator;
	Integer companyId;
}

