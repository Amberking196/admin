package com.server.module.system.companyManage.aliPayConfig;

public interface AliPayDao {

	/**
	 * 更新阿里支付配置
	 * @author hebiting
	 * @date 2018年8月7日上午8:52:58
	 * @return
	 */
	boolean updateAliConfig(AliPayConfig aliConfig);
	
	/**
	 * 新增阿里支付配置
	 * @author hebiting
	 * @date 2018年8月7日上午8:53:11
	 * @return
	 */
	boolean insertAliConfig(AliPayConfig aliConfig);
	
	/**
	 * 查询阿里支付配置
	 * @author hebiting
	 * @date 2018年8月7日上午8:53:42
	 * @return
	 */
	AliPayConfig findAliConfig(Integer companyId);
}
