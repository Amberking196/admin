package com.server.module.customer.product.bargainSponsor;

import java.util.List;

/**
 * author name: why create time: 2018-12-24 09:17:55
 */
public interface BargainSponsorDao {

	/**
	 * 查询砍价活动商品信息 
	 * @author why
	 * @date 2018年12月24日 上午9:24:24 
	 * @return
	 */
	public List<BargainSponsorBean> bargainGoodsList();
	
	/**
	 * 查询用户的砍价商品信息
	 * @author why
	 * @date 2018年12月24日 上午9:54:41 
	 * @return
	 */
	public List<BargainSponsorBean> bargainGoodsFindCustomerList();
	
	
	/**
	 * 砍价中——详情页
	 * @author why
	 * @date 2018年12月24日 上午10:46:08 
	 * @param id
	 * @return
	 */
	public BargainSponsorBean bargainDetails(Long id);
	
	/**
	 * 用户是否可以继续发起砍价
	 * @author why
	 * @date 2018年12月27日 下午3:50:36 
	 * @param id
	 * @return
	 */
	public boolean isCanBargain(Long id);
}
