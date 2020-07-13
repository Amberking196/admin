package com.server.module.system.couponManager.couponCustomer;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-06-28 09:15:49
 */
public interface CouponCustomerService {


    public ReturnDataUtil listPage(CouponCustomerForm condition);

    public List<CouponCustomerBean> list(CouponCustomerForm condition);

    public boolean update(CouponCustomerBean entity);

    public boolean del(Object id);

    public CouponCustomerBean get(Object id);

    public CouponCustomerBean add(CouponCustomerBean entity);

    public ReturnDataUtil listPageForCustomer(CouponCustomerForm condition);

    List<CustomerCouponVo> list(int customerId);

    /**
     * 优惠券短信提醒列表
     * @author why
     * @date 2018年12月17日 下午4:01:55 
     * @param conponCustomerNoteForm
     * @return
     */
	public ReturnDataUtil conponCustomerNoteList(ConponCustomerNoteForm conponCustomerNoteForm);
	
	/**
	 * 修改券短信状态
	 * @author why
	 * @date 2018年12月18日 上午9:44:48 
	 * @param conponCustomerNoteForm
	 * @return
	 */
	public boolean updateIsSendState(ConponCustomerNoteForm conponCustomerNoteForm);
}

