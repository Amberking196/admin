package com.server.module.customer.product.shoppingGoodsSpellGroup;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-10-16 16:41:04
 */
public interface ShoppingGoodsSpellGroupDao {

	/**
	 * 商品团购列表
	 * @author why
	 * @date 2018年10月16日 下午5:18:23 
	 * @param shoppingGoodsSpellGroupForm
	 * @return
	 */
	public ReturnDataUtil listPage(ShoppingGoodsSpellGroupForm shoppingGoodsSpellGroupForm);

	/**
	 * 查询商品团购信息列表
	 * @author why
	 * @date 2019年2月15日 上午9:40:40 
	 * @param condition
	 * @return
	 */
	public List<ShoppingGoodsSpellGroupBean> list(ShoppingGoodsSpellGroupForm condition);

	/**
	 * 商品团购修改
	 * @author why
	 * @date 2019年2月15日 上午9:40:54 
	 * @param entity
	 * @return
	 */
	public boolean update(ShoppingGoodsSpellGroupBean entity);

	/**
	 * 商品团购删除
	 * @author why
	 * @date 2019年2月15日 上午9:41:06 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 商品团购查询
	 * @author why
	 * @date 2019年2月15日 上午9:41:14 
	 * @param id
	 * @return
	 */
	public ShoppingGoodsSpellGroupBean get(Object id);

	/**
	 * 商品团购设置
	 * @author why
	 * @date 2018年10月16日 下午4:46:04 
	 * @param entity
	 * @return
	 */
	public ShoppingGoodsSpellGroupBean insert(ShoppingGoodsSpellGroupBean entity);
	
	/**
	 * 判断是否是拼团商品
	 * @author why
	 * @date 2018年10月17日 下午3:07:50 
	 * @param shoppingGoodsId
	 * @return
	 */
	public ShoppingGoodsSpellGroupBean isConglomerateCommodity(Long shoppingGoodsId);
}
