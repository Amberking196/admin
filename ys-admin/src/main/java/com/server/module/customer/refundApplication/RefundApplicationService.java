package com.server.module.customer.refundApplication;

import java.math.BigDecimal;
import java.util.List;

public interface RefundApplicationService {


	/**
	 * 判断订单是否可退款
	 * @author hebiting
	 * @date 2018年11月27日上午11:34:54
	 * @param payCode
	 * @param refundPrice
	 * @return
	 */
	public RefundApplicationDto judgeOrderRefund(String payCode,BigDecimal refundPrice,Integer orderType); 


	/**
	 * 退款申请
	 * @author hebiting
	 * @date 2018年10月31日上午8:58:46
	 * @return
	 */
	public Long insertRefundApplication(RefundApplicationBean refundApp);
	
	/**
	 * 获取退款申请信息
	 * @author hebiting
	 * @date 2018年10月31日下午1:36:42
	 * @param payCode
	 * @return
	 */
	public List<RefundApplicationBean> getRefundApplication(String payCode);
}
