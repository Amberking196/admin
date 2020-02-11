package com.server.module.system.synthesizeManage.roleManage;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserDao;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;

@Service
public class RoleManageServiceImpl implements RoleManageService {

	@Autowired
	RoleManageDao roleManageDao;
	@Autowired
	AdminUserDao  adminUserDao;
	@Autowired
	CompanyDao companyDao;
	@Override
	public ReturnDataUtil addRole(RoleBean role) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		role.setCreateTime(new Date());//设置创建时间
		role.setCreateUser(UserUtils.getUser().getId());//设置创建者
		AdminUserBean adminUserBean=adminUserDao.findUserById(UserUtils.getUser().getId());
		role.setCreateUserName(adminUserBean.getName());//设置创建者
		if(role!=null && roleManageDao.addRole(role)){
			returnData.setStatus(1);
			returnData.setMessage("新增成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("新增失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil updateRole(RoleBean role) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(role!=null && roleManageDao.updateRole(role)){
			returnData.setStatus(1);
			returnData.setMessage("更新成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("更新失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil deleteRole(Integer roleId) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<Integer> list=roleManageDao.findSonRoleById(roleId);
		
		if(list!=null && list.size()>1) {
			returnData.setStatus(0);
			returnData.setMessage("请先删除子角色");
		}else if(roleId!=null && roleManageDao.deleteRole(roleId)){
			returnData.setStatus(1);
			returnData.setMessage("删除成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("删除失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil findRoleByCompany(Integer companyId){
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(companyId!=null){
			List<RoleBean> roleList = roleManageDao.findRoleByCompany(companyId);
			if(roleList!=null && roleList.size()>0){
				returnData.setStatus(1);
				returnData.setMessage("查询成功");
				returnData.setReturnObject(roleList);
			}else{
				returnData.setStatus(0);
				returnData.setMessage("查询失败");
			}
		}else{
			returnData.setStatus(0);
			returnData.setMessage("参数错误");
		}
		return returnData;
	}

	@Override
	public boolean isOnlyOne(Integer companyId , String roleName) {
		
		return roleManageDao.isOnlyOne(companyId,roleName);
	}

	@Override
	public ReturnDataUtil listPage(RoleForm condition) {
		// TODO Auto-generated method stub
		ReturnDataUtil data = roleManageDao.listPage(condition);
		
		return data;
	}

	@Override
	public long findRoleByFormNum(RoleForm condition) {
		
		return roleManageDao.findRoleByFormNum(condition);
	}

	@Override
	public RoleBean findRoleById(int parseInt) {
		
		return roleManageDao.findRoleById(parseInt);
	}
	
	@Override
	public 	List<Integer> findSonRoleById(int parseInt) {
		
		return roleManageDao.findSonRoleById(parseInt);
	}


	/**
	 * 根据id查询角色与子角色信息
	 * @param parseInt
	 * @return
	 */
	@Override
	public List<RoleBean> findSonRole(int parseInt){
		List<RoleBean> roleList=roleManageDao.findSonRole(parseInt);
//		for(RoleBean role:roleList) {
//			CompanyBean cb=companyDao.findCompanyById(role.getCompanyId());
//			role.setParentCompanyName(cb!=null?cb.getName():"无");
//		}
		return roleList;
	}
	
	@Override
	public List<Integer>  findAdminUserListByRoleId(RoleBean role){
		return roleManageDao.findAdminUserListByRoleId(role);
	}
	
	@Override
	public List<AdminUserBean> findUserByRole(RoleBean role){
		return roleManageDao.findUserByRole(role);
	}
	
	@Override
	public Boolean addAdminUserList(String add,RoleBean role) {
		return roleManageDao.addAdminUserList(add,role);
	}
	
	@Override
	public Boolean reduceAdminUserList(String reduce,RoleBean role) {
		return roleManageDao.reduceAdminUserList(reduce,role);
	}
	


}
