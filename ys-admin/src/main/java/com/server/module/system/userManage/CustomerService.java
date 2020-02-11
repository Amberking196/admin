package com.server.module.system.userManage;

import java.util.Map;
import com.server.util.ReturnDataUtil;

public interface CustomerService {

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
	 * @date 2019年2月16日 下午3:19:11 
	 * @param customerId
	 * @return
	 */
	public Integer isFirstBuy(Long customerId);
	
	/**
	 * 顾客是否商城第一次购买
	 * @author why
	 * @date 2019年2月16日 下午3:28:25 
	 * @param customerId
	 * @return
	 */
	public Integer isStoreFirstBuy(Long customerId);
	
	/**
	 * 根据用户id发送公众号客服信息
	 * @author why
	 * @date 2019年2月18日 下午3:53:02 
	 * @param openId
	 * @param message
	 */
	public void sendWechatMessage(String openId,String message);
	
}
