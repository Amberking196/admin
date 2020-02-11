package com.server.module.system.logsManager.exportLog;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  export_log
 * author name: yjr
 * create time: 2018-09-14 10:31:38
 */ 
@Data
public class ExportLogCondition extends PageAssist{
	private Integer companyId;//公司id
	private String operatorName;//导出人姓名

}

