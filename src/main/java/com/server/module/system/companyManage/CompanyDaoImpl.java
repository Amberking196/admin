package com.server.module.system.companyManage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.server.module.sys.utils.UserUtils;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class CompanyDaoImpl extends MySqlFuns implements CompanyDao {


	public static Logger log = LogManager.getLogger(CompanyDaoImpl.class); 	

	@Override
	public List<CompanyBean> findAllSonCompany(Integer parentId) { 
		log.info("<CompanyDaoImpl>--<findAllSonCompany>--start"); 
		List<CompanyBean> companyList = new ArrayList<CompanyBean>();
		StringBuffer sql = new StringBuffer();
		sql.append("select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where state = 2050 and parentId ="+parentId+" union "
				+ "select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where state = 2050 and id = "+parentId);
		sql.append(" UNION select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where state = 2050 and parentId in( SELECT id FROM company WHERE state = 2050 and parentId = "+parentId+") order by id ");
		
		log.info("<CompanyDaoImpl>--<findAllSonCompany>--sql:"+sql.toString()); 
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CompanyBean company = null;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				company = setCompanyInfo(rs);
				companyList.add(company);
			}
			//从第四层公司开始查询 直到此层无公司
			StringBuffer sql3=new StringBuffer();
			sql3.append("select id from company where state = 2050 and parentId in( SELECT id FROM company WHERE state = 2050 and parentId = "+parentId+") order by id");
			while(rs != null) {
				log.info("<CompanyDaoImpl>--<findAllSonCompany>--sql3:"+sql3.toString());
				StringBuffer sql4=new StringBuffer();
				sql4.append("select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where parentId in("+sql3+") and  state = 2050");
				log.info("<CompanyDaoImpl>--<findAllSonCompany>--sql4:"+sql4.toString());
				ps = conn.prepareStatement(sql4.toString());
				rs = ps.executeQuery();
				while(rs != null && rs.next()){
					company = setCompanyInfo(rs);
					companyList.add(company);
				}
			    if(rs.first() == false) {
					break;
				}
			    //截取部分sql
				sql3=sql4.replace(7, 95, "id");
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<findAllSonCompany>--end"); 
		return companyList;
	}
	
	
	@Override
	public List<Integer> findAllSonCompanyIdWithoutState(Integer parentId) {
		log.info("<CompanyDaoImpl>--<findAllSonCompanyIdWithoutState>--start");  
		List<Integer> resultList = new ArrayList<Integer>();
		//String sql = "SELECT id FROM company WHERE parentId="+parentId;
		StringBuilder sql=new StringBuilder();
		sql.append(" select id from company where  parentId  in( SELECT id FROM company WHERE  parentId = "+parentId+")");
		sql.append(" UNION ");
		sql.append(" SELECT id FROM company WHERE  parentId = "+parentId);
		
		log.info("<CompanyDaoImpl>--<findAllSonCompanyIdWithoutState>--sql:"+sql.toString());   
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
			sql3.append("select id from company where  parentId  in( SELECT id FROM company  WHERE parentId = "+parentId+")");
			log.info("<CompanyDaoImpl>--<findAllSonCompanyIdWithoutState>--sql3:"+sql3.toString());
			while(rs != null) {
				StringBuffer sql4=new StringBuffer();
				sql4.append("select id from company where  parentId in("+sql3+")");
				log.info("<CompanyDaoImpl>--<findAllSonCompanyIdWithoutState>--sql4:"+sql4.toString());
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
		log.info("<CompanyDaoImpl>--<findAllSonCompanyIdWithoutState>--end");  
		
		return resultList;
	}
	
	
	@Override
	public List<Integer> findAllSonCompanyId(Integer parentId) {
		//log.info("<CompanyDaoImpl>--<findAllSonCompanyId>--start");  
		List<Integer> resultList = new ArrayList<Integer>();
		//String sql = "SELECT id FROM company WHERE parentId="+parentId;
		StringBuilder sql=new StringBuilder();
		sql.append(" select id from company where state = 2050 and parentId  in( SELECT id FROM company WHERE state = 2050 and parentId = "+parentId+")");
		sql.append(" UNION ");
		sql.append(" SELECT id FROM company WHERE state = 2050 and parentId = "+parentId);
		
		log.info("<CompanyDaoImpl>--<findAllSonCompanyId>--sql:"+sql.toString());   
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
			sql3.append("select id from company where state = 2050 and parentId  in( SELECT id FROM company  WHERE state = 2050 and parentId = "+parentId+")");
			log.info("<CompanyDaoImpl>--<findAllSonCompanyId>--sql3:"+sql3.toString());
			while(rs != null) {
				StringBuffer sql4=new StringBuffer();
				sql4.append("select id from company where state = 2050 and parentId in("+sql3+")");
				log.info("<CompanyDaoImpl>--<findAllSonCompanyId>--sql4:"+sql4.toString());
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
		//log.info("<CompanyDaoImpl>--<findAllSonCompanyId>--end");  
		
		return resultList;
	}
	//@Cacheable(value = "findAllSonCompanyIdForInSql", key = "#companyId")
	public String findAllSonCompanyIdForInSql(Integer companyId){ 
		log.info("<CompanyDaoImpl>--<findAllSonCompanyIdForInSql>--start");   
		List<Integer> list=findAllSonCompanyId(companyId);
		if (list.size()==0){
			return null;
		}
		StringBuilder sb=new StringBuilder("(");
		for (int i=0;i<list.size();i++) {
			sb.append(list.get(i));
			if(i!=list.size()-1){
				sb.append(",");
			}			
		}
		sb.append(")");
		log.info("<CompanyDaoImpl>--<findAllSonCompanyIdForInSql>--end");   
		return sb.toString();
	}

	public String findAllSonCompanyIdForInSql1(Integer companyId){
		Connection conn = null;
		List<Integer> list= Lists.newArrayList();

		try {
			conn = openConnection();
			recursionAllSon(conn,companyId,list);


		}catch (Exception e){
			e.printStackTrace();
		}finally {
			this.closeConnection(null,null,conn);
		}


		if (list.size()==0){
			return null;
		}
		return genInSqlStr(list);
	}
	private void recursionAllSon(Connection conn,int companyId,List<Integer> list){

		list.add(companyId);
		//查询下一级
		StringBuilder sql=new StringBuilder();
		sql.append("select id from company where state = 2050 and parentId = "+companyId);
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps=conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			System.out.println("查====="+companyId);
			while(rs.next()){
				Integer pid=rs.getInt("id");
				System.out.println("pid="+companyId+"  id="+pid);
				recursionAllSon(conn,pid,list);

			}


		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,null);
		}


	}
	public String findAllFatherCompanyIdForInSql1(Integer companyId){
		Connection conn = null;
		List<Integer> list= Lists.newArrayList();

		try {
			conn = openConnection();
			recursionAllFather(conn,companyId,list);


		}catch (Exception e){
			e.printStackTrace();
		}finally {
			this.closeConnection(null,null,conn);
		}


		if (list.size()==0){
			return null;
		}
		return genInSqlStr(list);
	}

    /**
     * 生成(1,2,3)样式的字符串
     * @param list
     * @return
     */
	private String genInSqlStr(List<Integer> list) {
		StringBuilder sb=new StringBuilder("(");
		for (int i=0;i<list.size();i++) {
			sb.append(list.get(i));
			if(i!=list.size()-1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	private void recursionAllFather(Connection conn,int companyId,List<Integer> list){

		list.add(companyId);
		//查询下一级
		StringBuilder sql=new StringBuilder();
		sql.append("select parentId from company where state = 2050 and id = "+companyId);
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps=conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			System.out.println("查====="+companyId);
			while(rs.next()){
				Integer pid=rs.getInt("parentId");
				recursionAllFather(conn,pid,list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,null);
		}


	}
	@Override
	public CompanyBean findCompanyById(Integer id) {
		//log.info("<CompanyDaoImpl>--<findCompanyById>--start");
		CompanyBean company = null;
		String sql = "select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where id = "+id;

		//log.info("<CompanyDaoImpl>--<findCompanyById>--sql"+sql); 
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				company = setCompanyInfo(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		//log.info("<CompanyDaoImpl>--<findCompanyById>--end");  
		return company;
	}
	
	public CompanyBean setCompanyInfo(ResultSet rs) throws SQLException{
		//log.info("<CompanyDaoImpl>--<setCompanyInfo>--start");  
		CompanyBean company = new CompanyBean();
		company.setAreaId(rs.getInt("areaId"));
		company.setCreateTime(rs.getTimestamp("createTime"));
		company.setId(rs.getInt("id"));
		company.setLocation(rs.getString("location"));
		company.setLogoPic(rs.getString("logoPic"));
		company.setMail(rs.getString("mail"));
		company.setName(rs.getString("name"));
		company.setParentId(rs.getInt("parentId"));
		company.setPhone(rs.getString("phone"));
		company.setPrincipal(rs.getString("principal"));
		company.setShortName(rs.getString("shortName"));
		company.setState(rs.getInt("state"));
		//log.info("<CompanyDaoImpl>--<setCompanyInfo>--end");  
		return company;
	}

	public List<CompanyBean> findAllCompany(){
		log.info("<CompanyDaoImpl>--<findAllCompany>--start");  
		String sql = "select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company";
		log.info("sql语句："+sql);
		List<CompanyBean> result = new ArrayList<CompanyBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CompanyBean company = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				company = setCompanyInfo(rs);
				result.add(company);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<findAllCompany>--end");  
		return result;
	}
	
	
	public Integer createCompany(CompanyBean company){
		log.info("<CompanyDaoImpl>--<createCompany>--start");   
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into company(name,parentId,principal,phone,mail,areaId,");
		sql.append(" state,shortName,createTime,location,logoPic,otherType)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(company.getName());
		param.add(company.getParentId());
		param.add(company.getPrincipal());
		param.add(company.getPhone());
		param.add(company.getMail());
		param.add(company.getAreaId());
		param.add(company.getState());
		param.add(company.getShortName());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		param.add(company.getLocation());
		param.add(company.getLogoPic());
		param.add(company.getOtherType());
		int id = insertGetID(sql.toString(),param);
		log.info("<CompanyDaoImpl>--<createCompany>--end");
		if(id>0){
			return id;
		}
		return null;
	}
	
	@Override
	public CompanyBean getCompany(Integer userId) {
		log.info("<CompanyDaoImpl>--<getCompany>--start"); 
		String sql = "select c.id,c.name,c.parentId,c.principal ,c.phone,c.mail,c.areaId,c.state,c.shortName,c.createTime,c.location,c.logoPic from company c,login_info l WHERE  c.id=l.companyId and l.id="+userId+"";
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CompanyBean company = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				company = setCompanyInfo(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<getCompany>--end");  
		return company;
	}
	
	public List<CompanyBean> findCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyDaoImpl>--<findCompanyByForm>--start");   
		StringBuffer sql = new StringBuffer();
		sql.append(" select  areaId,createTime,id,location,logoPic,mail,name,parentId,");
		sql.append(" phone,principal,shortName,state from company where 1=1 ");
		if(StringUtils.isNotBlank(companyForm.getCompanyIds())){
			sql.append(" and id in ("+companyForm.getCompanyIds()+")");
		}
		if(StringUtils.isNotBlank(companyForm.getCompanyName())){
			sql.append(" and name like '%"+companyForm.getCompanyName()+"%'");
		}
		if(companyForm.getParentId()!=null){
			sql.append(" and parentId = "+companyForm.getCompanyId());
		}
		if(companyForm.getState()!=null){
			sql.append(" and state="+companyForm.getState());
		}
		if(companyForm.getCompanyId()!=null) {
			sql.append(" and id ="+companyForm.getCompanyId());
		}
		if(companyForm.getThisCompanyId()!=null) {
			sql.append(" and id !="+companyForm.getThisCompanyId());
		}
		if(companyForm.getOtherType()!=null){
			sql.append(" and otherType ="+companyForm.getOtherType());

		}
		sql.append(" order by createTime desc ");//进行排序,根据新增时间倒叙
		if(companyForm.getIsShowAll()==0){
			sql.append(" limit "+(companyForm.getCurrentPage()-1)*companyForm.getPageSize()+","+companyForm.getPageSize());
		}

		log.info("<CompanyDaoImpl>--<findCompanyByForm>--sql:"+sql);    
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CompanyBean> companyList = new ArrayList<CompanyBean>();
		CompanyBean company = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				company = setCompanyInfo(rs);
				companyList.add(company);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<findCompanyByForm>--end");
		return companyList;
	}
	
	public Long findCompanyByFormNum(CompanyForm companyForm) {
		log.info("<CompanyDaoImpl>--<findCompanyByFormNum>--start"); 
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) as total from company where 1=1 ");
		if(StringUtils.isNotBlank(companyForm.getCompanyIds())){
			sql.append(" and id in ("+companyForm.getCompanyIds()+")");
		}
		if(StringUtils.isNotBlank(companyForm.getCompanyName())){
			sql.append(" and name like '%"+companyForm.getCompanyName()+"%'");
		}
		if(companyForm.getState()!=null){
			sql.append(" and state="+companyForm.getState());
		}
		if(companyForm.getThisCompanyId()!=null){
			sql.append(" and id !="+companyForm.getThisCompanyId());
		}
		log.info("<CompanyDaoImpl>--<findCompanyByFormNum>--sql:"+sql);  
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long total = 0L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				total= rs.getLong("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<findCompanyByFormNum>--end");  
		return total;
	}
	@Override
	public boolean updateCompany(CompanyBean company) {
		log.info("<CompanyDaoImpl>--<updateCompany>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update company set ");
		sql.append(" name=?,principal=?,location=?,shortName=?,state=?,phone=?,mail=?,logoPic=?,parentId=? where id=?");
		List<Object> param = new ArrayList<Object>();
		param.add(company.getName());
		param.add(company.getPrincipal());
		param.add(company.getLocation());
		param.add(company.getShortName());
		param.add(company.getState());
		param.add(company.getPhone());
		param.add(company.getMail());
		param.add(company.getLogoPic());
		param.add(company.getParentId());
		//param.add(company.getOtherType());
		param.add(company.getId());
		int result = insert(sql.toString(),param);
		log.info("<CompanyDaoImpl>--<updateCompany>--end"); 
		if(result ==1){
			return true;
		}
		return false;
	}
	@Override
	public boolean isOnlyOne(String companyName) {
		log.info("<CompanyDaoImpl>--<isOnlyOne>--start"); 
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from company where name = '"+companyName+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<CompanyDaoImpl>--<isOnlyOne>--end"); 
			if(rs!=null && rs.next()){
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		return true;
	}
	@Override
	public List<CompanyBean> findSecondCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyDaoImpl>--<findSecondCompanyByForm>--start");   
		StringBuffer sql = new StringBuffer();
		sql.append(" select  areaId,createTime,id,location,logoPic,mail,name,parentId,");
		sql.append(" phone,principal,shortName,state from company where 1=1 ");
		sql.append(" and (parentId = 1 or parentId = 0)");
		log.info("<CompanyDaoImpl>--<findSecondCompanyByForm>--sql:"+sql);    
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CompanyBean> companyList = new ArrayList<CompanyBean>();
		CompanyBean company = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				company = setCompanyInfo(rs);
				companyList.add(company);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<findSecondCompanyByForm>--end");
		return companyList;
	}

	@Override
	public List<CompanyBean> findLimitSecondCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyDaoImpl>--<findLimitSecondCompanyByForm>--start");   
		List<CompanyBean> companyList = new ArrayList<CompanyBean>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select  areaId,createTime,id,location,logoPic,mail,name,parentId,");
		sql.append(" phone,principal,shortName,state from company where 1=1 ");
		sql.append(" and id not in ( select parentId from company ) ");
		sql.append(" and parentId = 1");
		if(companyForm.getThisCompanyId()!=null) {
			sql.append(" and id!= "+companyForm.getThisCompanyId());
		}
		sql.append(" union");
		sql.append(" select areaId,createTime,id,location,logoPic,mail,name,parentId,"
				+ "phone,principal,shortName,state from company where id=1");
		log.info("<CompanyDaoImpl>--<findLimitSecondCompanyByForm>--sql:"+sql);    
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CompanyBean company = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				company = setCompanyInfo(rs);
				companyList.add(company);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<findLimitSecondCompanyByForm>--end");
		return companyList;
	}
	
	@Override
	public List<CompanyBean> findFatherCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyDaoImpl>--<findFatherCompanyByForm>--start");   
		StringBuffer sql = new StringBuffer();
		sql.append(" select  areaId,createTime,id,location,logoPic,mail,name,a.parentId,");
		sql.append(" phone,principal,shortName,state from company a ");
		sql.append(" inner join");
		sql.append(" (select parentId from company  where id = "+companyForm.getThisCompanyId()+") b");
		sql.append(" on a.id = b.parentId");
		log.info("<CompanyDaoImpl>--<findFatherCompanyByForm>--sql:"+sql);    
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CompanyBean> companyList = new ArrayList<CompanyBean>();
		CompanyBean company = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				company = setCompanyInfo(rs);
				companyList.add(company);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<findFatherCompanyByForm>--end");
		return companyList;
	}

	/**
	 * 判断是否拥有子公司
	 */
	@Override
	public boolean checkIsSubsidiaries(Integer companyId) {
		log.info("<CompanyDaoImpl>--<checkIsSubsidiaries>--start");
		String sql = "select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where parentId = "+companyId;
		log.info("<CompanyDaoImpl>--<checkIsSubsidiaries>--判断是否拥有子公司 sql"+sql); 
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<checkIsSubsidiaries>--end");  
		return false;
	}


	@Override
	public boolean checkIsReplenishCompany(Integer replenishId) {
		log.info("<CompanyDaoImpl>--<checkIsReplenishCompany>--start");
		String sql = "select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where otherType=3 and id = "+replenishId;
		log.info("<CompanyDaoImpl>--<checkIsReplenishCompany>--判断该公司是否为补货公司 sql"+sql); 
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		log.info("<CompanyDaoImpl>--<checkIsReplenishCompany>--end");
		return false;
	}
}
