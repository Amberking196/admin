package com.server.module.system.machineManage.machinesPic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
import java.util.List;

import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesBean;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesCondition;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
@Service
public class VendingMachinesPicServiceImpl implements VendingMachinesPicService {

	private static Logger log = LogManager.getLogger(VendingMachinesPicServiceImpl.class);
	@Autowired
	private VendingMachinesPicDao vendingPicDaoImpl;

	/**
	 * 查询售货机广告列表
	 */
	public ReturnDataUtil listPage(VendingMachinesPic vendingMachinesPic) {
		log.info("<VendingPicServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = vendingPicDaoImpl.listPage(vendingMachinesPic);
		log.info("<VendingPicServiceImpl>------<listPage>-----end");
		return listPage;
	}

	/**
	 * 增加售货机广告
	 */
	public VendingMachinesPicBean add(VendingMachinesPicBean entity) {
		log.info("<VendingPicServiceImpl>------<add>-----start");
		VendingMachinesPicBean bean = vendingPicDaoImpl.insert(entity);
		log.info("<VendingPicServiceImpl>------<add>-----end");
		return bean;
	}
	/**
	 * 修改售货机广告
	 */
	public boolean update(VendingMachinesPicBean entity) {
		log.info("<VendingPicServiceImpl>------<update>-----start");
		boolean update = vendingPicDaoImpl.update(entity);
		log.info("<VendingPicServiceImpl>------<update>-----end");
		return update;
	}
	
	/**
	 * 删除售货机广告
	 */
	public boolean del(Object id) {
		log.info("<VendingPicServiceImpl>------<del>-----start");
		boolean delete = vendingPicDaoImpl.delete(id);
		log.info("<VendingPicServiceImpl>------<del>-----end");
		return delete;
	}

	/**
	 * 手机端广告页查询
	 */
	public List<VendingMachinesPicBean> list(VendingMachinesPic vendingMachinesPic) {
		log.info("<VendingPicServiceImpl>------<list>-----start");
		List<VendingMachinesPicBean> list = vendingPicDaoImpl.list(vendingMachinesPic);
		log.info("<VendingPicServiceImpl>------<list>-----end");
		return list;
	}

	public VendingMachinesPicBean get(Object id) {
		return vendingPicDaoImpl.get(id);
	}

	@Override
	public String findVendingSlideshow(String vmCode) {
		log.info("<VendingPicServiceImpl>------<findVendingSlideshow>-----start");
		String homeImg = vendingPicDaoImpl.findVendingSlideshow(vmCode);
		log.info("<VendingPicServiceImpl>------<findVendingSlideshow>-----end");
		return homeImg;
	}
	
	@Override
	public List<VendingMachinesPicBean> findVendingPicMachinesBean(String vmCode) {
		log.info("<VendingPicServiceImpl>------<findVendingPicMachinesBean>------start");
		List<VendingMachinesPicBean> list = vendingPicDaoImpl.findVendingPicMachinesBean(vmCode);
		log.info("<VendingPicServiceImpl>------<findVendingPicMachinesBean>------end");
		return list;
	}

	@Override
    public boolean findPicMachinesBeanByCompanyIdAndItemId(Long companyId,Long itemBasicId) {
		log.info("<VendingPicServiceImpl>------<findPicMachinesBeanByCompanyId>-----start");
		boolean flag = vendingPicDaoImpl.findPicMachinesBeanByCompanyIdAndItemId(companyId,itemBasicId);
		log.info("<VendingPicServiceImpl>------<findPicMachinesBeanByCompanyId>-----end");
		return flag;
	}
	
    @Override
    public void addAll(Long advertisingId, List<String> codeList) {
    	vendingPicDaoImpl.addAll(advertisingId,codeList);
    }

    @Override
    public void deleteAll(Long[] ids) {
    	vendingPicDaoImpl.deleteAll(ids);
    }
    
    @Override
    public List<VendingPicMachinesBean> list(VendingPicMachinesCondition condition) {
        return vendingPicDaoImpl.list(condition);
    }
    
    @Override
	public VendingPicMachinesBean getPicVmCode(Object id) {
        return vendingPicDaoImpl.getPicVmCode(id);
	}

    @Override
	public VendingMachinesPicDto checkRepeat(Long basicItemId) {
        return vendingPicDaoImpl.checkRepeat(basicItemId);
    }

}
