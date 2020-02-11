package com.server.module.system.companyManage.companyMachines;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-14 15:10:24
 */
public interface ReplenishCompanyMachinesService {

	public ReturnDataUtil listPage(ReplenishCompanyMachinesCondition condition);

	public List<ReplenishCompanyMachinesBean> list(ReplenishCompanyMachinesCondition condition);

	public boolean update(ReplenishCompanyMachinesBean entity);

	public boolean del(Long id);

	public ReplenishCompanyMachinesBean get(Object id);

	public ReplenishCompanyMachinesBean add(ReplenishCompanyMachinesBean entity);

	public List<ReplenishCompanyMachinesBean> listOtherCompanyForSelect();
}

