package com.server.module.system.adminUser;

import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.commonBean.LoginInfoBean;
import com.server.util.MD5Utils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * 系统用户逻辑处理模块
 * 
 * @author denfer
 *
 */

@Service
public class AdminUserServiceImp implements AdminUserService {


	public static Logger log = LogManager.getLogger( AdminUserServiceImp.class);  
	@Autowired
	private AdminUserDao adminUserDao;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@Autowired
	private MD5Utils md;

	/**
	 * 用户登录
	 * 
	 * @param userName
	 * @param password
	 * @return ReturnDataUtil 返回固定格式数据
	 */
	public ReturnDataUtil adminUserLogin(String loginCode, String password) {
		log.info("<AdminUserServiceImp>--<adminUserLogin>--start");
		password = md.getMd5(password);
		AdminUserBean admin = adminUserDao.adminUserLogin(loginCode, password);

		if (admin != null && admin.getId() != null) {
			returnDataUtil.setMessage("用户登录成功");
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(admin);
		} else {
			returnDataUtil.setMessage("帐号密码错误");
			returnDataUtil.setStatus(0);
		}
		log.info("<AdminUserServiceImp>--<adminUserLogin>--end");
		return returnDataUtil;
	}

	/**
	 * 用户变更密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param newPassword
	 *            新密码
	 * @return ReturnDataUtil 更新标记 1成功，0失败
	 */
	public ReturnDataUtil updatPassword(AdminUserForm adminForm) {
		log.info("<AdminUserServiceImp>--<updatPassword>--start");
		String password = md.getMd5(adminForm.getNewPassword());
		int re = adminUserDao.updatPassword(adminForm.getId(), password);
		if (re == 1) {
			returnDataUtil.setMessage("修改密码成功");
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(1);
		} else {
			returnDataUtil.setMessage("修改密码失败");
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(0);
		}
		log.info("<AdminUserServiceImp>--<updatPassword>--end");
		return returnDataUtil;
	}

	/**
	 * 获取部门用户列表
	 * 
	 * @param departMent
	 *            部门
	 * @return
	 */
	public ReturnDataUtil getAdminUserList(Integer departMent, Integer companyId) {
		log.info("<AdminUserServiceImp>--<getAdminUserList>--start");
		if(departMent!=null && companyId!=null){
			List<AdminUserBean> reList = adminUserDao.getAdminUserList(departMent, companyId);
			if (reList != null && reList.size() > 0) {
				returnDataUtil.setMessage("获取成员成功");
				returnDataUtil.setStatus(1);
				returnDataUtil.setReturnObject(reList);
			} else {
				returnDataUtil.setMessage("没有部门成员");
				returnDataUtil.setStatus(0);
				returnDataUtil.setReturnObject(null);
			}
		}else{
			returnDataUtil.setMessage("条件为空");
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(null);
		}
		

		log.info("<AdminUserServiceImp>--<getAdminUserList>--end");
		return returnDataUtil;
	}

