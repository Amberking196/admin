package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
public interface CarryWaterVouchersCustomerDao {

	/**
	 * 提水券绑定用户列表
	 * @author why
	 * @date 2018年11月3日 下午4:54:52 
	 * @param carryWaterVouchersCustomerForm
	 * @return
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);

	/**
	 * 提水券范围内用户列表
	 * @author why
	 * @date 2018年11月22日 上午9:43:47 
	 * @param carryWaterVouchersCustomerForm
	 * @return
	 */
	public ReturnDataUtil listPageForCustomer(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);

	/**
	 * 更新用户提水券
	 * @author why
	 * @date 2018年11月5日 上午11:50:31 
	 * @param entity
	 * @return
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity);

	public boolean delete(Object id);

	public CarryWaterVouchersCustomerBean get(Object id);

	/**
	 * 提水券绑定用户
	 * @author why
	 * @date 2018年11月4日 上午10:03:13 
	 * @param entity
	 * @return
	 */
	public CarryWaterVouchersCustomerBean insert(CarryWaterVouchersCustomerBean entity);
	
	/**
	 * 获取用户绑定提水券信息
	 * @author why
	 * @date 2018年11月5日 上午11:45:28 
	 * @param customerId
	 * @param carryId
	 * @return
	 */
	public CarryWaterVouchersCustomerBean  getCarryWaterVouchersCustomerBean(Long customerId,Long carryId);
	
	/**
	 * 手机端 获取用户提水券信息
	 * @author why
	 * @date 2018年11月5日 下午3:12:17 
	 * @param carryWaterVouchersCustomerForm
	 * @return
	 */
	public List<CarryWaterVouchersCustomerDto> findCustomerIdByCarryWaterVouchersCustomerDto(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);
	
	/**
	 * 后台修改用户提水券数量
	 * @author why
	 * @date 2018年11月24日 上午10:22:21 
	 * @param entity
	 * @return
	 */
	public boolean updateQuantity(CarryWaterVouchersCustomerBean entity);
	
	/**
	 * 通过orderId查询用户提水券信息
	 * @author why
	 * @date 2019年1月14日 下午9:33:18 
	 * @param carryId
	 * @return
	 */
	public List<CarryWaterVouchersCustomerDto> queryCarryWaterCustomerDto(Long orderId);
}
