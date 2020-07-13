package com.server.module.system.synthesizeManage.roleManage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserDto;
import com.server.module.system.adminUser.SearchUserForm;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.machineManage.machineItem.VendingMachinesItemBean;
import com.server.util.ReturnDataUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class RoleManageDaoImpl extends MySqlFuns implements RoleManageDao {

	public static Logger log = LogManager.getLogger(RoleManageDaoImpl.class);
	@Override
	public boolean addRole(RoleBean role) {
		log.info("<RoleManageDaoImpl>--<addRole>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO role(`name`,privilege,companyId,createTime,createUser,parentId,createUserName)");
		sql.append(" values(?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(role.getName());
		param.add(role.getPrivilege());
		param.add(role.getCompanyId());
		param.add(role.getCreateTime());//添加创建时间
		param.add(role.getCreateUser());
		param.add(role.getParentId());
		param.add(role.getCreateUserName());
		int result = insert(sql.toString(),param);
		log.info("<RoleManageDaoImpl>--<addRole>--end");
		if(result!=0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateRole(RoleBean role) {
		log.info("<RoleManageDaoImpl>--<updateRole>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update role set privilege=?");
		sql.append(" where id=?");
		List<Object> param = new ArrayList<Object>();
		param.add(role.getPrivilege());
		param.add(role.getId());
		int result = upate(sql.toString(),param);
		log.info("<RoleManageDaoImpl>--<updateRole>--end");
		if(result!=0){
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteRole(Integer roleId) {
		log.info("<RoleManageDaoImpl>--<deleteRole>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from role where id ="+roleId);
		log.info("sql语句："+sql);
		int result = delete(sql.toString());
		log.info("<RoleManageDaoImpl>--<deleteRole>--end");
		if(result!=0){
			return true;
		}
		return false;
	}

	@Override
	public List<RoleBean> findRoleByCompany(Integer companyId) {
		log.info("<RoleManageDaoImpl>--<findAllRole>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select r.id,r.name,r.privilege,r.companyId,r.parentId,r.createUser,c.name as companyName from role as r");
		sql.append(" left join company as c on c.id = r.companyId");
		sql.append(" where 1=1 ");
		if(companyId!=null) {
			sql.append(" and companyId = "+companyId);
		}
		
		log.info("sql语句："+sql);
		List<RoleBean> roleList = new ArrayList<RoleBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RoleBean role = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				role = new RoleBean();
				role.setCompanyId(rs.getInt("companyId"));
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setPrivilege(rs.getString("privilege"));
				role.setCompanyName(rs.getString("companyName"));
				role.setParentId(rs.getInt("parentId"));
				role.setCreateUser(rs.getLong("createUser"));
				roleList.add(role);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<RoleManageDaoImpl>--<findAllRole>--end");
		return roleList;
	}

	@Override
	public boolean isOnlyOne(Integer companyId , String roleName) {
		log.info("<RoleManageDaoImpl>--<isOnlyOne>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from role where companyId ="+companyId+" and name = '"+roleName+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if(rs!=null && rs.next()){
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<RoleManageDaoImpl>--<isOnlyOne>--end");
		return true;
	}

	@Override
	public ReturnDataUtil listPage(RoleForm condition) {
		// TODO Auto-generated method stub
		log.info("<RoleManageDaoImpl>--<listPage>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(" select r.id,r.name,r.privilege,r.companyId,r.createUser,r.parentId,c.createTime,c.name as companyName from role as r");
		sql.append(" left join company as c on c.id = r.companyId");
		sql.append(" where 1=1");
		if(condition.getCompanyIds()!=null) {
			sql.append(" and companyId in ( "+condition.getCompanyIds()+")");
		}
		sql.append(" order by r.createTime is null, r.createTime desc ");
		 if (condition.getIsShowAll() == 0) {
	            sql.append(" limit " + (condition.getCurrentPage() - 1) * condition.getPageSize() + "," + condition.getPageSize());
	        }

		log.info("sql语句："+sql);
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RoleBean> roleList = new ArrayList<RoleBean>();
		RoleBean role=null;
		try {
			  conn = openConnection();
	          ps = conn.prepareStatement(sql.toString());
	          rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				role = new RoleBean();
				role.setCompanyId(rs.getInt("companyId"));
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setPrivilege(rs.getString("privilege"));
				role.setCompanyName(rs.getString("companyName"));
				role.setCreateUser(rs.getLong("createUser"));
				role.setParentId(rs.getInt("parentId"));
				roleList.add(role);
			}
			
			data.setCurrentPage(condition.getCurrentPage());
			data.setPageSize(condition.getPageSize());
			data.setTotal(count);
			data.setReturnObject(roleList);
			data.setStatus(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<RoleManageDaoImpl>--<listPage>--end");
		return data;
	}
	//查询总数
	 public Long findRoleByFormNum(RoleForm condition) {
			// TODO Auto-generated method stub
			log.info("<RoleManageDaoImpl>--<listPage>--start");
			ReturnDataUtil data = new ReturnDataUtil();
			StringBuffer sql = new StringBuffer();
			sql.append(" select count(1) as total from role as r");
			sql.append(" left join company as c on c.id = r.companyId");
			sql.append(" where 1=1");
			if(condition.getCompanyIds()!=null) {
				sql.append(" and companyId in("+condition.getCompanyIds()+")");
			}
			
			log.info("sql语句："+sql);
			
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
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				closeConnection(rs, ps, conn);
			}
			log.info(total);
			log.info("<RoleManageDaoImpl>--<listPage>--end");
			return total;
	 }

	@Override
	public RoleBean findRoleById(int parseInt) {
		log.info("<RoleManageDaoImpl>--<findRoleById>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,name,privilege,companyId,createTime,createUserName,parentId from role where id="+parseInt);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RoleBean role = new RoleBean() ;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setPrivilege(rs.getString("privilege"));
				role.setCompanyId(rs.getInt("companyId"));
				role.setCreateTime(rs.getDate("createTime"));
				role.setParentId(rs.getInt("parentId"));
				role.setCreateUserName(rs.getString("createUserName"));

			}
			log.info("<RoleManageDaoImpl>--<findRoleById>--end");
			return role;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("<RoleManageDaoImpl>--<findRoleById>--end");
			return role;
		} finally{
			closeConnection(rs, ps, conn);
		}
		
	}
	
	public List<Integer> findSonRoleById(int parentId){
		log.info("<RoleManageDaoImpl>--<findSonRoleById>--start");
		List<Integer> resultList = new ArrayList<Integer>();
		//String sql = "SELECT id FROM company WHERE parentId="+parentId;
		StringBuilder sql=new StringBuilder();
		sql.append(" select id from role where  parentId  in( SELECT id FROM role WHERE  parentId = "+parentId+")");
		sql.append(" UNION ");
		sql.append(" SELECT id FROM role WHERE  parentId = "+parentId);
		
		log.info("<RoleManageDaoImpl>--<findSonRoleById>--sql:"+sql.toString());   
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		resultList.add(parentId);
		try {
			conn = openConnection();
            conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				resultList.add(rs.getInt("id"));
			}
			//从第四层公司开始查询 直到此层无公司
			StringBuffer sql3=new StringBuffer();
			sql3.append("select id from role where  parentId  in( SELECT id FROM role  WHERE parentId = "+parentId+")");
			log.info("<RoleManageDaoImpl>--<findSonRoleById>--sql3:"+sql3.toString());
			while(rs != null) {
				StringBuffer sql4=new StringBuffer();
				sql4.append("select id from role where  parentId in("+sql3+")");
				log.info("<RoleManageDaoImpl>--<findSonRoleById>--sql4:"+sql4.toString());
				ps = conn.prepareStatement(sql4.toString());
				rs = ps.executeQuery();
				while(rs != null && rs.next()){
					resultList.add(rs.getInt("id"));
				}
			    if(rs.first() == false) {
					break;
				}
				sql3=sql4;
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<RoleManageDaoImpl>--<findSonRoleById>--end");
		return resultList;
		
	}
	
	public List<RoleBean> findSonRole(int parentId){
		log.info("<RoleManageDaoImpl>--<findSonRole>--start");
		List<Integer> resultList = new ArrayList<Integer>();
		//String sql = "SELECT id FROM company WHERE parentId="+parentId;
		StringBuilder sql=new StringBuilder();

		sql.append(" select role.id,role.name,privilege,companyId,role.parentId,createUser,createUserName,company.name as companyName from role left join company on role.companyId=company.id where  role.parentId  in( SELECT id FROM role WHERE  parentId = "+parentId+")");
		sql.append(" UNION ");
		sql.append(" select role.id,role.name,privilege,companyId,role.parentId,createUser,createUserName,company.name as companyName from role inner join company on role.companyId=company.id WHERE  role.parentId = "+parentId);
		sql.append(" UNION ");
		sql.append(" select role.id,role.name,privilege,companyId,role.parentId,createUser,createUserName,company.name as companyName from role inner join company on role.companyId=company.id where role.id = "+parentId);
		
		log.info("<RoleManageDaoImpl>--<findSonRole>--sql:"+sql.toString());   
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RoleBean role = null;
		List<RoleBean> roleList = new ArrayList<RoleBean>();

		try {
			conn = openConnection();
            conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				resultList.add(rs.getInt("id"));
				role = setRoleInfo(rs);
				roleList.add(role);
			}
			//从第四层公司开始查询 直到此层无公司
			StringBuffer sql3=new StringBuffer();
			sql3.append("select id from role where  parentId  in( SELECT id FROM role  WHERE parentId = "+parentId+")");
			log.info("<RoleManageDaoImpl>--<findSonRole>--sql3:"+sql3.toString());
			while(rs != null) {
				StringBuffer sql4=new StringBuffer();
				sql4.append("select role.id,role.name,privilege,companyId,role.parentId,createUser,createUserName,company.name as companyName from role inner join company on role.companyId=company.id  where  role.parentId in("+sql3+")");
				log.info("<RoleManageDaoImpl>--<findSonRole>--sql4:"+sql4.toString());
				ps = conn.prepareStatement(sql4.toString());
				rs = ps.executeQuery();
				while(rs != null && rs.next()){
					role = setRoleInfo(rs);
					roleList.add(role);
				}
			    if(rs.first() == false) {
					break;
				}
			    //截取部分sql
				sql3=sql4.replace(7, 112, "role.id");
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<RoleManageDaoImpl>--<findSonRoleById>--end");
		return roleList;
	}
	
	public RoleBean setRoleInfo(ResultSet rs) throws SQLException{
		RoleBean role = new RoleBean();
		role.setCompanyId(rs.getInt("companyId"));
		role.setId(rs.getInt("id"));
		role.setName(rs.getString("name"));
		role.setPrivilege(rs.getString("privilege"));
		role.setParentId(rs.getInt("parentId"));
		role.setCreateUser(rs.getLong("createUser"));
		role.setCreateUserName(rs.getString("createUserName"));
		if(rs.getString("companyName") != null) {
			role.setCompanyName(rs.getString("companyName") );
		}
		return role;
	}
	
	public List<Integer>  findAdminUserListByRoleId(RoleBean role){
		StringBuilder sql=new StringBuilder();
		sql.append(" select l.id as id from role r left join login_info l on r.id = l.role where 1 = 1 ");
		sql.append(" and r.id = "+role.getId());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Integer> adminUserList = Lists.newArrayList();

		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				adminUserList.add(rs.getInt("id"));
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
				
		return adminUserList;
		
	}

	public Boolean addAdminUserList(String add,RoleBean role) {
		StringBuilder sql=new StringBuilder();
		sql.append(" update login_info set role = "+role.getId());
		sql.append(" where  id in (" + add + ")");
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs>0) {
				return true;
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(null,ps,conn);
		}
		return false;
	}
	
	public Boolean reduceAdminUserList(String reduce,RoleBean role) {
		StringBuilder sql=new StringBuilder();
		sql.append(" update login_info set role = NULL ");
		sql.append(" where  id in (" + reduce + ")");
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs>0) {
				return true;
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(null,ps,conn);
		}
		return false;
	}
	
	public List<AdminUserBean> findUserByRole(RoleBean role){
		StringBuilder sql=new StringBuilder();
		sql.append(" select l.id,l.name,l.role,l.companyId from login_info l left join role r on l.role = r.id where 1 = 1 and l.deleteFlag = 0");
		if(role.getCompanyId()!=null) {
			sql.append(" and l.companyId = "+role.getCompanyId());
		}
		if(role.getId()!=null) {
			sql.append(" and l.role = "+role.getId());
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AdminUserBean> adminUserList = Lists.newArrayList();
		log.info("sql:语句"+sql.toString());
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				AdminUserBean admin = new AdminUserBean();
                admin.setRole(rs.getString("role"));
                admin.setName(rs.getString("name"));
                admin.setCompanyId(rs.getInt("companyId"));
                admin.setId(rs.getLong("id"));
                adminUserList.add(admin);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		return adminUserList;
	}
}
