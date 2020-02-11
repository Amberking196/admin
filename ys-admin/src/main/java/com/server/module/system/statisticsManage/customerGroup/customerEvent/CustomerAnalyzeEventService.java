package com.server.module.system.statisticsManage.customerGroup.customerEvent;

import java.util.List;

import com.server.module.system.statisticsManage.chart.ChartForm;
import com.server.module.system.statisticsManage.chart.DateCountVo;
import com.server.module.system.statisticsManage.chart.MultipleResponseData;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-10-18 09:10:39
 */
public interface CustomerAnalyzeEventService {

	public ReturnDataUtil listPage(CustomerAnalyzeEventCondition condition);

	public List<CustomerAnalyzeEventBean> list(CustomerAnalyzeEventCondition condition);

	public boolean update(CustomerAnalyzeEventBean entity);

	public boolean del(Object id);

	public CustomerAnalyzeEventBean get(Object id);

	public CustomerAnalyzeEventBean add(CustomerAnalyzeEventBean entity);
	
	
	public List<DateCountVo> listCustomerAnalyzeEventInfo(ChartForm form);
	
	public List<CustomerEventStateVo> listStateChange();
	public MultipleResponseData customerStateChangeStatistics(ChartForm form);
	public MultipleResponseData customerStateStatistics(ChartForm form);
}
