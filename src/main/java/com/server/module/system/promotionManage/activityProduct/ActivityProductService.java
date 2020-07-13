package com.server.module.system.promotionManage.activityProduct;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-23 18:07:34
 */
public interface ActivityProductService {

	/**
	 * 活动商品列表查询
	 * @param activityProductForm
	 * @return
	 */
	public ReturnDataUtil listPage(ActivityProductForm activityProductForm);

	/**
	 * 商品列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil list(ActivityProductForm activityProductForm);

	public boolean update(ActivityProductBean entity);

	/**
	 * 解绑商品
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	public ActivityProductBean get(Object id);

	/**
	 * 绑定商品
	 * @param entity
	 * @return
	 */
	public ActivityProductBean add(ActivityProductBean entity);
}
