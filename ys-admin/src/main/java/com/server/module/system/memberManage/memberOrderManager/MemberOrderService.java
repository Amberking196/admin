package com.server.module.system.memberManage.memberOrderManager;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-09-26 14:36:43
 */
public interface MemberOrderService {

	/**
	 * 会员订单列表
	 * @author why
	 * @date 2018年9月26日 下午5:04:17 
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(MemberOrderForm condition);

	public List<MemberOrderBean> list(MemberOrderForm condition);

	public boolean update(MemberOrderBean entity);

	public boolean del(Object id);

	public MemberOrderBean get(Object id);

	/**
	 * 增加会员订单
	 * @author why
	 * @date 2018年9月27日 上午11:08:23 
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil add(MemberOrderBean entity);
	
	/**
	 * 微信完成商城订单完成后的回调，用以确认订单是否完成支付，并更新状态
	 * 
	 * @author why
	 * @date 2019年2月18日 下午5:52:34
	 * @param outTradeNo
	 * @param transactionId
	 * @return
	 */
	public boolean paySuccessMemberOrder(String outTradeNo, String transactionId);
	
	/**
	 * 获取充值订单信息
	 * @author why
	 * @date 2019年2月18日 下午5:54:47 
	 * @param payCode
	 * @return
	 */
	public MemberOrderBean getMemberOrder(String payCode);
	
	/**
	 * 修改用户余额
	 * @author why
	 * @date 2019年2月18日 下午5:56:57 
	 * @param entity
	 * @return
	 */
	public boolean updateCustomerBalance(MemberOrderBean entity);
}
