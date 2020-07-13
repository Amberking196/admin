package com.server.module.system.region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.CompanyEnum;

@Repository
public class RegionDaoImpl extends MySqlFuns implements RegionDao{

	private final static Logger log = LogManager.getLogger(RegionDaoImpl.class);

	@Override
	public List<RegionBean> getByParentId(Integer parentId) {
		log.info("<RegionDaoImpl--getByParentId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,`name`,parentId FROM region WHERE parentId = "+parentId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RegionBean region = null;
		List<RegionBean> regionList = new ArrayList<RegionBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				region = new RegionBean();
				region.setId(rs.getInt("id"));
				region.setName(rs.getString("name"));
				region.setParentId(rs.getInt("parentId"));
				regionList.add(region);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RegionDaoImpl--getByParentId--end>");
		return regionList;
	}


	@Override
	public String getNameById(Integer id) {
		log.info("<RegionDaoImpl--getNameById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT `name` FROM region WHERE id = "+id);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				name = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RegionDaoImpl--getNameById--end>");
		return name;
	}


	@Override
	public boolean canEnterAli(String vmCode) {
		log.info("<RegionDaoImpl--canEnterAli--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 FROM vending_machines_info WHERE `code` = '"+vmCode+"' AND FIND_IN_SET(companyId , getChildList("+CompanyEnum.WUHANYOUSHUI.getIndex()+"))");
		boolean result = false;
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RegionDaoImpl--canEnterAli--end>");
		return result;
	}
}
