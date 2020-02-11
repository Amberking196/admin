package com.server.module.customer.product.bargainSponsor;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author why
 * @date: 2018年12月24日 上午9:20:54
 */
public interface BargainSponsorService {
	
	/**
	 * 砍价活动列表
	 * @author why
	 * @date 2018年12月24日 上午10:05:57 
	 * @return
	 */
	public Map<String,Object> bargainList();
	
	/**
	 * 砍价中——详情页
	 * @author why
	 * @date 2018年12月24日 上午11:14:52 
	 * @param id
	 * @return
	 */
	public BargainSponsorBean bargainDetails(Long id);
	
	/**
	 * 砍价商品列表
	 * @author why
	 * @date 2018年12月25日 上午11:16:04 
	 * @return
	 */
	public  List<BargainSponsorBean> bargainGoodsList();
	
	/**
	 * 用户是否可以继续发起砍价
	 * @author why
	 * @date 2018年12月27日 下午3:58:10 
	 * @param id
	 * @return
	 */
	public boolean isCanBargain(Long id);
}
