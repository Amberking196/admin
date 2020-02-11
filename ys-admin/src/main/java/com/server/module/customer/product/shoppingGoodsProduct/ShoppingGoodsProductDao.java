package com.server.module.customer.product.shoppingGoodsProduct;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-09-14 09:53:47
 */
public interface ShoppingGoodsProductDao {

	/**
	 * 商城商品关联商品列表查询
	 * @author why
	 * @date 2018年9月14日 上午10:19:51 
	 * @param shoppingGoodsProductForm
	 * @return
	 */
	public ReturnDataUtil listPage(ShoppingGoodsProductForm shoppingGoodsProductForm);

	/**
	 * 绑定商品列表查询
	 * @author why
	 * @date 2018年9月14日 上午10:35:53 
	 * @param shoppingGoodsProductForm
	 * @return
	 */
	public ReturnDataUtil list(ShoppingGoodsProductForm shoppingGoodsProductForm);

	/**
	 * 修改商城商品绑定机器商品信息
	 * @author why
	 * @date 2019年2月15日 上午9:44:13 
	 * @param entity
	 * @return
	 */
	public boolean update(ShoppingGoodsProductBean entity);

	/**
	 * 删除商城商品绑定机器商品信息
	 * @author why
	 * @date 2019年2月15日 上午9:44:39 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 查询商城商品绑定机器商品信息
	 * @author why
	 * @date 2019年2月15日 上午9:44:48 
	 * @param id
	 * @return
	 */
	public ShoppingGoodsProductBean get(Object id);

	/**
	 * 绑定商品
	 * @author why
	 * @date 2018年9月14日 上午11:30:22 
	 * @param entity
	 * @return
	 */
	public ShoppingGoodsProductBean insert(ShoppingGoodsProductBean entity);
	
	/**
	 * 根据商城商品id 查询绑定商品信息
	 * @author why
	 * @date 2018年10月13日 上午11:23:40 
	 * @param shoppingGoodsId
	 * @return
	 */
	public List<ShoppingGoodsProductBean> getShoppingGoodsProductBean(Long shoppingGoodsId);
}
