package com.server.module.customer.product.spellGroupShare;

/**
 * 
 * @author why
 * @date: 2019年1月16日 下午6:12:14
 */
public interface SpellGroupShareDao {

	/**
	 * 分享后用户点击页面查询
	 * @author why
	 * @date 2019年1月16日 下午6:11:50 
	 * @param customerSpellGroupId
	 * @return
	 */
	public SpellGroupShareBean list(Long customerSpellGroupId);
	
	/**
	 * 用户支付完成后 查询信息
	 * @author why
	 * @date 2019年1月18日 上午9:13:55 
	 * @param orderId
	 * @return
	 */
	public SpellGroupShareBean payFinishList(Long orderId); 
}
