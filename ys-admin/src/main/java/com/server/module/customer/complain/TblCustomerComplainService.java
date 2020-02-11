package com.server.module.customer.complain;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-17 08:48:16
 */
public interface TblCustomerComplainService {

	/**
	 * 用户故障申报列表
	 * @author why
	 * @date 2018年11月22日 下午5:15:25 
	 * @param tblCustomerComplainForm
	 * @return
	 */
	public ReturnDataUtil listPage(TblCustomerComplainForm tblCustomerComplainForm);

	/**
	 * 故障申报修改
	 * @author why
	 * @date 2019年2月15日 下午2:55:13 
	 * @param entity
	 * @return
	 */
	public boolean update(TblCustomerComplainBean entity);

	/**
	 * 故障申报删除
	 * @author why
	 * @date 2019年2月15日 下午2:55:23 
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	/**
	 * 故障申报查询
	 * @author why
	 * @date 2019年2月15日 下午2:55:33 
	 * @param id
	 * @return
	 */
	public TblCustomerComplainBean get(Object id);

	/**
	 * 故障申报增加
	 * @author why
	 * @date 2019年2月15日 下午2:55:40 
	 * @param entity
	 * @return
	 */
	public TblCustomerComplainBean add(TblCustomerComplainBean entity);
	
	/**
	 * 我的故障申报
	 * @author why
	 * @date 2018年12月6日 上午9:12:23 
	 * @param tblCustomerComplainForm
	 * @return
	 */
	public List<TblCustomerComplainBean> myDeclaration(TblCustomerComplainForm tblCustomerComplainForm);


	/**
	 *  用户每天申诉次数
	 * @param:
	 * @return:
	 * @auther: why
	 * @date: 2018/12/20 9:39
	 */
	public  Integer findComplaintsNumberById();
}
