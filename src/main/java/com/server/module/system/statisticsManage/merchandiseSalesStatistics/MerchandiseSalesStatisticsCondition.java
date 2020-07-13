package com.server.module.system.statisticsManage.merchandiseSalesStatistics;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * author name: why create time: 2018-05-09 21:15:27
 */

@Data
public class MerchandiseSalesStatisticsCondition extends PageAssist {

	// 商品id
	Integer itemId;
	Integer basicItemId;
	Integer areaId;
	// 售货机编号
	String vmCode;
	// 公司id
	Integer companyId;
	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endTime;
}
