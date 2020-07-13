package com.server.module.system.statisticsManage.payRecordPerHour;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-07-13 09:23:01
 */ 
public interface  PayRecordPerHourService{

	/**
	 * 查询商品销售统计(每小时)列表
	 * @param tblStatisticsPerHourForm 
	 * return 
	 */
	public ReturnDataUtil listPage(PayRecordPerHourForm form);
	public boolean update(PayRecordPerHourBean entity);
	public boolean del(Object id);
	public PayRecordPerHourBean get(Object id);
	public PayRecordPerHourBean add(PayRecordPerHourBean entity);
}

