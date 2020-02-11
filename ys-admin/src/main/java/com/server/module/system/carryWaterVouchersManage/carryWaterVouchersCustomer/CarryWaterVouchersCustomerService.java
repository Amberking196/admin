package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
public interface CarryWaterVouchersCustomerService {

	/**
	 * 提水券用户列表
	 * @author why
	 * @date 2018年11月4日 上午9:35:45 
	 * @param carryWaterVouchersCustomerForm
	 * @return
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);

	/**
	 * 提水券范围内用户列表
	 * @author why
	 * @date 2018年11月22日 上午10:43:15 
	 * @param carryWaterVouchersCustomerForm
	 * @return
	 */
	public ReturnDataUtil listPageForCustomer(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);

	/**
	 * 更新用户提水券
	 * @author why
	 * @date 2018年11月24日 上午10:13:06 
	 * @param entity
	 * @return
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity);

	public boolean del(Object id);

	public CarryWaterVouchersCustomerBean get(Object id);


	/**
	 *  提水券绑定用户[后台]
	 * @author why
	 * @date 2018年11月23日 下午3:20:57 
	 * @param carryId
	 * @param customerIds
	 * @param quantity
	 * @return
	 */
	public CarryWaterVouchersCustomerBean add(Long carryId,Long[] customerIds,Long quantity);
	
	/**
	 * 手机端 获取用户提水券信息
	 * @author why
	 * @date 2018年11月6日 上午10:04:59 
	 * @param carryWaterVouchersCustomerForm
	 * @return
	 */
	public List<CarryWaterVouchersCustomerDto> findCustomerIdByCarryWaterVouchersCustomerDto(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);

	/**
	 * 后台修改用户提水券数量
	 * @author why
	 * @date 2018年11月24日 上午10:29:58 
	 * @param entity
	 * @return
	 */
	public boolean updateQuantity(CarryWaterVouchersCustomerBean entity);
	
	/**
	 * 通过orderId查询用户提水券信息       
	 * @author why
	 * @date 2019年1月14日 下午9:42:44 
	 * @param carryId
	 * @return
	 */
	public List<CarryWaterVouchersCustomerDto> queryCarryWaterCustomerDto(Long orderId);
	
	/**
	 * 【商城购买后】提水券绑定用户
	 * 
	 * @author why
	 * @date 2018年11月23日 下午3:20:57
	 * @param carryId
	 * @param customerIds
	 * @param quantity
	 * @return
	 */
	public CarryWaterVouchersCustomerBean add(Long carryId,Long customerId,Integer num,Long orderId);
}
