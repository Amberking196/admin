package com.server.module.system.adminUser;

import java.util.List;

import com.server.module.commonBean.LoginInfoBean;
import com.server.util.ReturnDataUtil;

public interface AdminUserDao {

	/**
	 * 用户登录
	 * @param loginCode 登录名字 /手机号码
	 * @param password  密码
	 * @return  登录用户信息，如没有该用户返回null 
	 */
	public AdminUserBean  adminUserLogin(String loginCode,String password);
	/**
	 * 用户变更密码
	 * @param userId  用户ID
	 * @param newPassword  新密码
	 * @return  更新标记 1成功，0失败
	 */
	public int updatPassword(Long userId,String newPassword) ;
	/**
	 * 获取部门用户列表
	 * @param departMent 部门
	 * @return
	 */
	public List<AdminUserBean> getAdminUserList(Integer departMent,Integer companyId);
	
	/**
	 * 查询公司所有员工信息
	 * @param companyId
	 * @return
	 */
	List<AdminUserBean> findAllCompanyUser(Integer companyId);
	
	/**
	 * 创建公司员工
	 * @param user
	 * @return
	 */
	boolean createAdminUser(AdminUserBean user);
	/**
	 * 更新员工信息
	 * @param user
	 * @return
	 */
	boolean updateAdminUser(LoginInfoBean user);
	
	/**
	 * 更新员工部分信息
	 * @param user
	 * @return
	 */
	boolean update(LoginInfoBean user);
	/**
	 * 根据用户id查询用户信息
	 * @author hebiting
	 * @date 2018年4月10日上午8:54:42
	 * @param id
	 * @return
	 */
	AdminUserBean findUserById(Long id);
	/**
	 * 根据条件查询用户信息
	 * @author hebiting
	 * @date 2018年4月10日下午4:44:06
	 * @param userForm
	 * @return
	 */
	List<AdminUserDto> findUserByForm(SearchUserForm userForm);
	/**
	 * 根据条件查询用户信息总数
	 * @author hebiting
	 * @date 2018年4月12日下午9:35:36
	 * @param userForm
	 * @return
	 */
	Long findUserByFormNum(SearchUserForm userForm);
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
	List<String> findAllLoginCode();
	
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
	    * 查询用户 按照参数 companyId in(1,2 3)
	    * @param sqlIn
	    * @return
	    */
	 public List<UserVoForSelect> findUserByCompanyIsSql(String sqlIn);
	 /**
	  * 根据用户名验证手机号是否改变
	  * @param user
	  * @return
	  */
	public boolean checkPhoneIsChange(LoginInfoBean user);
	
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
    public boolean addMachineToUser( AdminMachine adminMachine );

	List<AdminMachine>  getMachinesByUserId( Integer userId );

	List<AdminMachine> getWayByUserIdAndVmcode( Integer userId, String vmCode );

	boolean delMachinesByUserId(List<Integer> mwIds);
}
