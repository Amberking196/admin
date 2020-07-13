package com.server.module.system.companyManage.companyMachines;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-14 15:10:24
 */
public interface ReplenishCompanyMachinesDao {

	public ReturnDataUtil listPage(ReplenishCompanyMachinesCondition condition);

	public List<ReplenishCompanyMachinesBean> list(ReplenishCompanyMachinesCondition condition);

	public boolean update(ReplenishCompanyMachinesBean entity);

	public boolean delete(Long id);

	public ReplenishCompanyMachinesBean get(Object id);

	public ReplenishCompanyMachinesBean insert(ReplenishCompanyMachinesBean entity);
	
	public boolean isNew(String code) ;


	public List<ReplenishCompanyMachinesBean> listOtherCompanyForSelect();

	/**
	 * 验证该售货机是否添加过补货公司
	 * @param code
	 * @return
	 */
	public Integer check(String code);
	/**
	 * 更新售货机的补货公司
	 * @param bean
	 * @param conn
	 * @return
	 */
	public boolean update(ReplenishCompanyMachinesBean bean, Connection conn);
	/**
	 * 为售货机添加补货公司
	 * @param bean
	 * @param conn
	 * @return
	 */
	public boolean insert(ReplenishCompanyMachinesBean bean, Connection conn);
	/**
	 * 删除补货该售货机的补货公司
	 * @param longValue
	 * @param conn
	 * @return
	 */
	public boolean delete(long longValue, Connection conn)throws SQLException;

}
