package com.server.module.system.replenishManage.machinesReplenishManage;

import java.util.List;

import com.server.module.system.machineManage.machineReplenish.ReplenishDetailVo;
import com.server.module.system.machineManage.machineReplenish.ReplenishDetailVo1;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-04-23 16:19:47
 */
public interface MachinesReplenishService {

	/**
	 * 补货管理 的列表查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(MachinesReplenishCondition condition);
	
	/**
	 *  根据 公司  查询 线路以及线路下的收货机编号

	 * @return
	 */
	public List<MachinesReplenishBean> getLine();

	/**
	 * 根据售货机编号 查询该售货机详细缺货信息
	 * @param code
	 * @param version
	 * @return
	 */
	public List<ReplenishmentDetailsBean> getDetails(String code,int version);
	
	
	/**
	 * 根据线路ID 查询这条线路上的售货机的缺货信息
	 * @param lineId
	 * @return
	 */
	public List<ReplenishmentDetailsBean> getPreview(String lineId,String code,int version);


	List<ReplenishDetailVo1> genReplenishDetail(List<MachinesReplenishBean> list,int version);


	List<MachinesReplenishBean> listDetailVm(String[] codes,int version);


	public List<ItemNumVo> getItemsByCode(List<String> listCode,int version);
}
