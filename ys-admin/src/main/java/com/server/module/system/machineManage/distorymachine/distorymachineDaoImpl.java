package com.server.module.system.machineManage.distorymachine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerDaoImpl;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
@Repository("distorymachineDao")
public class distorymachineDaoImpl extends BaseDao implements distorymachineDao{
	
	
	public static Logger log = LogManager.getLogger(distorymachineDaoImpl.class);
    @Autowired
	public CompanyDao companyDao;
	@Override
	public ReturnDataUtil findDistorymachine(distorymachineForm distorymachineForm) {
		log.info("<CustomerDaoImpl--findDistorymachine--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
	/*	sql.append(" select t.id,t.code,v.companyId,c.name,t.machineCode,t.info,t.createTime,t.createUser,t.updateTime,t.updateUser from tbl_distorymachine_log as t,vending_machines_info as v,company as c "
				+ "where t.code=v.code and v.companyId=c.id and t.createTime >=(select DATE_SUB(NOW(),INTERVAL 1 HOUR)) AND t.createTime<NOW() and t.info like '%机器掉线%' ");
		if(distorymachineForm!=null){
			if(StringUtil.isNotBlank(distorymachineForm.getCode())){
				sql.append(" and t.code like '%"+distorymachineForm.getCode()+"%'");
			}
			if(distorymachineForm.getCompanyId()!=null){
				sql.append(" and v.companyId ="+distorymachineForm.getCompanyId());
			}else if(StringUtils.isNotBlank(distorymachineForm.getCompanyIds())){
				sql.append(" and v.companyId in ("+distorymachineForm.getCompanyIds()+")");
			}
		}
		
	*/
		//添加了locationName字段
		 sql.append(" select t.id,t.code,v.companyId,v.locatoinName,c.name,t.machineCode,t.info,t.createTime,t.createUser,t.updateTime,t.updateUser from tbl_distorymachine_log as t,vending_machines_info as v,company as c "
				+ "where t.code=v.code and v.companyId=c.id and v.state=20001 ");
		if(distorymachineForm!=null){
		    if(distorymachineForm.getDate()==null){
		          sql.append(" and t.createTime >=(select DATE_SUB(NOW(),INTERVAL 14 DAY)) AND t.createTime<NOW() ");
		    }
		    if(StringUtil.isNotBlank(distorymachineForm.getDate())){
		       String ss=distorymachineForm.getDate();
	           if(ss.equals("1")) {
		           sql.append(" and t.createTime >=(select DATE_SUB(NOW(),INTERVAL 1 HOUR)) AND t.createTime<NOW()");
	           }else if(ss.equals("2")){
	               sql.append(" and t.createTime >=(select DATE_SUB(NOW(),INTERVAL 1 DAY)) AND t.createTime<NOW()");        
	           }else if(ss.equals("3")) {
	        	 sql.append(" and t.createTime >=(select DATE_SUB(NOW(),INTERVAL 7 DAY)) AND t.createTime<NOW()"); 
	           }
			}
			if(StringUtil.isNotBlank(distorymachineForm.getCode())){
				sql.append(" and t.code like '%"+distorymachineForm.getCode()+"%'");
			}
			if(distorymachineForm.getCompanyId()!=null){
				sql.append(" and v.companyId ="+distorymachineForm.getCompanyId());
			}else if(StringUtils.isNotBlank(distorymachineForm.getCompanyIds())){
				sql.append(" and v.companyId in ("+distorymachineForm.getCompanyIds()+")");
			}
			if (distorymachineForm.getAreaId() != null && distorymachineForm.getAreaId()>0) {
				sql.append("and v.areaId = '" + distorymachineForm.getAreaId() + "' ");
			}

		} 
		sql.append(" GROUP BY t.id order by t.createTime desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			if(distorymachineForm.getIsShowAll()==0) {
				long off = (distorymachineForm.getCurrentPage() - 1) * distorymachineForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + distorymachineForm.getPageSize());
			}
			else {
				pst = conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<distorymachineBean> list = Lists.newArrayList();
			int id=0;
			while (rs.next()) {
				id++;
				distorymachineBean distorymachineBean = new distorymachineBean();
				distorymachineBean =new distorymachineBean();
				distorymachineBean.setId(rs.getInt("id"));
				distorymachineBean.setCode(rs.getString("code"));
				distorymachineBean.setCompanyId(rs.getInt("companyId"));
				distorymachineBean.setName(rs.getString("name"));
				distorymachineBean.setMachineCode(rs.getString("machineCode"));
				distorymachineBean.setInfo(rs.getString("info"));
				distorymachineBean.setCreateTime(rs.getTimestamp("createTime"));
				distorymachineBean.setCreateUser(rs.getString("createUser"));
				distorymachineBean.setUpdateTime(rs.getTimestamp("updateTime"));
				distorymachineBean.setUpdateUser(rs.getString("updateUser"));
				distorymachineBean.setLocatoinName(rs.getString("locatoinName"));//新增字段
				list.add(distorymachineBean);
			}
			data.setCurrentPage(distorymachineForm.getCurrentPage());
			data.setPageSize(distorymachineForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<CustomerDaoImpl--findDistorymachine----end>");
			return data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return data;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public ReturnDataUtil perMachineCount(PerMachineForm perMachineForm) {
		int period=perMachineForm.getPeriod();
		DateTime dt1 = new DateTime();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beforeTime="";

		if(period==1){
			beforeTime=sf.format(dt1.minusHours(1).getMillis());
		}else if (period==2){
			beforeTime=sf.format(dt1.minusDays(1).withTime(0,0,0,0).getMillis());
		}else if(period==3){
			beforeTime=sf.format(dt1.minusDays(6).withTime(0,0,0,0).getMillis());
		}else{
			beforeTime=sf.format(dt1.minusDays(1).withTime(0,0,0,0).getMillis());
		}

		StringBuilder sql=new StringBuilder("SELECT i.locatoinName,c.name,l.code,l.machinecode,COUNT(*) AS num FROM tbl_distorymachine_log l INNER JOIN vending_machines_info i ON l.code=i.code INNER JOIN company c ON i.companyId=c.id WHERE 1=1 ");
		//l.createTime>='"+beforeTime+"' GROUP BY c.name,l.code,l.machinecode ";
		if(StringUtil.isNotBlank(beforeTime))
		sql.append(" and l.createTime>='"+beforeTime+"' ");

//        if(StringUtil.isNotBlank(perMachineForm.getCompanyId())){
//			sql.append(" and i.companyId="+perMachineForm.getCompanyId());
//		}else{
//			Integer companyId=UserUtils.getUser().getCompanyId();
//			String ins=companyDao.findAllSonCompanyIdForInSql(companyId);
//			sql.append(" and i.companyId in"+ins);
//		}
		if(perMachineForm.getCompanyId()!=null){
			sql.append(" and i.companyId ="+perMachineForm.getCompanyId());
		}else if(StringUtils.isNotBlank(perMachineForm.getCompanyIds())){
			sql.append(" and i.companyId in ("+perMachineForm.getCompanyIds()+")");
		}
		if (perMachineForm.getAreaId() != null && perMachineForm.getAreaId()>0) {
			sql.append("and i.areaId = '" + perMachineForm.getAreaId() + "' ");
		}
		if(StringUtils.isNotBlank(perMachineForm.getVmCode())){
        	sql.append(" and l.code='"+perMachineForm.getVmCode()+"' ");
		}
    	sql.append(" and l.info = '机器掉线' ");
    	sql.append(" and i.state = 20001");

		sql.append(" GROUP BY c.name,l.code,l.machinecode order by num desc");

		ReturnDataUtil data = new ReturnDataUtil();

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			String sqlcount="select count(*) from ("+sql.toString()+") as c";
			pst = conn.prepareStatement(sqlcount);
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				 count = rs.getInt(1);//改过
			}

			if(perMachineForm.getIsShowAll()==0) {
				long off = (perMachineForm.getCurrentPage() - 1) * perMachineForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + perMachineForm.getPageSize());
			}
			else {
				pst = conn.prepareStatement(sql.toString());
			}

			rs = pst.executeQuery();
			List<distorymachineBean> list = Lists.newArrayList();
			while (rs.next()) {
				distorymachineBean distorymachineBean = new distorymachineBean();
				distorymachineBean =new distorymachineBean();
				distorymachineBean.setCode(rs.getString("code"));
				//distorymachineBean.setCompanyId(rs.getInt("companyId"));
				distorymachineBean.setName(rs.getString("name"));
				distorymachineBean.setMachineCode(rs.getString("machineCode"));
				distorymachineBean.setNum(rs.getInt("num"));
				distorymachineBean.setLocatoinName(rs.getString("locatoinName"));//新增字段

				list.add(distorymachineBean);
			}
			data.setCurrentPage(perMachineForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<CustomerDaoImpl--findDistorymachine----end>");
			return data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return data;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public List<distorymachineBean> listDistoryBean(List<String> codes) {
		List<distorymachineBean> list = Lists.newArrayList();

		 StringBuilder insql=new StringBuilder();
	        for(int i=0;i<codes.size();i++){
	        	insql.append("'").append(codes.get(i)).append("'");
	        	if(i!=codes.size()-1)
	        	insql.append(",");
	        }
	        if(codes.size()==0) {
	        	return list;
	        }
		StringBuilder sql=new StringBuilder("SELECT l.code,l.info FROM tbl_distorymachine_log l  WHERE 1=1 ");
	
        sql.append(" and l.code in(").append(insql.toString()).append(")");
        DateTime dt=new DateTime(new Date().getTime());
        dt=dt.minusHours(1);
        dt=dt.minusMinutes(10);
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String before=sf.format(dt.toDate());
        sql.append(" and l.createTime>='"+before+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:"+sql.toString());

		try {
			conn = openConnection();		
			pst = conn.prepareStatement(sql.toString() );
			rs = pst.executeQuery();
			while (rs.next()) {
				distorymachineBean distorymachineBean = new distorymachineBean();
				distorymachineBean =new distorymachineBean();
				distorymachineBean.setCode(rs.getString("code"));
				distorymachineBean.setInfo(rs.getString("info"));
				list.add(distorymachineBean);
			}
			
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return list;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

	}


	/*public static void main(String[] args) {
		 DateTime dt=new DateTime(new Date().getTime());
		 dt=dt.minusHours(1);
		 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 System.out.println(sf.format(dt.toDate()));
	}*/
}
