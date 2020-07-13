package com.server.module.system.tblManager.tblRateShortsReport;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  tbl_rate_shorts_report
 * author name: yjr
 * create time: 2018-03-28 08:58:56
 */ 
@Data
public class TblRateShortsReportCondition extends PageAssist{
	@DateTimeFormat(iso=ISO.DATE) private Date startTime;
	@DateTimeFormat(iso=ISO.DATE) private Date endTime;
	private Integer companyId;

}

