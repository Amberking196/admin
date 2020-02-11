package com.server.module.customer.product.tblCustomerSpellGroup;

import java.util.List;
import java.util.Map;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-10-17 11:06:09
 */
public interface TblCustomerSpellGroupService {

	/**
	 * 查询用户拼团信息列表
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:37:10
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(TblCustomerSpellGroupForm condition);

	/**
	 * 查询商品下的用户拼团信息
	 * 
	 * @author why
	 * @date 2018年10月17日 下午2:27:25
	 * @param tblCustomerSpellGroupForm
	 * @return
	 */
	public List<TblCustomerSpellGroupBean> list(TblCustomerSpellGroupForm tblCustomerSpellGroupForm);

	/**
	 * 修改用户拼团信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:37:25
	 * @param entity
	 * @return
	 */
	public boolean update(TblCustomerSpellGroupBean entity);

	/**
	 * 删除用户拼团信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:37:40
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	/**
	 * 查询用户拼团信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:37:50
	 * @param id
	 * @return
	 */
	public TblCustomerSpellGroupBean get(Object id);

	/**
	 * 发起拼团
	 * 
	 * @author why
	 * @date 2018年10月17日 上午11:19:06
	 * @param entity
	 * @return
	 */
	public TblCustomerSpellGroupBean add(TblCustomerSpellGroupBean entity);

	/**
	 * 根据参加拼团的ids查询用户信息
	 * 
	 * @param ids
	 * @return
	 */
	public ReturnDataUtil findCustomerByIds(String ids);

	/**
	 * 判断当前用户是否重复参与拼团
	 * 
	 * @author why
	 * @date 2018年10月23日 下午2:45:17
	 * @param id
	 * @return
	 */
	public ReturnDataUtil isRepeatedSpellGroup(Long id);

	/**
	 * 判断用户是否可以发起拼团
	 * 
	 * @author why
	 * @date 2019年1月12日 下午4:43:56
	 * @param goodsId
	 * @return
	 */
	public ReturnDataUtil isStartSpellGroup(Long goodsId);

	/**
	 * 查询拼团订单信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:38:02
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listSpellOrders(SpellOrderCondition condition);

	/**
	 * 查询该拼团下所有拼友数据
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:38:21
	 * @param spellId
	 * @return
	 */
	public List<Map<String, Object>> listSpell(Integer spellId);

	/**
	 * 查询拼团订单下的商品信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:38:40
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> listSpellGoodsInfo(Integer orderId);

	/**
	 * 查询拼团订单下退款记录
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:39:03
	 * @param ptCode
	 * @return
	 */
	public List<Map<String, Object>> listRefundInfo(String ptCode);

	/**
	 * 查询拼团订单下的用户提水券信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:39:16
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> listVouchersInfo(Integer orderId);

	/**
	 * 查询拼团订单下的团购基本信息
	 * 
	 * @author why
	 * @date 2019年2月15日 上午9:39:26
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> listBaseSpellInfo(Integer orderId);
	
	/**
	 * 支付完成后更新用户拼团表
	 * @author why
	 * @date 2019年2月18日 下午3:18:15 
	 * @param customerGroupId
	 * @param participationCustomerId
	 * @param state
	 * @return
	 */
	public boolean updateParCustomerIdAndState(Long customerGroupId,String participationCustomerId,Integer state);

}
