package com.server.module.system.machineManage.machineList;

import java.util.List;
import java.util.Map;

import com.server.common.persistence.Page;
import com.server.module.system.machineManage.machinesWay.WayDto1;
import com.server.util.ReturnDataUtil;

public interface VendingMachinesInfoService {
	
	List<String> findVmcodeByCompanyId(String companyIds);
	
	/**
	 * 根据售卖机code查询售卖机信息
	 * @param code
	 * @return  VendingMachinesBean
	 */
	VendingMachinesInfoBean findVendingMachinesByCode(String code);

	/**
	 * 根据售卖机codes批量查询售卖机信息
	 * @param codes
	 * @return  List<VendingMachinesBean>
	 */
	List<VendingMachinesInfoBean> findVendingMachinesByCodes(List<String> codes);

	/**
	 * 售货机列表 查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(VendingMachinesInfoCondition condition);
	
	/**
	 * 根据公司id查询公司当下的售卖机信息
	 * @param companyId
	 * @return
	 */
	List<VendingMachinesInfoBean> findVmByCompanyId(Integer companyId);
	
	/**
	 *  添加售货机
	 * @param bean
	 * @return
	 */
	public VendingMachinesInfoBean addMachines(VendingMachinesInfoBean bean);
	
	/**
	 * 生成 售货机编号
	 * @param areadNumber 地区编号
	 * @param typeId 售货机类型编号
	 * @return
	 */
	public   String getVendingMachines(String areadNumber,Long typeId);

	/**
	 *  根据收货机编号 查询出相关信息
	 * @param code
	 * @return
	 */
	public MachinesInfoAndBaseDto getMachinesInfoAndBase(String code);
	
	/**
	 *  修改公司售货机信息
	 * @param entity
	 * @return
	 */
	public boolean updateEntity(VendingMachinesInfoBean entity);

	
	/**
	 * 根据路线lineId查询所有机器信息
	 * @author hebiting
	 * @date 2018年4月16日上午11:09:34
	 * @param lineId
	 * @return
	 */
	List<VendingMachinesInfoBean> findMachinesByLineId(Integer lineId);
	
	/**
	 * 根据售货机编号 查询二维码路径
	 * @param code
	 * @return
	 */
	public String getVendingMachinesInfoBean(String code);
	
	/**
	 *  查询所有售货机编号
	 * @return
	 */
	public List<String> getCode();
	
	/**
	 * 售货机列表 编辑
	 * @param dto
	 * @return
	 */
	public boolean updateInfoAndBase(MachinesInfoAndBaseDto dto);
	
	/**
	 * 判断是否该机器模板下 已经存在一台售货机
	 * @param machinesId
	 * @return
	 */
	public boolean getMachinesBaseId(Integer machinesId);
	
	/**
	 * 查询当前登录用户所属公司 所有的售货机编号
	 * @return
	 */
	public List<String> findCode(Integer state,Integer companyId,Integer areaId);

	/**
	 * 根据售货机编号 查询新二维码 路径
	 * @param code
	 * @return
	 */
	public String getQRCode(String code);
	
	/**
	 * 根据当前用户 查询出本公司以及子公司的售货机信息
	 * @param companyId
	 * @return
	 */
	public List<VendingMachinesInfoBean> getMachinesInfo(String code,Long companyId);
	
	/**
	 * 根据线路Id查询所有的售货机
	 * @param lineId
	 * @return
	 */
	public ReturnDataUtil findMachinesListPage(VendingMachinesInfoCondition condition);

	/**
	 * 解绑售货机
	 * @param untieBean
	 * @return
	 */
    ReturnDataUtil untieVendingMachine(UntieBean untieBean);
    //查看所有货道及其下商品(包含最大推荐容量)
    List<WayDto1> findAllWayAndItem(String vmCode);
    //查看所有货道及其下商品(导出)
    List<WayDto1> findAllWayAndItemForExport(String vmCode);
	/**
	 * 最近五台售货机列表 查询
	 * @param condition
	 * @return
	 */
    public ReturnDataUtil nearbyListPage(VendingMachinesInfoCondition condition);
	/**
	 * 根据编码找到售货机
	 * @param code
	 * @return
	 */
	public VendingMachinesInfoBean findVendingMachinesByCodeForMap(String code);

	/**
	 * 获取机器部分基础信息
	 * @author hebiting
	 * @date 2019年3月19日下午4:35:53
	 * @param vmCode
	 * @return
	 */
	public VmbaseInfoDto getBaseInfo(String vmCode);
	/**
	 * 根据机器编码获取该机器线路，区域，公司归属
	 * @author hebiting
	 * @date 2018年7月16日上午10:20:29
	 * @param vmCode
	 * @return
	 */
	public MachinesLAC getMachinesLAC(String vmCode);
}
