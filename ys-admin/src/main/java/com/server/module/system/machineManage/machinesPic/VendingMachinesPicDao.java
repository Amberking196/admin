package com.server.module.system.machineManage.machinesPic;

import java.util.List;
import java.util.Set;

import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesBean;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesCondition;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
public interface VendingMachinesPicDao {

	/**
	 * 查询售货机广告
	 * @author why
	 * @date 2018年11月2日 上午10:55:13 
	 * @param vendingMachinesPic
	 * @return
	 */
	public ReturnDataUtil listPage(VendingMachinesPic vendingMachinesPic);

	/**
	 * 手机端广告页查询
	 * @author why
	 * @date 2018年11月7日 上午8:53:29 
	 * @param vendingMachinesPic
	 * @return
	 */
	public List<VendingMachinesPicBean> list(VendingMachinesPic vendingMachinesPic);

	/**
	 * 修改售货机广告
	 * @author why
	 * @date 2018年11月2日 下午2:24:43 
	 * @param entity
	 * @return
	 */
	public boolean update(VendingMachinesPicBean entity);

	/**
	 * 删除售货机广告
	 * @author why
	 * @date 2018年11月2日 下午2:24:54 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	public VendingMachinesPicBean get(Object id);

	/**
	 * 增加售货机广告
	 * @author why
	 * @date 2018年11月2日 上午10:55:00 
	 * @param entity
	 * @return
	 */
	public VendingMachinesPicBean insert(VendingMachinesPicBean entity);
	
	/**
	 * 查询机器首页的充值弹窗
	 * @author why
	 * @date 2019年1月29日 上午11:31:42 
	 * @param vmCode
	 * @return
	 */
    public String  findVendingSlideshow(String vmCode);
    
    /**
	 * 查询售货机广告
	 * @author why
	 * @date 2018年12月25日 下午3:26:38 
	 * @param 
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
	     * @param id
	     * @return
	 */
	 public VendingPicMachinesBean getPicVmCode(Object id);

	 /**
	     * 根据商品basicItmeId判断是否有重复设置
	     * @author hjc
	     * @date 2019年3月15日 下午2:26:41 
	     * @param  basicItemId
	     * @return
	 */
	 public VendingMachinesPicDto checkRepeat(Long basicItemId);
	 
	 /**
	     * 新增绑定机器
	     * @author hjc
	     * @date 2019年3月15日 下午2:26:41 
	     * @param ids
	     * @return
	 */
	public boolean insertPicVmCode(VendingPicMachinesBean entity);
	
	 /**
     * 新增取消绑定机器
     * @author hjc
     * @date 2019年3月15日 下午2:26:41 
     * @param entity
     * @return
     */
	public boolean deletePicVmCode(VendingPicMachinesBean entity);

	 /**
     *查询机器盖公司的子公司
     * @author hjc
     * @date 2019年3月15日 下午2:26:41 
     * @param String
     * @return
     */
	public String  getChildList(Long companyId);
	public VendingMachinesPicDto vmCodeTranCompanyId(String vmCodes);
}
