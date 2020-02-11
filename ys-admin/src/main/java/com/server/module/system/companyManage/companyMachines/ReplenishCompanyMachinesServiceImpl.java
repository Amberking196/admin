package com.server.module.system.companyManage.companyMachines;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-14 15:10:24
 */
@Service
public class ReplenishCompanyMachinesServiceImpl implements ReplenishCompanyMachinesService {

	private static Log log = LogFactory.getLog(ReplenishCompanyMachinesServiceImpl.class);
	@Autowired
	private ReplenishCompanyMachinesDao replenishCompanyMachinesDaoImpl;

	public ReturnDataUtil listPage(ReplenishCompanyMachinesCondition condition) {
		return replenishCompanyMachinesDaoImpl.listPage(condition);
	}

	public ReplenishCompanyMachinesBean add(ReplenishCompanyMachinesBean entity) {
		if(replenishCompanyMachinesDaoImpl.isNew(entity.getCode())){
			
		   return replenishCompanyMachinesDaoImpl.insert(entity);
		}else{
			return null;
		}
	}

	public boolean update(ReplenishCompanyMachinesBean entity) {
		return replenishCompanyMachinesDaoImpl.update(entity);
	}

	public boolean del(Long id) {
		return replenishCompanyMachinesDaoImpl.delete(id);
	}

	public List<ReplenishCompanyMachinesBean> list(ReplenishCompanyMachinesCondition condition) {
		
		
		
		return null;
	}

	public ReplenishCompanyMachinesBean get(Object id) {
		return replenishCompanyMachinesDaoImpl.get(id);
	}

	public List<ReplenishCompanyMachinesBean> listOtherCompanyForSelect(){
		return replenishCompanyMachinesDaoImpl.listOtherCompanyForSelect();
	}
}
