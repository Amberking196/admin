package com.server.module.system.refund;

import java.util.List;

import com.server.module.commonBean.TotalResultBean;
import com.server.module.system.refund.application.RefundApplicationDto;
import com.server.module.system.refund.application.RefundApplicationForm;
import com.server.module.system.refund.principal.RefundPrincipalBean;
import com.server.module.system.refund.principal.RefundPrincipalForm;

public interface RefundService {

	/**
	 * 创建退款负责人
	 * @author hebiting
	 * @date 2018年10月23日下午2:19:12
	 * @param principal
	 * @return
	 */
	public boolean insertRefundPrincipal(RefundPrincipalBean principal);
	
	/**
	 * 更新退款负责人信息
	 * @author hebiting
	 * @date 2018年10月23日下午2:19:24
	 * @param principal
	 * @return
	 */
	public boolean updateRefundPrincipal(RefundPrincipalBean principal);
	
	/**
	 * 查询退款信息
	 * @author hebiting
	 * @date 2018年10月23日下午2:19:35
	 * @param form
	 * @return
	 */
	public List<RefundRecordBean> findRefundInfo(RefundRecordForm form);
	
	/**
	 * 获取退货记录总数
	 * @author hebiting
	 * @date 2018年10月23日下午3:27:11
	 * @param form
	 * @return
	 */
	public Long findRefundInfoNum(RefundRecordForm form);
	
	/**
	 * 获取负责人总数
	 * @author hebiting
	 * @date 2018年10月23日下午3:24:57
	 * @param form
	 * @return
	 */
	public Long getRefundPrincipalNum(RefundPrincipalForm form);
	
	/**
	 * 查询负责人信息
	 * @author hebiting
	 * @date 2018年10月23日下午2:58:02
	 * @param form
	 * @return
	 */
	public List<RefundPrincipalBean> getRefundPrincipalInfo(RefundPrincipalForm form);
	
	/**
	 * 根据订单类型查找订单信息
	 * @author hebiting
	 * @date 2018年10月23日上午10:08:15
	 * @param payCode
	 * @param orderType 1:机器订单;2:商城普通订单;3:团购订单
	 */
	public RefundOrderInfo findOrder(String payCode,Integer orderType);
	
	/**
	 * 根据id查询退款人电话号码
	 * @author hebiting
	 * @date 2018年10月25日下午2:54:28
	 * @param loginInfoId
	 * @return
	 */
	public String getPrincipalInfoById(Long loginInfoId);
	
	/**
	 * 获取退款申请信息
	 * @author hebiting
	 * @date 2018年11月26日下午3:51:57
	 * @param form
	 * @return
	 */
	public TotalResultBean<List<RefundApplicationDto>> findRefundApplication(RefundApplicationForm form);

	/**
	 * 审核
	 * @author hebiting
	 * @date 2018年11月27日下午3:04:25
	 * @param id
	 * @param state
	 * @param backReason
	 * @return
	 */
	public boolean updateRefundApplication(String payCode,Integer state,String backReason,Long updateUser);
	
	public boolean updatePayRecordItem(RefundDto refundDto);


}
