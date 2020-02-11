package com.server.module.app.vmway;

import java.util.List;

public interface AppVmwayService {

	/**
	 * 查询当前货道信息
	 * @author hebiting
	 * @date 2018年4月26日下午6:53:36
	 * @param vmCode
	 * @param wayNo
	 * @return
	 */
	public VendingMachinesWayBean queryByWayAndVmcode(String vmCode , Integer wayNo);
	
	/**
	 * 根据售货机编码查询所有货道信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:46:17
	 * @param vmCode
	 * @return
	 */
	public List<VendingMachinesWayBean> queryByWayAndVmcode(String vmCode);
}
