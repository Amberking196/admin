package com.server.module.system.machineManage.machinesAdvertising;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
public interface VendingMachinesAdvertisingService {

	/**
	 * 查询售货机广告列表
	 * 
	 * @author why
	 * @date 2018年11月2日 上午11:21:27
	 * @param vendingMachinesAdvertisingForm
	 * @return
	 */
	public ReturnDataUtil listPage(VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm);

	/**
	 * 手机端广告页查询
	 * @author why
	 * @date 2018年11月7日 上午9:04:23 
	 * @param vendingMachinesAdvertisingForm
	 * @return
	 */
	public List<VendingMachinesAdvertisingBean> list(VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm);

	/**
	 * 修改售货机广告
	 * @author why
	 * @date 2018年11月2日 下午2:28:16 
	 * @param entity
	 * @return
	 */
	public boolean update(VendingMachinesAdvertisingBean entity);

	/**
	 * 删除售货机广告
	 * @author why
	 * @date 2018年11月2日 下午2:28:26 
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	public VendingMachinesAdvertisingBean get(Object id);

	/**
	 * 增加售货机广告
	 * 
	 * @author why
	 * @date 2018年11月2日 上午11:21:48
	 * @param entity
	 * @return
	 */
	public VendingMachinesAdvertisingBean add(VendingMachinesAdvertisingBean entity);
	
	/**
	 * 查询机器首页的充值弹窗
	 * @author why
	 * @date 2019年1月29日 上午11:31:35 
	 * @param vmCode
	 * @return
	 */
    public String  findVendingSlideshow(String vmCode);
    
    /**
	 * 查询售货机广告
	 * @author why
	 * @date 2018年12月25日 下午3:41:19 
	 * @param vmCode
	 * @return
	 */
	 public List<VendingMachinesAdvertisingBean> findVendingAdvertisingMachinesBean(String  vmCode);
	 
	 /**
	     * 查询公司下  是否已经存在公司弹窗了
	     * @author why
	     * @date 2019年1月30日 下午2:26:41 
	     * @param companyId
	     * @return
	     */
	    public boolean findAdvertisingMachinesBeanByCompanyId(Long companyId);
}
