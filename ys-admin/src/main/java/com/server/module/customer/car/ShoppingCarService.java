package com.server.module.customer.car;

import java.util.List;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-06-29 11:03:25
 */ 
public interface  ShoppingCarService{

	/**
	 * 查询购物车列表
	 * @param shoppingCarForm 
	 */
	public ReturnDataUtil listPage(ShoppingCarForm shoppingCarForm);
	
	/**
	 * 增添某种商品进购物车
	 * @param shoppingCarForm newShoppingCarBean
	 */
	public ReturnDataUtil add(ShoppingCarForm shoppingCarForm, ShoppingCarBean newShoppingCarBean);
	/**
	 * 增添某种商品进购物车并购买
	 * @param  newShoppingCarBean
	 */
	public ReturnDataUtil addAndBuy(ShoppingCarBean newShoppingCarBean);

	/**
	 * 更新购物车中某种商品数量
	 * @param  shoppingCarIdList
	 * return 
	 */
	public ReturnDataUtil update(ShoppingCarForm shoppingCarForm, ShoppingCarBean newShoppingCarBean);
		
	/**
	 * 下单后购物车标志删除
	 * @param  shoppingCarIdList
	 * return 
	 */
	public boolean updateAllFlag(List<Long> shoppingCarIdList);
	
	/**
	 * 更新购物车中商品价格和名称
	 * @param  entity
	 * return 
	 */
	public boolean updatePriceAndName(ShoppingGoodsBean entity);
	
	/**
	 * 后台标志删除购物车中某种商品
	 * @param  shoppingCarBean
	 * return 
	 */
	public boolean updateFlag(ShoppingCarBean shoppingCarBean);

	/**
	 * 批量保存购物车
	 * @param shoppingCarForm newShoppingCarBean
	 */
	public ReturnDataUtil save(ShoppingCarForm shoppingCarForm, List<ShoppingCarBean> newShoppingCarBeanList);
	
	/**
	 * 批量删除购物车中的商品
	 * @param  shoppingCarIdList
	 * return 
	 */
	public ReturnDataUtil del(List<Long> shoppingCarIdList);
	
	/**
	 * 修改购物车信息
	 * @author why
	 * @date 2019年2月15日 下午4:12:42 
	 * @param shoppingCarBean
	 * @return
	 */
	public boolean update(ShoppingCarBean shoppingCarBean);

	/**
	 * 删除购物车信息
	 * @author why
	 * @date 2019年2月15日 下午4:12:49 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);
	/**
	 * 查询购物车信息
	 * @author why
	 * @date 2019年2月15日 下午4:12:55 
	 * @param id
	 * @return
	 */
	public ShoppingCarBean get(Object id);

}

