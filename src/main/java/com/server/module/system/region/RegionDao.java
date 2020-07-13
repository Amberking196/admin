package com.server.module.system.region;

import java.util.List;

public interface RegionDao {

	/**
	 * 根据parentId获取子地址信息
	 * @author hebiting
	 * @date 2018年8月1日下午3:37:55
	 * @param parentId
	 * @return
	 */
	List<RegionBean> getByParentId(Integer parentId);
	
	/**
	 * 根据id获取地址名称
	 * @author hebiting
	 * @date 2018年8月1日下午4:15:06
	 * @param id
	 * @return
	 */
	String getNameById(Integer id);
	
	/**
	 * 是否可以入驻ali
	 * @author hebiting
	 * @date 2018年8月2日上午10:55:27
	 * @param vmCode
	 * @return
	 */
	boolean canEnterAli(String vmCode);
	
}
