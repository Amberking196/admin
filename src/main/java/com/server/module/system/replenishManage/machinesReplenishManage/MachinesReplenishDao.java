package com.server.module.system.replenishManage.machinesReplenishManage;

import java.util.List;
import java.util.Map;

import com.server.module.system.machineManage.machineReplenish.ReplenishDetailVo;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-04-23 16:19:47
 */
public interface MachinesReplenishDao {
	//二版本查询方法
	public ReturnDataUtil listPage1(MachinesReplenishCondition condition);
	//一版本查询方法
	/**
	 * 补货管理 的列表查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(MachinesReplenishCondition condition);
	
	/**
	 *  根据 公司 区域  查询 线路以及线路下的收货机编号
	 * @param condition
	 * @return
	 */
	public List<MachinesReplenishBean> getLine();
	
	/**
	 * 根据售货机编号 查询该售货机详细缺货信息 一版本
	 * @param code
	 * @return
	 */
	public List<ReplenishmentDetailsBean> getDetails(String code);
	/**
	 * 根据售货机编号 查询该售货机详细缺货信息   二版本
	 * @param code
	 * @return
	 */
	public List<ReplenishmentDetailsBean> getDetails1(String code);

	/**
	 * 根据线路ID 查询这条线路上的售货机的缺货信息
	 * @param lineId
	 * @return
	 */
	public List<ReplenishmentDetailsBean> getPreview(String lineId,String code,int version);


    List<ReplenishDetailVo> selectReplenishDetail(List<MachinesReplenishBean> list);
	//版本2使用
    List<ReplenishDetailVo> selectReplenishDetail1(List<MachinesReplenishBean> list);

	List<MachinesReplenishBean> listDetailVm(String[] codes,int version);

	public List<ItemNumVo> getItemsByCode(List<String> listCode,int version);
	/**
	 * 查询有故障的机器
	 * @return
	 */
	public Map<String,String> listAllErrorMachine();

}
