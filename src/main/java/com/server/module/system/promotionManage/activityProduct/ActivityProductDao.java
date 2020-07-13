package com.server.module.system.promotionManage.activityProduct;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-23 18:07:34
 */
public interface ActivityProductDao {

	/**
	 * 活动商品 列表查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(ActivityProductForm condition);

	/**
	 * 商品列表
	 * @param activityProductForm
	 * @return
	 */
	public ReturnDataUtil list(ActivityProductForm activityProductForm);

	
	public boolean update(ActivityProductBean entity);

	/**
	 * 解绑商品
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	public ActivityProductBean get(Object id);

	/**
	 * 绑定商品
	 * @param entity
	 * @return
	 */
	public ActivityProductBean insert(ActivityProductBean entity);
}
