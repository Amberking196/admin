package com.server.module.app.vmitem;

import java.util.List;

public interface AppVmitemService {

	/**
	 * 根据机器编码，货道号查询商品基础id
	 * @author hebiting
	 * @date 2018年4月26日下午10:34:15
	 * @param vmCode
	 * @param wayNum
	 * @return
	 */
	public Long queryBasicItemId(String vmCode,Integer wayNum);
	/**
	 * 根据vmCode查询机器售卖商品信息
	 * hebiting
	 * 2018年4月28日上午11:53:06
	 * @param vmCode
	 * @param machineVersion
	 * @return
	 */
	public List<ItemDto> queryVmitem(String vmCode,int machineVersion);
}
