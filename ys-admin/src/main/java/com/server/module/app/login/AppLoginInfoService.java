package com.server.module.app.login;


public interface AppLoginInfoService {


	/**
	 * 根据alipayUserId获取工作人员信息
	 * @author hebiting
	 * @date 2018年4月28日下午4:43:52
	 * @return
	 */
	LoginInfoBean findLoginInfo(String alipayUserId);
	
	/**
	 * 根据手机号码查询登陆人信息
	 * @author hebiting
	 * @date 2018年5月12日下午3:18:12
	 * @param phone
	 * @return
	 */
	LoginInfoBean queryByPhone(String phone);
	
	
	/**
	 * 根据id查询登陆人信息
	 * @author hebiting
	 * @date 2018年5月12日下午3:18:12
	 * @param phone
	 * @return
	 */
	LoginInfoBean queryById(Long id);
}
