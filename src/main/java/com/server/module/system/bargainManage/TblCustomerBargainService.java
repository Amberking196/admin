package com.server.module.system.bargainManage;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-12-21 16:20:26
 */ 
public interface  TblCustomerBargainService{

	/**
	 * 查询砍价订单列表
	 * @param form
	 * @return returnDataUtil
	 */
	public ReturnDataUtil listPage(TblCustomerBargainForm form);
	/**
	 * 查询砍价订单列表详情
	 * @param form
	 * @return TblCustomerBargainDetailDto
	 */
	public TblCustomerBargainDetailDto detail(TblCustomerBargainForm tblCustomerBargainForm);
	/**
	 * 查询砍价订单列表(无各状态数量统计)
	 * @param form
	 * @return returnDataUtil
	 */
	public ReturnDataUtil listPageWithOutStateNum(TblCustomerBargainForm form);
	/**
	 * 更新砍价用户发送短信标志
	 * @param phone,id
	 * @return true
	 */
	public Boolean updateSendMessage(String phone, Integer id) ;

}

