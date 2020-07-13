package com.server.module.customer.product.bargainDetail;

import java.util.List;


public interface BargainDetailService {

	/**
	 * 好友砍价数据插入
	 * @author hebiting
	 * @date 2018年12月21日下午4:28:15
	 * @param bargainDetail
	 * @return
	 */
	Long insertBargainDetail(BargainDetailBean bargainDetail);
	
	/**
	 * 查看某一个订单的砍价数据
	 * @author hebiting
	 * @date 2018年12月21日下午4:34:28
	 * @param customerBargainId
	 * @return
	 */
	List<BargainDetailDto> findBargainDetailList(Long customerBargainId);
	
	/**
	 * 仅获取该商品砍价金额及最低价
	 * @author hebiting
	 * @date 2018年12月24日上午9:58:56
	 * @param customerBargianId
	 * @return
	 */
	BargainDto getBarginSomeInfo(Long customerBargianId);
	
	/**
	 * 是否已经砍过价
	 * @author hebiting
	 * @date 2018年12月24日下午1:58:14
	 * @param customerBargainId
	 * @param customerId
	 * @return 已经砍过价返回true
	 */
	boolean isBargained(Long customerBargainId,Long customerId);
	/**
	 * 砍价完成后生成订单
	 * @author hebiting
	 * @date 2018年12月24日下午5:53:29
	 * @return
	 */
	boolean createBargainOrder(Long customerBargainId);
}
