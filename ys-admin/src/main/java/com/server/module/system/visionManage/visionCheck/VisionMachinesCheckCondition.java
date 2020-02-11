package com.server.module.system.visionManage.visionCheck;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  vision_machines_check
 * author name: yjr
 * create time: 2019-10-10 10:54:34
 */ 
@Data
public class VisionMachinesCheckCondition extends PageAssist{

	private String id;//主表id
	private String vmCode;
}

