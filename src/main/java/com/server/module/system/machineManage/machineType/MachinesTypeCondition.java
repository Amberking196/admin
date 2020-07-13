package com.server.module.system.machineManage.machineType;

import com.server.module.commonBean.PageAssist;
import com.server.util.ReturnDataUtil;

import lombok.Data;

/**
 * table name:  machines_type
 * author name: yjr
 * create time: 2018-03-28 15:47:37
 */ 
@Data
public class MachinesTypeCondition extends PageAssist{

	 String name;
	 Integer wayCount;
	 Integer state;
	 Integer id;

}

