package com.server.module.system.machineManage.machinesAdvertising;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
@Service
public class VendingMachinesAdvertisingServiceImpl implements VendingMachinesAdvertisingService {

	private static Logger log = LogManager.getLogger(VendingMachinesAdvertisingServiceImpl.class);
	@Autowired
	private VendingMachinesAdvertisingDao vendingMachinesAdvertisingDaoImpl;

	/**
	 * 查询售货机广告列表
	 */
	public ReturnDataUtil listPage(VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = vendingMachinesAdvertisingDaoImpl.listPage(vendingMachinesAdvertisingForm);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<listPage>-----end");
		return listPage;
	}

	/**
	 * 增加售货机广告
	 */
	public VendingMachinesAdvertisingBean add(VendingMachinesAdvertisingBean entity) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<add>-----start");
		VendingMachinesAdvertisingBean bean = vendingMachinesAdvertisingDaoImpl.insert(entity);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<add>-----end");
		return bean;
	}
	/**
	 * 修改售货机广告
	 */
	public boolean update(VendingMachinesAdvertisingBean entity) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<update>-----start");
		boolean update = vendingMachinesAdvertisingDaoImpl.update(entity);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<update>-----end");
		return update;
	}
	
	/**
	 * 删除售货机广告
	 */
	public boolean del(Object id) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<del>-----start");
		boolean delete = vendingMachinesAdvertisingDaoImpl.delete(id);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<del>-----end");
		return delete;
	}

	/**
	 * 手机端广告页查询
	 */
	public List<VendingMachinesAdvertisingBean> list(VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<list>-----start");
		List<VendingMachinesAdvertisingBean> list = vendingMachinesAdvertisingDaoImpl.list(vendingMachinesAdvertisingForm);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<list>-----end");
		return list;
	}

	public VendingMachinesAdvertisingBean get(Object id) {
		return vendingMachinesAdvertisingDaoImpl.get(id);
	}

	@Override
	public String findVendingSlideshow(String vmCode) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<findVendingSlideshow>-----start");
		String homeImg = vendingMachinesAdvertisingDaoImpl.findVendingSlideshow(vmCode);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<findVendingSlideshow>-----end");
		return homeImg;
	}
	
	@Override
	public List<VendingMachinesAdvertisingBean> findVendingAdvertisingMachinesBean(String vmCode) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<findVendingAdvertisingMachinesBean>------start");
		List<VendingMachinesAdvertisingBean> list = vendingMachinesAdvertisingDaoImpl.findVendingAdvertisingMachinesBean(vmCode);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<findVendingAdvertisingMachinesBean>------end");
		return list;
	}

	@Override
	public boolean findAdvertisingMachinesBeanByCompanyId(Long companyId) {
		log.info("<VendingMachinesAdvertisingServiceImpl>------<findAdvertisingMachinesBeanByCompanyId>-----start");
		boolean flag = vendingMachinesAdvertisingDaoImpl.findAdvertisingMachinesBeanByCompanyId(companyId);
		log.info("<VendingMachinesAdvertisingServiceImpl>------<findAdvertisingMachinesBeanByCompanyId>-----end");
		return flag;
	}
	
	
}
