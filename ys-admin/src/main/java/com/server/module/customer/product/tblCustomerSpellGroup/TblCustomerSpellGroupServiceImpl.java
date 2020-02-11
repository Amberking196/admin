package com.server.module.customer.product.tblCustomerSpellGroup;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-10-17 11:06:09
 */
@Service
public class TblCustomerSpellGroupServiceImpl implements TblCustomerSpellGroupService {

	private static Logger log = LogManager.getLogger(TblCustomerSpellGroupServiceImpl.class);
	@Autowired
	private TblCustomerSpellGroupDao tblCustomerSpellGroupDaoImpl;

	public ReturnDataUtil listPage(TblCustomerSpellGroupForm condition) {
		return tblCustomerSpellGroupDaoImpl.listPage(condition);
	}

	/**
	 * 发起拼团
	 */
	public TblCustomerSpellGroupBean add(TblCustomerSpellGroupBean entity) {
		log.info("<TblCustomerSpellGroupServiceImpl>------<add>-------start");
		TblCustomerSpellGroupBean bean = tblCustomerSpellGroupDaoImpl.insert(entity);
		log.info("<TblCustomerSpellGroupServiceImpl>------<add>-------end");
		return bean;
	}

	public boolean update(TblCustomerSpellGroupBean entity) {
		return tblCustomerSpellGroupDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return tblCustomerSpellGroupDaoImpl.delete(id);
	}

	/**
	 * 查询商品下的用户拼团信息
	 */
	public List<TblCustomerSpellGroupBean> list(TblCustomerSpellGroupForm tblCustomerSpellGroupForm) {
		log.info("<TblCustomerSpellGroupServiceImpl>------<list>-------start");
		List<TblCustomerSpellGroupBean> list = tblCustomerSpellGroupDaoImpl.list(tblCustomerSpellGroupForm);
		log.info("<TblCustomerSpellGroupServiceImpl>------<list>-------end");
		return list;
	}

	public TblCustomerSpellGroupBean get(Object id) {
		return tblCustomerSpellGroupDaoImpl.get(id);
	}

	@Override
	public ReturnDataUtil findCustomerByIds(String ids) {
		log.info("<TblCustomerSpellGroupServiceImpl>------<findCustomerByIds>-------start");
		ReturnDataUtil returnData=tblCustomerSpellGroupDaoImpl.findCustomerByIds(ids);
		log.info("<TblCustomerSpellGroupServiceImpl>------<findCustomerByIds>-------end");
		return returnData;
	}

	@Override
	public ReturnDataUtil isRepeatedSpellGroup(Long id) {
		log.info("<TblCustomerSpellGroupServiceImpl>------<isRepeatedSpellGroup>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag = tblCustomerSpellGroupDaoImpl.isRepeatedSpellGroup(id);
		if(flag) {
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("该团您已参与了，请勿重复参与！");
		}else {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("可以参团");
		}
		log.info("<TblCustomerSpellGroupServiceImpl>------<isRepeatedSpellGroup>-------end");
		return returnDataUtil;
	}


	@Override
	public ReturnDataUtil isStartSpellGroup(Long goodsId) {
		log.info("<TblCustomerSpellGroupServiceImpl>------<isStartSpellGroup>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		//判断是否是新用户
		Integer checkNewCustomer = tblCustomerSpellGroupDaoImpl.checkNewCustomer(null);
		//判断次数
		Map<String, Integer> map = tblCustomerSpellGroupDaoImpl.isStartSpellGroup(goodsId);
		Integer num = map.get("num");
		Integer count = map.get("count");
		Integer userType = map.get("userType");
		if(checkNewCustomer==0 && userType==2 ) {//新用户
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("亲，本次拼团只针对注册过的老用户，你可以参加我们其他拼团活动，谢谢!");
		}else if(checkNewCustomer>0 && userType==1 ){
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("亲，本次拼团只针对注册过的新用户，你可以参加我们其他拼团活动，谢谢!");
		}else {
			returnDataUtil.setStatus(66);
		}
		if(returnDataUtil.getStatus()==66) {
			if(num==0) { //不限制发起拼团次数
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("可以参团");
			}else { //限制次数
				if(count>=num) {
					returnDataUtil.setStatus(-99);
					returnDataUtil.setMessage("亲，您已经参见本商品"+count+"次，不可以贪心哟，请您关注我们其他商品的拼团。谢谢!");
				}else {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("可以参团");
				}
			}
		}
		log.info("<TblCustomerSpellGroupServiceImpl>------<isStartSpellGroup>-------end");
		return returnDataUtil;
	}


	public  ReturnDataUtil listSpellOrders(SpellOrderCondition condition) {
		return tblCustomerSpellGroupDaoImpl.listSpellOrders(condition);
	}
	public List<Map<String,Object>> listSpell(Integer spellId){
		return tblCustomerSpellGroupDaoImpl.listSpell(spellId);
	}

	public List<Map<String,Object>> listSpellGoodsInfo(Integer orderId){
		return tblCustomerSpellGroupDaoImpl.listSpellGoodsInfo(orderId);
	}

	public List<Map<String,Object>> listRefundInfo(String ptCode){
		return tblCustomerSpellGroupDaoImpl.listRefundInfo(ptCode);
	}

	public List<Map<String,Object>> listVouchersInfo(Integer orderId){
		return tblCustomerSpellGroupDaoImpl.listVouchersInfo(orderId);
	}
	public List<Map<String,Object>> listBaseSpellInfo(Integer orderId){
		return tblCustomerSpellGroupDaoImpl.listBaseSpellInfo(orderId);
	}
	
	@Override
	public boolean updateParCustomerIdAndState(Long customerGroupId, String participationCustomerId, Integer state) {
		log.info("<TblCustomerSpellGroupServiceImpl>------<updateParCustomerIdAndState>-------start");
		boolean flag = tblCustomerSpellGroupDaoImpl.updateParCustomerIdAndState(customerGroupId, participationCustomerId, state);
		return flag;
	}

}
