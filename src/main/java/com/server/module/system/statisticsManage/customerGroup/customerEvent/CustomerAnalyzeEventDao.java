package com.server.module.system.statisticsManage.customerGroup.customerEvent;

import java.util.List;

import com.server.module.system.statisticsManage.chart.ChartForm;
import com.server.module.system.statisticsManage.chart.DateCountVo;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-10-18 09:10:39
 */
public interface CustomerAnalyzeEventDao {

	public ReturnDataUtil listPage(CustomerAnalyzeEventCondition condition);

	public List<CustomerAnalyzeEventBean> list(CustomerAnalyzeEventCondition condition);

	public boolean update(CustomerAnalyzeEventBean entity);

	public boolean delete(Object id);

	public CustomerAnalyzeEventBean get(Object id);

	public CustomerAnalyzeEventBean insert(CustomerAnalyzeEventBean entity);
	public List<DateCountVo> listCustomerStateInfo(ChartForm form,int currState);
	
	public List<DateCountVo> listCustomerAnalyzeEventInfo(ChartForm form,int fromState,int currState);
}
