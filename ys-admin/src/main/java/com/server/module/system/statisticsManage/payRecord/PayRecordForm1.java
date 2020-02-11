package com.server.module.system.statisticsManage.payRecord;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

@Data
public class PayRecordForm1 extends PageAssist {

	private String companyId;
	private String areaId;
	private String lineId;
	
	private String startTime;
	private String endTime;
	private String vmCode;
	
}
