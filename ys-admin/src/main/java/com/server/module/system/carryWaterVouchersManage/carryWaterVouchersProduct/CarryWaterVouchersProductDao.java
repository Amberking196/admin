package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersProduct;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 16:25:36
 */
public interface CarryWaterVouchersProductDao {
	
	
	public ReturnDataUtil listPage(CarryWaterVouchersProductForm condition);

	/**
	 * 提水券商品列表
	 * @author why
	 * @date 2018年11月4日 下午3:55:07 
	 * @param carryWaterVouchersProductForm
	 * @return
	 */
	public ReturnDataUtil list(CarryWaterVouchersProductForm carryWaterVouchersProductForm);

	public boolean update(CarryWaterVouchersProductBean entity);

	public boolean delete(Object id);

	public CarryWaterVouchersProductBean get(Object id);

	/**
	 * 提水券绑定商品
	 * @author why
	 * @date 2018年11月5日 上午10:38:53 
	 * @param entity
	 * @return
	 */
	public CarryWaterVouchersProductBean insert(CarryWaterVouchersProductBean entity);
}
