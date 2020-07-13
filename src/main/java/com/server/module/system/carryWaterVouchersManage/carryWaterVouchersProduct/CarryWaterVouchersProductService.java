package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersProduct;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 16:25:36
 */
public interface CarryWaterVouchersProductService {

	public ReturnDataUtil listPage(CarryWaterVouchersProductForm condition);

	/**
	 * 提水券商品列表
	 * @author why
	 * @date 2018年11月4日 下午4:08:41 
	 * @param carryWaterVouchersProductForm
	 * @return
	 */
	public ReturnDataUtil list(CarryWaterVouchersProductForm carryWaterVouchersProductForm);

	public boolean update(CarryWaterVouchersProductBean entity);

	public boolean del(Object id);

	public CarryWaterVouchersProductBean get(Object id);

	/**
	 *  提水券绑定商品
	 * @author why
	 * @date 2018年11月5日 上午10:41:04 
	 * @param entity
	 * @return
	 */
	public CarryWaterVouchersProductBean add(CarryWaterVouchersProductBean entity);
}
