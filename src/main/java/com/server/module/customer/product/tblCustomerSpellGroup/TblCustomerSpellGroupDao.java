package com.server.module.customer.product.tblCustomerSpellGroup;

import java.util.List;
import java.util.Map;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-10-17 11:06:09
 */
public interface TblCustomerSpellGroupDao {

	/**
	 * 查询用户拼团信息列表
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:26:02
	 * @param tblCustomerSpellGroupForm
	 * @return
	 */
	public ReturnDataUtil listPage(TblCustomerSpellGroupForm tblCustomerSpellGroupForm);

	/**
	 * 查询商品下的用户拼团信息
	 * 
	 * @author why
	 * @date 2018年10月17日 上午11:41:30
	 * @param tblCustomerSpellGroupForm
	 * @return
	 */
	public List<TblCustomerSpellGroupBean> list(TblCustomerSpellGroupForm tblCustomerSpellGroupForm);

	/**
	 * 修改用户拼团信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:26:18
	 * @param entity
	 * @return
	 */
	public boolean update(TblCustomerSpellGroupBean entity);

	/**
	 * 删除用户拼团信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:26:32
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 查询用户拼团信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:26:44
	 * @param id
	 * @return
	 */
	public TblCustomerSpellGroupBean get(Object id);

	/**
	 * 发起拼团
	 * 
	 * @author why
	 * @date 2018年10月17日 上午11:13:11
	 * @param entity
	 * @return
	 */
	public TblCustomerSpellGroupBean insert(TblCustomerSpellGroupBean entity);

	/**
	 * 根据参加拼团的用户IDS查询用户信息
	 * 
	 * @param ids
	 * @return
	 */
	public ReturnDataUtil findCustomerByIds(String ids);

	/**
	 * 判断当前用户是否重复参与拼团
	 * 
	 * @author why
	 * @date 2018年10月23日 下午2:13:08
	 * @param id
	 * @return
	 */
	public boolean isRepeatedSpellGroup(Long id);

	/**
	 * 判断用户是否可以发起拼团
	 * 
	 * @author why
	 * @date 2019年1月12日 下午4:32:41
	 * @param goodsId
	 * @return
	 */
	public Map<String, Integer> isStartSpellGroup(Long goodsId);

	/**
	 * 查询拼团订单信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:28:04
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listSpellOrders(SpellOrderCondition condition);

	/**
	 * 查询该拼团下所有拼友数据
	 * 
	 * @param spellId
	 * @return
	 */
	public List<Map<String, Object>> listSpell(Integer spellId);

	/**
	 * 查询拼团订单下的商品信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:30:43
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> listSpellGoodsInfo(Integer orderId);

	/**
	 * 查询拼团订单下退款记录
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:31:37
	 * @param ptCode
	 * @return
	 */
	public List<Map<String, Object>> listRefundInfo(String ptCode);

	/**
	 * 查询拼团订单下的用户提水券信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:32:17
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> listVouchersInfo(Integer orderId);

	/**
	 * 查询拼团订单下的团购基本信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:36:33
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> listBaseSpellInfo(Integer orderId);

	/**
	 * 发起拼团
	 * 
	 * @author hjc
	 * @date 2019年1月17日 上午11:13:11
	 * @param entity
	 * @return
	 */
	public Integer insertSQL(TblCustomerSpellGroupBean entity);

	/**
	 * 判断该用户是否是新用户 （一次都没有购买商品的用户）
	 * 
	 * @author why
	 * @date 2019年1月18日 下午5:37:42
	 * @return
	 */
	public Integer checkNewCustomer(Long customerId);

	/**
	 * 支付完成后更新用户拼团表
	 * @author why
	 * @date 2019年2月18日 下午3:16:46 
	 * @param customerGroupId
	 * @param participationCustomerId
	 * @param state
	 * @return
	 */
	public boolean updateParCustomerIdAndState(Long customerGroupId, String participationCustomerId, Integer state);
	
	

}
