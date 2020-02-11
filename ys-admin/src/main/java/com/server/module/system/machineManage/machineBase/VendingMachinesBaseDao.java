package com.server.module.system.machineManage.machineBase;

import java.util.List;

import com.server.module.system.machineManage.machineType.MachinesTypeBean;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-03-29 17:51:22
 */
public interface VendingMachinesBaseDao {

	public ReturnDataUtil listPage(VendingMachinesBaseCondition condition);

	public List<VendingMachinesBaseBean> list(VendingMachinesBaseCondition condition);

	public boolean update(VendingMachinesBaseBean entity);

	public boolean delete(Object id);

	public VendingMachinesBaseBean get(Object id);

	public VendingMachinesBaseBean insert(VendingMachinesBaseBean entity);
	
	public VendingMachinesBaseBean getBase(Object id,Integer companyId);
	
	/**
	 * 校验出厂编号 是否存在
	 * @param factoryNumber
	 * @return
	 */
	public boolean checkFactoryNumber(String factoryNumber);
	
	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 * @return
	 */
	public List<String> findItemStandard(String factnum);
	
	/**
	 * 根据售货机出厂编号 获取该机器所有商品规格信息
	 * @return
	 */
	public List<WayStandardDto> getStandardInfo(String factnum);
	
	/**版本2
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 * @return
	 */
	public List<VendingMachinesWayItemBean> findItemWeight(String factnum);
	
	/**
	 * 根据售货机出厂编号 得到版本号
	 * @return machineVersion
	 */
	public Integer getVersionByFactoryNumber(String factnum);
	/**
	 * 更新程序主控版本
	 * @author hebiting
	 * @date 2019年1月14日下午2:27:26
	 * @param programVersion
	 * @param factoryNum
	 * @return
	 */
	public boolean updateProgramVersion(String programVersion,String factoryNum);
	/**
	 * 将机器是否可以在线升级更新到数据库中
	 * @author hebiting
	 * @date 2019年1月21日下午2:40:28
	 * @param factoryNum
	 * @return
	 */
	public boolean updateCanOnlineUpdate(String factoryNum);
}
