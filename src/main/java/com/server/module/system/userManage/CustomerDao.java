package com.server.module.system.userManage;

import java.util.List;

import com.server.util.ReturnDataUtil;

public interface CustomerDao {

	/**
	 * 根据用户id查询用户信息
	 * @param id
	 * @return CustomerBean
	 */
	CustomerBean findCustomerById(Long id);
	
	
	/**
	 * 根据条件查询用户信息
	 * @param userManagerForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil findCustomer(UserManagerForm userManagerForm);

	
	/**
	 * 查询用户的购水信息
	 * @param userManagerForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil queryDetail(UserManagerForm userManagerForm);
	
	/**
	 * 根据手机号查询用户信息
	 * @author why
	 * @date 2018年12月3日 下午4:30:00 
	 * @param phone
	 * @return
	 */
	CustomerBean getCustomerByPhone(String phone);
	
	/**
	 * 查询用户推荐信息
	 * @author hjc
	 * @date 2018年12月6日 下午4:30:00 
	 * @param userManagerForm
	 * @return
	 */
	ReturnDataUtil queryInvite(UserManagerForm userManagerForm);
	
	/**
	 * 查询用户推荐详情信息
	 * @author hjc
	 * @date 2018年12月6日 下午4:30:00 
	 * @param userManagerForm
	 * @return
	 */
	ReturnDataUtil queryInviteDetail(UserManagerForm userManagerForm);
	
	/**
	 * 查询用户多个支付时间
	 * @author hjc
	 * @date 2019年1月28日 下午4:30:00 
	 * @return
	 */
	List<CustomerVo> customerVo();
	
	/**
	 * 游戏中奖后 更新用户积分或者余额
	 * @author why
	 * @date 2019年3月4日 上午10:07:16 
	 * @param customerId
	 * @param rewards
	 * @param type 1 积分  2 余额
	 * @return
	 */
	public boolean updateCustomerBean(Long customerId,Long rewards,Integer type);
	
	/**
	 * 顾客是否第一次购买
	 * @author why
	 * @date 2019年2月16日 下午3:17:26 
	 * @param customerId
	 * @return
	 */
	public Integer isFirstBuy(Long customerId);
	
	/**
	 * 顾客是否商城第一次购买
	 * @author why
	 * @date 2019年2月16日 下午3:26:21 
	 * @param customerId
	 * @return
	 */
	public Integer isStoreFirstBuy(Long customerId);
	
	/**
	 * 获取用户最后一次购买商品所在的售货机
	 * @author hjc
	 * @date 2019年3月8日 下午11:29:44 
	 * @param customerId
	 * @return
	 */
	public String getCustomerLastVmcode(Long customerId);
}
