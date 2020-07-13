package com.server.module.system.machineManage.machinesPic;

import java.util.List;

import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesBean;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesCondition;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
public interface VendingMachinesPicService {

	/**
	 * 查询售货机广告列表
	 * 
	 * @author why
	 * @date 2018年11月2日 上午11:21:27
	 * @param vendingMachinesPic
	 * @return
	 */
	public ReturnDataUtil listPage(VendingMachinesPic vendingMachinesPic);

	/**
	 * 手机端广告页查询
	 * @author why
	 * @date 2018年11月7日 上午9:04:23 
	 * @param vendingMachinesPic
	 * @return
	 */
	public List<VendingMachinesPicBean> list(VendingMachinesPic vendingMachinesPic);

	/**
	 * 修改售货机广告
	 * @author why
	 * @date 2018年11月2日 下午2:28:16 
	 * @param entity
	 * @return
	 */
	public boolean update(VendingMachinesPicBean entity);

	/**
	 * 删除售货机广告
	 * @author why
	 * @date 2018年11月2日 下午2:28:26 
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	public VendingMachinesPicBean get(Object id);

	/**
	 * 增加售货机广告
	 * 
	 * @author why
	 * @date 2018年11月2日 上午11:21:48
	 * @param entity
	 * @return
	 */
	public VendingMachinesPicBean add(VendingMachinesPicBean entity);
	
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
	 public List<VendingMachinesPicBean> findVendingPicMachinesBean(String  vmCode);
	 
	 /**
	     * 查询公司下  是否已经存在公司弹窗了
	     * @author why
	     * @date 2019年1月30日 下午2:26:41 
	     * @param companyId
	     * @return
	 */
	 public boolean findPicMachinesBeanByCompanyIdAndItemId(Long companyId,Long itemBasicId);
	 /**
	     * 新增绑定机器
	     * @author hjc
	     * @date 2019年3月15日 下午2:26:41 
	     * @param advertisingId,codeList
	     * @return
	 */
	 public void addAll(Long advertisingId,List<String> codeList);
	 /**
	     * 新增取消绑定机器
	     * @author hjc
	     * @date 2019年3月15日 下午2:26:41 
	     * @param ids
	     * @return
	 */
	 public void deleteAll(Long[] ids);
	 /**
	     * 绑定机器时查询机器列表
	     * @author hjc
	     * @date 2019年3月15日 下午2:26:41 
	     * @param ids
	     * @return
	 */
	 public List<VendingPicMachinesBean> list(VendingPicMachinesCondition condition);
	 /**
	     * 根据id查询明
	     * @author hjc
	     * @date 2019年3月15日 下午2:26:41 
	     * @param ids
	     * @return
	 */
	 public VendingPicMachinesBean getPicVmCode(Object id);

	 /**
	     * 根据商品basicItmeId判断是否有重复设置
	     * @author hjc
	     * @date 2019年3月15日 下午2:26:41 
	     * @param ids
	     * @return
	 */
	 public VendingMachinesPicDto checkRepeat(Long basicItemId);

}
