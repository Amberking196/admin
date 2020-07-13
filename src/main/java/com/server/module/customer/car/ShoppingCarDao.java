package com.server.module.customer.car;

import java.util.List;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-06-29 11:03:25
 */ 
public interface  ShoppingCarDao{

	/**
	 * 查询购物车列表
	 * @param shoppingCarForm 
	 * return 
	 */
	public ReturnDataUtil listPage(ShoppingCarForm shoppingCarForm);
	/**
	 * 修改购物车信息
	 * @author why
	 * @date 2019年2月15日 下午3:52:37 
	 * @param entity
	 * @return
	 */
	public boolean update(ShoppingCarBean entity);
	/**
	 * 修改购物车信息
	 * @author why
	 * @date 2019年2月15日 下午3:52:52 
	 * @param entity
	 * @return
	 */
	public boolean updateFlag(ShoppingCarBean entity);
	/**
	 * 删除购物车信息
	 * @author why
	 * @date 2019年2月15日 下午3:53:38 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);
	/**
	 * 删除购物车多条信息
	 * @author why
	 * @date 2019年2月15日 下午3:53:54 
	 * @param shoppingCarIdList
	 * @return
	 */
	public boolean del(List<Long> shoppingCarIdList);
	/**
	 * 查询购物车信息
	 * @author why
	 * @date 2019年2月15日 下午4:08:58 
	 * @param id
	 * @return
	 */
	public ShoppingCarBean get(Object id);
	/**
	 * 加入购物车
	 * @author why
	 * @date 2019年2月15日 下午4:09:37 
	 * @param entity
	 * @return
	 */
	public ShoppingCarBean insert(ShoppingCarBean entity);
	
	/**
	 * 修改购物车
	 * @author why
	 * @date 2019年2月15日 下午4:09:58 
	 * @param shoppingCarIdList
	 * @return
	 */
	public boolean updateAllFlag(List<Long> shoppingCarIdList);
	/**
	 * 修改购物车商品信息
	 * @author why
	 * @date 2019年2月15日 下午4:10:30 
	 * @param entity
	 * @return
	 */
	public boolean updatePriceAndName(ShoppingGoodsBean entity);
}

