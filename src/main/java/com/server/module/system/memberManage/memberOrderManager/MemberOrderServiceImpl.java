package com.server.module.system.memberManage.memberOrderManager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.customer.CustomerUtil;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerDao;
import com.server.util.IDUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;

/**
 * author name: yjr create time: 2018-09-26 14:36:43
 */
@Service
public class MemberOrderServiceImpl implements MemberOrderService {

	private static Logger log = LogManager.getLogger(MemberOrderServiceImpl.class);
	@Autowired
	private MemberOrderDao memberOrderDaoImpl;
	@Autowired
	private CustomerDao customerDaoImpl;

	/**
	 * 会员订单列表
	 */
	public ReturnDataUtil listPage(MemberOrderForm memberOrderForm) {
		log.info("<MemberOrderServiceImpl>-----<listPage>-------start");
		ReturnDataUtil listPage = memberOrderDaoImpl.listPage(memberOrderForm);
		log.info("<MemberOrderServiceImpl>-----<listPage>-------end");
		return listPage;
	}

	/**
	 * 增加会员订单
	 */
	public ReturnDataUtil add(MemberOrderBean entity) {
		log.info("<MemberOrderServiceImpl>-----<add>-------start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		// 得到用户id
		Long customerId = CustomerUtil.getCustomerId();
		// 得到用户信息
		CustomerBean bean = customerDaoImpl.findCustomerById(customerId);
		if (StringUtils.isBlank(bean.getOpenId())) {
			log.info("==========非微信用户，订单创建失败！============");
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("非微信用户，订单创建失败！");
			return returnDataUtil;
		} else {
			if(StringUtil.isNotBlank(entity.getFriendPhone())) {
				CustomerBean vo = customerDaoImpl.getCustomerByPhone(entity.getFriendPhone());
				if(vo==null){
					log.info("==========非忧水用户，订单创建失败！============");
					returnDataUtil.setStatus(-99);
					returnDataUtil.setMessage("输入手机号非忧水用户，订单创建失败！");
					return returnDataUtil;
				}else{
					entity.setFriendCustomerId(vo.getId());
				}
			}
			// 生成订单编号
			String payCode = IDUtil.getPayCode();
			entity.setState(PayStateEnum.NOT_PAY.getState().longValue());
			entity.setPayCode(payCode);
			entity.setCustomerId(customerId);
			MemberOrderBean memberOrderBean = memberOrderDaoImpl.insert(entity);
			if (memberOrderBean != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("订单创建成功！");
				returnDataUtil.setReturnObject(memberOrderBean);
			} else {
				returnDataUtil.setStatus(-99);
				returnDataUtil.setMessage("订单创建失败！");
				returnDataUtil.setReturnObject(memberOrderBean);
			}
		}
		log.info("<MemberOrderServiceImpl>-----<add>-------end");
		return returnDataUtil;
	}

	public boolean update(MemberOrderBean entity) {
		return memberOrderDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return memberOrderDaoImpl.delete(id);
	}

	public List<MemberOrderBean> list(MemberOrderForm condition) {
		return null;
	}

	public MemberOrderBean get(Object id) {
		return memberOrderDaoImpl.get(id);
	}
	
	@Override
	public boolean paySuccessMemberOrder(String outTradeNo, String transactionId) {
		log.info("<MemberOrderServiceImpl>------<paySuccessMemberOrder>-----start");
		boolean falg = memberOrderDaoImpl.paySuccessMemberOrder(outTradeNo, transactionId);
		log.info("<MemberOrderServiceImpl>------<paySuccessMemberOrder>-----end");
		return falg;
	}
	
	@Override
	public MemberOrderBean getMemberOrder(String payCode) {
		log.info("<MemberOrderServiceImpl>------<getMemberOrder>-----start");
		MemberOrderBean memberOrder = memberOrderDaoImpl.getMemberOrder(payCode);
		log.info("<MemberOrderServiceImpl>------<getMemberOrder>-----end");
		return memberOrder;
	}
	
	@Override
	public boolean updateCustomerBalance(MemberOrderBean entity) {
		log.info("<MemberOrderServiceImpl>------<updateCustomerBalance>-----start");
		boolean flag = memberOrderDaoImpl.updateCustomerBalance(entity);
		log.info("<MemberOrderServiceImpl>------<getMemberOrder>-----start");
		return flag;
	}
}
