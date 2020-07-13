package com.server.module.system.baseManager.stateInfo;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  state_info
 * author name: yjr
 * create time: 2018-03-30 11:10:15
 */ 
@Data
public class StateInfoCondition extends PageAssist{
	
	private String keyName;
	private Long state;
	private String name;


}

