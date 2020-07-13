package com.server.module.system.bargainManage;


import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-12-21 16:20:26
 */ 
public interface  TblCustomerBargainDao{
	/**
	 * 查询砍价订单列表
	 * @param form
	 * @return returnDataUtil
	 */
	public ReturnDataUtil listPage(TblCustomerBargainForm tblCustomerBargainForm);
	
	/**
	 * 查询砍价个各个状态的订单数
	 * @param form
	 * @return List<EachStateNumDto> 
	 */
	public List<EachStateNumDto>  eachStateNum(TblCustomerBargainForm tblCustomerBargainForm);
	
	/**
	 * 查询砍价订单详情
	 * @param form
	 * @return TblCustomerBargainDetailDto
	 */
	public TblCustomerBargainDetailDto detail(TblCustomerBargainForm tblCustomerBargainForm);
	
	/**
	 * 查询砍价参与人详情
	 * @param form
	 * @return List<TblCustomerBargainDetailBean>
	 */
	public List<TblCustomerBargainDetailBean> detailList(TblCustomerBargainForm tblCustomerBargainForm);
	
	/**
	 * 更新已发送短信提醒
	 * @param phone,id
	 * @return Boolean
	 */
	public Boolean updateSendMessage(String phone,Integer id); 

	
}

