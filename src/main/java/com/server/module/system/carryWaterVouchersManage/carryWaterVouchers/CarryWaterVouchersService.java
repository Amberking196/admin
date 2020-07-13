package com.server.module.system.carryWaterVouchersManage.carryWaterVouchers;

import java.util.List;

import com.server.module.customer.product.ShoppingGoodsVmCodeForm;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
public interface CarryWaterVouchersService {

	/**
	 * 提水券列表
	 * 
	 * @author why
	 * @date 2018年11月3日 上午9:37:11
	 * @param carryWaterVouchersForm
	 * @return
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersForm carryWaterVouchersForm);

	public List<CarryWaterVouchersBean> list(CarryWaterVouchersForm condition);

	/**
	 * 提水券修改
	 * @author why
	 * @date 2018年11月3日 上午10:11:40 
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil update(CarryWaterVouchersBean entity);

	/**
	 * 提水券删除
	 * @author why
	 * @date 2018年11月3日 上午10:29:36 
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	public CarryWaterVouchersBean get(Object id);

	/**
	 * 增加提水券
	 * 
	 * @author why
	 * @date 2018年11月3日 上午9:40:21
	 * @param entity
	 * @return
	 */
	public CarryWaterVouchersBean add(CarryWaterVouchersVo entity);
	/**
	 * 提水券绑定机器
	 * 
	 * @author hjc
	 * @date 2019年1月16日 上午9:40:21
	 * @param entity
	 * @return
	 */
	boolean bindVmCode(ShoppingGoodsVmCodeForm entity);
	/**
	 * 查询商品已绑定的提水券
	 * @author hjc
	 * @date 2019年1月16日 上午9:40:21
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil queryBindCarryWater(ShoppingGoodsVmCodeForm entity);

}
