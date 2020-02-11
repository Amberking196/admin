package com.server.module.customer.coupon;

import lombok.Data;

@Data
public class CouponForm {

	private String couponName;
	private Integer target;
	private Integer companyId;
	private Integer areaId;
	private String vmCode;

	private Integer way;// 1：购买返券，2：自助领券，3：活动赠券,4:注册赠券,5：可购买优惠券,6:关注赠券,7:会员赠券,8:邀请赠券

	private Integer useWhere;// 1 机器优惠劵 2 商城优惠劵
	// 是否限制范围
	private boolean limitRange;

	public boolean getLimitRange() {
		return limitRange;
	}

}
