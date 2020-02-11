package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;
import com.server.util.JsonUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
@Service
public class CarryWaterVouchersCustomerServiceImpl implements CarryWaterVouchersCustomerService {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersCustomerServiceImpl.class);
	@Autowired
	private CarryWaterVouchersCustomerDao carryWaterVouchersCustomerDaoImpl;
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;

	public ReturnDataUtil listPage(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>-----<listPage>-------start");
		ReturnDataUtil listPage = carryWaterVouchersCustomerDaoImpl.listPage(carryWaterVouchersCustomerForm);
		log.info("<CarryWaterVouchersCustomerServiceImpl>-----<listPage>-------end");		
		return listPage;
	}

	/**
	 * 提水券绑定用户
	 */
	public CarryWaterVouchersCustomerBean add(Long carryId,Long[] customerIds,Long quantity) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<add>------start"); 
		//得到提水券信息
		CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl.get(carryId);
		CarryWaterVouchersCustomerBean bean=null;
		for (Long customerId : customerIds) {
			CarryWaterVouchersCustomerBean entity=new CarryWaterVouchersCustomerBean();
			entity.setCarryId(carryId);
			entity.setCustomerId(customerId);
			entity.setCreateUser(UserUtils.getUser().getId());
			entity.setStartTime(carryWaterVouchersBean.getLogicStartTime()); 
			entity.setEndTime(carryWaterVouchersBean.getLogicEndTime());
			entity.setQuantity(quantity);
			bean = carryWaterVouchersCustomerDaoImpl.insert(entity);
		}
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<add>------end");
		return bean;
	}

	/**
	 * 更新用户提水券
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>-------<update>-----start");
		boolean update = carryWaterVouchersCustomerDaoImpl.update(entity);
		log.info("<CarryWaterVouchersCustomerServiceImpl>-------<update>-----end");
		return update;
	}

	public boolean del(Object id) {
		return carryWaterVouchersCustomerDaoImpl.delete(id);
		
	}

	/**
	 * 提水券范围内用户列表
	 */
	public  ReturnDataUtil listPageForCustomer(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>-----<listPageForCustomer>-------start");
		ReturnDataUtil listPage = carryWaterVouchersCustomerDaoImpl.listPageForCustomer(carryWaterVouchersCustomerForm);
		log.info("<CarryWaterVouchersCustomerServiceImpl>-----<listPageForCustomer>-------end");		
		return listPage;
	}

	public CarryWaterVouchersCustomerBean get(Object id) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>-----<get>-------start");
		CarryWaterVouchersCustomerBean bean = carryWaterVouchersCustomerDaoImpl.get(id);
		log.info("<CarryWaterVouchersCustomerServiceImpl>-----<get>-------end");
		return bean;
	}

	/**
	 * 手机端 获取用户提水券信息
	 */
	@Override
	public List<CarryWaterVouchersCustomerDto> findCustomerIdByCarryWaterVouchersCustomerDto(
			CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<findCustomerIdByCarryWaterVouchersCustomerDto>-------start");
		List<CarryWaterVouchersCustomerDto> list = carryWaterVouchersCustomerDaoImpl.findCustomerIdByCarryWaterVouchersCustomerDto(carryWaterVouchersCustomerForm);
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<findCustomerIdByCarryWaterVouchersCustomerDto>-------end");
		return list;
	}

	@Override
	public boolean updateQuantity(CarryWaterVouchersCustomerBean entity) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<updateQuantity>-------start");
		boolean updateQuantity = carryWaterVouchersCustomerDaoImpl.updateQuantity(entity);
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<updateQuantity>-------end");
		return updateQuantity;
	}
	
	@Override
	public List<CarryWaterVouchersCustomerDto> queryCarryWaterCustomerDto(Long orderId){
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<queryCarryWaterCustomerDto>-------start");
		List<CarryWaterVouchersCustomerDto> list = carryWaterVouchersCustomerDaoImpl.queryCarryWaterCustomerDto(orderId);
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<queryCarryWaterCustomerDto>-------end");
		return list;
	}
	
	/**
	 * 提水券绑定用户
	 */
	public CarryWaterVouchersCustomerBean add(Long carryId,Long customerId,Integer num,Long orderId) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<add>------start");
		//得到提水券信息
		CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl.get(carryId);
		//得到本次购买数量和下发张数  最终为下发张数
		Integer sum=carryWaterVouchersBean.getSendMax()*num;
		log.info("最终下发张数======"+sum);
		CarryWaterVouchersCustomerBean entity=new CarryWaterVouchersCustomerBean();
		entity.setCarryId(carryId);
		entity.setCustomerId(customerId);
		entity.setStartTime(carryWaterVouchersBean.getLogicStartTime());
		entity.setEndTime(carryWaterVouchersBean.getLogicEndTime());
		entity.setQuantity(sum.longValue());
		entity.setCreateUser(customerId);
		entity.setOrderId(orderId);
		CarryWaterVouchersCustomerBean bean = carryWaterVouchersCustomerDaoImpl.insert(entity);
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<add>------end");
		return bean;
	}
}
