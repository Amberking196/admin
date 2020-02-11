package com.server.module.system.statisticsManage.payRecordPerHour;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data; 
import com.server.module.commonBean.PageAssist;
/**
 * table name:  tbl_statistics_pay_per_hour
 * author name: hjc
 * create time: 2018-07-13 09:23:01
 */

@Data
public class PayRecordPerHourForm extends PageAssist{
	//公司id
	private Integer companyId;
	//公司及其子公司id
	private String companyIds;
	//起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	//结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
}

