package com.server.module.customer.userInfo.userWxInfo;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-15 15:05:14
 */
public interface TblCustomerWxService {

	public ReturnDataUtil listPage(TblCustomerWxForm condition);

	public List<TblCustomerWxBean> list(TblCustomerWxForm condition);

	public boolean update(TblCustomerWxBean entity);

	public boolean del(Object id);
	
	/**
	 * 查询微信用户信息
	 * @author why
	 * @date 2018年11月15日 下午3:16:25 
	 * @param customerId
	 * @return
	 */
	public TblCustomerWxBean get(Long customerId);

	public TblCustomerWxBean add(TblCustomerWxBean entity);
	
	/**
	 * 查询用户注册机器地址
	 * @author why
	 * @date 2019年1月16日 下午5:10:06 
	 * @return
	 */
	public String findCusteomerAddress();
	
	/**
	 * 我的邀请奖励
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 15:23
	 */
	public List<TblCustomerWxBean> myInviteRewards();



}
