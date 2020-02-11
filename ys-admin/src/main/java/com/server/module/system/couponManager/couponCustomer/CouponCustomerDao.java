package com.server.module.system.couponManager.couponCustomer;

import java.util.List;

import com.server.module.system.couponManager.coupon.AddAllCustomerForm;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-06-28 09:15:49
 */
public interface CouponCustomerDao {

    public ReturnDataUtil listPage(CouponCustomerForm condition);

    public List<CouponCustomerBean> list(CouponCustomerForm condition);

    public boolean update(CouponCustomerBean entity);

    public boolean delete(Object id);

    public CouponCustomerBean get(Object id);

    public CouponCustomerBean insert(CouponCustomerBean entity);

    public ReturnDataUtil listPageForCustomer(CouponCustomerForm condition);

    public List<Long> getAllCustomerId(AddAllCustomerForm condition);

    public void batchInsertSql(List<String> sqls);

    int batchInsert(List<Long> customerLists, Long couponId);
    public List<CustomerCouponVo> listByCouponId(int couponId);
    List<CustomerCouponVo> list(int customerId);
    List<CustomerCouponVo> list(String vmCode);

    boolean isHaveCustomer(Long id);

    /**
     * 优惠券短信提醒列表
     * @author why
     * @date 2018年12月17日 下午4:05:28 
     * @param conponCustomerNoteForm
     * @return
     */
	public ReturnDataUtil conponCustomerNoteList(ConponCustomerNoteForm conponCustomerNoteForm);

	/**
	 * 修改券短信状态
	 * @author why
	 * @date 2018年12月18日 上午9:48:02 
	 * @param conponCustomerNoteForm
	 * @return
	 */
	public boolean updateIsSendState(ConponCustomerNoteForm conponCustomerNoteForm);
}

