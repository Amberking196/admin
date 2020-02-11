package com.server.module.system.synthesizeManage.roleManage;


import java.util.List;

import com.server.module.system.adminUser.AdminUserBean;
import com.server.util.ReturnDataUtil;

public interface RoleManageService {


	/**
	 * 增加角色
	 * @param role
	 * @return
	 */
	ReturnDataUtil addRole(RoleBean role);
	/**
	 * 修改角色
	 * @param role
	 * @return
	 */
	ReturnDataUtil updateRole(RoleBean role);
	/**
	 * 删除角色
	 * @param role
	 * @return
	 */
	ReturnDataUtil deleteRole(Integer roleId);
	/**
	 * 根据公司id查询所有角色
	 * @return
	 */
	ReturnDataUtil findRoleByCompany(Integer companyId);
	/**
	 * 判断角色是否重复
	 * @author hebiting
	 * @date 2018年5月9日下午6:07:47
	 * @param roleName
	 * @return
	 */
	boolean isOnlyOne(Integer companyId , String roleName);
	/**
	 * 分页查询
	 * @author 26920
	 * param roleForm
	 * return ReturnDataUtil
	 */
	ReturnDataUtil listPage(RoleForm condition);
	/**
	 * @author 26920
	 * 查询总数
	 * 
	 */
	long findRoleByFormNum(RoleForm condition);
	/**
	 * 根据id查询角色信息
	 * @param parseInt
	 * @return
	 */
	RoleBean findRoleById(int parseInt);
	/**
	 * 根据id查询角色与子角色信息
	 * @param parseInt
	 * @return
	 */
	List<Integer> findSonRoleById(int parseInt);
	/**
	 * 根据id查询角色与子角色信息
	 * @param parseInt
	 * @return
	 */
	List<RoleBean> findSonRole(int parseInt);
	/**
	 * 根据角色查询用户
	 * @author hjc
	 * param role
	 * return List<Integer>
	 */
	public List<Integer>  findAdminUserListByRoleId(RoleBean role);
	/**
	 * 根据用户的公司和角色查询用户
	 * @author hjc
	 * param role
	 * return List<AdminUserBean>
	 */
	public List<AdminUserBean> findUserByRole(RoleBean role);
	/**
	 * 批量分配用户角色(增加)
	 * @author hjc
	 * param add,role
	 * return Boolean
	 */
	public Boolean addAdminUserList(String add,RoleBean role);
	/**
	 * 批量分配用户角色(减少)
	 * @author hjc
	 * param reduce,role
	 * return Boolean
	 */
	public Boolean reduceAdminUserList(String reduce,RoleBean role);
}
	
