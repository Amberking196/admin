package com.server.module.system.adminUser;

import com.server.module.commonBean.LoginInfoBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统用户持久层模块
 *
 * @author denfer
 */
@Repository(value = "AdminUserDaoImp")
public class AdminUserDaoImp extends MySqlFuns implements AdminUserDao {


    public static Logger log = LogManager.getLogger(AdminUserDaoImp.class);

    /**
     * 用户登录
     *
     * @param loginCode 登录名字 /手机号码
     * @param password  密码
     * @return 登录用户信息，如没有该用户返回null
     */
    public AdminUserBean adminUserLogin(String loginCode, String password) {
        log.info("<AdminUserDaoImp>--<adminUserLogin>--start");
        String sql = "select id,companyId,loginCode,phone,name,post,departMent,role "
                + " from login_info  lo "
                + " where"
                + " loginCode='" + loginCode + "'"
                + " and password='" + password + "' and status = 1 ";
        log.info("<AdminUserDaoImp>--<adminUserLogin>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdminUserBean admin = new AdminUserBean();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                admin.setLoginCode(rs.getString("loginCode"));
                admin.setPhone(rs.getString("phone"));
                admin.setId(rs.getLong("id"));
                admin.setName(rs.getString("name"));
                admin.setCompanyId(rs.getInt("companyId"));
                admin.setDepartMent(rs.getInt("departMent"));
                admin.setRole(rs.getString("role"));
                //admin.setPost(rs.getInt("post"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<adminUserLogin>--end");
        return admin;
    }

    /**
     * 用户变更密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @return 更新标记 1成功，0失败
     */
    public int updatPassword(Long userId, String newPassword) {
        log.info("<AdminUserDaoImp>--<updatPassword>--start");
        String sql = "update login_info set password='" + newPassword + "'"
                + " where id=" + userId;
        log.info("<AdminUserDaoImp>--<updatPassword>--addsql:" + sql);
        int updateFlag = upate(sql);
        log.info("<AdminUserDaoImp>--<updatPassword>--end");
        return updateFlag;
    }

    /**
     * 获取部门用户列表
     *
     * @param departMent 部门ID
     * @return
     */
    public List<AdminUserBean> getAdminUserList(Integer departMent, Integer companyId) {
        log.info("<AdminUserDaoImp>--<getAdminUserList>--end");
        String sql = "select id,companyId,loginCode,phone,name,post,departMent "
                + " from login_info  lo "
                + " where "
                + "departMent=" + departMent + ""
                + " and companyId = " + companyId;
        log.info("<AdminUserDaoImp>--<getAdminUserList>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdminUserBean> reList = new ArrayList<AdminUserBean>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                AdminUserBean admin = new AdminUserBean();
                admin.setLoginCode(rs.getString("loginCode"));
                admin.setPhone(rs.getString("phone"));
                admin.setId(rs.getLong("id"));
                admin.setName(rs.getString("name"));
                admin.setCompanyId(rs.getInt("companyId"));
                admin.setDepartMent(rs.getInt("departMent"));
                admin.setPost(rs.getInt("post"));
                reList.add(admin);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<getAdminUserList>--end");
        return reList;
    }

    /**
     * 查询公司所有员工信息
     *
     * @param companyId
     * @return
     */
    public List<AdminUserBean> findAllCompanyUser(Integer companyId) {
        log.info("<AdminUserDaoImp>--<findAllCompanyUser>--start");
        String sql = "select id,companyId,loginCode,phone,name,post,departMent,"
                + "companyId,codeNo,hireDate,loginModel,mail,role,status "
                + " from login_info  lo "
                + " where status=1 and "
                + " companyId=" + companyId;
        log.info("<AdminUserDaoImp>--<findAllCompanyUser>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdminUserBean> reList = new ArrayList<AdminUserBean>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                AdminUserBean admin = new AdminUserBean();
                admin.setLoginCode(rs.getString("loginCode"));
                admin.setPhone(rs.getString("phone"));
                admin.setId(rs.getLong("id"));
                admin.setName(rs.getString("name"));
                admin.setCompanyId(rs.getInt("companyId"));
                admin.setDepartMent(rs.getInt("departMent"));
                admin.setPost(rs.getInt("post"));
                admin.setCodeNo(rs.getString("codeNo"));
                admin.setHireDate(rs.getString("hireDate"));
                admin.setLoginModel(rs.getInt("loginModel"));
                admin.setMail(rs.getString("mail"));
                admin.setRole(rs.getString("role"));
                admin.setStatus(rs.getInt("status"));
                reList.add(admin);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findAllCompanyUser>--end");
        return reList;
    }

    @Override
    public boolean createAdminUser(AdminUserBean user) {
        log.info("<AdminUserDaoImp>--<createUser>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into login_info(loginCode,password,status,");
        sql.append(" name,hireDate,updateDate,role,companyId,phone,loginModel,");
        sql.append(" mail,founder,departMent,isPrincipal,level,areaId,lineId,isExpatriate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        List<Object> param = new ArrayList<Object>();
        param.add(user.getLoginCode());
        param.add(user.getPassword());
        param.add(user.getStatus());
        param.add(user.getName());
        param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
        param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
        param.add(user.getRole());
        param.add(user.getCompanyId());
        param.add(user.getPhone());
        param.add(user.getLoginModel());
        param.add(user.getMail());
        param.add(user.getFounder());
        param.add(user.getDepartMent());
        param.add(user.getIsPrincipal());
        param.add(user.getLevel());
        param.add(user.getAreaId());
        param.add(user.getLineId());
        param.add(user.getIsExpatriate());
        int num = insert(sql.toString(), param);
        log.info("<AdminUserDaoImp>--<createUser>--end");
        if (num > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAdminUser(LoginInfoBean user) {
        log.info("<AdminUserDaoImp>--<updateAdminUser>--start");
        StringBuffer sql = new StringBuffer();
//		sql.append(" update login_info set password='"+user.getPassword()+"',status="+user.getStatus()+",");
//		sql.append(" name='"+user.getName()+"',role='"+user.getRole()+"',companyId="+user.getCompanyId()+",");
//		sql.append(" phone='"+user.getPhone()+"',loginModel="+user.getLoginModel()+",mail='"+user.getMail()+"',");
//		sql.append(" updateDate='"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"',department="+user.getDepartMent()+",");
//		sql.append(" rightsMachines='"+user.getRightsMachines()+"',rights
// Line='"+user.getRightsLine()+"',");
//		sql.append(" rightsButton='"+user.getRightsButton()+"',extraMenu='"+user.getRightsButton()+"',");
//		sql.append(" rightsCompany='"+user.getRightsCompany()+"'");
//		sql.append(" where id = "+user.getId());

        sql.append(" update login_info set password =?,status=?,name=?,role=?,companyId=?,phone=?,");
        sql.append(" loginModel=?,mail=?,updateDate=?,department=? where id =?");
        List<Object> param = new ArrayList<Object>();
        param.add(user.getPassword());
        param.add(user.getStatus());
        param.add(user.getName());
        param.add(user.getRole());
        param.add(user.getCompanyId());
        param.add(user.getPhone());
        param.add(user.getLoginModel());
        param.add(user.getMail());
        param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
        param.add(user.getDepartMent());
        param.add(user.getId());
        int result = upate(sql.toString(), param);
        log.info("<AdminUserDaoImp>--<updateAdminUser>--end");
        if (result != 0) {
            return true;
        }
        return false;
    }

    public boolean update(LoginInfoBean user) {
        log.info("<AdminUserDaoImp>--<update>--start");
        StringBuffer sql = new StringBuffer();
//		sql.append(" update login_info set status="+user.getStatus()+",");
//		sql.append(" name='"+user.getName()+"',role='"+user.getRole()+"',companyId="+user.getCompanyId()+",");
//		sql.append(" phone='"+user.getPhone()+"',loginModel="+user.getLoginModel()+",mail='"+user.getMail()+"',");
//		sql.append(" updateDate='"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"',department="+user.getDepartMent());	
//		sql.append(" where id = "+user.getId());
        sql.append(" update login_info set status=?,name=?,role=?,companyId=?,phone=?,level=?,areaId=?,lineId=?,isExpatriate=?, ");
        if (StringUtil.isNotBlank(user.getPassword())) {
            sql.append("password=?,");
        }
        if (StringUtil.isNotBlank(user.getMail())) {
            sql.append("mail=?,");
        }
        sql.append(" loginModel=?,updateDate=?,department=? where id =?");
        List<Object> param = new ArrayList<Object>();
        param.add(user.getStatus());
        param.add(user.getName());
        param.add(user.getRole());
        param.add(user.getCompanyId());
        param.add(user.getPhone());

        // 员工等级，线路，区域，是否为外派人员
        param.add(user.getLevel());
        param.add(user.getAreaId());
        param.add(user.getLineId());
        if (user.getIsExpatriate() == null) {
            param.add(0);
        }else {
            param.add(user.getIsExpatriate());
        }

        if (StringUtil.isNotBlank(user.getPassword())) {
            param.add(user.getPassword());
        }
        if (StringUtil.isNotBlank(user.getMail())) {
            param.add(user.getMail());
        }
        param.add(user.getLoginModel());
        param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
        param.add(user.getDepartMent());
        param.add(user.getId());
        int result = upate(sql.toString(), param);
        log.info("<AdminUserDaoImp>--<update>--end");
        if (result != 0) {
            return true;
        }
        return false;
    }

    @Override
    public AdminUserBean findUserById(Long id) {
        log.info("<AdminUserDaoImp>--<findUserById>--start");
        String sql = "select id,companyId,loginCode,phone,name,post,departMent,"
                + "companyId,codeNo,hireDate,loginModel,mail,role,status,level,areaId,lineId,isExpatriate "
                + " from login_info  lo "
                + " where "
                + " id=" + id;
        log.info("<AdminUserDaoImp>--<findUserById>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdminUserBean admin = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                admin = new AdminUserBean();
                admin.setLoginCode(rs.getString("loginCode"));
                admin.setPhone(rs.getString("phone"));
                admin.setId(rs.getLong("id"));
                admin.setName(rs.getString("name"));
                admin.setCompanyId(rs.getInt("companyId"));
                admin.setDepartMent(rs.getInt("departMent"));
                admin.setPost(rs.getInt("post"));
                admin.setCodeNo(rs.getString("codeNo"));
                admin.setHireDate(rs.getString("hireDate"));
                admin.setLoginModel(rs.getInt("loginModel"));
                admin.setMail(rs.getString("mail"));
                admin.setRole(rs.getString("role"));
                admin.setStatus(rs.getInt("status"));
                admin.setLevel(rs.getInt("level"));
                admin.setAreaId(rs.getInt("areaId"));
                admin.setLineId(rs.getInt("lineId"));
                admin.setIsExpatriate(rs.getInt("isExpatriate"));
            }
            log.info("<AdminUserDaoImp>--<findAllCompanyUser>--end");
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findUserById>--end");
        return admin;
    }

    @Override
    public List<AdminUserDto> findUserByForm(SearchUserForm userForm) {
        log.info("<AdminUserDaoImp>--<findUserByForm>--start");
        StringBuffer sql = new StringBuffer();
        sql.append("select va.id as areaId,vl.id as lineId, li.id, li.companyid, c.name as companyname, li.logincode, li.phone , li.name, li.post, li.role as role, li.companyid, li.codeno , li.hiredate, li.loginmodel, li.mail, li.status, li.isprincipal , r.name as rolename, d.name as departmentname, va.name as areaname, vl.name as linename, li.isexpatriate , li.level from login_info li left join company c on li.companyid = c.id left join role r on r.id = li.role left join department d on li.department = d.id left join vending_area va on va.id = li.areaid left join vending_line vl on vl.id = li.lineid where 1 = 1 and li.deleteFlag = 0");
        if (StringUtils.isNotBlank(userForm.getName())) {
            sql.append(" and li.name like '%" + userForm.getName() + "%'");
        }
        if (userForm.getCompanyId() != null) {
            sql.append(" and li.companyId =" + userForm.getCompanyId());
        } else if (StringUtils.isNotBlank(userForm.getCompanyIds())) {
            sql.append(" and li.companyId in (" + userForm.getCompanyIds() + ")");
        }
        if (userForm.getDepartMent() != null) {
            sql.append(" and li.departMent = " + userForm.getDepartMent());
        }
        sql.append(" order by updateDate desc");
        if (userForm.getIsShowAll() == 0) {
            sql.append(" limit " + (userForm.getCurrentPage() - 1) * userForm.getPageSize() + "," + userForm.getPageSize());
        }

        log.info("<AdminUserDaoImp>--<findUserByForm>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdminUserDto> reList = new ArrayList<AdminUserDto>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                AdminUserDto admin = new AdminUserDto();
                admin.setLoginCode(rs.getString("loginCode"));
                admin.setRole(rs.getString("role"));
                admin.setRoleName(rs.getString("roleName"));
                admin.setPhone(rs.getString("phone"));
                admin.setId(rs.getLong("id"));
                admin.setName(rs.getString("name"));
                admin.setCompanyName(rs.getString("companyName"));
                admin.setDepartmentName(rs.getString("departmentName"));
                admin.setPost(rs.getInt("post"));
                admin.setCodeNo(rs.getString("codeNo"));
                admin.setHireDate(rs.getString("hireDate"));
                admin.setLoginModel(rs.getInt("loginModel"));
                admin.setMail(rs.getString("mail"));
                admin.setStatus(rs.getInt("status"));
                admin.setIsPrincipal(rs.getInt("isPrincipal"));
                admin.setCompanyId(rs.getInt("companyId"));
                admin.setIsExpatriate(rs.getInt("isExpatriate"));
                admin.setLevel(rs.getInt("level"));
                admin.setAreaName(rs.getString("areaName"));
                admin.setLineName(rs.getString("lineName"));
                admin.setLineId(rs.getInt("lineId"));
                admin.setAreaId(rs.getInt("areaId"));

                reList.add(admin);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findUserByForm>--end");
        return reList;
    }

    public Long findUserByFormNum(SearchUserForm userForm) {
        log.info("<AdminUserDaoImp>--<findUserByFormNum>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(1) as total from login_info as li");
        sql.append(" left join company as c on li.companyId=c.id");
        sql.append(" left join department as d on li.departMent = d.id  where 1=1 and li.deleteFlag = 0");
        if (StringUtils.isNotBlank(userForm.getName())) {
            sql.append(" and li.name like '%" + userForm.getName() + "%'");
        }
        if (userForm.getCompanyId() != null) {
            sql.append(" and li.companyId =" + userForm.getCompanyId());
        } else if (StringUtils.isNotBlank(userForm.getCompanyIds())) {
            sql.append(" and li.companyId in (" + userForm.getCompanyIds() + ")");
        }
        if (userForm.getDepartMent() != null) {
            sql.append(" and li.departMent = " + userForm.getDepartMent());
        }
        log.info("<AdminUserDaoImp>--<findUserByForm>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Long total = 0L;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                total = rs.getLong("total");
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findUserByFormNum>--end");
        return total;
    }

    @Override
    public List<DepartMentBean> findAllDepartMent() {
        log.info("<AdminUserDaoImp>--<findAllDepartMent>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" select id,name,parentId");
        sql.append(" from departMent");
        log.info("<AdminUserDaoImp>--<findAllDepartMent>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DepartMentBean> reList = new ArrayList<DepartMentBean>();
        DepartMentBean depart = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                depart = new DepartMentBean();
                depart.setId(rs.getInt("id"));
                depart.setName(rs.getString("name"));
                depart.setParentId(rs.getInt("parentId"));
                reList.add(depart);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findAllDepartMent>--end");
        return reList;
    }

    @Override
    public List<String> findAllLoginCode() {
        log.info("<AdminUserDaoImp>--<findAllLoginCode>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" select loginCode from login_info");
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> loginCodeList = new ArrayList<String>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                loginCodeList.add(rs.getString("loginCode"));
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findAllLoginCode>--end");
        return loginCodeList;
    }

    @Override
    public boolean phoneRepeat(String phone) {
        log.info("<AdminUserDaoImp>--<phoneRepeat>--start");
        boolean result = false;
        StringBuffer sql = new StringBuffer();
        sql.append(" select 1 from login_info where phone = '" + phone + "'");
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                result = true;
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<phoneRepeat>--end");
        return result;
    }

    @Override
    public List<AdminAreaDto> findAreaByCompanyId(String companyId) {

        log.info("<AdminUserDaoImp>--<findAreaByCompanyId>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.id, a.name FROM company c, vending_area a WHERE c.id = a.companyId AND c.id = " + companyId + " and a.isUsing=1 and a.deleteFlag=0 ");
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdminAreaDto> areaNames = new ArrayList<>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            while (rs != null && rs.next()) {
                AdminAreaDto areaDto = new AdminAreaDto();
                areaDto.setAreaId(rs.getString("id"));
                areaDto.setAreaName(rs.getString("name"));
                areaNames.add(areaDto);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findAreaByCompanyId>--end");
        return areaNames;

    }

    @Override
    public List<AdminLineDto> findLineByAreaIdAndCompanyId(String areaId, String companyId) {
        log.info("<AdminUserDaoImp>--<findLineByAreaIdAndCompanyId>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,name from vending_line where areaId =" + areaId + " and companyId = " + companyId + " and deleteFlag =0 ");
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdminLineDto> lineNames = new ArrayList<>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            while (rs != null && rs.next()) {

                AdminLineDto lineDto = new AdminLineDto();
                lineDto.setLineName(rs.getString("name"));
                lineDto.setLineId(rs.getString("id"));
                lineNames.add(lineDto);

            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findLineByAreaIdAndCompanyId>--end");
        return lineNames;
    }

   /**
    * 查询用户 按照参数 companyId in(1,2 3)
    * @param sqlIn
    * @return
    */
    
    @Override
    public List<UserVoForSelect> findUserByCompanyIsSql(String sqlIn) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.id, a.name FROM login_info a WHERE a.status=1 and a.companyId in" + sqlIn + "  order by id");
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserVoForSelect> userList = new ArrayList<>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            while (rs != null && rs.next()) {
            	UserVoForSelect dto = new UserVoForSelect();
            	dto.setId(rs.getLong("id"));
            	dto.setName(rs.getString("name"));
                userList.add(dto);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<findAreaByCompanyId>--end");
        return userList;

    }

	@Override
	public boolean checkPhoneIsChange(LoginInfoBean user) {
		log.info("<AdminUserDaoImp>--<checkPhoneIsChange>--start");
		  StringBuffer sql = new StringBuffer();
	      sql.append(" select loginCode from login_info where loginCode='"+user.getLoginCode()+"' and phone='"+user.getPhone()+"'");
	      log.info("sql语句：" + sql);
	      Connection conn = null;
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      boolean result=true;
	      try {
	          conn = openConnection();
	          ps = conn.prepareStatement(sql.toString());
	          rs = ps.executeQuery();
	          while (rs != null && rs.next()) {//该用户手机号没有修改
	        	  result=false;
	          }
	          log.info("<AdminUserDaoImp>--<checkPhoneIsChange>--end");
	          return result;
	      } catch (Exception e) {
	          log.error(e);
	          log.info("<AdminUserDaoImp>--<checkPhoneIsChange>--end");
	          return result;
	      } finally {
	          this.closeConnection(rs, ps, conn);
	      }
	}
	
	public Boolean deleteAdminUser(AdminUserBean adminUserBean) {
		log.info("<AdminUserDaoImp>--<adminUserBean>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update login_info set deleteFlag = 1 , status = 0  where id = ?");
		log.info("sql语句：" + sql);
        List<Object> param = new ArrayList<Object>();
        param.add(adminUserBean.getId());
        int result = upate(sql.toString(), param);
        log.info("<AdminUserDaoImp>--<adminUserBean>--end");
        if (result != 0) {
            return true;
        }
		return false;
	}

    @Override
    public boolean addMachineToUser( AdminMachine adMachine ) {
        log.info("<AdminUserDaoImp>--<addMachineToUser>--start");
        StringBuffer sql = new StringBuffer();
        sql.append("insert into login_info_machine (vmCode,way,userId) values (?,?,?)");
        log.info("sql语句：" + sql);
        List<Object> param = new ArrayList<Object>();
        param.add(adMachine.getVmCode());
        param.add(adMachine.getWay());
        param.add(adMachine.getUserId());
        int result = insert(sql.toString(), param);
        log.info("<AdminUserDaoImp>--<addMachineToUser>--end");
        if (result != 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<AdminMachine> getMachinesByUserId( Integer userId ){
        log.info("<AdminUserDaoImp>--<getMachinesByUserId>--start");
        String sql = "select id,vmCode,way "
                + " from login_info_machine  lm "
                + " where"
                + " userId='" + userId + "'";
        log.info("<AdminUserDaoImp>--<getMachinesByUserId>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdminMachine> list = new ArrayList<>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                AdminMachine admin = new AdminMachine();
                admin.setId(rs.getInt("id"));
                admin.setVmCode(rs.getString("vmCode"));
                admin.setWay(rs.getInt("way"));
                //admin.setPost(rs.getInt("post"));
                list.add(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<getMachinesByUserId>--end");
        return list;
    }

    @Override
    public List<AdminMachine> getWayByUserIdAndVmcode( Integer userId, String vmCode ) {
        log.info("<AdminUserDaoImp>--<getWayByUserIdAndVmcode>--start");
        String sql = "select way "
                + " from login_info_machine  lm "
                + " where"
                + " userId='" + userId + "'"
                + "and"
                + " vmCode='" + vmCode + "'";
        log.info("<AdminUserDaoImp>--<getWayByUserIdAndVmcode>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdminMachine> list = new ArrayList<>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                AdminMachine admin = new AdminMachine();
                admin.setWay(rs.getInt("way"));
                list.add(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<AdminUserDaoImp>--<getWayByUserIdAndVmcode>--end");
        return list;
    }

    @Override
    public boolean delMachinesByUserId( List<Integer> mwIds ) {
        log.info("<AdminUserDaoImp>--<delMachinesByUserId>--start");
        StringBuffer sql = new StringBuffer();
        String id = org.apache.commons.lang.StringUtils.join(mwIds, ",");
        //System.out.println(id);
        sql.append("delete from login_info_machine where id in (" + id + ")");
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            int sign = ps.executeUpdate();
            if (sign == mwIds.size()) {
                log.info("<AdminUserDaoImp>--<delMachinesByUserId>--end");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            this.closeConnection(null, ps, conn);
        }
        return false;
    }


}
