package com.server.module.system.adminUser;


import java.util.List;

import com.server.module.commonBean.LoginInfoBean;
import com.server.util.ReturnDataUtil;

public interface AdminUserService {
	/**
     * 用户登录
     * @param userName
     * @param password
     * @return  ReturnDataUtil  返回固定格式数据
     */
	 public ReturnDataUtil adminUserLogin(String loginCode,String password);  
	/**
		 * 用户变更密码
		 * @param userId  用户ID
		 * @param newPassword  新密码
		 * @return ReturnDataUtil 更新标记 1成功，0失败
	*/
	 public ReturnDataUtil updatPassword(AdminUserForm adminForm);
	 /**
		 * 获取部门用户列表
		 * @param departMent 部门
		 * @return
		 */
	public ReturnDataUtil getAdminUserList(Integer departMent,Integer companyId);
		
		
		/**
		 * 查询公司所有员工信息
		 * @param companyId
		 * @return
		 */
	ReturnDataUtil findAllCompanyUser(Integer companyId);
	
	
	/**
	 * 创建公司员工
	 * @param user
	 * @return
	 */
	ReturnDataUtil createAdminUser(AdminUserBean user);
	
	/**
	 * 更新员工信息
	 * @param user
	 * @return
	 */
	ReturnDataUtil updateAdminUser(LoginInfoBean user);
	/**
	 * 根据用户id查询用户信息
	 * @author hebiting
	 * @date 2018年4月10日上午8:54:42
	 * @param id
	 * @return
	 */
	ReturnDataUtil findUserById(Long id);
	
	/**
	 * 根据条件查询用户信息
	 * @author hebiting
	 * @date 2018年4月10日下午4:44:06
	 * @param userForm
	 * @return
	 */
	ReturnDataUtil findUserByForm(SearchUserForm userForm);
	
	/**
	 * 查询所有部门信息
	 * @author hebiting
	 * @date 2018年4月10日下午5:02:50
	 * @return
	 */
	List<DepartMentBean> findAllDepartMent();
	
	/**
	 * 查询所有的logincode
	 * @author hebiting
	 * @date 2018年4月24日下午3:58:42
	 * @return
	 */
	boolean loginCodeIsOnlyOne(String loginCode);
	
	/**
	 * 判断手机号码是否重复(重复返回true，非重复返回false)
	 * @author hebiting
	 * @date 2018年5月28日下午6:16:24
	 * @param phone
	 * @return
	 */
	boolean phoneRepeat(String phone);

	/**
	 * 根据公司查找出公司对应的区域
	 * @author zfc
	 * @date 2018年7月21日09:27:34
	 * @param companyId
	 * @return
	 */
	List<AdminAreaDto> findAreaByCompanyId(String companyId);

	/**
	 * 根据区域和公司查找出区域对应的线路
	 *
	 * @param areaId
	 * @param companyId
	 * @return
	 * @author zfc
	 * @date 2018年7月21日09:27:34
	 */
	List<AdminLineDto> findLineByAreaIdAndCompanyId(String areaId, String companyId);

	/**
	 * 标志删除用户
	 * @param adminUserBean
	 * @return
	 * @author hjc
	 * @date 2019年2月28日09:27:34
	 */
	public Boolean deleteAdminUser(AdminUserBean adminUserBean) ;

	/**
	 *将售货机绑定给某个员工
	 * @param adminMachine
	 * @return
	 */
    public ReturnDataUtil addMachineToUser( AdminMachine adMachine );

	List<AdminMachine>  getMachinesByUserId( Integer userId );

	//根据userID，vmcode查询已绑定的货道
	List<AdminMachine> getWayByUserIdAndVmcode( Integer userId, String vmCode );

	ReturnDataUtil delMachinesByUserId(List<Integer> mwIds);

	//同一个员工不能多次绑定同一个货道
	//boolean theWayIsOnlyOne( String way );
}
