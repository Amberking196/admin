package com.server.module.customer.product.spellGroupShare;

/**
 * 
 * @author why
 * @date: 2019年1月16日 下午9:12:01
 */
public interface SpellGroupShareService {

	
	/**
	 * 分享后用户点击页面查询
	 * @author why
	 * @date 2019年1月16日 下午9:11:52 
	 * @param customerSpellGroupId
	 * @return
	 */
	public SpellGroupShareBean list(Long customerSpellGroupId);
	
	/**
	 * 用户支付完成后 查询信息
	 * @author why
	 * @date 2019年1月18日 上午9:21:20 
	 * @param orderId
	 * @return
	 */
	public SpellGroupShareBean payFinishList(Long orderId); 
}
