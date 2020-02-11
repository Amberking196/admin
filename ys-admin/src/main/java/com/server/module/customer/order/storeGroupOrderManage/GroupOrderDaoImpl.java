package com.server.module.customer.order.storeGroupOrderManage;

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

@Repository
public class GroupOrderDaoImpl extends MySqlFuns implements GroupOrderDao {

	private static Logger log = LogManager.getLogger(GroupOrderDaoImpl.class);
	
	@Override
	public GroupOrderBean getStoreOrderbyOutTradeNo(String outTradeNo) {
		log.info("<GroupOrderDaoImpl----getStoreOrderbyOutTradeNo----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select sg.typeId,sg.vouchersId,sg.vouchersIds,sg.name as goodsName,so.product,so.id,so.customerGroupId,so.customerId,so.openid,so.price,so.num,sgs.minimumGroupSize,tcs.participationCustomerId,timeLimit,tcs.startCustomerId from store_order so left join tbl_customer_spellGroup tcs on so.customerGroupId=tcs.id ");
		sql.append(" left join shopping_goods_spellGroup sgs on sgs.id=tcs.spellGroupId");
		sql.append(" left join shopping_goods sg on sg.id=sgs.goodsId");
		sql.append(" where so.payCode ='"+outTradeNo+"' ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GroupOrderBean order = new GroupOrderBean();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				order.setId(rs.getLong("id"));
				order.setCustomerGroupId(rs.getLong("customerGroupId"));
				order.setCustomerId(rs.getLong("customerId"));
				order.setOpenid(rs.getString("openid"));
				order.setPrice(rs.getBigDecimal("price"));
				order.setMinimumGroupSize(rs.getInt("minimumGroupSize"));
				order.setParticipationCustomerId(rs.getString("participationCustomerId"));
				order.setTimeLimit(rs.getInt("timeLimit"));
				order.setProduct(rs.getString("product"));
				order.setStartCustomerId(rs.getLong("startCustomerId"));
				order.setGoodsName(rs.getString("goodsName"));
				order.setTypeId(rs.getInt("typeId"));
				order.setVouchersId(rs.getLong("vouchersId"));
				order.setVouchersIds(rs.getString("vouchersIds"));
				order.setNum(rs.getInt("num"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GroupOrderDaoImpl----getStoreOrderbyOutTradeNo----end>");
		return order;
	}

	@Override
	public List<GroupOrderBean> getStoreOrderbyCustomerGroupId(Long customerGroupId) {
		log.info("<GroupOrderDaoImpl>----<getStoreOrderbyCustomerGroupId>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select so.id,so.customerGroupId,so.customerId,so.openid,so.price,so.num,so.payCode,sg.typeId,sg.vouchersId,sg.vouchersIds  ");
		sql.append(" from store_order so ");
		sql.append(" left join tbl_customer_spellGroup tcs on so.customerGroupId=tcs.id");
		sql.append(" left join shopping_goods_spellGroup sgs on sgs.id=tcs.spellGroupId");
		sql.append(" left join shopping_goods sg on sg.id=sgs.goodsId");
		sql.append(" where so.state=10001 and so.customerGroupId ='"+customerGroupId+"' ");
		
		log.info("根据用户拼团id 查询订单信息sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GroupOrderBean> list=new ArrayList<GroupOrderBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				GroupOrderBean order = new GroupOrderBean();
				order.setId(rs.getLong("id"));
				order.setCustomerGroupId(rs.getLong("customerGroupId"));
				order.setCustomerId(rs.getLong("customerId"));
				order.setOpenid(rs.getString("openid"));
				order.setPrice(rs.getBigDecimal("price"));
				order.setNum(rs.getInt("num"));
				order.setPayCode(rs.getString("payCode"));
				order.setTypeId(rs.getInt("typeId"));
				order.setVouchersId(rs.getLong("vouchersId"));
				order.setVouchersIds(rs.getString("vouchersIds"));
				order.setNum(rs.getInt("num"));
				list.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GroupOrderDaoImpl>----<getStoreOrderbyCustomerGroupId>----end>");
		return list;
	}

	@Override
	public int paySpellgroupStroeOrder(String outTradeNo, Integer type) {
		log.info("<GroupOrderDaoImpl>----<getStoreOrderbyCustomerGroupId>----start>");
		StringBuffer sql = new StringBuffer();
		if(type==0) { //已完成状态
			sql.append("update store_order set spellgroupState=4  where id='" + outTradeNo + "'") ;
		}else {
			sql.append(" update store_order set spellgroupState="+type+"  where payCode='" + outTradeNo + "'") ;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			re=ps.executeUpdate(); 
		}  catch (SQLException e) { 
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<GroupOrderDaoImpl>----<getStoreOrderbyCustomerGroupId>----end>");
		return re;
		
	}
}
