package com.server.module.customer.userInfo.stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class StoreOrderDaoImpl extends MySqlFuns implements StoreOrderDao{

	private static Logger log = LogManager.getLogger(StoreOrderDaoImpl.class);

	@Override
	public CustomerStockBean getStock(Integer itemId,Long customerId) {
		log.info("<StoreOrderDaoImpl--getStock--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,itemId,itemName,stock,pickNum,createTime,updateTime,basicItemId");
		sql.append(" FROM `tbl_customer_stock` WHERE customerId = "+customerId+" AND itemId = "+itemId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerStockBean cusStock = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cusStock = new CustomerStockBean();
				cusStock.setId(rs.getLong("id"));
				cusStock.setCreateTime(rs.getTimestamp("createTime"));
				cusStock.setCustomerId(rs.getLong("customerId"));
				cusStock.setItemId(rs.getInt("itemId"));
				cusStock.setItemName(rs.getString("itemName"));
				cusStock.setPickNum(rs.getInt("pickNum"));
				cusStock.setStock(rs.getInt("stock"));
				cusStock.setUpdateTime(rs.getTimestamp("updateTime"));
				cusStock.setBasicItemId(rs.getLong("basicItemId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getStock--end>");
		return cusStock;
	}

	@Override
	public CustomerStockBean insert(CustomerStockBean cusStock) {
		log.info("<StoreOrderDaoImpl--insert--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `tbl_customer_stock`(customerId,itemId,itemName,stock,pickNum,basicItemId)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(cusStock.getCustomerId());
		param.add(cusStock.getItemId());
		param.add(cusStock.getItemName());
		param.add(cusStock.getStock());
		param.add(cusStock.getPickNum());
		param.add(cusStock.getBasicItemId());
		int insert = insertGetID(sql.toString(),param);
		log.info("<StoreOrderDaoImpl--insert--end>");
		if(insert > 0){
			cusStock.setId(Long.valueOf(insert));
			return cusStock;
		}
		return null;
	}

	@Override
	public boolean update(CustomerStockBean cusStock) {
		log.info("<StoreOrderDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `tbl_customer_stock` SET stock = ?, pickNum = ? ,updateTime =?");
		sql.append(" WHERE customerId = ? ");
		List<Object> param = new ArrayList<Object>();
		param.add(cusStock.getStock());
		param.add(cusStock.getPickNum());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		param.add(cusStock.getCustomerId());
		if(cusStock.getBasicItemId()!=null){
			sql.append(" AND basicItemId= ?");
			param.add(cusStock.getBasicItemId());
		}else{
			sql.append(" AND itemId= ?");
			param.add(cusStock.getItemId());
		}
		int upate = upate(sql.toString(),param);
		log.info("<StoreOrderDaoImpl--update--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public List<OrderDetailBean> getOrderDetail(String payCode) {
		log.info("<StoreOrderDaoImpl--getOrderDetail--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT sod.id,sod.orderId,sod.itemId,sod.price,sod.num,sod.customerId ");
		sql.append(" FROM `store_order_detile` AS sod ");
		sql.append(" INNER JOIN `store_order` AS so ON sod.orderId = so.id");
		sql.append(" WHERE  so.payCode = "+payCode);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderDetailBean> orderList = new ArrayList<OrderDetailBean>();
		OrderDetailBean order = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				order = new OrderDetailBean();
				order.setId(rs.getLong("id"));
				order.setOrderId(rs.getLong("orderId"));
				order.setItemId(rs.getInt("itemId"));
				order.setPrice(rs.getBigDecimal("price"));
				order.setNum(rs.getInt("num"));
				order.setCustomerId(rs.getLong("customerId"));
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getOrderDetail--end>");
		return orderList;
	}

	@Override
	public Integer getStoreItem(Integer itemId) {
		log.info("<StoreOrderDaoImpl--getMachinesItem--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT sg.id AS storeItemId FROM `vending_machines_item` AS vmi");
		sql.append(" INNER JOIN item_basic AS ib ON vmi.basicItemId = ib.id");
		sql.append(" INNER JOIN `shopping_goods` AS sg ON sg.basicItemId = ib.id");
		sql.append(" WHERE vmi.id = "+itemId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer storeItemId = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				storeItemId = rs.getInt("storeItemId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getMachinesItem--end>");
		return storeItemId;
	}

	@Override
	public Integer insertCouponLog(CouponLog coupouLog) {
		log.info("<StoreOrderDaoImpl--insertCouponLog--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `coupon_use_log`(couponId,couponCustomerId,orderId,`type`,money,deductionMoney,createTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(coupouLog.getCouponId());
		param.add(coupouLog.getCouponCustomerId());
		param.add(coupouLog.getOrderId());
		param.add(coupouLog.getType());
		param.add(coupouLog.getMoney());
		param.add(coupouLog.getDeductionMoney());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<StoreOrderDaoImpl--insertCouponLog--end>");
		return insertGetID;
	}

	@Override
	public Integer insertPickRecord(CustomerPickRecord pickRecord) {
		log.info("<StoreOrderDaoImpl--insertPickRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO tbl_customer_record(customerId,basicItemId,pickNum,vmCode,wayNum,createTime)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(pickRecord.getCustomerId());
		param.add(pickRecord.getBasicItemId());
		param.add(pickRecord.getPickNum());
		param.add(pickRecord.getVmCode());
		param.add(pickRecord.getWayNum());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		int insertGetID = insertGetID(sql.toString(),param);
		log.info("<StoreOrderDaoImpl--insertPickRecord--end>");
		if(insertGetID>0){
			return insertGetID;
		}
		return null;
	}
}
