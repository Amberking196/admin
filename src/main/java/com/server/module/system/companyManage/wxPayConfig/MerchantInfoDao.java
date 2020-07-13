package com.server.module.system.companyManage.wxPayConfig;

import java.util.List;


public interface MerchantInfoDao {
	
	/**
	 * 根据type查询相应的配置信息
	 * @author hebiting
	 * @date 2018年6月29日下午5:02:17
	 * @param type（1：服务商，0：普通商户）
	 * @return
	 */
	List<MerchantInfo> findMerchantInfo(Integer type);
	
	/**
	 * 查看公司支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午9:50:09
	 * @param companyId
	 * @return
	 */
	MerchantInfo getPayConfig(Integer companyId);
	
	/**
	 * 修改公司支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午10:16:29
	 * @param merchant
	 * @return
	 */
	boolean updatePayConfig(MerchantInfo merchant);
	
	/**
	 * 新增公司支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午11:25:14
	 * @param merchant
	 * @return
	 */
	Integer insertPayConfig(MerchantInfo merchant);
	
	/**
	 * 根据公司id获取用户信息
	 * @author hebiting
	 * @date 2018年11月12日下午4:21:37
	 * @param companyId
	 * @return
	 */
	MerchantInfo findMerchantInfoByCompanyId(Integer companyId);
}
