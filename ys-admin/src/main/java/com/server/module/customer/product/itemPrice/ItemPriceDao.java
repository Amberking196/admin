package com.server.module.customer.product.itemPrice;

import java.util.List;

public interface ItemPriceDao {

	/**
	 * 新增商品价格
	 * @author hebiting
	 * @date 2019年3月25日上午11:23:39
	 * @param itemPrice
	 * @return
	 */
	public ItemPriceBean insert(ItemPriceBean itemPrice);
	/**
	 * 更新商品价格
	 * @author hebiting
	 * @date 2019年3月25日上午11:23:49
	 * @param itemPrice
	 * @return
	 */
	public boolean update(ItemPriceBean itemPrice);
	/**
	 * 获取商品价格信息列表
	 * @author hebiting
	 * @date 2019年3月25日上午11:24:05
	 * @param basicItemId
	 * @return
	 */
	public List<ItemPriceDto> getPriceByBasicItemId(Long basicItemId);
}
