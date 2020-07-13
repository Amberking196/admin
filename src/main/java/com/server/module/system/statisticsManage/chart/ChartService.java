package com.server.module.system.statisticsManage.chart;

import java.util.List;
import java.util.Map;

public interface ChartService {
	public MultipleResponseData baseStatistics(ChartForm form);
	/**
	 * 用户订单24小时分布图
	 * @param form
	 * @return
	 */
	public MultipleResponseData orderByHour(ChartForm form);

  MultipleResponseData goodsStatistics(ChartForm form);

  public Map<Integer,String> listGoods();

}
