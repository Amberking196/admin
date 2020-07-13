package com.server.module.customer.userInfo.record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.customer.userInfo.stock.CustomerStockBean;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.ItemUnitEnum;
@Repository
public class PickRecordDaoImpl extends MySqlFuns implements PickRecordDao{

	private final static Logger log = LogManager.getLogger(PickRecordDaoImpl.class);

	@Override
	public List<PickRecordBean> getPickRecord(Long customerId,Long itemId) {
		log.info("<PickRecordDaoImpl--getPickRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tcr.id,tcr.customerId,tcr.basicItemId,tcr.pickNum,tcr.vmCode,tcr.wayNum,tcr.createTime");
		sql.append(" FROM `tbl_customer_record` AS tcr INNER JOIN `shopping_goods` AS sg");
		sql.append(" ON tcr.basicItemId = sg.basicItemId ");
		sql.append(" WHERE tcr.customerId  = "+customerId+" AND sg.id="+itemId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PickRecordBean> pickList = new ArrayList<PickRecordBean>();
		PickRecordBean pick = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				pick = new PickRecordBean();
				pick.setBasicItemId(rs.getLong("basicItemId"));
				pick.setCreateTime(DateUtil.formatYYYYMMDDHHMMSS(rs.getTimestamp("createTime")));
				pick.setCustomerId(rs.getLong("customerId"));
				pick.setId(rs.getLong("id"));
				pick.setPickNum(rs.getInt("pickNum"));
				pick.setVmCode(rs.getString("vmCode"));
				pick.setWayNum(rs.getInt("wayNum"));
				pickList.add(pick);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PickRecordDaoImpl--getPickRecord--end>");
		return pickList;
	}

	@Override
	public List<CustomerStockBean> getCustomerStock(Long customerId) {
		log.info("<PickRecordDaoImpl--getCustomerStock--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tcs.id,tcs.customerId,tcs.itemId,sg.name AS itemName,tcs.stock,tcs.pickNum,");
		sql.append(" tcs.createTime,tcs.updateTime,sg.pic,sg.unit");
		sql.append(" FROM `tbl_customer_stock` AS tcs");
		sql.append(" LEFT JOIN shopping_goods AS sg ON tcs.itemId = sg.id WHERE tcs.customerId = " + customerId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CustomerStockBean> stockList = new ArrayList<CustomerStockBean>();
		CustomerStockBean stock = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				stock = new CustomerStockBean();
				stock.setCreateTime(rs.getTimestamp("createTime"));
				stock.setCustomerId(rs.getLong("customerId"));
				stock.setId(rs.getLong("id"));
				stock.setItemId(rs.getInt("itemId"));
				stock.setItemName(rs.getString("itemName"));
				stock.setPickNum(rs.getInt("pickNum"));
				stock.setStock(rs.getInt("stock"));
				stock.setUpdateTime(rs.getTimestamp("updateTime"));
				stock.setPic(rs.getString("pic"));
				stock.setUnit(ItemUnitEnum.findUnit(rs.getInt("unit")));
				stockList.add(stock);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PickRecordDaoImpl--getCustomerStock--end>");
		return stockList;
	}

	@Override
	public List<PickRecordBean> getPickRecord(Long customerId) {
		log.info("<PickRecordDaoImpl--getPickRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tcr.id,tcr.customerId,tcr.basicItemId,tcr.pickNum,tcr.vmCode,tcr.wayNum,tcr.createTime");
		sql.append(" FROM `tbl_customer_record` AS tcr");
		sql.append(" WHERE tcr.customerId  = "+customerId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PickRecordBean> pickList = new ArrayList<PickRecordBean>();
		PickRecordBean pick = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				pick = new PickRecordBean();
				pick.setBasicItemId(rs.getLong("basicItemId"));
				pick.setCreateTime(DateUtil.formatYYYYMMDDHHMMSS(rs.getTimestamp("createTime")));
				pick.setCustomerId(rs.getLong("customerId"));
				pick.setId(rs.getLong("id"));
				pick.setPickNum(rs.getInt("pickNum"));
				pick.setVmCode(rs.getString("vmCode"));
				pick.setWayNum(rs.getInt("wayNum"));
				pickList.add(pick);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PickRecordDaoImpl--getPickRecord--end>");
		return pickList;
	}
	
	
}
