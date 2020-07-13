package com.server.module.system.machineManage.machinesReplenishContrast;

import java.util.List;
import com.server.util.ReturnDataUtil;


/**
 * 
 * @author why
 * @date: 2019年5月14日 下午5:47:49
 */
public interface MachinesReplenishContrastDao {

	/**
	 * 查询销售补货预警信息
	 * @author why
	 * @date 2019年5月14日 下午5:53:36 
	 * @param machinesReplenishContrastForm
	 * @return
	 */
	public ReturnDataUtil listPage(MachinesReplenishContrastForm machinesReplenishContrastForm);

	public List<MachinesReplenishContrastBean> list(MachinesReplenishContrastForm condition);

	public boolean update(MachinesReplenishContrastBean entity);

	public boolean delete(Object id);

	public MachinesReplenishContrastBean get(Object id);

	public MachinesReplenishContrastBean insert(MachinesReplenishContrastBean entity);
}
