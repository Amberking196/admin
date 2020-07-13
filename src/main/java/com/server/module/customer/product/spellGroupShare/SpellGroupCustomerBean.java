package com.server.module.customer.product.spellGroupShare;

import lombok.Data;

/**
 * 
 * @author why
 * @date: 2019年1月16日 下午6:06:50
 */
@Data
public class SpellGroupCustomerBean {

	//用户id
	private Long customerId;
	//用户名
	private String nickname;
	//用户头像
	private String headimgurl;
	//是否是拼主 1 位拼主 0是参与者
	private Integer isSpellTheMain;
	//发起拼团用户id
	private Long startCustomerId;
	
	public Integer getIsSpellTheMain() {
		if(this.customerId.equals(this.startCustomerId)) {
			isSpellTheMain=1;
		}else {
			isSpellTheMain=0;
		}
		return isSpellTheMain;
	}
}
