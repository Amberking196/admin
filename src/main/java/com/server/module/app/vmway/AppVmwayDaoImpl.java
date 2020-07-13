package com.server.module.app.vmway;

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
@Repository("aliVmwayDao")
public class AppVmwayDaoImpl extends MySqlFuns implements AppVmwayDao{

	public static Logger log = LogManager.getLogger(AppVmwayDaoImpl.class); 

	@Override
	public VendingMachinesWayBean queryByWayAndVmcode(String vmCode, Integer wayNo) {
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,vendingMachinesCode,wayNumber,itemId,");
		sql.append(" state,num,fullNum,updateTime,createTime FROM `vending_machines_way`");
		sql.append(" WHERE vendingMachinesCode= '"+vmCode+"'");
		sql.append(" AND wayNumber = "+wayNo);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingMachinesWayBean vmWay = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vmWay = new VendingMachinesWayBean();
				vmWay.setCreateTime(rs.getDate("createTime"));
				vmWay.setFullNum(rs.getInt("fullNum"));
				vmWay.setId(rs.getLong("id"));
				vmWay.setItemId(rs.getLong("itemId"));
				vmWay.setNum(rs.getInt("num"));
				vmWay.setState(rs.getInt("state"));
				vmWay.setUpdateTime(rs.getDate("updateTime"));
				vmWay.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				vmWay.setWayNumber(rs.getInt("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--end>");
		return vmWay;
	}

	@Override
	public List<VendingMachinesWayBean> queryByWayAndVmcode(String vmCode) {
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,vendingMachinesCode,wayNumber,itemId,");
		sql.append(" state,num,fullNum,updateTime,createTime FROM `vending_machines_way`");
		sql.append(" WHERE vendingMachinesCode= '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VendingMachinesWayBean> vmwList = new ArrayList<VendingMachinesWayBean>();
		VendingMachinesWayBean vmWay = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vmWay = new VendingMachinesWayBean();
				vmWay.setCreateTime(rs.getDate("createTime"));
				vmWay.setFullNum(rs.getInt("fullNum"));
				vmWay.setId(rs.getLong("id"));
				vmWay.setItemId(rs.getLong("itemId"));
				vmWay.setNum(rs.getInt("num"));
				vmWay.setState(rs.getInt("state"));
				vmWay.setUpdateTime(rs.getDate("updateTime"));
				vmWay.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				vmWay.setWayNumber(rs.getInt("wayNumber"));
				vmwList.add(vmWay);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--end>");
		return vmwList;
	}
}