	@Override
	public ReturnDataUtil findAllCompanyUser(Integer companyId) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		log.info("<AdminUserServiceImp>--<getAdminUserList>--start");
		if(companyId!=null){
			List<AdminUserBean> reList = adminUserDao.findAllCompanyUser(companyId);
			if (reList != null && reList.size() > 0) {
				returnData.setMessage("获取公司成员成功");
				returnData.setStatus(1);
				returnData.setReturnObject(reList);
			} else {
				returnData.setMessage("没有公司成员");
				returnData.setStatus(0);
				returnData.setReturnObject(null);
			}
		}else{
			returnData.setMessage("条件为空");
			returnData.setStatus(0);
			returnData.setReturnObject(null);
		}
		log.info("<AdminUserServiceImp>--<getAdminUserList>--end");
		return returnData;
	}

	@Override
	public ReturnDataUtil createAdminUser(AdminUserBean user) {
		log.info("<AdminUserServiceImp>--<createUser>--end");
		ReturnDataUtil returnData = new ReturnDataUtil();
		//验证手机号是否存在，如果存在直接返回
		boolean result = adminUserDao.phoneRepeat(user.getPhone());
		if(result) {//手机号存在
			returnData.setStatus(0);
			returnData.setMessage("创建失败，该手机号已被注册或输入号码不正常，请更换手机号");
			return returnData;
		}
		user.setPassword(md.getMd5(user.getPassword()));
		if(user!=null && adminUserDao.createAdminUser(user)){
			returnData.setStatus(1);
			returnData.setMessage("创建成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("创建失败");
		}
		log.info("<AdminUserServiceImp>--<createUser>--end");
		return returnData;
	}

	@Override
	public ReturnDataUtil updateAdminUser(LoginInfoBean user) {
		log.info("<AdminUserServiceImp>--<updateAdminUser>--start");
		ReturnDataUtil returnData = new ReturnDataUtil();
		//验证手机号是否已经修改，没有修改直接保存，修改了就验证该手机号码是否已经存在
		boolean isChange=adminUserDao.checkPhoneIsChange(user);
		if(isChange) {//手机号码已修改
			//验证手机号是否存在，如果存在直接返回
			boolean result = adminUserDao.phoneRepeat(user.getPhone());
			if(result) {//手机号存在
				returnData.setStatus(0);
				returnData.setMessage("创建失败，该手机号已被注册或输入号码不正常，请更换手机号");
				return returnData;
			}
			//手机号不存在就直接保存
		}
		if(StringUtil.isNotBlank(user.getPassword())){
			user.setPassword(md.getMd5(user.getPassword()));
		}
		if(user!=null && adminUserDao.update(user)){
			returnData.setStatus(1);
			returnData.setMessage("更新成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("更新失败");
		}
		log.info("<AdminUserServiceImp>--<updateAdminUser>--end");
		return returnData;
	}

	@Override
	public ReturnDataUtil findUserById(Long id) {
		log.info("<AdminUserServiceImp>--<findUserById>--start");
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(id!=null){
			AdminUserBean userBean = adminUserDao.findUserById(id);
			if(userBean!=null){
				returnData.setStatus(1);
				returnData.setMessage("更新成功");
				returnData.setReturnObject(userBean);
			}else{
				returnData.setStatus(0);
				returnData.setMessage("更新失败");
			}
		}else{
			returnData.setStatus(0);
			returnData.setMessage("参数为空");
		}
		log.info("<AdminUserServiceImp>--<findUserById>--end");
		return returnData;
	}

	@Override
	public ReturnDataUtil findUserByForm(SearchUserForm userForm) {
		log.info("<AdminUserServiceImp>--<findUserByForm>--start");
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<AdminUserDto> userList = adminUserDao.findUserByForm(userForm);
		Long total = adminUserDao.findUserByFormNum(userForm);
		if(userList!=null && userList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(userList);
			returnData.setTotal(total);
			returnData.setTotalPage(returnData.getTotalPage());
			returnData.setCurrentPage(userForm.getCurrentPage());
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		log.info("<AdminUserServiceImp>--<findUserByForm>--end");
		return returnData;
	}

	@Override
	public List<DepartMentBean> findAllDepartMent() { 
		log.info("<AdminUserServiceImp>--<findAllDepartMent>--start");
		List<DepartMentBean> reList=adminUserDao.findAllDepartMent();
		log.info("<AdminUserServiceImp>--<findAllDepartMent>--end");
		return reList;
	}

	@Override
	public boolean loginCodeIsOnlyOne(String loginCode) {
		log.info("<AdminUserServiceImp>--<loginCodeIsOnlyOne>--start");
		List<String> loginCodeList = adminUserDao.findAllLoginCode();
		for (String code : loginCodeList) {
			if(loginCode.equals(code)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean phoneRepeat(String phone) {
		log.info("<AdminUserServiceImp>--<phoneRepeat>--start");
		boolean result = adminUserDao.phoneRepeat(phone);
		log.info("<AdminUserServiceImp>--<phoneRepeat>--end");
		return result;
	}

	@Override
	public List<AdminAreaDto> findAreaByCompanyId(String companyId) {
		log.info("<AdminUserServiceImp>--<findAreaByCompanyId>--start");
		List<AdminAreaDto> areas = adminUserDao.findAreaByCompanyId(companyId);
		log.info("<AdminUserServiceImp>--<findAreaByCompanyId>--end");

		return areas;
	}

	@Override
	public List<AdminLineDto> findLineByAreaIdAndCompanyId(String areaId, String companyId) {


		log.info("<AdminUserServiceImp>--<findLineByAreaIdAndCompanyId>--start");
		List<AdminLineDto> lines = adminUserDao.findLineByAreaIdAndCompanyId(areaId, companyId);
		log.info("<AdminUserServiceImp>--<findLineByAreaIdAndCompanyId>--end");

		return lines;

	}
	
	@Override
	public Boolean deleteAdminUser(AdminUserBean adminUserBean) {
		return adminUserDao.deleteAdminUser(adminUserBean);
	}

    @Override
    public ReturnDataUtil addMachineToUser( AdminMachine adMachine ) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (adminUserDao.addMachineToUser(adMachine)){
			returnData.setStatus(1);
			returnData.setMessage("创建成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("创建失败");
		}
        return returnData;
    }

	@Override
	public List<AdminMachine> getMachinesByUserId( Integer userId ) {
		return adminUserDao.getMachinesByUserId(userId);
	}

	@Override
	public List<AdminMachine> getWayByUserIdAndVmcode( Integer userId, String vmCode ) {
		return adminUserDao.getWayByUserIdAndVmcode(userId,vmCode);
	}

	@Override
	public ReturnDataUtil delMachinesByUserId(List<Integer> mwIds) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (mwIds != null && mwIds.size() > 0 && adminUserDao.delMachinesByUserId(mwIds)) {
			returnData.setStatus(1);
			returnData.setMessage("删除成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("删除失败");
		}
		return returnData;
	}


}
