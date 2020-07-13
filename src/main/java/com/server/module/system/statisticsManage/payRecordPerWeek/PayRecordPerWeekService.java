package com.server.module.system.statisticsManage.payRecordPerWeek;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-07-14 14:38:10
 */ 
public interface  PayRecordPerWeekService{

	/**
	 * 查询商品销售统计(每小时)列表
	 * @param PayRecordPerWeekForm 
	 * return 
	 */
	public ReturnDataUtil listPage(PayRecordPerWeekForm PayRecordPerWeekForm);
	public boolean update(PayRecordPerWeekBean entity);
	public boolean del(Object id);
	public PayRecordPerWeekBean get(Object id);
	public PayRecordPerWeekBean add(PayRecordPerWeekBean entity);
}

