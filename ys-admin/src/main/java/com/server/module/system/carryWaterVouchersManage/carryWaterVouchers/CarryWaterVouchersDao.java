package com.server.module.system.carryWaterVouchersManage.carryWaterVouchers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.server.module.customer.product.ShoppingGoodsVmCodeForm;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
public interface CarryWaterVouchersDao {

	/**
	 * 提水券列表查询
	 * @author why
	 * @date 2018年11月3日 上午9:20:35 
	 * @param carryWaterVouchersForm
	 * @return
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersForm carryWaterVouchersForm);

	public List<CarryWaterVouchersBean> list(CarryWaterVouchersForm condition);

	/**
	 * 提水券修改
	 * @author why
	 * @date 2018年11月3日 上午10:08:29 
	 * @param entity
	 * @return
	 */
	public boolean update(CarryWaterVouchersBean entity);

	/**
	 * 提水券删除
	 * @author why
	 * @date 2018年11月3日 上午10:27:27 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	public CarryWaterVouchersBean get(Object id);

	/**
	 * 增加提水券
	 * @author why
	 * @date 2018年11月3日 上午9:27:55 
	 * @param entity
	 * @return
	 */
	public CarryWaterVouchersBean insert(CarryWaterVouchersBean entity);
	
	/**
	 * 查询绑定提水券
	 * @author hjc
	 * @date 2019年1月13日 上午9:27:55 
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil queryBindCarryWater(ShoppingGoodsVmCodeForm entity);

}
